--liquibase formatted sql
--changeset ref_data_update:18_10.ref_data_update.sql

-- clean up any data
delete from external_interface;

--insert the data

insert into external_interface(SYSTEM,PARAM,URL_SUFFIX,CREATED_TIME,CREATED_BY,LAST_UPDATED_TIME,LAST_UPDATED_BY)
values('aai','vnf-name','nodes/generic-vnfs?vnf-name=',sysdate(),'nengsys',sysdate(),'nengsys');

insert into external_interface(SYSTEM,PARAM,URL_SUFFIX,CREATED_TIME,CREATED_BY,LAST_UPDATED_TIME,LAST_UPDATED_BY)
values('aai','vnfc-name','nodes/vnfcs?vnfc-name=',sysdate(),'nengsys',sysdate(),'nengsys');

insert into external_interface(SYSTEM,PARAM,URL_SUFFIX,CREATED_TIME,CREATED_BY,LAST_UPDATED_TIME,LAST_UPDATED_BY)
values('aai','vserver-name','nodes/vservers?vserver-name=',sysdate(),'nengsys',sysdate(),'nengsys');

insert into external_interface(SYSTEM,PARAM,URL_SUFFIX,CREATED_TIME,CREATED_BY,LAST_UPDATED_TIME,LAST_UPDATED_BY)
values('aai','vf-module-name','nodes/vf-modules?vf-module-name=',sysdate(),'nengsys',sysdate(),'nengsys');

insert into external_interface(SYSTEM,PARAM,URL_SUFFIX,CREATED_TIME,CREATED_BY,LAST_UPDATED_TIME,LAST_UPDATED_BY)
values('aai','service-instance-id','nodes/service-instances?service-instance-id=',sysdate(),'nengsys',sysdate(),'nengsys');

commit;
