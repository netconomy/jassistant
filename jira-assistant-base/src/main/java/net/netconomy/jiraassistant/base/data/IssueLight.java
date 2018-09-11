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
package net.netconomy.jiraassistant.base.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class IssueLight {

    private String key = "";
    private String project = "";
    private String issueType = "";
    private String summary = "";
    private String status = "";
    private String assignee = "";
    private String priority = "";
    private String createdDate = "";
    private String updatedDate = "";
    private String dueDate = "";
    private String desiredDate = "";
    private String resolution = "";
    private Integer minutesEstimated = 0;
    private Integer minutesSpent = 0;
    private Integer minutesRemaining = 0;
    private String accountKey = "";
    private String accountName = "";

    private List<String> fixVersions = new ArrayList<>();

    private Map<String, String> fields = new TreeMap<>();

    private List<String> wantedFields;

    public IssueLight() {

    }

    public IssueLight(IssueLight issueLight) {
        this.key = issueLight.getKey();
        this.project = issueLight.getProject();
        this.issueType = issueLight.getIssueType();
        this.summary = issueLight.getSummary();
        this.status = issueLight.getStatus();
        this.assignee = issueLight.getAssignee();
        this.priority = issueLight.getPriority();
        this.createdDate = issueLight.getCreatedDate();
        this.updatedDate = issueLight.getUpdatedDate();
        this.dueDate = issueLight.getDueDate();
        this.desiredDate = issueLight.getDesiredDate();
        this.resolution = issueLight.getResolution();
        this.minutesEstimated = issueLight.getMinutesEstimated();
        this.minutesSpent = issueLight.getMinutesSpent();
        this.minutesRemaining = issueLight.getMinutesRemaining();
        this.accountKey = issueLight.getAccountKey();
        this.accountName = issueLight.getAccountName();
        this.fixVersions = issueLight.getFixVersions();
    }

    public void addWantedField(String wantedFieldName) {
        this.wantedFields.add(wantedFieldName);
    }

    public void addField(String fieldName, String fieldValue) {
        this.fields.put(fieldName, fieldValue);
    }

    public void addFixVersion(String fixVersionName) {
        this.fixVersions.add(fixVersionName);
    }

    public String getFieldByName(String fieldName) {
        return this.fields.get(fieldName);
    }

    // Getters and Setters
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getIssueType() {
        return issueType;
    }

    public void setIssueType(String issueType) {
        this.issueType = issueType;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public List<String> getFixVersions() {
        return fixVersions;
    }

    public void setFixVersions(List<String> fixVersions) {
        this.fixVersions = fixVersions;
    }

    public Map<String, String> getFields() {
        return fields;
    }

    public void setFields(Map<String, String> fields) {
        this.fields = fields;
    }

    public List<String> getWantedFields() {
        return wantedFields;
    }

    public void setWantedFields(List<String> wantedFields) {
        this.wantedFields = wantedFields;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public Integer getMinutesEstimated() {
        return minutesEstimated;
    }

    public void setMinutesEstimated(Integer minutesEstimated) {
        this.minutesEstimated = minutesEstimated;
    }

    public Integer getMinutesSpent() {
        return minutesSpent;
    }

    public void setMinutesSpent(Integer minutesSpent) {
        this.minutesSpent = minutesSpent;
    }

    public Integer getMinutesRemaining() {
        return minutesRemaining;
    }

    public void setMinutesRemaining(Integer minutesRemaining) {
        this.minutesRemaining = minutesRemaining;
    }

    public String getAccountKey() {
        return accountKey;
    }

    public void setAccountKey(String accountID) {
        this.accountKey = accountID;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getDesiredDate() {
        return desiredDate;
    }

    public void setDesiredDate(String desiredDate) {
        this.desiredDate = desiredDate;
    }

    @Override
    public String toString() {
        return "IssueLight{" +
            "key='" + key + '\'' +
            ", project='" + project + '\'' +
            ", issueType='" + issueType + '\'' +
            ", summary='" + summary + '\'' +
            ", status='" + status + '\'' +
            ", assignee='" + assignee + '\'' +
            ", priority='" + priority + '\'' +
            ", createdDate='" + createdDate + '\'' +
            ", updatedDate='" + updatedDate + '\'' +
            ", dueDate='" + dueDate + '\'' +
            ", desiredDate='" + desiredDate + '\'' +
            ", resolution='" + resolution + '\'' +
            ", minutesEstimated=" + minutesEstimated +
            ", minutesSpent=" + minutesSpent +
            ", minutesRemaining=" + minutesRemaining +
            ", accountKey='" + accountKey + '\'' +
            ", accountName='" + accountName + '\'' +
            ", fixVersions=" + fixVersions +
            ", fields=" + fields +
            ", wantedFields=" + wantedFields +
            '}';
    }
}
