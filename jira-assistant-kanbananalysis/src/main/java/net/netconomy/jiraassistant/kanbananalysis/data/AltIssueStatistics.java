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

import net.netconomy.jiraassistant.sprintanalysis.data.flagging.FlaggingStatistics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AltIssueStatistics {

    private Integer numberOfIssues = 0;
    private Integer numberOfSubIssues = 0;

    private Map<String, Integer> issuesByEstimation = new HashMap<>();

    private Integer reopenCount = 0;

    private Integer minutesSpent = 0;

    private Integer inProgressIssues = 0;
    private Integer waitingIssues = 0;
    private Integer implementedIssues = 0;
    private Integer finishedIssues = 0;
    private Integer closedIssues = 0;

    private Map<String, Integer> inProgress = new HashMap<>();
    private Map<String, Integer> waiting = new HashMap<>();
    private Map<String, Integer> implemented = new HashMap<>();
    private Map<String, Integer> finished = new HashMap<>();
    private Map<String, Integer> closed = new HashMap<>();

    private List<String> inProgressKeys = new ArrayList<>();
    private List<String> waitingKeys = new ArrayList<>();
    private List<String> implementedKeys = new ArrayList<>();
    private List<String> finishedKeys = new ArrayList<>();
    private List<String> closedKeys = new ArrayList<>();

    private FlaggingStatistics flaggingStatistics;

    public AltIssueStatistics() {

    }

    //Adders
    public void addIssueWithEstimation(String altEstimation) {
        if(issuesByEstimation.containsKey(altEstimation)) {
            issuesByEstimation.put(altEstimation, issuesByEstimation.get(altEstimation) + 1);
        } else {
            issuesByEstimation.put(altEstimation, 1);
        }
    }

    public void raiseInProgress(String altEstimation) {
        if(inProgress.containsKey(altEstimation)) {
            inProgress.put(altEstimation, inProgress.get(altEstimation) + 1);
        } else {
            inProgress.put(altEstimation, 1);
        }
    }

    public void raiseWaiting(String altEstimation) {
        if(waiting.containsKey(altEstimation)) {
            waiting.put(altEstimation, waiting.get(altEstimation) + 1);
        } else {
            waiting.put(altEstimation, 1);
        }
    }

    public void raiseImplemented(String altEstimation) {
        if(implemented.containsKey(altEstimation)) {
            implemented.put(altEstimation, implemented.get(altEstimation) + 1);
        } else {
            implemented.put(altEstimation, 1);
        }
    }

    public void raiseFinished(String altEstimation) {
        if(finished.containsKey(altEstimation)) {
            finished.put(altEstimation, finished.get(altEstimation) + 1);
        } else {
            finished.put(altEstimation, 1);
        }
    }

    public void raiseClosed(String altEstimation) {
        if(closed.containsKey(altEstimation)) {
            closed.put(altEstimation, closed.get(altEstimation) + 1);
        } else {
            closed.put(altEstimation, 1);
        }
    }

    public void addInProgressKey(String inProgressKey) {
        this.inProgressKeys.add(inProgressKey);
    }

    public void addWaitingKey(String waitingKey) {
        this.waitingKeys.add(waitingKey);
    }

    public void addImplementedKey(String implementedKey) {
        this.implementedKeys.add(implementedKey);
    }

    public void addFinishedKey(String finishedKey) {
        this.finishedKeys.add(finishedKey);
    }

    public void addClosedKey(String closedKey) {
        this.closedKeys.add(closedKey);
    }

    // Getters and Setters
    public Integer getNumberOfIssues() {
        return numberOfIssues;
    }

    public void setNumberOfIssues(Integer numberOfIssues) {
        this.numberOfIssues = numberOfIssues;
    }

    public Integer getNumberOfSubIssues() {
        return numberOfSubIssues;
    }

    public void setNumberOfSubIssues(Integer numberOfSubIssues) {
        this.numberOfSubIssues = numberOfSubIssues;
    }

    public Map<String, Integer> getIssuesByEstimation() {
        return issuesByEstimation;
    }

    public void setIssuesByEstimation(Map<String, Integer> issuesByEstimation) {
        this.issuesByEstimation = issuesByEstimation;
    }

    public Integer getReopenCount() {
        return reopenCount;
    }

    public void setReopenCount(Integer reopenCount) {
        this.reopenCount = reopenCount;
    }

    public Integer getMinutesSpent() {
        return minutesSpent;
    }

    public void setMinutesSpent(Integer minutesSpent) {
        this.minutesSpent = minutesSpent;
    }

    public Integer getInProgressIssues() {
        return inProgressIssues;
    }

    public void setInProgressIssues(Integer inProgressIssues) {
        this.inProgressIssues = inProgressIssues;
    }

    public Integer getWaitingIssues() {
        return waitingIssues;
    }

    public void setWaitingIssues(Integer waitingIssues) {
        this.waitingIssues = waitingIssues;
    }

    public Integer getImplementedIssues() {
        return implementedIssues;
    }

    public void setImplementedIssues(Integer implementedIssues) {
        this.implementedIssues = implementedIssues;
    }

    public Integer getFinishedIssues() {
        return finishedIssues;
    }

    public void setFinishedIssues(Integer finishedIssues) {
        this.finishedIssues = finishedIssues;
    }

    public Integer getClosedIssues() {
        return closedIssues;
    }

    public void setClosedIssues(Integer closedIssues) {
        this.closedIssues = closedIssues;
    }

    public Map<String, Integer> getInProgress() {
        return inProgress;
    }

    public void setInProgress(Map<String, Integer> inProgress) {
        this.inProgress = inProgress;
    }

    public Map<String, Integer> getWaiting() {
        return waiting;
    }

    public void setWaiting(Map<String, Integer> waiting) {
        this.waiting = waiting;
    }

    public Map<String, Integer> getImplemented() {
        return implemented;
    }

    public void setImplemented(Map<String, Integer> implemented) {
        this.implemented = implemented;
    }

    public Map<String, Integer> getFinished() {
        return finished;
    }

    public void setFinished(Map<String, Integer> finished) {
        this.finished = finished;
    }

    public Map<String, Integer> getClosed() {
        return closed;
    }

    public void setClosed(Map<String, Integer> closed) {
        this.closed = closed;
    }

    public List<String> getInProgressKeys() {
        return inProgressKeys;
    }

    public void setInProgressKeys(List<String> inProgressKeys) {
        this.inProgressKeys = inProgressKeys;
    }

    public List<String> getWaitingKeys() {
        return waitingKeys;
    }

    public void setWaitingKeys(List<String> waitingKeys) {
        this.waitingKeys = waitingKeys;
    }

    public List<String> getImplementedKeys() {
        return implementedKeys;
    }

    public void setImplementedKeys(List<String> implementedKeys) {
        this.implementedKeys = implementedKeys;
    }

    public List<String> getFinishedKeys() {
        return finishedKeys;
    }

    public void setFinishedKeys(List<String> finishedKeys) {
        this.finishedKeys = finishedKeys;
    }

    public List<String> getClosedKeys() {
        return closedKeys;
    }

    public void setClosedKeys(List<String> closedKeys) {
        this.closedKeys = closedKeys;
    }

    public FlaggingStatistics getFlaggingStatistics() {
        return flaggingStatistics;
    }

    public void setFlaggingStatistics(FlaggingStatistics flaggingStatistics) {
        this.flaggingStatistics = flaggingStatistics;
    }

    @Override
    public String toString() {
        return "AltIssueStatistics{" +
            "numberOfIssues=" + numberOfIssues +
            ", numberOfSubIssues=" + numberOfSubIssues +
            ", issuesByEstimation=" + issuesByEstimation +
            ", reopenCount=" + reopenCount +
            ", minutesSpent=" + minutesSpent +
            ", inProgressIssues=" + inProgressIssues +
            ", waitingIssues=" + waitingIssues +
            ", implementedIssues=" + implementedIssues +
            ", finishedIssues=" + finishedIssues +
            ", closedIssues=" + closedIssues +
            ", inProgress=" + inProgress +
            ", waiting=" + waiting +
            ", implemented=" + implemented +
            ", finished=" + finished +
            ", closed=" + closed +
            ", inProgressKeys=" + inProgressKeys +
            ", waitingKeys=" + waitingKeys +
            ", implementedKeys=" + implementedKeys +
            ", finishedKeys=" + finishedKeys +
            ", closedKeys=" + closedKeys +
            ", flaggingStatistics=" + flaggingStatistics +
            '}';
    }

}
