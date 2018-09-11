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
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import net.netconomy.jiraassistant.sprintanalysis.data.defectreasons.DefectReasonData;

public class AdditionalDefectBugStatistics {

    private Integer createdDefectsBugs = 0;

    private Integer createdDefects = 0;
    private Integer createdBugs = 0;

    private Integer finishedDefects = 0;
    private Integer finishedBugs = 0;

    private Integer minutesSpentOnFinishedDefectsAndBugs = 0;

    private Integer minutesSpentOnFinishedDefects = 0;
    private Integer minutesSpentOnFinishedBugs = 0;

    private Double minutesSpentOnFinishedDefectsAndBugsPerDefectAndBug = 0.0;

    private Double minutesSpentOnFinishedDefectsPerDefect = 0.0;
    private Double minutesSpentOnFinishedBugsPerBug = 0.0;

    private Integer closedDefects = 0;
    private Integer closedBugs = 0;

    private List<String> createdDefectsList = new ArrayList<>();
    private List<String> createdBugsList = new ArrayList<>();

    private List<String> finishedDefectsList = new ArrayList<>();
    private List<String> finishedBugsList = new ArrayList<>();

    private List<String> closedDefectsList = new ArrayList<>();
    private List<String> closedBugsList = new ArrayList<>();
    
    private Map<String, DefectReasonData> defectReasonDataMap = new TreeMap<>();
    private Map<String, Integer> allDefectReasonInfoTags = new TreeMap<>();

    public AdditionalDefectBugStatistics() {

    }

    public void addDefectReasonInfoTags(Map<String, Integer> defectReasonInfoTags) {

        Integer currentCount = 0;
        Integer newCount = 0;

        for (Entry<String, Integer> currentEntry : defectReasonInfoTags.entrySet()) {

            if (this.allDefectReasonInfoTags.containsKey(currentEntry.getKey())) {
                currentCount = this.allDefectReasonInfoTags.get(currentEntry.getKey());
                newCount = currentCount + currentEntry.getValue();
            } else {
                newCount = currentEntry.getValue();
            }

            this.allDefectReasonInfoTags.put(currentEntry.getKey(), newCount);

        }

    }

    public void addDefectReasonInfoTags(List<String> defectReasonInfoTags) {

        Integer currentCount = 0;
        Integer newCount = 0;

        for (String currentKey : defectReasonInfoTags) {

            if (this.allDefectReasonInfoTags.containsKey(currentKey)) {
                currentCount = this.allDefectReasonInfoTags.get(currentKey);
                newCount = currentCount + 1;
            } else {
                newCount = 1;
            }

            this.allDefectReasonInfoTags.put(currentKey, newCount);

        }

    }

    public Map<String, DefectReasonData> getDefectReasonDataMap() {
        return defectReasonDataMap;
    }

    public void setDefectReasonDataMap(Map<String, DefectReasonData> defectReasonDataMap) {
        this.defectReasonDataMap = defectReasonDataMap;
    }

    public Map<String, Integer> getAllDefectReasonInfoTags() {
        return allDefectReasonInfoTags;
    }

    public void setAllDefectReasonInfoTags(Map<String, Integer> allDefectReasonInfoTags) {
        this.allDefectReasonInfoTags = allDefectReasonInfoTags;
    }

    public Integer getCreatedDefects() {
        return createdDefects;
    }

    public void setCreatedDefects(Integer createdDefects) {
        this.createdDefects = createdDefects;
    }

    public Integer getCreatedBugs() {
        return createdBugs;
    }

    public void setCreatedBugs(Integer createdBugs) {
        this.createdBugs = createdBugs;
    }

    public Integer getCreatedDefectsBugs() {
        return createdDefectsBugs;
    }

    public void setCreatedDefectsBugs(Integer createdDefectsBugs) {
        this.createdDefectsBugs = createdDefectsBugs;
    }

    public Integer getFinishedDefects() {
        return finishedDefects;
    }

    public void setFinishedDefects(Integer finishedDefects) {
        this.finishedDefects = finishedDefects;
    }

    public Integer getFinishedBugs() {
        return finishedBugs;
    }

    public void setFinishedBugs(Integer finishedBugs) {
        this.finishedBugs = finishedBugs;
    }

    public Integer getMinutesSpentOnFinishedDefectsAndBugs() {
        return minutesSpentOnFinishedDefectsAndBugs;
    }

    public void setMinutesSpentOnFinishedDefectsAndBugs(Integer minutesSpentOnFinishedDefectsAndBugs) {
        this.minutesSpentOnFinishedDefectsAndBugs = minutesSpentOnFinishedDefectsAndBugs;
    }

    public Integer getMinutesSpentOnFinishedDefects() {
        return minutesSpentOnFinishedDefects;
    }

    public void setMinutesSpentOnFinishedDefects(Integer minutesSpentOnFinishedDefects) {
        this.minutesSpentOnFinishedDefects = minutesSpentOnFinishedDefects;
    }

