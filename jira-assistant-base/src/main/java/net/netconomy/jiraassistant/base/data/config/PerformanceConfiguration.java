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
package net.netconomy.jiraassistant.base.data.config;

public class PerformanceConfiguration {

    private Integer maxKanbanResults;
    private Integer maxSprintsMultiSprintAnalysis;

    public PerformanceConfiguration() {

    }

    public Integer getMaxKanbanResults() {
        return maxKanbanResults;
    }

    public void setMaxKanbanResults(Integer maxKanbanResults) {
        this.maxKanbanResults = maxKanbanResults;
    }

    public Integer getMaxSprintsMultiSprintAnalysis() {
        return maxSprintsMultiSprintAnalysis;
    }

    public void setMaxSprintsMultiSprintAnalysis(Integer maxSprintsMultiSprintAnalysis) {
        this.maxSprintsMultiSprintAnalysis = maxSprintsMultiSprintAnalysis;
    }

    @Override
    public String toString() {
        return "PerformanceConfiguration [maxKanbanResults=" + maxKanbanResults + ", maxSprintsMultiSprintAnalysis="
                + maxSprintsMultiSprintAnalysis + "]";
    }

}
