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

import java.util.ArrayList;
import java.util.List;

public class SupportConfiguration {

    private String billableSupportFieldName;
    private String reactionSlaFieldName;
    private String interactionSlaFieldName;
    private String solutionSlaFieldName;
    private String errorCategoryFieldName;
    private String technicalSeverityFieldName;

    private List<String> reactionTimeEndStatus = new ArrayList<>();
    private List<String> interactionTimeStartStatus = new ArrayList<>();
    private List<String> interactionTimeEndStatus = new ArrayList<>();
    private List<String> solutionTimeStartStatus = new ArrayList<>();
    private List<String> solutionTimeEndStatus = new ArrayList<>();
    private List<String> openStatus = new ArrayList<>();
    private List<String> solvedStatus = new ArrayList<>();

    public SupportConfiguration() {

    }

    public String getBillableSupportFieldName() {
        return billableSupportFieldName;
    }

    public void setBillableSupportFieldName(String billableSupportFieldName) {
        this.billableSupportFieldName = billableSupportFieldName;
    }

    public String getReactionSlaFieldName() {
        return reactionSlaFieldName;
    }

    public void setReactionSlaFieldName(String responceSlaFieldName) {
        this.reactionSlaFieldName = responceSlaFieldName;
    }

    public String getInteractionSlaFieldName() {
        return interactionSlaFieldName;
    }

    public void setInteractionSlaFieldName(String interactionSlaFieldName) {
        this.interactionSlaFieldName = interactionSlaFieldName;
    }

    public String getSolutionSlaFieldName() {
        return solutionSlaFieldName;
    }

    public void setSolutionSlaFieldName(String solutionSlaFieldName) {
        this.solutionSlaFieldName = solutionSlaFieldName;
    }

    public String getErrorCategoryFieldName() {
        return errorCategoryFieldName;
    }

    public void setErrorCategoryFieldName(String errorCategoryFieldName) {
        this.errorCategoryFieldName = errorCategoryFieldName;
    }

    public String getTechnicalSeverityFieldName() {
        return technicalSeverityFieldName;
    }

    public void setTechnicalSeverityFieldName(String technicalSeverityFieldName) {
        this.technicalSeverityFieldName = technicalSeverityFieldName;
    }

    public List<String> getReactionTimeEndStatus() {
        return reactionTimeEndStatus;
    }

    public void setReactionTimeEndStatus(List<String> reactionTimeEndStatus) {
        this.reactionTimeEndStatus = reactionTimeEndStatus;
    }

    public List<String> getInteractionTimeStartStatus() {
        return interactionTimeStartStatus;
    }

    public void setInteractionTimeStartStatus(List<String> interactionTimeStartStatus) {
        this.interactionTimeStartStatus = interactionTimeStartStatus;
    }

    public List<String> getInteractionTimeEndStatus() {
        return interactionTimeEndStatus;
    }

    public void setInteractionTimeEndStatus(List<String> interactionTimeEndStatus) {
        this.interactionTimeEndStatus = interactionTimeEndStatus;
    }

    public List<String> getSolutionTimeStartStatus() {
        return solutionTimeStartStatus;
    }

    public void setSolutionTimeStartStatus(List<String> solutionTimeStartStatus) {
        this.solutionTimeStartStatus = solutionTimeStartStatus;
    }

    public List<String> getSolutionTimeEndStatus() {
        return solutionTimeEndStatus;
    }

    public void setSolutionTimeEndStatus(List<String> solutionTimeEndStatus) {
        this.solutionTimeEndStatus = solutionTimeEndStatus;
    }

    public List<String> getOpenStatus() {
        return openStatus;
    }

    public void setOpenStatus(List<String> openStatus) {
        this.openStatus = openStatus;
    }

    public List<String> getSolvedStatus() {
        return solvedStatus;
    }

    public void setSolvedStatus(List<String> solvedStatus) {
        this.solvedStatus = solvedStatus;
    }

    @Override
    public String toString() {
        return "SupportConfiguration [billableSupportFieldName=" + billableSupportFieldName + ", reactionSlaFieldName="
                + reactionSlaFieldName + ", interactionSlaFieldName=" + interactionSlaFieldName
                + ", solutionSlaFieldName=" + solutionSlaFieldName + ", errorCategoryFieldName="
                + errorCategoryFieldName + ", technicalSeverityFieldName=" + technicalSeverityFieldName
                + ", reactionTimeEndStatus=" + reactionTimeEndStatus + ", interactionTimeStartStatus="
                + interactionTimeStartStatus + ", interactionTimeEndStatus=" + interactionTimeEndStatus
                + ", solutionTimeStartStatus=" + solutionTimeStartStatus + ", solutionTimeEndStatus="
                + solutionTimeEndStatus + ", openStatus=" + openStatus + ", solvedStatus=" + solvedStatus + "]";
    }

}
