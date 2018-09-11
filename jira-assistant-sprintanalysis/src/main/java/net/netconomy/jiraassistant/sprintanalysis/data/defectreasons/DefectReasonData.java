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

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class DefectReasonData {

    private String reasonName = "";
    private Integer reasonCount = 0;
    private Double reasonMinutesSpentAliquot = 0.0;
    private Integer reasonMinutesSpentFull = 0;
    private Map<String, Integer> defectReasonInfoTags = new TreeMap<>();

    public DefectReasonData() {

    }

    public void addDefectReasonInfoTag(String defectReasonInfoTag) {

        Integer newCount = 0;

        if (defectReasonInfoTags.containsKey(defectReasonInfoTag)) {
            newCount = defectReasonInfoTags.get(defectReasonInfoTag) + 1;
        } else {
            newCount = 1;
        }

        defectReasonInfoTags.put(defectReasonInfoTag, newCount);

    }

    public void addDefectReasonInfoTags(List<String> defectReasonInfoTags) {

        for (String currentInfoTag : defectReasonInfoTags) {
            addDefectReasonInfoTag(currentInfoTag);
        }

    }

    public String getReasonName() {
        return reasonName;
    }

    public void setReasonName(String reasonName) {
        this.reasonName = reasonName;
    }

    public Integer getReasonCount() {
        return reasonCount;
    }

    public void setReasonCount(Integer reasonCount) {
        this.reasonCount = reasonCount;
    }

    public Double getReasonMinutesSpentAliquot() {
        return reasonMinutesSpentAliquot;
    }

    public void setReasonMinutesSpentAliquot(Double reasonMinutesSpentAliquot) {
        this.reasonMinutesSpentAliquot = reasonMinutesSpentAliquot;
    }

    public Integer getReasonMinutesSpentFull() {
        return reasonMinutesSpentFull;
    }

    public void setReasonMinutesSpentFull(Integer reasonMinutesSpentFull) {
        this.reasonMinutesSpentFull = reasonMinutesSpentFull;
    }

    public Map<String, Integer> getDefectReasonInfoTags() {
        return defectReasonInfoTags;
    }

    public void setDefectReasonInfoTags(Map<String, Integer> defectReasonInfoTags) {
        this.defectReasonInfoTags = defectReasonInfoTags;
    }

    @Override
    public String toString() {
        return "DefectReasonData [reasonName=" + reasonName + ", reasonCount=" + reasonCount
                + ", reasonMinutesSpentAliquot=" + reasonMinutesSpentAliquot + ", reasonMinutesSpentFull="
                + reasonMinutesSpentFull + ", defectReasonInfoTags=" + defectReasonInfoTags + "]";
    }

}
