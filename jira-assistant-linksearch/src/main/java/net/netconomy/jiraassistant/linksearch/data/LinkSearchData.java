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

public class LinkSearchData {

    private ProjectSearchData project1Data = new ProjectSearchData();
    private ProjectSearchData project2Data = new ProjectSearchData();
    private Boolean noLink = false;
    private String linkType = "";
    private Boolean negateLinkType = false;

    public LinkSearchData() {

    }

    public LinkSearchData(LinkSearchDataBuilder builder) {
        this.project1Data = builder.project1Data;
        this.project2Data = builder.project2Data;
        this.noLink = builder.noLink;
        this.linkType = builder.linkType;
        this.negateLinkType = builder.negateLinkType;
    }

    public ProjectSearchData getProject1Data() {
        return project1Data;
    }

    public void setProject1Data(ProjectSearchData project1Data) {
        this.project1Data = project1Data;
    }

    public ProjectSearchData getProject2Data() {
        return project2Data;
    }

    public void setProject2Data(ProjectSearchData project2Data) {
        this.project2Data = project2Data;
    }

    public Boolean getNoLink() {
        return noLink;
    }

    public void setNoLink(Boolean noLink) {
        this.noLink = noLink;
    }

    public String getLinkType() {
        return linkType;
    }

    public void setLinkType(String linkType) {
        this.linkType = linkType;
    }

    public Boolean getNegateLinkType() {
        return negateLinkType;
    }

    public void setNegateLinkType(Boolean negateLinkType) {
        this.negateLinkType = negateLinkType;
    }

    @Override
    public String toString() {
        return "LinkSearchData [project1Data=" + project1Data + ", project2Data=" + project2Data + ", noLink=" + noLink
                + ", linkType=" + linkType + ", negateLinkType=" + negateLinkType + "]";
    }

    public static class LinkSearchDataBuilder {

        private final ProjectSearchData project1Data;
        private final ProjectSearchData project2Data;
        private Boolean noLink = false;
        private String linkType = "";
        private Boolean negateLinkType = false;

        public LinkSearchDataBuilder(ProjectSearchData project1Data, ProjectSearchData project2Data) {
            this.project1Data = project1Data;
            this.project2Data = project2Data;
        }

        public LinkSearchDataBuilder noLink(Boolean noLink) {
            this.noLink = noLink;
            return this;
        }

        public LinkSearchDataBuilder linkType(String linkType) {
            this.linkType = linkType;
            return this;
        }

        public LinkSearchDataBuilder negateLinkType(Boolean negateLinkType) {
            this.negateLinkType = negateLinkType;
            return this;
        }

        public LinkSearchData build() {
            return new LinkSearchData(this);
        }

    }

}
