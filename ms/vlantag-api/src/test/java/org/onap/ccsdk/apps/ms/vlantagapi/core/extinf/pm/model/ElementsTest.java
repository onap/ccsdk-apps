/*******************************************************************************
 * Copyright © 2018 IBM.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************
*/

package org.onap.ccsdk.apps.ms.vlantagapi.core.extinf.pm.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class ElementsTest {
	private Elements elements;

	@Before
	public void setUp() {
		elements = new Elements();
	}

	@Test
	public void testGetSetRecycleVlantagRange() {
		elements.setRecycleVlantagRange("recycleVlantagRange");
		assertEquals("recycleVlantagRange", elements.getRecycleVlantagRange());
	}

	@Test
	public void testGetSetOverwrite() {
		elements.setOverwrite("overwrite");
		assertEquals("overwrite", elements.getOverwrite());
	}

	@Test
	public void testGetSetElementVlanRole() {
		elements.setElementVlanRole("elementVlanRole");
		assertEquals("elementVlanRole", elements.getElementVlanRole());
	}

	@Test
	public void testToString() {
		assertTrue(elements.toString() instanceof String);
	}
}
