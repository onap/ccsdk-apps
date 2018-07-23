--liquibase formatted sql
--changeset index_creation:18_10.create_indexes_01.sql

-- Add indexes on GENERATED_NAME

create index gen_name_ext_id_idx on GENERATED_NAME(EXTERNAL_ID);
create index gen_name_name_idx on GENERATED_NAME(NAME);
create index gen_name_pre_suf_idx on GENERATED_NAME(PREFIX, SUFFIX);
