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
package net.netconomy.jiraassistant.sprintanalysis.cli;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.springframework.stereotype.Service;

import net.netconomy.jiraassistant.base.cli.OptionFactory;

@Service
public class SprintAnalysisOptionFactoryService implements OptionFactory {

    @Override
    public Options createOptions() {

        Options options = new Options();

        Option sprintAnalysisByNameJSON = Option.builder("sprintAnalysisByNameJSON").hasArgs()
                .argName("sprintName> <lightAnalysis> <projects> <fileName> <encoding")
                .desc("analyse Sprint and write it to a File in JSON. Encoding will be UTF-8, when left empty.")
                .build();

        Option sprintAnalysisByIdJSON = Option.builder("sprintAnalysisByIdJSON").hasArgs()
                .argName("sprintID> <lightAnalysis> <projects> <fileName> <encoding")
                .desc("analyse Sprint and write it to a File in JSON. Encoding will be UTF-8, when left empty.")
                .build();

        Option multipleSprintAnalysisFromFolder = Option
                .builder("multipleSprintAnalysisFromFolder")
                .hasArgs()
                .argName("folderName> <fileName> <encoding")
                .desc("aggregates SprintAnalysis JSON Files from the given Sub Folder into a CSV File.  Encoding will be UTF-8, when left empty.")
                .build();

        Option multipleSprintAnalysisFromFolderAndRestByName = Option
                .builder("multipleSprintAnalysisFromFolderAndRestByName")
                .hasArgs()
                .argName("sprintNames> <folderName> <projects> <fileName> <encoding")
                .desc("analyse given Sprints (seperated by ,) and aggregate with SprintAnalysis JSON Files from the given Sub Folder into a CSV File.  Encoding will be UTF-8, when left empty.")
                .build();

        Option multipleSprintAnalysisFromFolderAndRestById = Option
                .builder("multipleSprintAnalysisFromFolderAndRestById")
                .hasArgs()
                .argName("sprintIDs> <folderName> <projects> <fileName> <encoding")
                .desc("analyse given Sprints (seperated by ,) and aggregate with SprintAnalysis JSON Files from the given Sub Folder into a CSV File.  Encoding will be UTF-8, when left empty.")
                .build();

        Option multipleSprintAnalysisFromFolderAndRestByBoard = Option
                .builder("multipleSprintAnalysisFromFolderAndRestByBoard")
                .hasArgs()
                .argName("boardID> <startDate(yyyy-mm-dd)> <folderName> <projects> <fileName> <encoding")
                .desc("analyse Sprints from the given Board and aggregate with SprintAnalysis JSON Files from the given Sub Folder into a CSV File.  Encoding will be UTF-8, when left empty.")
                .build();

        Option multipleSprintAnalysisFromRestByID = Option
                .builder("multipleSprintAnalysisFromRestByID")
                .hasArgs()
                .argName("sprintIDs> <projects> <fileName> <encoding")
                .desc("analyse given Sprints identified by their IDs (seperated by ,) into a CSV File.  Encoding will be UTF-8, when left empty.")
                .build();

        Option help = new Option("help", "print this message");

        options.addOption(sprintAnalysisByNameJSON);
        options.addOption(sprintAnalysisByIdJSON);
        options.addOption(multipleSprintAnalysisFromFolder);
        options.addOption(multipleSprintAnalysisFromFolderAndRestByName);
        options.addOption(multipleSprintAnalysisFromFolderAndRestById);
        options.addOption(multipleSprintAnalysisFromFolderAndRestByBoard);
        options.addOption(multipleSprintAnalysisFromRestByID);
        options.addOption(help);

        return options;
    }

}
