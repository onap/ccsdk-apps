--liquibase formatted sql
--changeset generate_name_ddl_update:18_10.table_update.sql

alter table generated_name MODIFY COLUMN created_time DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL;; 
