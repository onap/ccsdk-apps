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

package org.onap.ccsdk.apps.controllerblueprints.service.common;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;
import org.onap.ccsdk.apps.controllerblueprints.service.common.ErrorMessage;

public class ErrorMessageTest {

    private static final String MESSAGE ="testMessage";
    private static final Integer CODE=404;
    private static final String DEBUGMESSAGE="Check the code";
    ErrorMessage errorMessage = new ErrorMessage(MESSAGE,CODE,DEBUGMESSAGE);

    @Test
    public void testErrorMessage(){
        assertEquals("testMessage", errorMessage.getMessage());
        assertEquals("404", errorMessage.getCode());
        assertEquals("Check the code",errorMessage.getMessage());
    }

    @Test
    public void testErrorMessageNotNull() {
        Assert.assertNotNull(errorMessage);
    }
}
