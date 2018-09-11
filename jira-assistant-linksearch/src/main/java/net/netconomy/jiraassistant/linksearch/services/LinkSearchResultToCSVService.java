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

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.netconomy.jiraassistant.base.data.IssueLight;
import net.netconomy.jiraassistant.base.data.csv.CSVLine;
import net.netconomy.jiraassistant.base.data.csv.CSVTable;
import net.netconomy.jiraassistant.base.services.CSVService;
import net.netconomy.jiraassistant.linksearch.data.LinkSearchResult;
import net.netconomy.jiraassistant.linksearch.data.LinkedIssues;

@Service
public class LinkSearchResultToCSVService {

    @Autowired
    CSVService csvService;

    public CSVTable generateCSVTableFromLinkSearchResult(LinkSearchResult linkSearchResult) {
        CSVTable table = new CSVTable();
        CSVLine currentLine = new CSVLine();

        if (linkSearchResult.getSearchResultIssueLinks().isEmpty()) {
            return table;
        }

        List<String> wantedFields = linkSearchResult.getSearchResultIssueLinks().get(0).getRootIssue()
                .getWantedFields();

        table.addCSVLineToTable(csvService.generateFirstLine(wantedFields));

        currentLine.addStringToLine("This Link Search found " + linkSearchResult.getNumberOfFoundLinkItems()
                + " Issue Link Kombinations");
        table.addCSVLineToTable(currentLine);

        for (LinkedIssues currentLinkedIssues : linkSearchResult.getSearchResultIssueLinks()) {

            currentLine = csvService.generateLineFromLightIssue(currentLinkedIssues.getRootIssue());
            table.addCSVLineToTable(currentLine);

            if (!currentLinkedIssues.getLinkedIssueList().isEmpty()) {

                currentLine = new CSVLine();
                currentLine.addStringToLine("is linked to:");
                table.addCSVLineToTable(currentLine);

                for (IssueLight currentLinkedIssue : currentLinkedIssues.getLinkedIssueList()) {

                    currentLine = csvService.generateLineFromLightIssue(currentLinkedIssue);
                    table.addCSVLineToTable(currentLine);

                }

            }

            table.addEmptyLineToTable();

        }

        currentLine = new CSVLine();
        currentLine.addStringToLine("This Link Search was created " + linkSearchResult.getLinkSearchDate());
        table.addCSVLineToTable(currentLine);

        return table;

    }

}
