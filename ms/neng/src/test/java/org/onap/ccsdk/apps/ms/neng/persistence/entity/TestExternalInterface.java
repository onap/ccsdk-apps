package org.onap.ccsdk.apps.ms.neng.persistence.entity;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestExternalInterface {
	private ExternalInterface externalInterface;

	@Before
	public void setUp() {
		externalInterface = new ExternalInterface();
	}

	@Test
	public void testGetSetExternalInteraceId() {
		externalInterface.setExternalInteraceId(1);
		Integer expected = 1;
		Assert.assertEquals(expected, externalInterface.getExternalInteraceId());
	}

	@Test
	public void testGetSetSystem() {
		externalInterface.setSystem("testSystem");
		Assert.assertEquals("testSystem", externalInterface.getSystem());
	}

	@Test
	public void testGetSetParam() {
		externalInterface.setParam("testParam");
		Assert.assertEquals("testParam", externalInterface.getParam());
	}

	@Test
	public void testGetSetUrlSuffix() {
		externalInterface.setUrlSuffix("testUrlSuffix");
		Assert.assertEquals("testUrlSuffix", externalInterface.getUrlSuffix());
	}

	@Test
	public void testGetSetTimeStamp() throws ParseException {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date date = dateFormat.parse("23/09/2007");
		long time = date.getTime();
		Timestamp timeStamp = new Timestamp(time);
		externalInterface.setCreatedTime(timeStamp);
		Assert.assertEquals(timeStamp, externalInterface.getCreatedTime());
	}

	@Test
	public void testGetSetCreatedBy() {
		externalInterface.setCreatedBy("testUser");
		Assert.assertEquals("testUser", externalInterface.getCreatedBy());
	}

	@Test
	public void testGetSetLastUpdatedTime() throws ParseException {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date date = dateFormat.parse("23/09/2007");
		long time = date.getTime();
		Timestamp timeStamp = new Timestamp(time);
		externalInterface.setLastUpdatedTime(timeStamp);
		Assert.assertEquals(timeStamp, externalInterface.getLastUpdatedTime());
	}

	@Test
	public void testGetSetLastUpdatedBy() {
		externalInterface.setLastUpdatedBy("testUser");
		Assert.assertEquals("testUser", externalInterface.getLastUpdatedBy());
	}
}
