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
package net.netconomy.jiraassistant.linksearch.cli.optionprocessors;

import java.util.Arrays;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import net.netconomy.jiraassistant.linksearch.data.CliOutputConfiguration;
import net.netconomy.jiraassistant.linksearch.data.LinkSearchData;
import net.netconomy.jiraassistant.linksearch.data.ProjectSearchData;

public abstract class IssueTypeOptionProcessor implements LinkSearchOptionProcessor {

    protected abstract ProjectSearchData getProjectSearchData(LinkSearchData linkSearchData);

    @Override
    public void processOption(Option thisOption, LinkSearchData linkSearchData, CliOutputConfiguration cliConfig, Options allOptions) {

        String issueTypes = "";
        String negateIssueTypes = "";

        if (!thisOption.getValuesList().isEmpty()) {
            issueTypes = thisOption.getValue(0);

            getProjectSearchData(linkSearchData).setIssueTypes(Arrays.asList(issueTypes.trim().split(",")));
        } else {
            return;
        }

        if (thisOption.getValuesList().size() == 2) {
            negateIssueTypes = thisOption.getValue(1);

            if ("true".equalsIgnoreCase(negateIssueTypes.trim())) {
                getProjectSearchData(linkSearchData).setNegateIssueTypes(true);
            }
        }

    }

}
