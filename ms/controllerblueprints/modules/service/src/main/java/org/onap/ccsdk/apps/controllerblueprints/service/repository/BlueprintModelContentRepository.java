package org.onap.ccsdk.apps.controllerblueprints.service.repository;

import org.onap.ccsdk.apps.controllerblueprints.service.domain.BlueprintModel;
import org.onap.ccsdk.apps.controllerblueprints.service.domain.BlueprintModelContent;
import org.onap.ccsdk.apps.controllerblueprints.service.repository.generic.model.content.GenericModelContentRepository;

public interface BlueprintModelContentRepository extends
    GenericModelContentRepository<BlueprintModelContent, BlueprintModel> {

}
