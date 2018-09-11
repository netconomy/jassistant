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
package net.netconomy.jiraassistant.linksearch.data;

import java.util.ArrayList;
import java.util.List;

import net.netconomy.jiraassistant.base.data.IssueLight;

public class LinkedIssues {

    private IssueLight rootIssue;
    private List<IssueLight> linkedIssueList = new ArrayList<>();

    public LinkedIssues() {

    }

    // Adders
    public void addLinkedIssue(IssueLight linkedIssue) {
        this.linkedIssueList.add(linkedIssue);
    }

    // Getters and Setters
    public IssueLight getRootIssue() {
        return rootIssue;
    }

    public void setRootIssue(IssueLight rootIssue) {
        this.rootIssue = rootIssue;
    }

    public List<IssueLight> getLinkedIssueList() {
        return linkedIssueList;
    }

    public void setLinkedIssueList(List<IssueLight> linkedIssues) {
        this.linkedIssueList = linkedIssues;
    }

    @Override
    public String toString() {
        return "LinkedIssues [rootIssue=" + rootIssue + ", linkedIssueList=" + linkedIssueList + "]";
    }

}
