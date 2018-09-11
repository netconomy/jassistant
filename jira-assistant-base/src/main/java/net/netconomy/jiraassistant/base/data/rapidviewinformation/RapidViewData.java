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
package net.netconomy.jiraassistant.base.data.rapidviewinformation;


public class RapidViewData {

    private Integer id;
    private String name;
    private Boolean sprintSupportEnabled;

    public RapidViewData(Integer id, String name, Boolean sprintSupportEnabled) {
        this.id = id;
        this.name = name;
        this.sprintSupportEnabled = sprintSupportEnabled;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getSprintSupportEnabled() {
        return sprintSupportEnabled;
    }

    public void setSprintSupportEnabled(Boolean sprintSupportEnabled) {
        this.sprintSupportEnabled = sprintSupportEnabled;
    }

    @Override
    public String toString() {
        return "RapidViewData [id=" + id + ", name=" + name + ", sprintSupportEnabled=" + sprintSupportEnabled + "]";
    }

}
