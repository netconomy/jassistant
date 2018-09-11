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

public class CliOutputConfiguration {

    private Boolean asJson = false;
    private String encoding = null;

    public CliOutputConfiguration() {

    }

    public CliOutputConfiguration(CliOutputConfiguration cliConfig) {

        this.asJson = cliConfig.asJson;
        this.encoding = cliConfig.encoding;

    }

    public Boolean getAsJson() {
        return asJson;
    }

    public void setAsJson(Boolean asJson) {
        this.asJson = asJson;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    @Override
    public String toString() {
        return "CliOutputConfiguration [asJson=" + asJson + ", encoding=" + encoding + "]";
    }

}
