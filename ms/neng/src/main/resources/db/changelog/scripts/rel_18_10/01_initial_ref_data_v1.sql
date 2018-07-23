--liquibase formatted sql
--changeset ref_data_load_p1:18_10_ref_data_load_p1.sql

--comment: 07/23/2018 Intial Data Load - LOAD PARAMETERS

--set @@autocommit:=0;

--delete all records
delete from SERVICE_PARAMETER;

INSERT INTO SERVICE_PARAMETER(NAME, VALUE, CREATED_BY) VALUES ('initial_increment','10,40,50,100','Initial');
INSERT INTO SERVICE_PARAMETER(NAME, VALUE, CREATED_BY) VALUES ('recipe_separator','|":",','Initial');

delete from IDENTIFIER_MAP;

INSERT INTO IDENTIFIER_MAP(POLICY_FN_NAME, JS_FN_NAME, CREATED_BY) VALUES ('substr','substring', 'Initial'); 

commit;
