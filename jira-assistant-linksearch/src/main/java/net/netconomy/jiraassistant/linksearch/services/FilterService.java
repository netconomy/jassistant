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
package net.netconomy.jiraassistant.linksearch.services;

import org.springframework.stereotype.Service;

import net.netconomy.jiraassistant.linksearch.data.ProjectSearchData;

@Service
public class FilterService {

    private boolean isNotNullOrEmpty(String string) {
        return string != null && !string.isEmpty();
    }

    private void processIssueTypes(ProjectSearchData projectSearchData, StringBuilder constructedFilter) {

        String currentIssueType;

        if (!projectSearchData.getIssueTypes().isEmpty()) {

            constructedFilter.append(" and issuetype");

            if (projectSearchData.getNegateIssueTypes()) {
                constructedFilter.append(" not");
            }

            constructedFilter.append(" in (");

            for (int i = 0; i < projectSearchData.getIssueTypes().size(); i++) {

                currentIssueType = projectSearchData.getIssueTypes().get(i);

                constructedFilter.append("'" + currentIssueType + "'");

                if (i != projectSearchData.getIssueTypes().size() - 1) {
                    constructedFilter.append(",");
                }

            }

            constructedFilter.append(")");

        }
    }

    private void processStatus(ProjectSearchData projectSearchData, StringBuilder constructedFilter) {

        String currentStatus;

        if (!projectSearchData.getStatus().isEmpty()) {

            constructedFilter.append(" and status");

            if (projectSearchData.getNegateStatus()) {
                constructedFilter.append(" not");
            }

            constructedFilter.append(" in (");

            for (int i = 0; i < projectSearchData.getStatus().size(); i++) {

                currentStatus = projectSearchData.getStatus().get(i);

                constructedFilter.append("'" + currentStatus + "'");

                if (i != projectSearchData.getStatus().size() - 1) {
                    constructedFilter.append(",");
                }

            }

            constructedFilter.append(")");

        }
    }

    public String createFilter(ProjectSearchData projectSearchData) {

        StringBuilder constructedFilter = new StringBuilder();

        constructedFilter.append("project = '" + projectSearchData.getProject() + "'");

        processIssueTypes(projectSearchData, constructedFilter);

        processStatus(projectSearchData, constructedFilter);

        if (isNotNullOrEmpty(projectSearchData.getAndClause())) {
            constructedFilter.append(" and " + projectSearchData.getAndClause());
        }

        return constructedFilter.toString();
    }

}
