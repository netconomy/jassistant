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
package net.netconomy.jiraassistant.projectsetup.services;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Service;

import net.netconomy.jiraassistant.base.data.config.ProjectSetupConfiguration;
import net.netconomy.jiraassistant.projectsetup.data.NewProjectConfiguration;

@Service
public class JsonBodyCreatorService {

    public JSONObject createJsonBodyForNewProject(ProjectSetupConfiguration projectTypeConfiguration,
            NewProjectConfiguration newProjectConfiguration) throws JSONException {

        JSONObject jsonBody = new JSONObject();

        jsonBody.put("key", newProjectConfiguration.getKey());
        jsonBody.put("name", newProjectConfiguration.getName());
        jsonBody.put("projectTypeKey", projectTypeConfiguration.getProjectTypeKey());
        jsonBody.put("projectTemplateKey", projectTypeConfiguration.getProjectTemplateKey());
        jsonBody.put("description", newProjectConfiguration.getDescription());
        jsonBody.put("lead", newProjectConfiguration.getLead());
        jsonBody.put("url", newProjectConfiguration.getUrl());
        jsonBody.put("issueSecurityScheme", projectTypeConfiguration.getIssueSecurityScheme());
        jsonBody.put("permissionScheme", projectTypeConfiguration.getPermissionScheme());
        jsonBody.put("notificationScheme", projectTypeConfiguration.getNotificationScheme());

        return jsonBody;

    }

}
