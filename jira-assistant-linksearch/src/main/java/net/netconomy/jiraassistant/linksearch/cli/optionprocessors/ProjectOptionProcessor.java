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

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import net.netconomy.jiraassistant.base.JiraAssistantException;
import net.netconomy.jiraassistant.linksearch.data.CliOutputConfiguration;
import net.netconomy.jiraassistant.linksearch.data.LinkSearchData;

public abstract class ProjectOptionProcessor implements LinkSearchOptionProcessor {

    protected abstract Boolean getAsJSON();

    @Override
    public void processOption(Option thisOption, LinkSearchData linkSearchData, CliOutputConfiguration cliConfig, Options allOptions) {

        String encoding = "";
        String project1 = "";
        String project2 = "";

        if (thisOption.getValuesList().size() < 2) {
            throw new JiraAssistantException("Link Search needs two Projects to search Links in between them.");
        }

        project1 = thisOption.getValue(0);
        project2 = thisOption.getValue(1);

        linkSearchData.getProject1Data().setProject(project1);
        linkSearchData.getProject2Data().setProject(project2);

        cliConfig.setAsJson(getAsJSON());

        if (thisOption.getValuesList().size() == 3) {
            encoding = thisOption.getValue(2).trim();
            cliConfig.setEncoding(encoding);
        }

    }

}
