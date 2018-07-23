--liquibase formatted sql
--changeset ref_data_update_extin:18_10.ref_data_update.sql

-- clean up any data
delete from external_interface where PARAM in ('VNF', 'VNFC', 'VM', 'VFMODULE');

--insert the data

insert into external_interface(SYSTEM,PARAM,URL_SUFFIX,CREATED_TIME,CREATED_BY,LAST_UPDATED_TIME,LAST_UPDATED_BY)
values('aai','VNF','nodes/generic-vnfs?vnf-name=',sysdate(),'nengsys',sysdate(),'nengsys');

insert into external_interface(SYSTEM,PARAM,URL_SUFFIX,CREATED_TIME,CREATED_BY,LAST_UPDATED_TIME,LAST_UPDATED_BY)
values('aai','VNFC','nodes/vnfcs?vnfc-name=',sysdate(),'nengsys',sysdate(),'nengsys');

insert into external_interface(SYSTEM,PARAM,URL_SUFFIX,CREATED_TIME,CREATED_BY,LAST_UPDATED_TIME,LAST_UPDATED_BY)
values('aai','VM','nodes/vservers?vserver-name=',sysdate(),'nengsys',sysdate(),'nengsys');

insert into external_interface(SYSTEM,PARAM,URL_SUFFIX,CREATED_TIME,CREATED_BY,LAST_UPDATED_TIME,LAST_UPDATED_BY)
values('aai','VFMODULE','nodes/vf-modules?vf-module-name=',sysdate(),'nengsys',sysdate(),'nengsys');

commit;
