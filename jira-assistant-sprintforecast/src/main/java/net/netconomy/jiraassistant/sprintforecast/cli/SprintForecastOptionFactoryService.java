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
package net.netconomy.jiraassistant.sprintforecast.cli;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.springframework.stereotype.Service;

import net.netconomy.jiraassistant.base.cli.OptionFactory;

@Service
public class SprintForecastOptionFactoryService implements OptionFactory {

    @Override
    public Options createOptions() {

        Options options = new Options();

        Option forecastFilterJSON = Option.builder("forecastFilterJSON").hasArgs()
                .argName("backlogFilter> <forecastVelocity> <numberOfSprints> <fileName> <encoding")
                .desc("generate a Forecast for the backlogFilter. encoding will be UTF-8, when left empty.").build();

        Option forecastProjectsJSON = Option
                .builder("forecastProjectsJSON")
                .hasArgs()
                .argName(
                        "projectKeys(separated_by_colon)> <forecastVelocity> <numberOfSprints> <andClause> <fileName> <encoding")
                .desc("generate a Forecast for the given Projects. No Spaces in the ProjectKeys allowed, e.g. 'PRJ,TST,...'. Encoding will be UTF-8, when left empty.")
                .build();

        Option forecastFilterCSV = Option.builder("forecastFilterCSV").hasArgs()
                .argName("backlogFilter> <forecastVelocity> <numberOfSprints> <fileName> <encoding")
                .desc("generate a Forecast for the backlogFilter. encoding will be UTF-8, when left empty.").build();

        Option forecastProjectsCSV = Option
                .builder("forecastProjectsCSV")
                .hasArgs()
                .argName(
                        "projectKeys(separated_by_colon)> <forecastVelocity> <numberOfSprints> <andClause> <fileName> <encoding")
                .desc("generate a Forecast for the given Projects. No Spaces in the ProjectKeys allowed, e.g. 'PRJ,TST,...'. Encoding will be UTF-8, when left empty.")
                .build();

        Option help = new Option("help", "print this message");

        options.addOption(forecastFilterJSON);
        options.addOption(forecastProjectsJSON);
        options.addOption(forecastFilterCSV);
        options.addOption(forecastProjectsCSV);
        options.addOption(help);

        return options;
    }

}
