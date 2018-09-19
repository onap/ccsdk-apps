/*-
 * ============LICENSE_START=======================================================
 * ONAP : CCSDK.apps
 * ================================================================================
 * Copyright (C) 2018 IBM.
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=========================================================
 */

package org.onap.ccsdk.apps.ms.neng.persistence.entity;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GeneratedNameTest {
	private GeneratedName generatedName;
	
	 @Before
	 public void setUp() {
		 	generatedName = new GeneratedName();
	 }
	 
	 @Test
	  public void testGetSetGeneratedNameId() {
		 	generatedName.setGeneratedNameId(1);
	        Integer expected = 1;
	        Assert.assertEquals(expected, generatedName.getGeneratedNameId());
	 }
	 
	 @Test
	  public void testGetSetSequenceNumber() {
		 	generatedName.setSequenceNumber(2121314312321L);
	        Long expected = 2121314312321L;
	        Assert.assertEquals(expected, generatedName.getSequenceNumber());
	 }
	 
	 @Test
	  public void testGetSetSequenceNumberEnc() {
		 	generatedName.setSequenceNumberEnc("TestSequenceNumberEnc");
	        String expected = "TestSequenceNumberEnc";
	        Assert.assertEquals(expected, generatedName.getSequenceNumberEnc());
	 }
	 
	 @Test
	  public void testGetSetElementType() {
		 	generatedName.setElementType("TestElementType");
	        String expected = "TestElementType";
	        Assert.assertEquals(expected, generatedName.getElementType());
	 }
	 
	 @Test
	  public void testGetSetName() {
		 	generatedName.setName("TestName");
	        String expected = "TestName";
	        Assert.assertEquals(expected, generatedName.getName());
	 }
	 
	 @Test
	  public void testGetSetPrefix() {
		 	generatedName.setPrefix("TestPrefix");
	        String expected = "TestPrefix";
	        Assert.assertEquals(expected, generatedName.getPrefix());
	 }
	 
	 @Test
	  public void testGetSetSuffix() {
		 	generatedName.setSuffix("TestSuffix");
	        String expected = "TestSuffix";
	        Assert.assertEquals(expected, generatedName.getSuffix());
	 }
	 
	 @Test
	  public void testGetSetIsReleased() {
		 	generatedName.setIsReleased("TestIsReleased");
	        String expected = "TestIsReleased";
	        Assert.assertEquals(expected, generatedName.getIsReleased());
	 }
	
}
