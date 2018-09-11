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
package net.netconomy.jiraassistant.base.data.sprint;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.atlassian.jira.rest.client.api.domain.Issue;

/**
 * 
 * This Class holds all MetaData about a Sprint, that can be extracted over the JIRA API and all Issues in a List.
 * 
 * @author mweilbuchner
 *
 */
public class SprintDataFull extends SprintData {

    private List<Issue> issueList = new ArrayList<>();

    public SprintDataFull() {

    }

    public SprintDataFull(SprintData sprintData) {
        super(sprintData);
    }

    /**
     * get the Issue with the given Key from SprintData or null id the Key was not found
     * 
     * @param key
     * @return
     */
    public Issue getIssueByKey(String key) {

        for (Issue current : this.issueList) {

            if (current.getKey().equalsIgnoreCase(key)) {
                return current;
            }

        }

        return null;

    }

    public void addIssue(Issue issue) {
        issueList.add(issue);
    }

    public void addAllIssues(Collection<Issue> issueCollection) {
        issueList.addAll(issueCollection);
    }

    // Getters and Setters
    public List<Issue> getIssueList() {
        return issueList;
    }

    public void setIssueList(List<Issue> issueList) {
        this.issueList = issueList;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

}
