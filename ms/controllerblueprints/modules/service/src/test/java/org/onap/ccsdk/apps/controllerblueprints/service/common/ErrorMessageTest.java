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
