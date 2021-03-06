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

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractOptionProcessorService implements OptionProcessor {

    @Autowired
    private OptionFactory optionFactory;

    protected Options allOptions;

    @PostConstruct
    public void init() {
        allOptions = optionFactory.createOptions();
    }

    @Override
    public abstract void processOptions(Option[] options);

}
