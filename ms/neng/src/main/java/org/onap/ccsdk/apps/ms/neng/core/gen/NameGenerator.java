/*-
 * ============LICENSE_START=======================================================
 * ONAP : CCSDK.apps
 * ================================================================================
 * Copyright (C) 2018 AT&T Intellectual Property. All rights reserved.
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=========================================================
 */

package org.onap.ccsdk.apps.ms.neng.core.gen;

import static org.onap.ccsdk.apps.ms.neng.core.policy.PolicyReader.namingModel;
import static org.onap.ccsdk.apps.ms.neng.core.policy.PolicyReader.namingModelRelaxed;
import static org.onap.ccsdk.apps.ms.neng.core.policy.PolicyReader.namingModels;
import static org.onap.ccsdk.apps.ms.neng.core.policy.PolicyReader.namingOperation;
import static org.onap.ccsdk.apps.ms.neng.core.policy.PolicyReader.namingProperty;
import static org.onap.ccsdk.apps.ms.neng.core.policy.PolicyReader.namingRecipe;
import static org.onap.ccsdk.apps.ms.neng.core.policy.PolicyReader.namingType;
import static org.onap.ccsdk.apps.ms.neng.core.policy.PolicyReader.propertyValue;
import static org.onap.ccsdk.apps.ms.neng.core.policy.PolicyReader.relaxedNamingType;
import static org.onap.ccsdk.apps.ms.neng.core.policy.PolicyReader.seq;
import static org.onap.ccsdk.apps.ms.neng.core.policy.PolicyReader.value;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.onap.ccsdk.apps.ms.neng.core.exceptions.NengException;
import org.onap.ccsdk.apps.ms.neng.core.persistence.NamePersister;
import org.onap.ccsdk.apps.ms.neng.core.policy.PolicyFinder;
import org.onap.ccsdk.apps.ms.neng.core.policy.PolicyParameters;
import org.onap.ccsdk.apps.ms.neng.core.policy.PolicyPropertyMethodUtils;
import org.onap.ccsdk.apps.ms.neng.core.policy.PolicySequence;
import org.onap.ccsdk.apps.ms.neng.core.policy.PropertyOperator;
import org.onap.ccsdk.apps.ms.neng.core.policy.RecipeParser;
import org.onap.ccsdk.apps.ms.neng.core.seq.SequenceGenerator;
import org.onap.ccsdk.apps.ms.neng.core.validator.AaiNameValidator;
import org.onap.ccsdk.apps.ms.neng.core.validator.DbNameValidator;
import org.onap.ccsdk.apps.ms.neng.persistence.entity.GeneratedName;

/**
 * Generates names of network elements based on policy data.
 */
public class NameGenerator {
    private static final String RESOURCE_NAME_ELEMENT_ITEM = "resource-name";
    private static final String RESOURCE_VALUE_ELEMENT_ITEM = "resource-value";
    private static final String EXTERNAL_KEY_ELEMENT_ITEM = "external-key";
    private static final String NAMING_TYPE_ELEMENT_ITEM = "naming-type";

    private final PolicyFinder policyFinder;
    private final PolicyParameters policyParams;
    private final SequenceGenerator seqGenerator;
    private final DbNameValidator dbValidator;
    private final AaiNameValidator aaiValidator;
    private final NamePersister namePersister;
    private final Map<String, String> requestElement;
    private final List<Map<String, String>> allElements;
    private final Map<String, Map<String, String>> earlierNames;
    private final Map<String, Map<String, ?>> policyCache;

