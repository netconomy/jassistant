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

import javax.annotation.PostConstruct;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.netconomy.jiraassistant.base.JiraAssistantException;

@Service
public class JiraAssistantCLI {

    @Autowired
    private OptionFactory optionFactory;

    private Options allOptions;

    @PostConstruct
    public void init() {
        allOptions = optionFactory.createOptions();
    }

    public Option[] parseCommandline(String[] args) {

        CommandLineParser parser = new DefaultParser();

        CommandLine line = null;

        try {

            line = parser.parse(allOptions, args);

        } catch (ParseException e) {

            throw new JiraAssistantException("Parsing failed.  Reason: " + e.getMessage(), e);

        }

        return line.getOptions();
    }

}
