package com.adobe.training.core.models;

import javax.inject.Named;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.Self;

/**
 * This model represents an IEX api stock data structure created from the StockDataImporter:
 * /content/stocks/
 *   + <STOCK_SYMBOL> [sling:OrderedFolder]
 *     + lastTrade [nt:unstructured]
 *         	 - companyName = <value>
 *       	 - sector = <value>
 *           - lastTrade = <value>
 *           - ..
 */

@Model(adaptables=Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class StockModel{
	
	@Self
	private Resource stock;

	@ChildResource
	private Resource lastTrade;

	@ChildResource
	@Named("lastTrade")
	private ValueMap lastTradeValues;

	
	
	//Uses the resource to read the stock symbol
	public String getSymbol() {
		return stock.getName();
	}

	// uses the ValueMap to read the stock data
	public double getLastTrade() {
        return lastTradeValues.get("lastTrade", Double.class);
    }
    public String getRequestDate() {
        return lastTradeValues.get("dayOfLastUpdate", String.class);
    }
    public String getRequestTime() {
    	return lastTradeValues.get("timeOfUpdate", String.class);
    }
    public double getUpDown() {
        return lastTradeValues.get("upDown", Double.class);
    }
    public double getOpenPrice() {
        return lastTradeValues.get("openPrice", Double.class);
    }
    public double getRangeHigh() {
        return lastTradeValues.get("rangeHigh", Double.class);
    }
    public double getRangeLow() {
        return lastTradeValues.get("rangeLow", Double.class);
    }
    public int getVolume() {
        return lastTradeValues.get("volume", Integer.class);
    }
    public String getCompanyName() {
    	return lastTradeValues.get("companyName", String.class);
    }
    public String getSector() {
    	return lastTradeValues.get("sector", String.class);
    }
    public double get52weekHigh() {
    	return lastTradeValues.get("week52High", Double.class);
    }
    public double get52weekLow() {
    	return lastTradeValues.get("week52Low", Double.class);
    }
    public double getYtdPercentChange() {
    	return lastTradeValues.get("ytdPercentageChange", Double.class);
    }
}