    /**
     * Constructor.
     * 
     * @param policyFinder a way to find policies
     * @param policyParams parameters related to policy
     * @param seqGenerator a way to generate sequences
     * @param dbValidator a way to validate generated names against DB
     * @param aaiValidator a way to validate generated names against A&AI
     * @param namePersister a way to persist names
     * @param requestElement the request element for which the name is generated, containing data such 
     *        as policy name, naming-type, external-key and resource-name
     * @param allElements all the elements in the request (including the current request element for 
     *        which name is generated), as this is needed to re-use names generated from other request elements 
     *        within the same transaction
     * @param earlierNames names generated earlier in the same transaction, as a map from naming-type 
     *        to names (which is a map with keys "resource-name", "resource-value" and "external-key")
     * @param policyCache cache containing policies retrieved in this transaction, to avoid repeated 
     *        calls to policy manager within the same transaction
     */
    public NameGenerator(PolicyFinder policyFinder, PolicyParameters policyParams, SequenceGenerator seqGenerator,
                    DbNameValidator dbValidator, AaiNameValidator aaiValidator, NamePersister namePersister,
                    Map<String, String> requestElement, List<Map<String, String>> allElements,
                    Map<String, Map<String, String>> earlierNames, Map<String, Map<String, ?>> policyCache) {
        this.policyFinder = policyFinder;
        this.policyParams = policyParams;
        this.seqGenerator = seqGenerator;
        this.dbValidator = dbValidator;
        this.aaiValidator = aaiValidator;
        this.namePersister = namePersister;
        this.requestElement = requestElement;
        this.allElements = allElements;
        this.earlierNames = earlierNames;
        this.policyCache = policyCache;
    }

    /**
     * Generates the name.
     * 
     * @return the map (with keys "resource-name", "resource-value" and "external-key") containing the name.
     */
    public Map<String, String> generate() throws Exception {
        String policyName = findElementPolicyName();
        if (policyName == null) {
            throw new NengException("Could not find policy name in the request");
        }
        String namingType = findElementNamingType();
        if (namingType != null) {
            Map<String, String> generated = this.earlierNames.get(namingType);
            if (generated != null) {
                return generated;
            }
            return generateNew(policyName, namingType);
        } else {
            throw new NengException("Could not find naming type in the request for policy " + policyName);
        }
    }

    String applyNameOperation(Map<String, ?> namingModel, String name) throws Exception {
        String nameOperation = namingOperation(namingModel);
        if (nameOperation != null && !"".equals(nameOperation)) {
            name = new PropertyOperator().apply(name, nameOperation, this.policyParams);
        }
        return name;
    }

    String applyPropertyOperation(String value, Map<String, ?> propertyMap) throws Exception {
        return new PropertyOperator().apply(value, propertyMap, this.policyParams);
    }

    static Map<String, String> buildResponse(String key, String name, String value) {
        Map<String, String> response = new HashMap<String, String>();
        response.put(EXTERNAL_KEY_ELEMENT_ITEM, key);
        response.put(RESOURCE_NAME_ELEMENT_ITEM, name);
        response.put(RESOURCE_VALUE_ELEMENT_ITEM, value);
        return response;
    }

    String buildSequenceSuffix(Map<String, Object> recipeValues, String recipeName, List<String> recipe)
                    throws Exception {
        StringBuffer buf = new StringBuffer();
        boolean postItem = false;
        for (String key : recipe) {
            if (postItem) {
                buf.append(recipeValues.get(key).toString());
            } else if (key.equals(recipeName)) {
                postItem = true;
            }
        }
        String value = buf.toString();
        if (value.length() == 0) {
            value = null;
        }
        return value;
    }

    String buildSequencePrefix(Map<String, Object> recipeValues, String recipeName, List<String> recipe)
                    throws Exception {
        StringBuffer buf = new StringBuffer();
        for (String key : recipe) {
            if (key.equals(recipeName)) {
                break;
            }
            buf.append(recipeValues.get(key).toString());
        }
        return buf.toString();
    }

    Map<String, String> generateNew(String policyName, String namingType) throws Exception {
        Map<String, ?> policy = findPolicy(policyName);
        if (policy != null) {
            List<Map<String, ?>> namingModels = namingModels(policy);
            Map<String, ?> namingModel = namingModel(namingModels, namingType);
            if (namingModel == null) {
                throw new NengException(
                                "Could not find the policy data for " + policyName + " and naming-type " + namingType);
            }
            return generateNew(policyName, namingType, namingModels, namingModel);
        } else {
            throw new NengException("Could not find the policy data for " + policyName);
        }
    }

