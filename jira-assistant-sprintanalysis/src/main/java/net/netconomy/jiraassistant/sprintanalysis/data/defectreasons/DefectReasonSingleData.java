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
package net.netconomy.jiraassistant.sprintanalysis.data.defectreasons;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class DefectReasonSingleData {

    private String issueKey = "";
    private Integer minutesSpent = 0;
    private List<String> defectReasons = new ArrayList<>();
    private List<String> defectReasonInfos = new ArrayList<>();

    private Map<String, Double> minutesPerReasonAliquot = new TreeMap<>();

    public DefectReasonSingleData() {

    }

    public void addMinutesPerReasonAliquot(String reasonName, Double minutes) {
        this.minutesPerReasonAliquot.put(reasonName, minutes);
    }

    public String getIssueKey() {
        return issueKey;
    }

    public void setIssueKey(String issueKey) {
        this.issueKey = issueKey;
    }

    public Integer getMinutesSpent() {
        return minutesSpent;
    }

    public void setMinutesSpent(Integer minutesSpent) {
        this.minutesSpent = minutesSpent;
    }

    public List<String> getDefectReasons() {
        return defectReasons;
    }

    public void setDefectReasons(List<String> defectReasons) {
        this.defectReasons = defectReasons;
    }

    public List<String> getDefectReasonInfos() {
        return defectReasonInfos;
    }

    public void setDefectReasonInfos(List<String> defectReasonInfos) {
        this.defectReasonInfos = defectReasonInfos;
    }

    public Map<String, Double> getMinutesPerReasonAliquot() {
        return minutesPerReasonAliquot;
    }

    public void setMinutesPerReasonAliquot(Map<String, Double> minutesPerReasonAliquot) {
        this.minutesPerReasonAliquot = minutesPerReasonAliquot;
    }

    @Override
    public String toString() {
        return "SingleDefectReasonData [issueKey=" + issueKey + ", minutesSpent=" + minutesSpent + ", defectReasons="
                + defectReasons + ", defectReasonInfos=" + defectReasonInfos + ", minutesPerReasonAliquot="
                + minutesPerReasonAliquot + "]";
    }

}
