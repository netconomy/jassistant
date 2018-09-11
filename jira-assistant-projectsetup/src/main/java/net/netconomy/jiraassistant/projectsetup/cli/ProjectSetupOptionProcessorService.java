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

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.netconomy.jiraassistant.base.cli.AbstractOptionProcessorService;
import net.netconomy.jiraassistant.base.output.FileOutput;
import net.netconomy.jiraassistant.base.output.ZipFileOutput;
import net.netconomy.jiraassistant.base.services.config.ConfigurationService;
import net.netconomy.jiraassistant.projectsetup.services.ProjectSetupService;

@Service
public class ProjectSetupOptionProcessorService extends AbstractOptionProcessorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectSetupOptionProcessorService.class);

    @Autowired
    ProjectSetupService projectSetupService;

    @Autowired
    ConfigurationService configuration;

    @Autowired
    FileOutput fileOutput;

    @Autowired
    ZipFileOutput zipFileOutput;

    @Override
    public void processOptions(Option[] options) {

        // try {

            for (Option thisOption : options) {

                switch (thisOption.getOpt()) {
                case "createTestProject":
                    executeCreateProject(thisOption);
                    break;
                case "createCustomerProject":
                    break;
                case "createTeamProject":
                    break;
            case "createCustomerServiceProject":
                break;
                case "help":
                    HelpFormatter formatter = new HelpFormatter();
                    formatter.printHelp("JiraAssistant", allOptions);
                    break;
                default:
                    break;
                }

            }
        /*
         * } catch (ConfigurationException e) { LOGGER.error(e.getMessage(), e); } catch (IOException e) {
         * LOGGER.error(e.getMessage(), e); }
         */
    }

    private void executeCreateProject(Option thisOption) {
        
        projectSetupService.createTestProject();
        
    }
    
}