    Map<String, String> generateNew(String policyName, String namingType, 
                    List<Map<String, ?>> namingModels, Map<String, ?> namingModel) throws Exception {
        String recipe = namingRecipe(namingModel);
        if (recipe == null) {
            throw new NengException("Could not find the recipe for " 
                                      + policyName + " and naming-type " + namingType);
        }
        List<String> recipeItems = RecipeParser.parseRecipe(this.policyParams, recipe);
        return generateNew(namingModels, policyName, namingType, namingModel, recipeItems);
    }

    Map<String, String> generateNew(List<Map<String, ?>> namingModels, String policyName, 
                    String namingType, Map<String, ?> namingModel, List<String> recipe) throws Exception {
        Map<String, Object> recipeValues = new LinkedHashMap<>();
        for (String recipeItem : recipe) {
            Map<String, ?> propMap = namingProperty(namingModel, recipeItem);
            if ("SEQUENCE".equals(recipeItem)) {
                PolicySequence seq = seq(propMap);
                recipeValues.put(recipeItem, seq);
            } else if ("UUID".equals(recipeItem)) {
                String uuid = PolicyPropertyMethodUtils.genUuid();
                recipeValues.put(recipeItem, uuid);
            } else if ("TIMESTAMP".equals(recipeItem)) {
                String ts = PolicyPropertyMethodUtils.getIsoDateString();
                recipeValues.put(recipeItem, ts);
            } else {
                String val = generateNonSequenceValue(namingModels, policyName, namingType, namingModel, propMap,
                                recipeItem);
                if (val != null) {
                    recipeValues.put(recipeItem, val);
                }
            }
        }
        validateAllItemsPresent(policyName, namingType, recipe, recipeValues);
        SeqGenData seqData = generateNameWithSequences(policyName, namingType, recipe, recipeValues, namingModel);
        String name = seqData.getName();
        storeGeneratedName(findElementExternalKey(), name, namingType, seqData);
        Map<String, String> response = buildResponse(findElementExternalKey(), findElementResourceName(), name);
        String relaxedNamingType = relaxedNamingType(namingType);
        this.earlierNames.put(relaxedNamingType, response);
        return response;
    }

    SeqGenData generateNameWithSequences(String policyName, String namingType, List<String> recipe,
                    Map<String, Object> recipeValues, Map<String, ?> namingModel) throws Exception {
        int attemptCount = 0;
        int maxGenAttempt = this.policyParams.getMaxGenAttempt();
        if (maxGenAttempt <= 0) {
            maxGenAttempt = 1;
        }
        String name = null;
        SeqGenData lastSeq = null;
        boolean valid = false;
        String additionalErrorMsg = "";
        while (attemptCount <= maxGenAttempt && !valid) {
            ++attemptCount;
            lastSeq = generateSequenceValues(policyName, namingType, recipe, recipeValues, lastSeq, attemptCount);
            name = generateNameFromSegments(recipeValues, recipe);
            boolean sequenceLess = false;
            if (lastSeq == null) {
                lastSeq = new SeqGenData();
                sequenceLess = true;
            }
            name = applyNameOperation(namingModel, name);
            lastSeq.setName(name);
            valid = this.dbValidator.validate(namingType, name);
            if (valid) {
                valid = this.aaiValidator.validate(namingType, name);
                if (!valid) {
                    storeGeneratedName("AAI-BACKPOPULATE", name, namingType, lastSeq);
                    additionalErrorMsg = "AAI Name validation failed";
                }
            } else {
                additionalErrorMsg = "DB Name validation failed";
            }
            if (sequenceLess) {
                break; // handle names with no sequence in them
            }
        }
        if (attemptCount > maxGenAttempt) {
            throw new NengException("Could not generate a name successfully for policy " + policyName
                            + " and naming-type " + namingType + " even after " + maxGenAttempt + " attempts.");
        }
        if (!valid) {
            throw new NengException("Could not generate a valid name successfully for policy " + policyName
                            + " and naming-type " + namingType + ". " + additionalErrorMsg);
        }
        return lastSeq;
    }

