/*
 * Copyright © 2017-2018 AT&T Intellectual Property.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.onap.ccsdk.apps.controllerblueprints.service.utils;

@Deprecated
public class ConfigModelUtils {
    /*

    private ConfigModelUtils() {

    }

    private static EELFLogger log = EELFManager.getInstance().getLogger(ConfigModelUtils.class);

    public static ConfigModel getConfigModel(String blueprintPath) throws Exception {
        Preconditions.checkArgument(StringUtils.isNotBlank(blueprintPath), "Blueprint Path is missing");
        ToscaMetaData toscaMetaData = BluePrintMetadataUtils.Companion.toscaMetaData(blueprintPath);

        Preconditions.checkNotNull(toscaMetaData, "failed to get Blueprint Metadata information");
        Preconditions.checkArgument(StringUtils.isNotBlank(toscaMetaData.getEntityDefinitions()), "failed to get Blueprint Definition file");
        Preconditions.checkArgument(StringUtils.isNotBlank(toscaMetaData.getCreatedBy()), "failed to get Blueprint created by");
        Preconditions.checkArgument(StringUtils.isNotBlank(toscaMetaData.getToscaMetaFileVersion()), "failed to get Blueprint package version");

        String bluePrintName = FilenameUtils.getBaseName(toscaMetaData.getEntityDefinitions());
        Preconditions.checkArgument(StringUtils.isNotBlank(bluePrintName), "failed to get Blueprint Definition Name");

        // TODO - Update Rest of the Model
        ConfigModel configModel = new ConfigModel();
        configModel.setUpdatedBy(toscaMetaData.getCreatedBy());
        configModel.setArtifactName(bluePrintName);
        configModel.setArtifactVersion(toscaMetaData.getToscaMetaFileVersion());
        configModel.setTags(toscaMetaData.getTemplateTags());
        configModel.setArtifactType("SDNC_MODEL");

        String blueprintContent =
                getPathContent(blueprintPath + "/" + toscaMetaData.getEntityDefinitions());

        Preconditions.checkArgument(StringUtils.isNotBlank(blueprintPath), "failed to get Blueprint content");

        List<ConfigModelContent> configModelContents = new ArrayList<>();
        ConfigModelContent stConfigModelContent = new ConfigModelContent();
        stConfigModelContent.setName(configModel.getArtifactName());
        stConfigModelContent.setContentType(ConfigModelConstant.MODEL_CONTENT_TYPE_TOSCA_JSON);
        stConfigModelContent.setContent(blueprintContent);
        configModelContents.add(stConfigModelContent);

        String velocityDir = blueprintPath + "/Templates";
        List<File> velocityTemplateFiles = getFileOfExtension(velocityDir, new String[]{"vtl"});

        if (CollectionUtils.isNotEmpty(velocityTemplateFiles)) {
            for (File velocityTemplateFile : velocityTemplateFiles) {
                if (velocityTemplateFile != null) {
                    String contentName = velocityTemplateFile.getName().replace(".vtl", "");
                    ConfigModelContent velocityConfigModelContent = new ConfigModelContent();
                    String velocityConfigContent = getPathContent(velocityTemplateFile);
                    velocityConfigModelContent.setName(contentName);
                    velocityConfigModelContent
                            .setContentType(ConfigModelConstant.MODEL_CONTENT_TYPE_TEMPLATE);
                    velocityConfigModelContent.setContent(velocityConfigContent);
                    configModelContents.add(velocityConfigModelContent);
                    log.info("Loaded blueprint template successfully: {}", velocityTemplateFile.getName());
                }
            }
        }
        configModel.setConfigModelContents(configModelContents);

        return configModel;

    }

    public static String getPathContent(String path) throws IOException {
        Preconditions.checkArgument(StringUtils.isNotBlank(path), "Path is missing");
        return FileUtils.readFileToString(new File(path), Charset.defaultCharset());
    }

    public static String getPathContent(File file) throws IOException {
        Preconditions.checkNotNull(file, "File is missing");
        return FileUtils.readFileToString(file, Charset.defaultCharset());
    }

    public static List<File> getFileOfExtension(String basePath, String[] extensions) {
        Preconditions.checkArgument(StringUtils.isNotBlank(basePath), "Path is missing");
        Preconditions.checkNotNull(extensions, "Extensions is missing");
        return (List<File>) FileUtils.listFiles(new File(basePath), extensions, true);
    }

    public static List<String> getBlueprintNames(String pathName) {
        File blueprintDir = new File(pathName);
        Preconditions.checkNotNull(blueprintDir, "failed to find the blueprint pathName file");
        String[] dirs = blueprintDir.list(DirectoryFileFilter.INSTANCE);
        Preconditions.checkNotNull(dirs, "failed to find the blueprint directories" + blueprintDir.getAbsolutePath());
        return Arrays.asList(dirs);
    }
    */
}
