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
package net.netconomy.jiraassistant.base.data;

public class AccountBaseData {

    private Integer id;
    private String key;
    private String name;
    private String leadUserName;
    private String leadName;
    private String status;
    private String customerShortName;
    private String customerFullName;
    private String categoryShortName;
    private String categoryLongName;

    public AccountBaseData() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLeadUserName() {
        return leadUserName;
    }

    public void setLeadUserName(String leadUserName) {
        this.leadUserName = leadUserName;
    }

    public String getLeadName() {
        return leadName;
    }

    public void setLeadName(String leadName) {
        this.leadName = leadName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCustomerShortName() {
        return customerShortName;
    }

    public void setCustomerShortName(String customerShortName) {
        this.customerShortName = customerShortName;
    }

    public String getCustomerFullName() {
        return customerFullName;
    }

    public void setCustomerFullName(String customerFullName) {
        this.customerFullName = customerFullName;
    }

    public String getCategoryShortName() {
        return categoryShortName;
    }

    public void setCategoryShortName(String categoryShortName) {
        this.categoryShortName = categoryShortName;
    }

    public String getCategoryLongName() {
        return categoryLongName;
    }

    public void setCategoryLongName(String categoryLongName) {
        this.categoryLongName = categoryLongName;
    }

    @Override
    public String toString() {
        return "AccountBaseData [id=" + id + ", key=" + key + ", name=" + name + ", leadUserName=" + leadUserName
                + ", leadName=" + leadName + ", status=" + status + ", customerShortName=" + customerShortName
                + ", customerFullName=" + customerFullName + ", categoryShortName=" + categoryShortName
                + ", categoryLongName=" + categoryLongName + "]";
    }

}