    String generateNameFromSegments(Map<String, Object> recipeValues, List<String> recipe) throws Exception {
        StringBuffer buf = new StringBuffer();
        for (String recName : recipe) {
            Object val = recipeValues.get(recName);
            if (val instanceof PolicySequence) {
                PolicySequence poly = (PolicySequence) val;
                buf.append(poly.getValue());
            } else {
                buf.append(val.toString());
            }
        }
        String value = buf.toString();
        return value;
    }

    SeqGenData generateSequenceValues(String policyName, String namingType, List<String> recipe,
                    Map<String, Object> recipeValues, SeqGenData lastSeq, int attemptCount) throws Exception {
        SeqGenData precedSeq = generateSequenceValuesOfScope(
                        policyName, namingType, recipe, recipeValues, "PRECEEDING", lastSeq, attemptCount);
        SeqGenData entireSeq = generateSequenceValuesOfScope(
                        policyName, namingType, recipe, recipeValues, "ENTIRETY", lastSeq, attemptCount);
        if (entireSeq != null) {
            return entireSeq;
        }
        return precedSeq;
    }

    SeqGenData generateSequenceValuesOfScope(String policyName, String namingType, List<String> recipe,
                    Map<String, Object> recipeValues, String scope, SeqGenData lastSeq, int attemptCount)
                    throws Exception {
        for (String item : recipe) {
            Object val = recipeValues.get(item);
            if (val instanceof PolicySequence) {
                PolicySequence seq = (PolicySequence) val;
                if (scope.equals(seq.getScope())) {
                    SeqGenData seqVal = generateSequenceValue(seq, policyName, namingType, recipeValues, item, 
                                    lastSeq, attemptCount, recipe);
                    String seqStr = SequenceFormatter.formatSequence(seqVal.getSeq(), seq);
                    seqVal.setSeqEncoded(seqStr);
                    seq.setKey(item);
                    seq.setValue(seqStr);
                    return seqVal;
                }
            }
        }
        return null;
    }

    SeqGenData generateSequenceValue(PolicySequence seq, String policyName, String namingType,
                    Map<String, Object> recipeValues, String recipeName, SeqGenData lastSeq, int attemptCount,
                    List<String> recipe) throws Exception {
        String prefix = buildSequencePrefix(recipeValues, recipeName, recipe);
        String suffix = buildSequenceSuffix(recipeValues, recipeName, recipe);
        SeqGenData seqData = new SeqGenData();
        Long lastSeqValue = null;
        if (lastSeq != null) {
            lastSeqValue = lastSeq.getSeq();
        }
        long seqValue = this.seqGenerator.generate(prefix, suffix, seq, lastSeqValue, attemptCount);
        seqData.setSeq(seqValue);
        seqData.setPrefix(prefix);
        seqData.setSuffix(suffix);
        return seqData;
    }

    String generateNonSequenceValue(List<Map<String, ?>> namingModels, String policyName, String namingType,
                    Map<String, ?> namingModel, Map<String, ?> propMap, String recipeItem) throws Exception {
        String val = propertyValue(propMap);
        if (val == null) {
            val = value(this.requestElement, recipeItem);
        }
        if (val == null) {
            val = value(this.requestElement, recipeItem, true);
        }
        if (val == null) {
            val = generateValueRecursively(namingModels, policyName, recipeItem);
        }
        if (val != null) {
            val = applyPropertyOperation(val, propMap);
        }
        return val;
    }

