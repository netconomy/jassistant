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
package net.netconomy.jiraassistant.restservices.data;

import java.util.ArrayList;
import java.util.List;

import net.netconomy.jiraassistant.base.data.AccountBaseData;

public class TempoAccountDataCollection {

    private List<AccountBaseData> accountBaseDataList = new ArrayList<>();

    public TempoAccountDataCollection() {

    }

    public void addAccountBaseData(AccountBaseData accountBaseData) {
        accountBaseDataList.add(accountBaseData);
    }

    public List<AccountBaseData> getAccountBaseDataList() {
        return accountBaseDataList;
    }

    @Override
    public String toString() {
        return "TempoAccountDataCollection [accountBaseDataList=" + accountBaseDataList + "]";
    }

}
