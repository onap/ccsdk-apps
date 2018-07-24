--liquibase formatted sql
--changeset ref_data_update:18_10.ref_data_update.sql

DELETE FROM SERVICE_PARAMETER where NAME='use_db_policy';
INSERT INTO SERVICE_PARAMETER(NAME, VALUE, CREATED_BY) VALUES ('use_db_policy','Y','Initial');
