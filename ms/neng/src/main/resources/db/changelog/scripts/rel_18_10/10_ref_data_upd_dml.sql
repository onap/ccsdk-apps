--liquibase formatted sql
--changeset service_param_update_sql:18_10.ref_update.sql

DELETE FROM SERVICE_PARAMETER where NAME='use_db_policy';
INSERT INTO SERVICE_PARAMETER(NAME, VALUE, CREATED_BY) VALUES ('use_db_policy','N','Initial');
