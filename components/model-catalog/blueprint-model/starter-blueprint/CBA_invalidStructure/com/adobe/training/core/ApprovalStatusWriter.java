package com.adobe.training.core;

import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.metadata.MetaDataMap;

import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;
import org.osgi.framework.Constants;

@Component(service = WorkflowProcess.class,
		   property = {Constants.SERVICE_DESCRIPTION + "=Workflow process to set status to approved",
				       Constants.SERVICE_VENDOR + "=Adobe",
				       "process.label=Approval Status Writer"
		   })

public class ApprovalStatusWriter implements WorkflowProcess {

	private static final String TYPE_JCR_PATH = "JCR_PATH";

	@Override
	public void execute(WorkItem item, WorkflowSession workflowSession, MetaDataMap args) throws WorkflowException {
		WorkflowData workflowData = item.getWorkflowData();
		if (workflowData.getPayloadType().equals(TYPE_JCR_PATH)) {
			String path = workflowData.getPayload().toString() + "/jcr:content";
			try (ResourceResolver rr = workflowSession.adaptTo(ResourceResolver.class)){
				Resource resource = rr.getResource(path);
				resource.adaptTo(ModifiableValueMap.class).put("approved", readArgument(args));
				rr.commit();
			}
			catch (PersistenceException e ) {
				throw new WorkflowException(e.getMessage(), e);
			}
		}
	}

	private static boolean readArgument(MetaDataMap args) {
		String argument = args.get("PROCESS_ARGS", "false");
		return argument.equalsIgnoreCase("true");
	}
}
	