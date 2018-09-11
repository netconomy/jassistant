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
package net.netconomy.jiraassistant.billing;

import net.netconomy.jiraassistant.base.BaseDIConfiguration;
import net.netconomy.jiraassistant.base.cli.OptionFactory;
import net.netconomy.jiraassistant.billing.cli.BillingOptionFactoryService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(BaseDIConfiguration.class)
@ComponentScan(value = { "net.netconomy.jiraassistant.billing" })
public class BillingDIConfiguration {

    @Bean
    public OptionFactory optionFactory() {
        return new BillingOptionFactoryService();
    }

}
