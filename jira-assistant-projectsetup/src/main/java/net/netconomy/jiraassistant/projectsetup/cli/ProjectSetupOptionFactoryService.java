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
package net.netconomy.jiraassistant.projectsetup.cli;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.springframework.stereotype.Service;

import net.netconomy.jiraassistant.base.cli.OptionFactory;

@Service
public class ProjectSetupOptionFactoryService implements OptionFactory {

    @Override
    public Options createOptions() {

        Options options = new Options();

        Option createTestProject = Option.builder("createTestProject").desc("create Testproject on Testserver").build();

        Option createCustomerProject = Option.builder("createCustomerProject").hasArgs()
                .argName("projects> <issueTypes> <startDate> <endDate> <fileName> <encoding").desc("create Project")
                .build();

        Option createTeamProject = Option.builder("createTeamProject").hasArgs()
                .argName("projects> <issueTypes> <startDate> <endDate> <fileName> <encoding").desc("create Project")
                .build();

        Option createCustomerServiceProject = Option.builder("createCustomerServiceProject").hasArgs()
                .argName("projects> <issueTypes> <startDate> <endDate> <fileName> <encoding").desc("create Project")
                .build();

        Option help = new Option("help", "print this message");

        options.addOption(createTestProject);
        options.addOption(createCustomerProject);
        options.addOption(createTeamProject);
        options.addOption(createCustomerServiceProject);
        options.addOption(help);

        return options;
    }

}
