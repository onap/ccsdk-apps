package org.onap.ccsdk.apps.ms.vlantagapi.core.exception;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;


class VlantagApiExceptionTest {

	@Test
	public void TestVlantagApiException() {
		assertNotNull(new VlantagApiException());
	
	}
	
	@Test
	public void VlantagApiExceptionTestString() {
		assertNotNull(new VlantagApiException("JUnit Test"));
	
	}
	
	@Test
	public void VlantagApiExceptionTestStringThrowable() {
		assertNotNull(new VlantagApiException("JUnit Test", new Exception("JUnit Test")));
	
	}
	
	@Test
	public void VlantagApiExceptionTestStringThrowableArgs() {
		assertNotNull(new VlantagApiException(new Exception("JUnit Test")));
	
	}

}
