--liquibase formatted sql
--changeset ref_data_update:18_10.ref_data_update.sql

INSERT INTO SERVICE_PARAMETER(NAME, VALUE, CREATED_BY) VALUES ('max_gen_attempt','50','Initial');

delete from IDENTIFIER_MAP;

INSERT INTO IDENTIFIER_MAP(POLICY_FN_NAME, JS_FN_NAME, CREATED_BY) VALUES ('sub_str','substr', 'Initial'); 
