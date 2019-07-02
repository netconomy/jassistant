/*********************************************************************
 * The Initial Developer of the content of this file is NETCONOMY.
 * All portions of the code written by NETCONOMY are property of
 * NETCONOMY. All Rights Reserved.
 *
 * NETCONOMY Software & Consulting GmbH
 * Hilmgasse 4, A-8010 Graz (Austria)
 * FN 204360 f, Landesgericht fuer ZRS Graz
 * Tel: +43 (316) 815 544
 * Fax: +43 (316) 815544-99
 * www.netconomy.net
 *
 * (c) 2019 by NETCONOMY Software & Consulting GmbH
 *********************************************************************/

package net.netconomy.jiraassistant.base.services.filters;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * IssueFilter acts as configuration object which is handed down from the REST layer to services like the basicIssueService in order
 * to filter out issues that should not be taken into consideration for reports.
 */
public class IssueFilter {
    private Set<String> projectKeys;

    public IssueFilter() {
        this.projectKeys = new HashSet<>();
    }

    public Set<String> getProjectKeys() {
        return projectKeys;
    }

    public void setProjectKeys(Set<String> projectKeys) {
        this.projectKeys = projectKeys;
    }

    public void setProjectKeys(List<String> projectKeys) {
        this.projectKeys.clear();
        this.projectKeys.addAll(projectKeys);
    }
}
