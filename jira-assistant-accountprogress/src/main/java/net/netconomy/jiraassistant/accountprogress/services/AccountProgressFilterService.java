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
package net.netconomy.jiraassistant.accountprogress.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.google.common.base.Joiner;

@Service
public class AccountProgressFilterService {

    public String generateAccountFilter(List<Integer> accountIDs, String andClause) {

        StringBuilder generatedFilter = new StringBuilder();
        Joiner joiner = Joiner.on(",");

        generatedFilter.append("account in (");

        joiner.appendTo(generatedFilter, accountIDs);

        if (andClause != null && !andClause.isEmpty()) {
            generatedFilter.append(") and (" + andClause);
        }

        generatedFilter.append(") order by Rank asc");

        return generatedFilter.toString();

    }

}
