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
package net.netconomy.jiraassistant.linksearch.cli;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.springframework.stereotype.Service;

import net.netconomy.jiraassistant.base.cli.OptionFactory;

@Service
public class LinkSearchOptionFactoryService implements OptionFactory {

    @Override
    public Options createOptions() {

        Options options = new Options();

        Option projectsJSON = Option
                .builder("projectsJSON")
                .numberOfArgs(3)
                .argName("project1> <project2> <encoding")
                .desc("search in Links over two Projects and write it to a File in JSON. encoding will be UTF-8, when left empty.")
                .build();

        Option projectsCSV = Option
                .builder("projectsCSV")
                .numberOfArgs(3)
                .argName("project1> <project2> <encoding")
                .desc("search in Links over two Projects and write it to a CSV File. encoding will be UTF-8, when left empty.")
                .build();

        Option issueTypesProject1 = Option
                .builder("issueTypesProject1")
                .numberOfArgs(2)
                .argName("issueTypesP1> <negate")
                .desc("search in Links over two Projects, where the Issues in the first Project are (or are not) of certain Types.")
                .build();

        Option issueTypesProject2 = Option
                .builder("issueTypesProject2")
                .numberOfArgs(2)
                .argName("issueTypesP2> <negate")
                .desc("search in Links over two Projects, where the Issues in the second Project are (or are not) of certain Types.")
                .build();

        Option linkType = Option.builder("linkType").numberOfArgs(1).argName("linkType> <negate")
                .desc("search in Links over two Projects, where the Link is (or is not) of a certain Type.").build();

        Option noLink = Option.builder("noLink").numberOfArgs(1).argName("noLink")
                .desc("search Issues in a Project with no Link to the given second Project.").build();

        Option statusProject1 = Option
                .builder("statusProject1")
                .numberOfArgs(2)
                .argName("statusP1> <negate")
                .desc("search in Links over two Projects, where the Issues in the first Project are (or are not) in certain Status.")
                .build();

        Option statusProject2 = Option
                .builder("statusProject2")
                .numberOfArgs(2)
                .argName("statusP2> <negate")
                .desc("search in Links over two Projects, where the Issues in the second Project are (or are not) in certain Status.")
                .build();

        Option andClauseProject1 = Option.builder("andClauseProject1").numberOfArgs(1).argName("andClauseP1")
                .desc("add a Clause to the Filter for Project 1 that is combined with the rest of the filter per AND.")
                .build();

        Option help = new Option("help", "print this message");

        options.addOption(projectsJSON);
        options.addOption(projectsCSV);
        options.addOption(issueTypesProject1);
        options.addOption(issueTypesProject2);
        options.addOption(linkType);
        options.addOption(noLink);
        options.addOption(statusProject1);
        options.addOption(statusProject2);
        options.addOption(andClauseProject1);
        options.addOption(help);

        return options;
    }

}
