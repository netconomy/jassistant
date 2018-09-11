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
package net.netconomy.jiraassistant.kanbananalysis.cli;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.springframework.stereotype.Service;

import net.netconomy.jiraassistant.base.cli.OptionFactory;

@Service
public class KanbanAnalysisOptionFactoryService implements OptionFactory {

    @Override
    public Options createOptions() {

        Options options = new Options();

        Option kanbanAnalysisByFilterJSON = Option
                .builder("kanbanAnalysisByFilterJSON")
                .hasArgs()
                .argName(
                        "backlogFilter> <startDate(yyyy-mm-dd)> <endDate(yyyy-mm-dd)> <withAltEstimations> <lightAnalysis> <projects> <fileName> <encoding")
                .desc("analyse Projects during a given Timeperiod and write it to a File in JSON. Encoding will be UTF-8, when left empty.")
                .build();

        Option kanbanAnalysisByProjectsJSON = Option
                .builder("kanbanAnalysisByProjectsJSON")
                .hasArgs()
                .argName(
                        "projectKeys(separated_by_colon)> <startDate(yyyy-mm-dd)> <endDate(yyyy-mm-dd)> <withAltEstimations> <lightAnalysis> <excludedStatus(separated_by_colon)> <excludedTypes(separated_by_colon)> <andClause> <fileName> <encoding")
                .desc("analyse Projects during a given Timeperiod and write it to a File in JSON. Encoding will be UTF-8, when left empty.")
                .build();

        Option multipleKanbanAnalysisFromFolder = Option
                .builder("multipleKanbanAnalysisFromFolder")
                .hasArgs()
                .argName("folderName> <fileName> <encoding")
                .desc("aggregates KanbanAnalysis JSON Files from the given Sub Folder into a CSV File.  Encoding will be UTF-8, when left empty.")
                .build();

        Option help = new Option("help", "print this message");

        options.addOption(kanbanAnalysisByFilterJSON);
        options.addOption(kanbanAnalysisByProjectsJSON);
        options.addOption(multipleKanbanAnalysisFromFolder);
        options.addOption(help);

        return options;
    }

}
