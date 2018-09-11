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
package net.netconomy.jiraassistant.kanbananalysis.data;

import java.util.ArrayList;
import java.util.List;

public class MultipleKanbanAnalysisData {

    private List<KanbanResultData> kanbanResultList = new ArrayList<>();

    public MultipleKanbanAnalysisData() {

    }

    public void addKanbanResult(KanbanResultData kanbanResultData) {
        this.kanbanResultList.add(kanbanResultData);
    }

    public List<KanbanResultData> getKanbanResultList() {
        return kanbanResultList;
    }

    public void setKanbanResultList(List<KanbanResultData> kanbanResultList) {
        this.kanbanResultList = kanbanResultList;
    }

    @Override
    public String toString() {
        return "MultipleKanbanAnalysisData [kanbanResultList=" + kanbanResultList + "]";
    }

}
