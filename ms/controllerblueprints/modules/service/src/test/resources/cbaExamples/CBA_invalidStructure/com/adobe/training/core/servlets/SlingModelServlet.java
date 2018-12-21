package com.adobe.training.core.servlets;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.osgi.service.component.annotations.Component;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.training.core.models.StockModel;
//import com.adobe.training.core.models.StockModelAdaptFromRequest;

/**
 * Example URI http://localhost:4502/content/trainingproject/en.model.html/content/stocks/ADBE
 * 
 * To use this servlet, a content structure must be created:
 * /content/stocks/
 *   + ADBE [sling:OrderedFolder]
 *     + lastTrade [nt:unstructured]
 *       - lastTrade = "300"
 *       - dayOfLastUpdate = "11/13/2016"
 *       - companyName = "Adobe Systems Incorporated"
 *       - week52High = "310"
 */

@Component(service = Servlet.class,
					 property = {
								"sling.servlet.resourceTypes=trainingproject/components/structure/page",
								"sling.servlet.selectors=model"
					})

public class SlingModelServlet extends SlingAllMethodsServlet{
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private static final long serialVersionUID = 1L;

    @Override
	public void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)throws ServletException,IOException{
		response.setContentType("text/html");
		try
			// Get the resource (a node in the JCR) using ResourceResolver from the request
				(ResourceResolver resourceResolver = request.getResourceResolver()) {

			//Specify the node of interest in the suffix on the request
			String nodePath = request.getRequestPathInfo().getSuffix();
			if(nodePath != null){
				Resource resource = resourceResolver.getResource(nodePath);
				
				// Adapt resource properties to variables using ValueMap, and log their values
				Resource parent = resource.getChild("lastTrade");
				ValueMap valueMap=parent.adaptTo(ValueMap.class);
				response.getOutputStream().println("<h3>");
				response.getOutputStream().println(resource.getName() + " lastTrade node with ValueMap is");
				response.getOutputStream().println("</h3><br />");
				response.getOutputStream().println("(Last Trade) "+ valueMap.get("lastTrade").toString() + " (Requested Day) " + valueMap.get("dayOfLastUpdate").toString()
						+ " (Company) " + valueMap.get("companyName").toString() + " (52 week High) " + valueMap.get("week52High").toString());

				//Adapt the resource to our model (part 1)
				StockModel stockModel = resource.adaptTo(StockModel.class);
				response.getOutputStream().println("<br /><h3>");
				response.getOutputStream().println(stockModel.getSymbol() + " lastTrade node with StockModel is");
				response.getOutputStream().println("</h3><br />");
				response.getOutputStream().println("(Last Trade) " + stockModel.getLastTrade() + " (Requested Day) " + stockModel.getRequestDate()
					+ " (Company) " + stockModel.getCompanyName() + " (52 week High) " + stockModel.get52weekHigh());

				//Adapt the request object to our model (part 2)
				/*StockModelAdaptFromRequest stockModelAdaptFromRequest = request.adaptTo(StockModelAdaptFromRequest.class);
				response.getOutputStream().println("<br /><h3>");
				response.getOutputStream().println(stockModelAdaptFromRequest.getSymbol() + " lastTrade node with StockModelAdaptFromRequest is");
				response.getOutputStream().println("</h3><br />");
				response.getOutputStream().println("(Last Trade) " + stockModelAdaptFromRequest.getLastTrade() + " (Requested Time) " + stockModel.getRequestDate()
					+ " (Company) " + stockModelAdaptFromRequest.getCompanyName() + " (52 week High) " + stockModelAdaptFromRequest.get52weekHigh());*/
			}
			else {
				response.getWriter().println("Can't get the last trade node, enter a suffix in the URI");
			}
		} catch (Exception e) {
			response.getWriter().println("Can't read last trade node. make sure the suffix path exists!");
			logger.error(e.getMessage());
		}
		response.getWriter().close();
	}
}