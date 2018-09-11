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
package net.netconomy.jiraassistant.accountprogress.cli;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.springframework.stereotype.Service;

import net.netconomy.jiraassistant.base.cli.OptionFactory;

@Service
public class AccountProgressOptionFactoryService implements OptionFactory {

    @Override
    public Options createOptions() {

        Options options = new Options();

        Option accountProgressByKeyJSON = Option
                .builder("accountProgressByKeyJSON")
                .hasArgs()
                .argName("accountKeys> <andClause> <fileName> <encoding")
                .desc("give the Progress of Issues belonging to the Accounts and write it to a File in JSON. Encoding will be UTF-8, when left empty.")
                .build();

        Option accountProgressByIDJSON = Option
                .builder("accountProgressByIDJSON")
                .hasArgs()
                .argName("accountIDs> <andClause> <fileName> <encoding")
                .desc("give the Progress of Issues belonging to the Accounts and write it to a File in JSON. Encoding will be UTF-8, when left empty.")
                .build();

        Option help = new Option("help", "print this message");

        options.addOption(accountProgressByKeyJSON);
        options.addOption(accountProgressByIDJSON);
        options.addOption(help);

        return options;
    }

}
