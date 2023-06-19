/**
 * ============LICENSE_START====================================================
 * org.onap.ccsdk
 * ===========================================================================
 * Copyright (c) 2023 AT&T Intellectual Property. All rights reserved.
 * ===========================================================================
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
 * ============LICENSE_END====================================================
 */

package org.onap.ccsdk.apps.cadi.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

import org.onap.ccsdk.apps.cadi.util.Holder;

/**
 * Add various Filters by CADI Property not in the official Chain
 *
 * @author Instrumental(Jonathan)
 *
 */
public class SideChain {
    private List<Filter> sideChain;

    public SideChain() {
        sideChain = new ArrayList<Filter>();
    }

    public void add(Filter f) {
        sideChain.add(f);
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)throws IOException, ServletException {
        final Holder<Boolean> hbool = new Holder<Boolean>(Boolean.TRUE);
        FilterChain truth = new FilterChain() {
            @Override
            public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
               hbool.set(Boolean.TRUE);
            }
            public String toString() {
                return hbool.get().toString();
            }
        };
        for(Filter f : sideChain) {
            hbool.set(Boolean.FALSE);
            f.doFilter(request, response, truth);
            if(!hbool.get()) {
                return;
            }
        }
        if(hbool.get()) {
            chain.doFilter(request, response);
        }
    }
}