    public Integer getMinutesSpentOnFinishedBugs() {
        return minutesSpentOnFinishedBugs;
    }

    public void setMinutesSpentOnFinishedBugs(Integer minutesSpentOnFinishedBugs) {
        this.minutesSpentOnFinishedBugs = minutesSpentOnFinishedBugs;
    }

    public Double getMinutesSpentOnFinishedDefectsAndBugsPerDefectAndBug() {
        return minutesSpentOnFinishedDefectsAndBugsPerDefectAndBug;
    }

    public void setMinutesSpentOnFinishedDefectsAndBugsPerDefectAndBug(
            Double minutesSpentOnFinishedDefectsAndBugsPerDefectAndBug) {
        this.minutesSpentOnFinishedDefectsAndBugsPerDefectAndBug = minutesSpentOnFinishedDefectsAndBugsPerDefectAndBug;
    }

    public Double getMinutesSpentOnFinishedDefectsPerDefect() {
        return minutesSpentOnFinishedDefectsPerDefect;
    }

    public void setMinutesSpentOnFinishedDefectsPerDefect(Double minutesSpentOnFinishedDefectsPerDefect) {
        this.minutesSpentOnFinishedDefectsPerDefect = minutesSpentOnFinishedDefectsPerDefect;
    }

    public Double getMinutesSpentOnFinishedBugsPerBug() {
        return minutesSpentOnFinishedBugsPerBug;
    }

    public void setMinutesSpentOnFinishedBugsPerBug(Double minutesSpentOnFinishedBugsPerBug) {
        this.minutesSpentOnFinishedBugsPerBug = minutesSpentOnFinishedBugsPerBug;
    }

    public Integer getClosedDefects() {
        return closedDefects;
    }

    public void setClosedDefects(Integer closedDefects) {
        this.closedDefects = closedDefects;
    }

    public Integer getClosedBugs() {
        return closedBugs;
    }

    public void setClosedBugs(Integer closedBugs) {
        this.closedBugs = closedBugs;
    }

    public List<String> getCreatedDefectsList() {
        return createdDefectsList;
    }

    public void setCreatedDefectsList(List<String> createdDefectsList) {
        this.createdDefectsList = createdDefectsList;
    }

    public List<String> getCreatedBugsList() {
        return createdBugsList;
    }

    public void setCreatedBugsList(List<String> createdBugsList) {
        this.createdBugsList = createdBugsList;
    }

    public List<String> getFinishedDefectsList() {
        return finishedDefectsList;
    }

    public void setFinishedDefectsList(List<String> finishedDefectsList) {
        this.finishedDefectsList = finishedDefectsList;
    }

    public List<String> getFinishedBugsList() {
        return finishedBugsList;
    }

    public void setFinishedBugsList(List<String> finishedBugsList) {
        this.finishedBugsList = finishedBugsList;
    }

    public List<String> getClosedDefectsList() {
        return closedDefectsList;
    }

    public void setClosedDefectsList(List<String> closedDefectsList) {
        this.closedDefectsList = closedDefectsList;
    }

    public List<String> getClosedBugsList() {
        return closedBugsList;
    }

    public void setClosedBugsList(List<String> closedBugsList) {
        this.closedBugsList = closedBugsList;
    }

    @Override
    public String toString() {
        return "AdditionalDefectBugStatistics [createdDefectsBugs=" + createdDefectsBugs + ", createdDefects="
                + createdDefects + ", createdBugs=" + createdBugs + ", finishedDefects=" + finishedDefects
                + ", finishedBugs=" + finishedBugs + ", minutesSpentOnFinishedDefectsAndBugs="
                + minutesSpentOnFinishedDefectsAndBugs + ", minutesSpentOnFinishedDefects="
                + minutesSpentOnFinishedDefects + ", minutesSpentOnFinishedBugs=" + minutesSpentOnFinishedBugs
                + ", minutesSpentOnFinishedDefectsAndBugsPerDefectAndBug="
                + minutesSpentOnFinishedDefectsAndBugsPerDefectAndBug + ", minutesSpentOnFinishedDefectsPerDefect="
                + minutesSpentOnFinishedDefectsPerDefect + ", minutesSpentOnFinishedBugsPerBug="
                + minutesSpentOnFinishedBugsPerBug + ", closedDefects=" + closedDefects + ", closedBugs=" + closedBugs
                + ", createdDefectsList=" + createdDefectsList + ", createdBugsList=" + createdBugsList
                + ", finishedDefectsList=" + finishedDefectsList + ", finishedBugsList=" + finishedBugsList
                + ", closedDefectsList=" + closedDefectsList + ", closedBugsList=" + closedBugsList
                + ", defectReasonDataMap=" + defectReasonDataMap + ", allDefectReasonInfoTags="
                + allDefectReasonInfoTags + "]";
    }

}
