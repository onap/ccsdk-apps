--liquibase formatted sql
--changeset identifier_map_update_sql_recipefunction:18_10.identifier_map_upd_dml.sql

delete from IDENTIFIER_MAP where POLICY_FN_NAME='UUID';

INSERT INTO IDENTIFIER_MAP(POLICY_FN_NAME, JS_FN_NAME, CREATED_BY) VALUES ('UUID','genUUID', 'Initial'); 

delete from IDENTIFIER_MAP where POLICY_FN_NAME='TIMESTAMP';

INSERT INTO IDENTIFIER_MAP(POLICY_FN_NAME, JS_FN_NAME, CREATED_BY) VALUES ('TIMESTAMP','getISODateString', 'Initial'); 
