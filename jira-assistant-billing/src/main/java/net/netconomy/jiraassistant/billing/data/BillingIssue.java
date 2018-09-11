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
package net.netconomy.jiraassistant.billing.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.netconomy.jiraassistant.base.data.IssueLight;
import net.netconomy.jiraassistant.base.data.SingleTimeMetric;

public class BillingIssue extends IssueLight {
	
    private String priority = "";
    private List<String> components = new ArrayList<>();
    private String reporter = "";

    private String epicKey = "";
    private String epicName = "";

    private String accountKey = "";
    private String accountName = "";

    private List<String> linkedIssuesKeysToList = new ArrayList<>();

    private SingleTimeMetric bookedTimeInTimeFrameMinutes;
    private SingleTimeMetric originalEstimateMinutes;
    private Double personDaysBooked = 0.0;
    private Double personDaysBookedRounded = 0.0;
    private Double personDaysEstimated = 0.0;

    private String lastComment = "";

    private Double personDaysBillableMaintenanceLog = 0.0;
    private String inMaintenanceLog = "";

    private Map<String, String> additionalFields = new HashMap<>();

    public BillingIssue() {
     
	}

    public BillingIssue(IssueLight issueLight) {
        super(issueLight);
    }

    // List
    public void addLinkedIssueKeyToList(String linkedIssueKeyToList) {
        this.linkedIssuesKeysToList.add(linkedIssueKeyToList);
    }

    public void addComponent(String component) {
        this.components.add(component);
    }

    // Getters and Setters
    public SingleTimeMetric getBookedTimeInTimeFrameMinutes() {
        return bookedTimeInTimeFrameMinutes;
    }

    public void setBookedTimeInTimeFrameMinutes(Double bookedTimeInTimeFrameMinutes) {
        this.bookedTimeInTimeFrameMinutes = new SingleTimeMetric(bookedTimeInTimeFrameMinutes);
    }

    public SingleTimeMetric getOriginalEstimateMinutes() {
        return originalEstimateMinutes;
    }

    public void setOriginalEstimateMinutes(Double originalEstimateMinutes) {
        this.originalEstimateMinutes = new SingleTimeMetric(originalEstimateMinutes);
    }

    public Double getPersonDaysBooked() {
        return personDaysBooked;
    }

    public void setPersonDaysBooked(Double personDaysBooked) {
        this.personDaysBooked = personDaysBooked;
    }

    public Double getPersonDaysBookedRounded() {
        return personDaysBookedRounded;
    }

    public void setPersonDaysBookedRounded(Double personDaysBookedRounded) {
        this.personDaysBookedRounded = personDaysBookedRounded;
    }

    public Double getPersonDaysEstimated() {
        return personDaysEstimated;
    }

    public void setPersonDaysEstimated(Double personDaysEstimated) {
        this.personDaysEstimated = personDaysEstimated;
    }

    public String getAccountKey() {
        return accountKey;
    }

    public void setAccountKey(String accountKey) {
        this.accountKey = accountKey;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getEpicKey() {
        return epicKey;
    }

    public void setEpicKey(String epicKey) {
        this.epicKey = epicKey;
    }

    public String getEpicName() {
        return epicName;
    }

    public void setEpicName(String epicName) {
        this.epicName = epicName;
    }

    public List<String> getLinkedIssuesKeysToList() {
        return linkedIssuesKeysToList;
    }

    public void setLinkedIssuesKeysToList(List<String> linkedIssuesKeysToList) {
        this.linkedIssuesKeysToList = linkedIssuesKeysToList;
    }

    @Override
    public String getPriority() {
        return priority;
    }

    @Override
    public void setPriority(String priority) {
        this.priority = priority;
    }

    public List<String> getComponents() {
        return components;
    }

    public void setComponents(List<String> components) {
        this.components = components;
    }

    public String getReporter() {
        return reporter;
    }

    public void setReporter(String reporter) {
        this.reporter = reporter;
    }

    public Double getPersonDaysBillableMaintenanceLog() {
        return personDaysBillableMaintenanceLog;
    }

    public void setPersonDaysBillableMaintenanceLog(Double personDaysBillableMaintenanceLog) {
        this.personDaysBillableMaintenanceLog = personDaysBillableMaintenanceLog;
    }

    public String getInMaintenanceLog() {
        return inMaintenanceLog;
    }

    public void setInMaintenanceLog(String inMaintenanceLog) {
        this.inMaintenanceLog = inMaintenanceLog;
    }

    public String getLastComment() {
        return lastComment;
    }

    public void setLastComment(String lastComment) {
        this.lastComment = lastComment;
    }

    public Map<String, String> getAdditionalFields() {
        return additionalFields;
    }

    public void setAdditionalFields(Map<String, String> additionalFields) {
        this.additionalFields = additionalFields;
    }

    @Override
    public String toString() {
        return "BillingIssue{" +
                "priority='" + priority + '\'' +
                ", components=" + components +
                ", reporter='" + reporter + '\'' +
                ", epicKey='" + epicKey + '\'' +
                ", epicName='" + epicName + '\'' +
                ", accountKey='" + accountKey + '\'' +
                ", accountName='" + accountName + '\'' +
                ", linkedIssuesKeysToList=" + linkedIssuesKeysToList +
                ", bookedTimeInTimeFrameMinutes=" + bookedTimeInTimeFrameMinutes +
                ", originalEstimateMinutes=" + originalEstimateMinutes +
                ", personDaysBooked=" + personDaysBooked +
                ", personDaysBookedRounded=" + personDaysBookedRounded +
                ", personDaysEstimated=" + personDaysEstimated +
                ", lastComment='" + lastComment + '\'' +
                ", personDaysBillableMaintenanceLog=" + personDaysBillableMaintenanceLog +
                ", inMaintenanceLog='" + inMaintenanceLog + '\'' +
                ", additionalFields=" + additionalFields +
                '}';
    }
}
