--liquibase formatted sql
--changeset template_table:18_10.create_tables_p1.sql

CREATE TABLE GENERATED_NAME (GENERATED_NAME_ID INTEGER PRIMARY KEY AUTO_INCREMENT(0,100),
EXTERNAL_ID VARCHAR(500) NOT NULL COMMENT 'Key sent by SDNC', 
SEQUNCE_NUMBER INTEGER COMMENT 'Sequence number used for this name',
SEQUENCE_NUMBER_ENC VARCHAR(100) COMMENT 'Alpha numeric equivalent of sequence_number column',
ELEMENT_TYPE VARCHAR(100) COMMENT 'The type of network element vnf-name, vm-name etc',
NAME VARCHAR(500) COMMENT 'Generated name',
PREFIX VARCHAR(100) COMMENT 'The prefix for the name. (The part of the name before the sequence number.)',
SUFFIX VARCHAR(100) COMMENT 'The suffix for the name. (The part of the name after the sequence number.)',
IS_RELEASED CHAR(1) COMMENT 'Indicating if the entry is released/unassigned',
CREATED_TIME DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL, 
CREATED_BY VARCHAR(50), 
LAST_UPDATED_TIME DATETIME, 
LAST_UPDATED_BY VARCHAR(50));
-- COMMENT 'TABLE TO STORE GENERATED NAMES';

CREATE TABLE SERVICE_PARAMETER (SERVICE_PARAMETER_ID INTEGER PRIMARY KEY AUTO_INCREMENT(0,100),
NAME VARCHAR(500) NOT NULL COMMENT 'Parameter name',
VALUE VARCHAR(500) COMMENT 'Parameter value',
CREATED_TIME DATETIME DEFAULT CURRENT_TIMESTAMP, 
CREATED_BY VARCHAR(50), 
LAST_UPDATED_TIME DATETIME, 
LAST_UPDATED_BY VARCHAR(50)
) ;

CREATE TABLE IDENTIFIER_MAP (IDENTIFIER_MAP_ID INTEGER PRIMARY KEY AUTO_INCREMENT(0,100),
POLICY_FN_NAME VARCHAR(500) COMMENT 'Function name in policy manager' NOT NULL,
JS_FN_NAME VARCHAR(500) COMMENT 'Equivalent name in Java or script',
CREATED_TIME DATETIME DEFAULT CURRENT_TIMESTAMP, 
CREATED_BY VARCHAR(50), 
LAST_UPDATED_TIME DATETIME, 
LAST_UPDATED_BY VARCHAR(50)
) ;


CREATE TABLE EXTERNAL_INTERFACE (EXTERNAL_INTERFACE_ID INTEGER PRIMARY KEY AUTO_INCREMENT(0,100),
SYSTEM VARCHAR(500) COMMENT 'Name of the external system',
PARAM VARCHAR(500) COMMENT 'Parameters controlling the url',
URL_SUFFIX VARCHAR(500) COMMENT 'The suffix of the URL for each value of the parameter',
CREATED_TIME DATETIME DEFAULT CURRENT_TIMESTAMP, 
CREATED_BY VARCHAR(50), 
LAST_UPDATED_TIME DATETIME, 
LAST_UPDATED_BY VARCHAR(50)
);

CREATE TABLE POLICY_MAN_SIM (POLICY_ID INTEGER PRIMARY KEY AUTO_INCREMENT(0,100),
POLICY_NAME VARCHAR(500) COMMENT 'Policy name' NOT NULL,
POLICY_RESPONSE VARCHAR(10000) COMMENT 'Policy value',
CREATED_TIME DATETIME DEFAULT CURRENT_TIMESTAMP
) ;
