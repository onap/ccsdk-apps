--liquibase formatted sql
--changeset ref_data_update:18_10.ref_data_update.sql

INSERT INTO IDENTIFIER_MAP(POLICY_FN_NAME, JS_FN_NAME, CREATED_BY) VALUES ('substr','substr', 'Initial'); 
INSERT INTO SERVICE_PARAMETER(NAME, VALUE, CREATED_BY) VALUES ('use_db_policy','Y','Initial');
