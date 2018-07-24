--liquibase formatted sql
--changeset policy_man_sim:18_06.table_load.sql

insert into policy_man_sim(policy_name, policy_response) values ('JQINSRIOV.Config_MS_SriovBigJson.1.xml', '[
  {
    "policyConfigMessage": "Config Retrieved! ",
    "policyConfigStatus": "CONFIG_RETRIEVED",
    "type": "JSON",
    "config": {"riskLevel":"4","riskType":"test","policyName":"SriovBigJson","service":"SDNC-GenerateName","guard":"False","description":"SriovBigJson","templateVersion":"1607","priority":"4","version":"pannny_nnnn","content":{"policy-instance-name":"NameGenerationPolicyForSRIOV","naming-models":[{"naming-properties":[{"property-operation":"substr(5)","property-name":"COMPLEX"},{"property-name":"SEQUENCE","increment-sequence":{"max":"zzz","scope":"ENTIRETY","start-value":"001","length":"3","increment":"1","sequence-type":"alpha-numeric"}},{"property-name":"NF_NAMING_CODE"}],"naming-type":"VNF","nfRole":"vPE","naming-recipe":"COMPLEX|SEQUENCE|NF_NAMING_CODE"},{"naming-properties":[{"property-name":"VNF_NAME"},{"property-name":"SEQUENCE","increment-sequence":{"max":"999","scope":"ENTIRETY","start-value":"001","length":"3","increment":"1","sequence-type":"numeric"}},{"property-operation":"substr(-3)","property-name":"NFC_NAMING_CODE"}],"naming-type":"VM","nfRole":"vPE","naming-recipe":"VNF_NAME|SEQUENCE|NFC_NAMING_CODE"},{"naming-properties":[{"property-name":"VNF_NAME"},{"property-value":"-","property-name":"DELIMITER"},{"property-name":"VF_MODULE_LABEL"},{"property-name":"VF_MODULE_TYPE"},{"property-name":"SEQUENCE","increment-sequence":{"max":"99","scope":"PRECEEDING","start-value":"01","length":"2","increment":"1","sequence-type":"numeric"}}],"naming-type":"VF-MODULE","nfRole":"vPE","naming-recipe":"VNF_NAME|DELIMITER|VF_MODULE_LABEL|DELIMITER|VF_MODULE_TYPE|DELIMITER|SEQUENCE"},{"naming-properties":[{"property-name":"VF-MODULE_NAME"},{"property-value":"-","property-name":"DELIMITER"},{"property-value":"volumegroup","property-name":"CONSTANT"}],"naming-type":"VOLUME_GROUP","nfRole":"vPE","naming-recipe":"VF-MODULE_NAME|DELIMITER|CONSTANT"},{"naming-properties":[{"property-name":"VOLUME_GROUP_NAME"},{"property-value":"-","property-name":"DELIMITER"},{"property-value":"volume","property-name":"CONSTANT"},{"property-name":"SEQUENCE","increment-sequence":{"max":"99","scope":"PRECEEDING","start-value":"01","length":"2","increment":"1","sequence-type":"numeric"}}],"naming-type":"VOLUME","nfRole":"vPE","naming-recipe":"VOLUME_GROUP_NAME|DELIMITER|CONSTANT|DELIMITER|SEQUENCE"},{"naming-properties":[{"property-name":"VNF_NAME"},{"property-value":"-","property-name":"DELIMITER"},{"property-value":"affinity","property-name":"CONSTANT"}],"naming-type":"AFFINITY","nfRole":"vPE","naming-recipe":"VNF_NAME|DELIMITER|CONSTANT"},{"naming-properties":[{"property-name":"VNF_NAME"},{"property-value":"-","property-name":"DELIMITER"},{"property-value":"INT","property-name":"CONSTANT"},{"property-name":"SEQUENCE","increment-sequence":{"max":"99","scope":"PRECEEDING","start-value":"01","length":"2","increment":"1","sequence-type":"numeric"}}],"naming-type":"INTERNAL_NETWORK","nfRole":"vPE","naming-recipe":"VNF_NAME|DELIMITER|CONSTANT|SEQUENCE"}]}},
    "policyName": "JQINSRIOV.Config_MS_SriovBigJson.1.xml",
    "policyType": "MicroService",
    "policyVersion": "1",
    "matchingConditions": {
      "ECOMPName": "SDNC",
      "ONAPName": "SDNC",
      "service": "SDNC-GenerateName"
    },
    "responseAttributes": {},
    "property": null
  }
]');

commit;