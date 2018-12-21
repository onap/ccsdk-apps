package com.adobe.training.core.models;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.*;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;

import com.adobe.cq.export.json.ComponentExporter;
import com.day.cq.wcm.api.Page;

@Model(adaptables=SlingHttpServletRequest.class,
		adapters= {ComponentExporter.class},
		resourceType="trainingproject/components/content/trainingtitle",
		defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(name = "jackson", extensions = "json")
public class TrainingTitle implements ComponentExporter{
	
	@Self
    private SlingHttpServletRequest request;
	
	@Inject
    @Named("log")
    private Logger logger;
	
	@ScriptVariable
	private Page currentPage;
	
	@ValueMapValue(name="subtitle", injectionStrategy=InjectionStrategy.OPTIONAL)
	private String subtitle;
	
	@ValueMapValue(name="jcr:title", injectionStrategy=InjectionStrategy.OPTIONAL)
	private String title;
	
	@ValueMapValue(name="type", injectionStrategy=InjectionStrategy.OPTIONAL)
	private String type;
	
	@PostConstruct
	protected void initModel(){
		if(title == null){
			title = "";
		}
		
		if(subtitle == null){
			subtitle = "";
		}
		logger.info("TrainingTitle InitModel");
	}
	
	public String getText() {
		return title;
	}
	
	public String getType() {
		return type;
	}
	
	public String getSubtitle() {	
		return subtitle;
	}

	@Override
	public String getExportedType() {
		return request.getResource().getResourceType();
	}
}
