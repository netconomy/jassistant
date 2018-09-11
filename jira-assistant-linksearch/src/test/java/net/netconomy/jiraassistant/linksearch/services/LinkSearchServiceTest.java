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
package net.netconomy.jiraassistant.linksearch.services;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.IssueLink;
import com.atlassian.jira.rest.client.api.domain.IssueLinkType;

import net.netconomy.jiraassistant.linksearch.LinkSearchTestDIConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = { LinkSearchTestDIConfiguration.class })
public class LinkSearchServiceTest {

    @Autowired
    private LinkSearchService linkSearch;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void linkedToProjectTest() {

        Issue testIssue = mock(Issue.class);
        List<IssueLink> linkList = new ArrayList<>();
        IssueLink testLink = new IssueLink("PROJ1-123", null, null);
        IssueLink testLink2 = new IssueLink("PROJ2-674", null, null);
        IssueLink testLink3 = new IssueLink("PROJ3-5555", null, null);
        IssueLink testLink4 = new IssueLink("PROJ1-4563", null, null);

        linkList.add(testLink);
        linkList.add(testLink2);
        linkList.add(testLink3);
        linkList.add(testLink4);

        when(testIssue.getIssueLinks()).thenReturn(linkList);

        assertTrue(linkSearch.linkedToProject(testIssue, "PROJ1", null));
        assertTrue(linkSearch.linkedToProject(testIssue, "PROJ2", null));
        assertTrue(linkSearch.linkedToProject(testIssue, "PROJ3", null));
        assertFalse(linkSearch.linkedToProject(testIssue, "PROJ4", null));
        assertFalse(linkSearch.linkedToProject(testIssue, "PROJ5", null));
        assertFalse(linkSearch.linkedToProject(testIssue, "PRO", null));

    }

    @Test
    public void linkedToProjectWithTypeTest() {

        Issue testIssue = mock(Issue.class);
        List<IssueLink> linkList = new ArrayList<>();
        IssueLinkType testLinkType = new IssueLinkType("PROJ5", null, null);
        IssueLinkType testLinkType2 = new IssueLinkType("relates to", null, null);
        IssueLinkType testLinkType3 = new IssueLinkType("PROJ5", null, null);
        IssueLinkType testLinkType4 = new IssueLinkType("bla", null, null);

        IssueLink testLink = new IssueLink("PROJ1-123", null, testLinkType);
        IssueLink testLink2 = new IssueLink("PROJ2-674", null, testLinkType2);
        IssueLink testLink3 = new IssueLink("PROJ3-5555", null, testLinkType3);
        IssueLink testLink4 = new IssueLink("PROJ1-4563", null, testLinkType4);

        linkList.add(testLink);
        linkList.add(testLink2);
        linkList.add(testLink3);
        linkList.add(testLink4);

        when(testIssue.getIssueLinks()).thenReturn(linkList);

        assertTrue(linkSearch.linkedToProject(testIssue, "PROJ1", "PROJ5"));
        assertTrue(linkSearch.linkedToProject(testIssue, "PROJ1", "bla"));
        assertTrue(linkSearch.linkedToProject(testIssue, "PROJ2", "relates to"));
        assertTrue(linkSearch.linkedToProject(testIssue, "PROJ3", "PROJ5"));
        assertFalse(linkSearch.linkedToProject(testIssue, "PROJ3", "bla"));
        assertFalse(linkSearch.linkedToProject(testIssue, "PROJ1", "relates to"));
        assertFalse(linkSearch.linkedToProject(testIssue, "PROJ2", "PROJ5"));

    }

    @After
    public void tearDown() throws Exception {
    }

}
