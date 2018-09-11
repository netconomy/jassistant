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
package net.netconomy.jiraassistant.accountprogress.data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.netconomy.jiraassistant.base.data.AccountBaseData;
import net.netconomy.jiraassistant.base.data.SingleTimeMetric;

public class AccountProgressResultData {

    private String accountProgressDate;
    private String andClause;
    private List<AccountBaseData> accountsBaseData = new ArrayList<>();

    private Integer numberOfIssuesInAccounts = 0;
    private Integer numberOfEstimatedIssuesInAccounts = 0;

    private Double percentEstimated = 0.0;

    private Double numberOfAllStoryPoints = 0.0;
    private Double numberOfFinishedStoryPoints = 0.0;
    private Double numberOfClosedStoryPoints = 0.0;

    private Double percentOfStoryPointsFinished = 0.0;
    private Double percentOfStoryPointsClosed = 0.0;

    private SingleTimeMetric spentTimeOnOpenIssues;
    private SingleTimeMetric spentTimeOnFinishedIssues;
    private SingleTimeMetric spentTimeOnClosedIssues;

    private Set<String> openStatus = new HashSet<>();
    private List<String> finishedStatus = new ArrayList<>();
    private List<String> closedStatus = new ArrayList<>();

    public AccountProgressResultData() {

    }

    public void addAccountBaseData(AccountBaseData accountBaseData) {
        accountsBaseData.add(accountBaseData);
    }

    public void addOpenStatus(String statusName) {
        openStatus.add(statusName);
    }

    // Getters and Setters
    public String getAccountProgressDate() {
        return accountProgressDate;
    }

    public void setAccountProgressDate(String accountProgressDate) {
        this.accountProgressDate = accountProgressDate;
    }

    public String getAndClause() {
        return andClause;
    }

    public void setAndClause(String andClause) {
        this.andClause = andClause;
    }

    public List<AccountBaseData> getAccountsBaseData() {
        return accountsBaseData;
    }

    public void setAccountsBaseData(List<AccountBaseData> accountsBaseData) {
        this.accountsBaseData = accountsBaseData;
    }

    public Integer getNumberOfIssuesInAccounts() {
        return numberOfIssuesInAccounts;
    }

    public void setNumberOfIssuesInAccounts(Integer numberOfIssuesInAccounts) {
        this.numberOfIssuesInAccounts = numberOfIssuesInAccounts;
    }

    public Integer getNumberOfEstimatedIssuesInAccounts() {
        return numberOfEstimatedIssuesInAccounts;
    }

    public void setNumberOfEstimatedIssuesInAccounts(Integer numberOfEstimatedIssuesInAccounts) {
        this.numberOfEstimatedIssuesInAccounts = numberOfEstimatedIssuesInAccounts;
    }

    public Double getPercentEstimated() {
        return percentEstimated;
    }

    public void setPercentEstimated(Double percentEstimated) {
        this.percentEstimated = percentEstimated;
    }

    public Double getNumberOfAllStoryPoints() {
        return numberOfAllStoryPoints;
    }

    public void setNumberOfAllStoryPoints(Double numberOfAllStoryPoints) {
        this.numberOfAllStoryPoints = numberOfAllStoryPoints;
    }

    public Double getNumberOfFinishedStoryPoints() {
        return numberOfFinishedStoryPoints;
    }

    public void setNumberOfFinishedStoryPoints(Double numberOfFinishedStoryPoints) {
        this.numberOfFinishedStoryPoints = numberOfFinishedStoryPoints;
    }

    public Double getNumberOfClosedStoryPoints() {
        return numberOfClosedStoryPoints;
    }

    public void setNumberOfClosedStoryPoints(Double numberOfClosedStoryPoints) {
        this.numberOfClosedStoryPoints = numberOfClosedStoryPoints;
    }

    public Double getPercentOfStoryPointsFinished() {
        return percentOfStoryPointsFinished;
    }

    public void setPercentOfStoryPointsFinished(Double percentOfStoryPointsFinished) {
        this.percentOfStoryPointsFinished = percentOfStoryPointsFinished;
    }

    public Double getPercentOfStoryPointsClosed() {
        return percentOfStoryPointsClosed;
    }

    public void setPercentOfStoryPointsClosed(Double percentOfStoryPointsClosed) {
        this.percentOfStoryPointsClosed = percentOfStoryPointsClosed;
    }

    public SingleTimeMetric getSpentTimeOnOpenIssues() {
        return spentTimeOnOpenIssues;
    }

    public void setSpentTimeOnOpenIssues(Double spentTimeOnOpenIssues) {
        this.spentTimeOnOpenIssues = new SingleTimeMetric(spentTimeOnOpenIssues);
    }

    public SingleTimeMetric getSpentTimeOnFinishedIssues() {
        return spentTimeOnFinishedIssues;
    }

    public void setSpentTimeOnFinishedIssues(Double spentTimeOnFinishedIssues) {
        this.spentTimeOnFinishedIssues = new SingleTimeMetric(spentTimeOnFinishedIssues);
    }

    public SingleTimeMetric getSpentTimeOnClosedIssues() {
        return spentTimeOnClosedIssues;
    }

    public void setSpentTimeOnClosedIssues(Double spentTimeOnClosedIssues) {
        this.spentTimeOnClosedIssues = new SingleTimeMetric(spentTimeOnClosedIssues);
    }

    public Set<String> getOpenStatus() {
        return openStatus;
    }

    public void setOpenStatus(Set<String> openStatus) {
        this.openStatus = openStatus;
    }

    public List<String> getFinishedStatus() {
        return finishedStatus;
    }

    public void setFinishedStatus(List<String> finishedStatus) {
        this.finishedStatus = finishedStatus;
    }

    public List<String> getClosedStatus() {
        return closedStatus;
    }

    public void setClosedStatus(List<String> closedStatus) {
        this.closedStatus = closedStatus;
    }

    @Override
    public String toString() {
        return "AccountProgressResultData [accountProgressDate=" + accountProgressDate + ", andClause=" + andClause
                + ", accountsBaseData=" + accountsBaseData + ", numberOfIssuesInAccounts=" + numberOfIssuesInAccounts
                + ", numberOfEstimatedIssuesInAccounts=" + numberOfEstimatedIssuesInAccounts + ", percentEstimated="
                + percentEstimated + ", numberOfAllStoryPoints=" + numberOfAllStoryPoints
                + ", numberOfFinishedStoryPoints=" + numberOfFinishedStoryPoints + ", numberOfClosedStoryPoints="
                + numberOfClosedStoryPoints + ", percentOfStoryPointsFinished=" + percentOfStoryPointsFinished
                + ", percentOfStoryPointsClosed=" + percentOfStoryPointsClosed + ", spentTimeOnOpenIssues="
                + spentTimeOnOpenIssues + ", spentTimeOnFinishedIssues=" + spentTimeOnFinishedIssues
                + ", spentTimeOnClosedIssues=" + spentTimeOnClosedIssues + ", openStatus=" + openStatus
                + ", finishedStatus=" + finishedStatus + ", closedStatus=" + closedStatus + "]";
    }

}
