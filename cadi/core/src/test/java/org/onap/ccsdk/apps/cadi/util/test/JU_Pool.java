/*******************************************************************************
 * * org.onap.ccsdk
 * * ===========================================================================
 * * Copyright © 2023 AT&T Intellectual Property. All rights reserved.
 * * ===========================================================================
 * * Licensed under the Apache License, Version 2.0 (the "License");
 * * you may not use this file except in compliance with the License.
 * * You may obtain a copy of the License at
 * *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 * *
 *  * Unless required by applicable law or agreed to in writing, software
 * * distributed under the License is distributed on an "AS IS" BASIS,
 * * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * * See the License for the specific language governing permissions and
 * * limitations under the License.
 * * ============LICENSE_END====================================================
 * *
 * *
 ******************************************************************************/

package org.onap.ccsdk.apps.cadi.util.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import org.junit.*;
import org.onap.ccsdk.apps.cadi.CadiException;
import org.onap.ccsdk.apps.cadi.util.Log;
import org.onap.ccsdk.apps.cadi.util.Pool;
import org.onap.ccsdk.apps.cadi.util.Pool.*;

public class JU_Pool {

    private class IntegerCreator implements Creator<Integer> {
        private int current = 0;

        @Override
        public Integer create() {
            return current++;
        }

        @Override
        public void destroy(Integer t) {
            t = 0;
        }

        @Override
        public boolean isValid(Integer t) {
            return (t & 0x1) == 0;
        }

        @Override
        public void reuse(Integer t) {
        }
    }

    // Used for CustomLogger Testing
    private StringBuilder sb = new StringBuilder();

    private class CustomLogger implements Log {
        @Override
        public void log(Log.Type type, Object... o) {
            for (Object item : o) {
                sb.append(item.toString());
            }
        }
    }

    /**
	 * Enter variable amount in this order 
	 * 
	 *   count, used, max_range, max_objects
	 * @param intPool
	 * @param ints
	 */
	private void check(Pool<Integer> intPool, int ... ints) {
		String rpt = intPool.toString();
		// Fallthrough on purpose, to process only the ints entered, but in the right order.
		switch(ints.length) {
			case 4:
		    	assertTrue(rpt.contains(String.format("max_objects(%d)", ints[3])));
			case 3:
		    	assertTrue(rpt.contains(String.format("max_range(%d)", ints[2])));
			case 2:
		    	assertTrue(rpt.contains(String.format("used(%d)", ints[1])));
			case 1:
				assertTrue(rpt.contains(String.format("count(%d)", ints[0])));
		}
	}

	@Test
    public void settings() throws CadiException {
    	Pool<Integer> intPool = new Pool<Integer>(new IntegerCreator());
    	check(intPool,0,0,Pool.MAX_RANGE,Pool.MAX_OBJECTS);

    	// Check MaxObjects, min is 0
    	intPool.setMaxObjects(-10);
    	check(intPool,0,0,Pool.MAX_RANGE,0);

    	intPool.setMaxObjects(10);
    	check(intPool,0,0,Pool.MAX_RANGE,10);

    	// Check MaxRange, min is 0
    	intPool.setMaxRange(-10);
    	check(intPool,0,0,0,10);

    	intPool.setMaxRange(2);
    	check(intPool,0,0,2,10);

    	// Validate Priming
    	intPool.prime(3);
    	check(intPool,3,3,2,10);
    	
    	// Drain 
    	intPool.drain();
    	check(intPool,0,0,2,10);
    }
    
    @Test
    public void range() throws CadiException {
    	Pool<Integer> intPool = new Pool<Integer>(new IntegerCreator());
    	intPool.setMaxRange(2); 
    	check(intPool,0,0,2);
    	
    	// Prime
    	intPool.prime(3);
    	check(intPool,3,3,2);
    	
    	// Using 3 leaves count (in Pool) and Used (by System) 3
    	List<Pooled<Integer>> using = new ArrayList<>();
    	for(int i=0;i<3;++i) {
    		using.add(intPool.get());
    	}
    	check(intPool,0,3,2);

    	// Using 3 more creates more Objects, and uses immediately
    	for(int i=0;i<3;++i) {
    		using.add(intPool.get());
    	}
    	check(intPool,0,6,2);
    	
    	// Clean out all Objects in possession, but there are 6 Objects not returned yet.
    	intPool.drain();
    	check(intPool,0,6,2);
    	
    	// Returning Objects 
    	for(Pooled<Integer> i : using)  {
    		i.done();
    	}
    	
    	// Since Range is 2, keep only 2, and destroy the rest
    	check(intPool,2,2,2);

    	// Shutdown (helpful for stopping Services) involves turning off range
    	intPool.setMaxRange(0).drain();
    	check(intPool,0,0,0);
    }
    
    @Test
	public void tooManyObjects() throws CadiException {
    	/*
    	 * It should be noted that "tooManyObjects" isn't enforced by the Pool, because Objects are not 
    	 * tracked (other than used) once they leave the pool.  
    	 * 
    	 * It is information that using entities, like Thread Pools, can use to limit creations of expensive objects
    	 */
		Pool<Integer> intPool = new Pool<Integer>(new IntegerCreator());
		intPool.setMaxObjects(10).setMaxRange(2);
		check(intPool,0,0,2,10);

		assertFalse(intPool.tooManyObjects());

		// Obtain up to maxium Objects
		List<Pooled<Integer>> using = new ArrayList<>();
		for(int i=0;i<10;++i) {
			using.add(intPool.get());
		}
		
		check(intPool,0,10,2,10);
		assertFalse(intPool.tooManyObjects());
		
		using.add(intPool.get());
		check(intPool,0,11,2,10);
		assertTrue(intPool.tooManyObjects());
		
    	// Returning Objects 
    	for(Pooled<Integer> i : using)  {
    		i.done();
    	}
    	
    	// Returning Objects puts Pool back in range
		check(intPool,2,2,2,10);
		assertFalse(intPool.tooManyObjects());

	}

	@Test
    public void bulkTest() throws CadiException {
        Pool<Integer> intPool = new Pool<Integer>(new IntegerCreator());

        intPool.prime(10);
        // Remove all of the invalid items (in this case, odd numbers)
        assertFalse(intPool.validate());

        // Make sure we got them all
        assertTrue(intPool.validate());

        // Get an item from the pool
        Pooled<Integer> gotten = intPool.get();
        assertThat(gotten.content, is(0));

        // finalize that item, then check the next one to make sure we actually purged
        // the odd numbers
        gotten = intPool.get();
        assertThat(gotten.content, is(2));

        intPool.drain();

    }

    @Test
    public void loggingTest() {
        Pool<Integer> intPool = new Pool<Integer>(new IntegerCreator());

        // Log to Log.NULL for coverage
        intPool.log(Log.Type.info,"Test log output");

        intPool.setLogger(new CustomLogger());
        intPool.log(Log.Type.info,"Test log output");

        assertThat(sb.toString(), is("Test log output"));
    }

}