    String generateValueRecursively(List<Map<String, ?>> namingModels, String policyName, String recipeItem)
                    throws Exception {
        String val = null;
        String relaxedVal = relaxedNamingType(recipeItem);
        Map<String, String> generated = this.earlierNames.get(relaxedVal);
        if (generated != null) {
            return generated.get("resource-value");
        }
        Map<String, ?> relaxedModel = namingModelRelaxed(namingModels, recipeItem);
        if (relaxedModel != null) {
            String relaxedNamingType = namingType(relaxedModel);
            Map<String, String> relaxedElement = findElement(relaxedNamingType);
            if (relaxedElement != null) {
                relaxedElement = new HashMap<>(relaxedElement);
                relaxedElement.put(NAMING_TYPE_ELEMENT_ITEM, relaxedNamingType);
            } else {
                relaxedElement = new HashMap<>(this.requestElement);
                relaxedElement.put(NAMING_TYPE_ELEMENT_ITEM, relaxedNamingType);
                relaxedElement.remove(EXTERNAL_KEY_ELEMENT_ITEM);
                relaxedElement.remove(RESOURCE_NAME_ELEMENT_ITEM);
            }
            if (relaxedElement != null) {
                NameGenerator recursive = new NameGenerator(policyFinder, policyParams, seqGenerator, dbValidator,
                                aaiValidator, namePersister, relaxedElement, allElements, earlierNames, policyCache);
                Map<String, String> gen =
                                recursive.generateNew(policyName, relaxedNamingType, namingModels, relaxedModel);
                if (gen != null) {
                    val = value(gen, RESOURCE_VALUE_ELEMENT_ITEM);
                }
            }
        }
        return val;
    }

    Map<String, String> findElement(String namingType) throws Exception {
        Map<String, String> theElement = null;
        for (Map<String, String> anElement : this.allElements) {
            String oneNamingType = namingType(anElement);
            if (namingType.equals(oneNamingType)) {
                theElement = anElement;
                break;
            }
        }
        return theElement;
    }

    String findElementPolicyName() throws Exception {
        return value(this.requestElement, "policy-instance-name");
    }

    String findElementNamingType() throws Exception {
        return value(this.requestElement, NAMING_TYPE_ELEMENT_ITEM);
    }

    String findElementResourceName() throws Exception {
        return value(this.requestElement, RESOURCE_NAME_ELEMENT_ITEM);
    }

    String findElementExternalKey() throws Exception {
        return value(this.requestElement, EXTERNAL_KEY_ELEMENT_ITEM);
    }

    Map<String, ?> findPolicy(String name) throws Exception {
        Map<String, ?> policy = null;
        if (name != null) {
            policy = this.policyCache.get(name);
            if (policy == null) {
                policy = this.policyFinder.findPolicy(name);
                if (policy != null) {
                    this.policyCache.put(name, policy);
                }
            }
        }
        return policy;
    }

    void storeGeneratedName(String key, String name, String namingType, 
                    SeqGenData seqData) throws Exception {
        String prefix = null;
        String suffix = null;
        Long seqNum = null;
        String seqEncoded = null;
        if (seqData != null) {
            prefix = seqData.getPrefix();
            suffix = seqData.getSuffix();
            seqNum = seqData.getSeq();
            seqEncoded = seqData.getSeqEncoded();
        }
        GeneratedName record = new GeneratedName();
        GeneratedName releasedName = namePersister.findBy(namingType, name, "Y");
        if (releasedName != null) {
            record = releasedName;
            record.setLastUpdatedTime(new Timestamp(System.currentTimeMillis()));
            record.setIsReleased(null);
        }
        record.setName(name);
        record.setExternalId(key);
        record.setElementType(namingType);
        record.setPrefix(prefix);
        record.setSuffix(suffix);
        if (seqNum != null) {
            record.setSequenceNumber(seqNum);
        }
        record.setSequenceNumberEnc(seqEncoded);
        this.namePersister.persist(record);
    }

    void validateAllItemsPresent(String policyName, String namingType, List<String> recipe,
                    Map<String, Object> recipeValues) throws Exception {
        List<String> missing = new ArrayList<>();
        for (String item : recipe) {
            Object val = recipeValues.get(item);
            if (val == null) {
                missing.add(item);
            }
        }
        if (missing.size() > 0) {
            StringBuffer msg = new StringBuffer();
            for (int i = 0; i < missing.size(); ++i) {
                String item = missing.get(i);
                if (i > 0) {
                    String separator = ", ";
                    if (i == missing.size() - 1) {
                        separator = " and ";
                    }
                    msg.append(separator);
                }
                msg.append(item);
            }
            String itemString = "items";
            if (missing.size() == 1) {
                itemString = "item";
            }
            throw new NengException("Could not find data for recipe " + itemString + " " + msg.toString()
                            + " in policy " + policyName + " and naming-type " + namingType);
        }
    }
}
