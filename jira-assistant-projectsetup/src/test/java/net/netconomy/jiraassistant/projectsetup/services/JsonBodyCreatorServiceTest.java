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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import net.netconomy.jiraassistant.base.data.config.ProjectSetupConfiguration;
import net.netconomy.jiraassistant.projectsetup.ProjectSetupTestDIConfiguration;
import net.netconomy.jiraassistant.projectsetup.data.NewProjectConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = { ProjectSetupTestDIConfiguration.class })
public class JsonBodyCreatorServiceTest {

    @Autowired
    private JsonBodyCreatorService jsonBodyCreatorService;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void createJsonBodyForNewProjectTest() {
        
        ProjectSetupConfiguration projectTypeConfiguration = new ProjectSetupConfiguration();
        NewProjectConfiguration newProjectConfiguration = new NewProjectConfiguration();
        JSONObject resultObject;
        
        projectTypeConfiguration.setProjectTypeKey("software");
        projectTypeConfiguration.setProjectTemplateKey("Test Key");
        projectTypeConfiguration.setIssueSecurityScheme("Test Scheme 1");
        projectTypeConfiguration.setPermissionScheme("Test Scheme 2");
        projectTypeConfiguration.setNotificationScheme("Test Scheme 3");
        
        newProjectConfiguration.setKey("TST");
        newProjectConfiguration.setName("Test Projekt");
        newProjectConfiguration.setLead("lead");
        newProjectConfiguration.setDescription("short Description");
        newProjectConfiguration.setUrl("www.url.com");
        
        try {

            resultObject = jsonBodyCreatorService.createJsonBodyForNewProject(projectTypeConfiguration,
                    newProjectConfiguration);

            assertEquals(newProjectConfiguration.getKey(), resultObject.get("key"));
            assertEquals(newProjectConfiguration.getName(), resultObject.get("name"));
            assertEquals(projectTypeConfiguration.getProjectTypeKey(), resultObject.get("projectTypeKey"));
            assertEquals(projectTypeConfiguration.getProjectTemplateKey(), resultObject.get("projectTemplateKey"));
            assertEquals(newProjectConfiguration.getDescription(), resultObject.get("description"));
            assertEquals(newProjectConfiguration.getLead(), resultObject.get("lead"));
            assertEquals(newProjectConfiguration.getUrl(), resultObject.get("url"));
            assertEquals(projectTypeConfiguration.getIssueSecurityScheme(), resultObject.get("issueSecurityScheme"));
            assertEquals(projectTypeConfiguration.getPermissionScheme(), resultObject.get("permissionScheme"));
            assertEquals(projectTypeConfiguration.getNotificationScheme(), resultObject.get("notificationScheme"));

        } catch (JSONException e) {
            fail("Exception was thrown during Test: " + e.getMessage());
        }
        
    }

    @After
    public void tearDown() throws Exception {
    }

}
