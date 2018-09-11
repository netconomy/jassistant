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
package net.netconomy.jiraassistant.base.data.config;

public class ProjectSetupConfiguration {
    private String projectTypeKey;

    private String projectTemplateKey;

    private String issueSecurityScheme;

    private String permissionScheme;

    private String notificationScheme;

    public String getProjectTypeKey() {
        return projectTypeKey;
    }

    public void setProjectTypeKey(String projectTypeKey) {
        this.projectTypeKey = projectTypeKey;
    }

    public String getProjectTemplateKey() {
        return projectTemplateKey;
    }

    public void setProjectTemplateKey(String projectTemplateKey) {
        this.projectTemplateKey = projectTemplateKey;
    }

    public String getIssueSecurityScheme() {
        return issueSecurityScheme;
    }

    public void setIssueSecurityScheme(String issueSecurityScheme) {
        this.issueSecurityScheme = issueSecurityScheme;
    }

    public String getPermissionScheme() {
        return permissionScheme;
    }

    public void setPermissionScheme(String permissionScheme) {
        this.permissionScheme = permissionScheme;
    }

    public String getNotificationScheme() {
        return notificationScheme;
    }

    public void setNotificationScheme(String notificationScheme) {
        this.notificationScheme = notificationScheme;
    }
}
