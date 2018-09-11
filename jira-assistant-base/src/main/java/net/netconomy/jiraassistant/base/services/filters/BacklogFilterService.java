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
package net.netconomy.jiraassistant.base.services.filters;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Joiner;

import net.netconomy.jiraassistant.base.services.config.ConfigurationService;

@Service
public class BacklogFilterService {

    @Autowired
    ConfigurationService configuration;

    private boolean isNotNullOrEmpty(String string) {
        return string != null && !string.isEmpty();
    }

    private boolean isNotNullOrEmpty(List<String> list) {
        return list != null && !list.isEmpty();
    }

    private void addAndClause(String andClause, StringBuilder generatedFilter) {

        if (isNotNullOrEmpty(andClause)) {
            generatedFilter.append(" and (" + andClause + ")");
        }
    }

    /**
     * This Function generates a JiraQL Filter based on a given JiraQL Filter, adding the andClause if it's not empty or
     * null. The order by part of the given Query is replaced by "order by Rank asc" or added, if there was no order by
     * part.
     * 
     * @param givenFilter
     * @param andClause
     * @return
     */
    public String generateBacklogFilterBasedOnFilter(String givenFilter, String andClause) {

        StringBuilder generatedFilter = new StringBuilder();
        Integer indexOfOrder = 0;

        indexOfOrder = givenFilter.toLowerCase().lastIndexOf(" order by");

        if (indexOfOrder == -1) {
            generatedFilter.append(givenFilter);
            addAndClause(andClause, generatedFilter);
            generatedFilter.append(" order by Rank asc");
        } else {
            generatedFilter.append(givenFilter.substring(0, indexOfOrder));
            addAndClause(andClause, generatedFilter);
            generatedFilter.append(" order by Rank asc");
        }

        return generatedFilter.toString();
    }

    /**
     * This Function generates a JiraQL Filter based on a given JiraQL Filter. The order by part of the given Query is
     * replaced by "order by Rank asc" or added, if there was no order by part.
     * 
     * @param givenFilter
     * @return
     */
    public String generateBacklogFilterBasedOnFilter(String givenFilter) {

        return generateBacklogFilterBasedOnFilter(givenFilter, null);
    }

    private void addExcludedStatusAndTypes(List<String> status, List<String> types, Boolean excludeStatusTypes,
            StringBuilder generatedFilter) {

        StringBuilder statusToExclude = new StringBuilder();
        StringBuilder typesToExclude = new StringBuilder();
        String excludeString = "";

        if (excludeStatusTypes) {
            excludeString = "not ";
        }

        if (isNotNullOrEmpty(status)) {

            for (String currentStatus : status) {

                if (statusToExclude.length() != 0) {
                    statusToExclude.append(",");
                }

                statusToExclude.append("'" + currentStatus + "'");
            }

            generatedFilter.append(" and status " + excludeString + "in(" + statusToExclude.toString() + ")");
        }

        if (isNotNullOrEmpty(types)) {

            for (String currentType : types) {

                if (typesToExclude.length() != 0) {
                    typesToExclude.append(",");
                }

                typesToExclude.append("'" + currentType + "'");
            }

            generatedFilter.append(" and issuetype " + excludeString + "in (" + typesToExclude.toString() + ")");
        }
    }

    /**
     * generate a Filter for the given Projects excluding the given status and types, can exclude or include Types and
     * Status
     * 
     * @param projects
     * @param andClause
     * @param andClause2
     * @param status
     * @param types
     * @param excludeStatusTypes
     * @param excludeRunningOrClosedSprints
     * @return
     */
    public String generateBacklogFilterBasedOnProjects(String projects, String andClause, String andClause2,
            List<String> status, List<String> types, Boolean excludeStatusTypes, Boolean excludeRunningOrClosedSprints) {

        StringBuilder generatedFilter = new StringBuilder();
        Joiner joiner = Joiner.on("','");
        List<String> projectsList = Arrays.asList(projects.trim().split(","));

        generatedFilter.append("project in ('");

        joiner.appendTo(generatedFilter, projectsList);

        generatedFilter.append("')");

        addAndClause(andClause, generatedFilter);

        addAndClause(andClause2, generatedFilter);

        addExcludedStatusAndTypes(status, types, excludeStatusTypes, generatedFilter);

        if (excludeRunningOrClosedSprints != null && excludeRunningOrClosedSprints) {
            generatedFilter.append(" and (sprint not in openSprints() or sprint is empty)");
        }

        generatedFilter.append(" order by Rank asc");

        return generatedFilter.toString();
    }

    /**
     * generate a Filter for the given Projects excluding the given status and types, can exclude or include Types and
     * Status
     * 
     * @param projects
     * @param andClause
     * @param andClause2
     * @param status
     * @param types
     * @param excludeStatusTypes
     * @param excludeRunningOrClosedSprints
     * @return
     */
    public String generateBacklogFilterBasedOnProjects(List<String> projects, String andClause, String andClause2,
            List<String> status, List<String> types, Boolean excludeStatusTypes,
            Boolean excludeRunningOrClosedSprints) {

        StringBuilder projectsString = new StringBuilder();

        for (String currentProject : projects) {

            if (projectsString.length() != 0) {
                projectsString.append(",");
            }

            projectsString.append(currentProject);
        }

        return generateBacklogFilterBasedOnProjects(projectsString.toString(), andClause, andClause2, status, types,
                excludeStatusTypes, excludeRunningOrClosedSprints);

    }

    /**
     * generate a Filter for the given Projects excluding the given status and types
     * 
     * @param projects
     * @param andClause
     * @param andClause2
     * @param excludedStatus
     * @param excludedTypes
     * @param excludeRunningOrClosedSprints
     * @return
     */
    public String generateBacklogFilterBasedOnProjectsExcludes(String projects, String andClause, String andClause2,
            List<String> excludedStatus, List<String> excludedTypes, Boolean excludeRunningOrClosedSprints) {

        return generateBacklogFilterBasedOnProjects(projects, andClause, andClause2, excludedStatus, excludedTypes,
                true, excludeRunningOrClosedSprints);

    }

    /**
     * generate a Filter for the given Projects excluding the given status and types
     * 
     * @param projects
     * @param andClause
     * @param andClause2
     * @param excludedStatus
     * @param excludedTypes
     * @param excludeRunningOrClosedSprints
     * @return
     */
    public String generateBacklogFilterBasedOnProjectsExcludes(List<String> projects, String andClause,
            String andClause2,
            List<String> excludedStatus, List<String> excludedTypes, Boolean excludeRunningOrClosedSprints) {

        StringBuilder projectsString = new StringBuilder();

        for (String currentProject : projects) {

            if (projectsString.length() != 0) {
                projectsString.append(",");
            }

            projectsString.append(currentProject);
        }

        return generateBacklogFilterBasedOnProjects(projectsString.toString(), andClause, andClause2, excludedStatus,
                excludedTypes, true, excludeRunningOrClosedSprints);

    }

    /**
     * generate a Filter for the given Issue Keys
     * 
     * @param issueKeys
     * @return null if there were no Keys in the List
     */
    public String generateFilterForIssueKeys(List<String> issueKeys) {

        StringBuilder generatedFilter = new StringBuilder();
        Joiner joiner = Joiner.on(",");

        if (issueKeys == null || issueKeys.isEmpty()) {
            return null;
        }

        generatedFilter.append("key in (");

        joiner.appendTo(generatedFilter, issueKeys);

        generatedFilter.append(") order by key");

        return generatedFilter.toString();

    }

}
