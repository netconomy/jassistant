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
package net.netconomy.jiraassistant.billing.services;

import net.netconomy.jiraassistant.base.services.CSVService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Joiner;

import net.netconomy.jiraassistant.base.data.csv.CSVLine;
import net.netconomy.jiraassistant.base.data.csv.CSVTable;
import net.netconomy.jiraassistant.billing.data.BillingData;
import net.netconomy.jiraassistant.billing.data.BillingIssue;

import java.util.List;

@Service
public class BillingDataToCSVService {

    @Autowired
    CSVService csvService;

    private CSVLine generateFirstCSVLineForBillingData(List<String> additionalFields) {

        CSVLine firstLine = new CSVLine();

        firstLine.addStringToLine("Project");
        firstLine.addStringToLine("Issue Type");
        firstLine.addStringToLine("Priority");
        firstLine.addStringToLine("Key");
        firstLine.addStringToLine("Summary");
        firstLine.addStringToLine("Status");
        firstLine.addStringToLine("Reporter");
        firstLine.addStringToLine("Components");
        firstLine.addStringToLine("FixVersions");
        firstLine.addStringToLine("Epic Key");
        firstLine.addStringToLine("Epic Name");
        firstLine.addStringToLine("Account Key");
        firstLine.addStringToLine("Account Name");
        firstLine.addStringToLine("Linked Issues");
        firstLine.addStringToLine("booked Time in PD");
        firstLine.addStringToLine("booked Time in PD Rounded");
        firstLine.addStringToLine("original Estimate in PD");
        firstLine.addStringToLine("booked Time in Minutes");
        firstLine.addStringToLine("original Estimate in Minutes");
        firstLine.addStringToLine("last Comment");
        firstLine.addStringToLine("billable Maintenance Log in Person Days");
        firstLine.addStringToLine("in Maintenance Log");

        for(String current : additionalFields) {
            firstLine.addStringToLine(current);
        }

        return firstLine;

    }

    private CSVLine generateCSVLineForBillingIssue(BillingIssue billingIssue, List<String> additionalFields) {

        CSVLine line = new CSVLine();
        Joiner joiner = Joiner.on(", ");

        line.addStringToLine(billingIssue.getProject());
        line.addStringToLine(billingIssue.getIssueType());
        line.addStringToLine(billingIssue.getPriority());
        line.addStringToLine(billingIssue.getKey());
        line.addStringToLine(billingIssue.getSummary());
        line.addStringToLine(billingIssue.getStatus());
        line.addStringToLine(billingIssue.getReporter());
        line.addStringToLine(joiner.join(billingIssue.getComponents()));
        line.addStringToLine(joiner.join(billingIssue.getFixVersions()));
        line.addStringToLine(billingIssue.getEpicKey());
        line.addStringToLine(billingIssue.getEpicName());
        line.addStringToLine(billingIssue.getAccountKey());
        line.addStringToLine(billingIssue.getAccountName());
        line.addStringToLine(joiner.join(billingIssue.getLinkedIssuesKeysToList()));
        line.addStringToLine(billingIssue.getPersonDaysBooked().toString().replace('.', ','));
        line.addStringToLine(billingIssue.getPersonDaysBookedRounded().toString().replace('.', ','));

        if (billingIssue.getPersonDaysEstimated() != null) {
            line.addStringToLine(billingIssue.getPersonDaysEstimated().toString().replace('.', ','));
        } else {
            line.addStringToLine("");
        }

        if (billingIssue.getBookedTimeInTimeFrameMinutes() != null) {
            line.addStringToLine(billingIssue.getBookedTimeInTimeFrameMinutes().getMinutes().toString()
                    .replace('.', ','));
        } else {
            line.addStringToLine("");
        }

        if (billingIssue.getOriginalEstimateMinutes() != null) {
            line.addStringToLine(billingIssue.getOriginalEstimateMinutes().getMinutes().toString().replace('.', ','));
        } else {
            line.addStringToLine("");
        }

        line.addStringToLine(csvService.sanitizeCSVString(billingIssue.getLastComment(), ';'));
        line.addStringToLine(billingIssue.getPersonDaysBillableMaintenanceLog().toString().replace('.', ','));
        line.addStringToLine(billingIssue.getInMaintenanceLog());

        for(String current : additionalFields) {
            line.addStringToLine(billingIssue.getAdditionalFields().get(current));
        }

        return line;

    }

    private void addGeneralLines(CSVTable table, BillingData billingData) {

        CSVLine line;

        line = new CSVLine();
        line.addStringToLine("used Filter for Issues");
        line.addStringToLine(billingData.getUsedFilterIssues());
        table.addCSVLineToTable(line);

        line = new CSVLine();
        line.addStringToLine("used Filter for Sub Issues");
        line.addStringToLine(billingData.getUsedFilterSubIssues());
        table.addCSVLineToTable(line);

    }

    public CSVTable generateCSVTableFromBillingData(BillingData billingData, List<String> additionalFields) {

        CSVTable table = new CSVTable();

        addGeneralLines(table, billingData);

        table.addEmptyLineToTable();

        // add First Line
        table.addCSVLineToTable(generateFirstCSVLineForBillingData(additionalFields));

        for (BillingIssue current : billingData.getIssuesToBill()) {
            table.addCSVLineToTable(generateCSVLineForBillingIssue(current, additionalFields));
        }
        
        return table;

    }

}
