--liquibase formatted sql
--changeset ref_data_update_more:18_10.ref_data_update_more.sql

-- update policy functions to java implementations

UPDATE IDENTIFIER_MAP SET JS_FN_NAME = 'substring' WHERE POLICY_FN_NAME='sub_str';
UPDATE IDENTIFIER_MAP SET JS_FN_NAME = 'substring' WHERE POLICY_FN_NAME='substr';
INSERT INTO IDENTIFIER_MAP(POLICY_FN_NAME, JS_FN_NAME, CREATED_BY) VALUES ('to_lower_case','toLowerCase', 'Initial');
INSERT INTO IDENTIFIER_MAP(POLICY_FN_NAME, JS_FN_NAME, CREATED_BY) VALUES ('to_upper_case','toUpperCase', 'Initial'); 
