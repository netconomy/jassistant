/**
*  Copyright 2018 The JASSISTANT Authors.
*
*  Licensed under the Apache License, Version 2.0 (the "License");
*  you may not use this file except in compliance with the License.
*  You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing, software
*  distributed under the License is distributed on an "AS IS" BASIS,
*  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*  See the License for the specific language governing permissions and
*  limitations under the License.
**/
package net.netconomy.jiraassistant.sprintanalysis.data;

import java.util.ArrayList;
import java.util.List;

public class MultipleSprintAnalysisData {

    private List<SprintResultData> sprintResultList = new ArrayList<>();

    public MultipleSprintAnalysisData() {

    }

    public void addSprintResult(SprintResultData sprintResultData) {
        this.sprintResultList.add(sprintResultData);
    }

    public List<SprintResultData> getSprintResultList() {
        return sprintResultList;
    }

    public void setSprintResultList(List<SprintResultData> sprintResultList) {
        this.sprintResultList = sprintResultList;
    }

    @Override
    public String toString() {
        return "MultipleSprintAnalysisData [sprintResultList=" + sprintResultList + "]";
    }

}
