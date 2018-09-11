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
package net.netconomy.jiraassistant.supportanalysis;

import org.apache.commons.cli.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import net.netconomy.jiraassistant.base.cli.JiraAssistantCLI;
import net.netconomy.jiraassistant.base.cli.OptionProcessor;
import net.netconomy.jiraassistant.supportanalysis.cli.SupportAnalysisOptionProcessorService;

public class JiraAssistantSupportAnalysis {

    private static final Logger LOGGER = LoggerFactory.getLogger(JiraAssistantSupportAnalysis.class);

    private JiraAssistantSupportAnalysis() {

    }

    public static void main(String[] args) {

        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                SupportAnalysisDIConfiguration.class);) {

            JiraAssistantCLI myCLI = context.getBean(JiraAssistantCLI.class);

            OptionProcessor optionProcessor = context.getBean(SupportAnalysisOptionProcessorService.class);

            Option[] commandLineOptions;

            commandLineOptions = myCLI.parseCommandline(args);

            optionProcessor.processOptions(commandLineOptions);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }

    }

}
