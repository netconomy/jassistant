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
package net.netconomy.jiraassistant.base.cli;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class BaseOptionProcessorService extends AbstractOptionProcessorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseOptionProcessorService.class);

    @Override
    public void processOptions(Option[] options) {

        for (Option thisOption : options) {

            switch (thisOption.getOpt()) {
            case "getIssue":
                LOGGER.info(String.format("getIssue was called with the Argument: %s", thisOption.getValue()));
                break;
            case "help":
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("JiraAssistant", allOptions);
                break;
            default:
                break;
            }

        }

    }

}
