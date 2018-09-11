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
package net.netconomy.jiraassistant.linksearch.data;

import java.util.ArrayList;
import java.util.List;

public class LinkSearchResult {

    private Integer numberOfFoundLinkItems = 0;
    private String usedFilter;
    private String linkSearchDate;
    private LinkSearchData linkSearchData;
    private List<LinkedIssues> searchResultIssueLinks = new ArrayList<>();

    public LinkSearchResult() {

    }

    // Adders
    public void addSearchResultIssueLink(LinkedIssues linkedIssues) {
        this.searchResultIssueLinks.add(linkedIssues);
    }

    // Getters and Setters
    public List<LinkedIssues> getSearchResultIssueLinks() {
        return searchResultIssueLinks;
    }

    public void setSearchResultIssueLinks(List<LinkedIssues> searchResultIssueLinks) {
        this.searchResultIssueLinks = searchResultIssueLinks;
    }

    public LinkSearchData getLinkSearchData() {
        return linkSearchData;
    }

    public void setLinkSearchData(LinkSearchData linkSearchData) {
        this.linkSearchData = linkSearchData;
    }

    public String getUsedFilter() {
        return usedFilter;
    }

    public void setUsedFilter(String usedFilter) {
        this.usedFilter = usedFilter;
    }

    public Integer getNumberOfFoundLinkItems() {
        return numberOfFoundLinkItems;
    }

    public void setNumberOfFoundLinkItems(Integer numberOfFoundLinkItems) {
        this.numberOfFoundLinkItems = numberOfFoundLinkItems;
    }

    public String getLinkSearchDate() {
        return linkSearchDate;
    }

    public void setLinkSearchDate(String linkSearchDate) {
        this.linkSearchDate = linkSearchDate;
    }

    @Override
    public String toString() {
        return "LinkSearchResult [numberOfFoundLinkItems=" + numberOfFoundLinkItems + ", usedFilter=" + usedFilter
                + ", linkSearchDate=" + linkSearchDate + ", linkSearchData=" + linkSearchData
                + ", searchResultIssueLinks=" + searchResultIssueLinks + "]";
    }

}
