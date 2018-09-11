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
package net.netconomy.jiraassistant.restservices.controller;

import net.netconomy.jiraassistant.base.JiraAssistantException;
import net.netconomy.jiraassistant.restservices.JiraAssistantDownloadException;

import com.atlassian.jira.rest.client.api.RestClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import javax.naming.ConfigurationException;

@ControllerAdvice
public class ExceptionController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionController.class);

    @ExceptionHandler({JiraAssistantException.class, RestClientException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleInputBasedException(Exception e, Model model) {

        LOGGER.debug(e.getMessage(), e);

        model.addAttribute("status", HttpStatus.BAD_REQUEST.value());
        model.addAttribute("error", HttpStatus.BAD_REQUEST.getReasonPhrase());
        model.addAttribute("message", e.getMessage());

        return "UserErrorPage";
    }

    @ExceptionHandler(JiraAssistantDownloadException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleDownloadBasedException(JiraAssistantDownloadException e, Model model) {

        LOGGER.debug(e.getMessage(), e);

        model.addAttribute("status", HttpStatus.NOT_FOUND.value());
        model.addAttribute("error", HttpStatus.NOT_FOUND.getReasonPhrase());
        model.addAttribute("message", e.getMessage());

        return "InternalErrorPage";
    }

    @ExceptionHandler({ConfigurationException.class, IOException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleInternalException(Exception e, Model model) {

        LOGGER.error(e.getMessage(), e);

        model.addAttribute("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        model.addAttribute("error", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        model.addAttribute("message", e.getMessage());

        return "InternalErrorPage";
    }

    @ExceptionHandler(ExecutionException.class)
    public String handleExecutionException(Exception e, Model model) {

        Exception cause = (Exception) e.getCause();

        if(cause instanceof JiraAssistantException) {
            return handleInputBasedException((JiraAssistantException) e.getCause(), model);
        } else if(cause instanceof RestClientException) {
            return handleInputBasedException((RestClientException) e.getCause(), model);
        } else {
            return handleInternalException(e, model);
        }

    }

}
