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
package net.netconomy.jiraassistant.supportanalysis.cli;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.springframework.stereotype.Service;

import net.netconomy.jiraassistant.base.cli.OptionFactory;

@Service
public class SupportAnalysisOptionFactoryService implements OptionFactory {

    @Override
    public Options createOptions() {

        Options options = new Options();

        Option supportAnalysis = Option
                .builder("supportAnalysis")
                .hasArgs()
                .argName(
                        "projects> <startDate(yyyy-mm-dd)> <endDate(yyyy-mm-dd)> <IssueTypes> <andClause> <filename> <encoding")
                .desc("analyse given Support Projects and write it to a File in JSON. encoding will be UTF-8, when left empty.")
                .build();

        Option supportAnalysisMonth = Option
                .builder("supportAnalysisMonth")
                .hasArgs()
                .argName("projects> <month(yyyy-mm)> <IssueTypes> <andClause> <filename> <encoding")
                .desc("analyse given Support Projects and write it to a File in JSON. encoding will be UTF-8, when left empty.")
                .build();

        Option help = new Option("help", "print this message");

        options.addOption(supportAnalysis);
        options.addOption(supportAnalysisMonth);
        options.addOption(help);

        return options;
    }

}
