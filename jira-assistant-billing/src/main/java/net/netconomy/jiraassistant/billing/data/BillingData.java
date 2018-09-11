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
import java.util.List;

public class BillingData {
	
    private String usedFilterIssues = "";
    private String usedFilterSubIssues = "";
    private List<BillingIssue> issuesToBill = new ArrayList<>();
    private List<String> projectKeys = new ArrayList<>();
    private List<String> accountKeys = new ArrayList<>();
    private List<String> issueTypes = new ArrayList<>();
    private String startDate;
    private String endDate;
    private Double hoursInAPersonday = 0.0;
    private String andClause = "";
    private List<String> linksToList = new ArrayList<>();
    private List<String> additionalFields = new ArrayList<>();

    public BillingData() {

    }

    // Lists
    public void addBillingIssue(BillingIssue billingIssue) {
        this.issuesToBill.add(billingIssue);
    }

    public void addAllBillingIssues(List<BillingIssue> billingIssues) {
        this.issuesToBill.addAll(billingIssues);
    }

    // Getters and Setters
    public String getUsedFilterIssues() {
        return usedFilterIssues;
    }

    public String getUsedFilterSubIssues() {
        return usedFilterSubIssues;
    }

    public void setUsedFilterIssues(String usedFilterIssues) {
        this.usedFilterIssues = usedFilterIssues;
    }

    public void setUsedFilterSubIssues(String usedFilterSubIssues) {
        this.usedFilterSubIssues = usedFilterSubIssues;
    }

    public List<BillingIssue> getIssuesToBill() {
        return issuesToBill;
    }

    public void setIssuesToBill(List<BillingIssue> issuesToBill) {
        this.issuesToBill = issuesToBill;
    }

    public List<String> getProjectKeys() {
        return projectKeys;
    }

    public void setProjectKeys(List<String> projectKeys) {
        this.projectKeys = projectKeys;
    }

    public List<String> getAccountKeys() {
        return accountKeys;
    }

    public void setAccountKeys(List<String> accountKeys) {
        this.accountKeys = accountKeys;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Double getHoursInAPersonday() {
        return hoursInAPersonday;
    }

    public void setHoursInAPersonday(Double hoursInAPersonday) {
        this.hoursInAPersonday = hoursInAPersonday;
    }

    public List<String> getIssueTypes() {
        return issueTypes;
    }

    public void setIssueTypes(List<String> issueTypes) {
        this.issueTypes = issueTypes;
    }

    public String getAndClause() {
        return andClause;
    }

    public void setAndClause(String andClause) {
        this.andClause = andClause;
    }

    public List<String> getLinksToList() {
        return linksToList;
    }

    public void setLinksToList(List<String> linksToList) {
        this.linksToList = linksToList;
    }

    public List<String> getAdditionalFields() {
        return additionalFields;
    }

    public void setAdditionalFields(List<String> additionalFields) {
        this.additionalFields = additionalFields;
    }

    @Override
    public String toString() {
        return "BillingData{" +
                "usedFilterIssues='" + usedFilterIssues + '\'' +
                ", usedFilterSubIssues='" + usedFilterSubIssues + '\'' +
                ", issuesToBill=" + issuesToBill +
                ", projectKeys=" + projectKeys +
                ", accountKeys=" + accountKeys +
                ", issueTypes=" + issueTypes +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", hoursInAPersonday=" + hoursInAPersonday +
                ", andClause='" + andClause + '\'' +
                ", linksToList=" + linksToList +
                ", additionalFields=" + additionalFields +
                '}';
    }
}
