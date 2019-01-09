/*
 * Copyright © 2018-2019 Bell Canada Intellectual Property.
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
 */

package org.onap.ccsdk.apps.controllerblueprints.service.model;

import org.onap.ccsdk.apps.controllerblueprints.service.domain.BlueprintModelSearch;

import java.util.List;

/**
 * BlueprintModelSearchList.java Purpose: POJO to maintain list of BlueprintModelSearch
 *
 * @author Steve Siani
 * @version 1.0
 */
public class BlueprintModelSearchList {
    private List<BlueprintModelSearch> blueprintModelSearchList;

    public List<BlueprintModelSearch> getBlueprintModelSearchList() {
        return blueprintModelSearchList;
    }

    public void setBlueprintModelSearchList(List<BlueprintModelSearch> blueprintModelSearchList) {
        this.blueprintModelSearchList = blueprintModelSearchList;
    }
}
