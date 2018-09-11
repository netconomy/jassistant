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
package net.netconomy.jiraassistant.restservices;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import net.netconomy.jiraassistant.accountprogress.AccountProgressDIConfiguration;
import net.netconomy.jiraassistant.base.BaseDIConfiguration;
import net.netconomy.jiraassistant.billing.BillingDIConfiguration;
import net.netconomy.jiraassistant.estimationstatistics.EstimationStatisticsDIConfiguration;
import net.netconomy.jiraassistant.kanbananalysis.KanbanAnalysisDIConfiguration;
import net.netconomy.jiraassistant.linksearch.LinkSearchDIConfiguration;
import net.netconomy.jiraassistant.projectstatus.ProjectStatusDIConfiguration;
import net.netconomy.jiraassistant.reopenfactor.ReopenFactorDIConfiguration;
import net.netconomy.jiraassistant.sprintanalysis.SprintAnalysisDIConfiguration;
import net.netconomy.jiraassistant.sprintforecast.SprintForecastDIConfiguration;
import net.netconomy.jiraassistant.supportanalysis.SupportAnalysisDIConfiguration;

import java.util.Collections;
import java.util.concurrent.Executor;

@Configuration
@EnableAsync
@EnableScheduling
@EnableWebSecurity
@EnableSwagger2
@Import({ BaseDIConfiguration.class, SprintAnalysisDIConfiguration.class, SprintForecastDIConfiguration.class,
        ReopenFactorDIConfiguration.class, LinkSearchDIConfiguration.class, EstimationStatisticsDIConfiguration.class,
        KanbanAnalysisDIConfiguration.class, ProjectStatusDIConfiguration.class, SupportAnalysisDIConfiguration.class,
        AccountProgressDIConfiguration.class, BillingDIConfiguration.class })
@EnableAutoConfiguration
@ComponentScan(value = { "net.netconomy.jiraassistant.restservices" })
public class RestServicesDIConfiguration extends WebSecurityConfigurerAdapter implements AsyncConfigurer, WebMvcConfigurer {
    private final Integer minThreadPoolSize = 10;
    private final Integer maxThreadPoolSize = 10;
    private final Integer threadWaitingQueueSize = 20;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers("/**").permitAll()
            .and()
            .httpBasic();
    }

    private ApiInfo apiInfo() {
        return new ApiInfo("Jira Assistant API",
            "The Jira Assistant API provides access to sophisticated Jira Data Analysis", "1.0", null, null, null,
            null, Collections.emptyList());
    }

    @Bean
    public Docket getApi() {
        return new Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.any()).paths(PathSelectors.regex("/api/.*"))
            .build()
            .pathMapping("/")
            .apiInfo(apiInfo());
    }

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(minThreadPoolSize);
        executor.setMaxPoolSize(maxThreadPoolSize);
        executor.setQueueCapacity(threadWaitingQueueSize);
        executor.initialize();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new CustomAsyncExceptionHandler();
    }
}
