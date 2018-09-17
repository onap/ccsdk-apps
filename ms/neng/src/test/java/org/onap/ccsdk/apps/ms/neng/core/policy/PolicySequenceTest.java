package org.onap.ccsdk.apps.ms.neng.core.policy;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PolicySequenceTest {

    @Test
    public void testGetMaxValue() throws Exception {
        PolicySequence seq = new PolicySequence();
        seq.setMaxValueString("0AB");
        seq.setType(PolicySequence.Type.ALPHA);
        assertEquals(371L, seq.getMaxValue());
    }

    @Test
    public void testGetMaxValue_Exp() throws Exception {
        PolicySequence seq = new PolicySequence();
        seq.setMaxValueString("0AB");
        seq.setType(PolicySequence.Type.NUMERIC);
        assertEquals(999, seq.getMaxValue());
    }

    @Test
    public void testGetMaxValue_Numeric() throws Exception {
        PolicySequence seq = new PolicySequence();
        seq.setMaxValueString("011");
        seq.setType(PolicySequence.Type.NUMERIC);
        assertEquals(11, seq.getMaxValue());
    }

    @Test
    public void testGetMaxValue_null_maxvalue() throws Exception {
        PolicySequence seq = new PolicySequence();
        seq.setLength(3);
        seq.setType(PolicySequence.Type.NUMERIC);
        assertEquals(999, seq.getMaxValue());
    }
}
