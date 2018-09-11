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
package net.netconomy.jiraassistant.restservices.controller;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/")
public class FormController {
    private static List<Pair<String, String>> forms = new ArrayList<>();
    static {
        forms.add(Pair.of("SprintAnalysis", "Sprint analysis"));
        forms.add(Pair.of("MultiSprintAnalysis", "Multi-sprint analysis"));
        forms.add(Pair.of("KanbanAnalysis", "Kanban analysis"));
        forms.add(Pair.of("SprintForecast", "Sprint forecast"));
        forms.add(Pair.of("ReopenFactor", "Reopen-factor"));
        forms.add(Pair.of("LinkSearch", "Link search"));
        forms.add(Pair.of("NoLinkSearch", "No-link search"));
        forms.add(Pair.of("EstimationStatistics", "Estimation statistics"));
        forms.add(Pair.of("ProjectStatus", "Project status"));
        forms.add(Pair.of("AccountProgress", "Account progress"));
        forms.add(Pair.of("SupportAnalysis", "Support analysis"));
        forms.add(Pair.of("Billing", "Billing"));
    }

    @ModelAttribute("forms")
    public List<Pair<String, String>> getForms() {
        return forms;
    }

    @RequestMapping(value = "")
    public String index() {
        return "index";
    }

    @RequestMapping(value = "forms/{form}.html")
    public String form(@PathVariable String form) {
        for (Pair<String, String> pair : forms) {
            if (pair.getLeft().equals(form)) {
                return "forms/" + pair.getLeft();
            }
        }
        return "error";
    }
}
