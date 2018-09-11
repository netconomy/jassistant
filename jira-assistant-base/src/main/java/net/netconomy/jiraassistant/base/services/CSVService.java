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
package net.netconomy.jiraassistant.base.services;

import java.util.List;

import net.netconomy.jiraassistant.base.data.csv.CSVTable;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.stereotype.Service;

import net.netconomy.jiraassistant.base.data.IssueLight;
import net.netconomy.jiraassistant.base.data.csv.CSVLine;

@Service
public class CSVService {

    /**
     * Generates the Headline for a CSV containing light Jira Issues. Project, Key, Issue Type, Summary, Status,
     * Assignee, Resolution, Fix Version and additional wanted Fields.
     * 
     * @param wantedFields
     * @return
     */
    public CSVLine generateFirstLine(List<String> wantedFields) {
        CSVLine line = new CSVLine();

        line.addStringToLine("Project");
        line.addStringToLine("Key");
        line.addStringToLine("issueType");
        line.addStringToLine("Summary");
        line.addStringToLine("Status");
        line.addStringToLine("Assignee");
        line.addStringToLine("Resolution");
        line.addStringToLine("FixVersions");

        for (String currentField : wantedFields) {
            line.addStringToLine(currentField);
        }

        return line;
    }

    /**
     * Generates a CSV Line containing the Data from the given light Issue. Project, Key, Issue Type, Summary, Status,
     * Assignee, Resolution, Fix Version and additional wanted Fields.
     * 
     * @param lightIssue
     * @return
     */
    public CSVLine generateLineFromLightIssue(IssueLight lightIssue) {

        CSVLine line = new CSVLine();
        StringBuilder fixVersions = new StringBuilder();

        line.addStringToLine(lightIssue.getProject());
        line.addStringToLine(lightIssue.getKey());
        line.addStringToLine(lightIssue.getIssueType());
        line.addStringToLine(lightIssue.getSummary());
        line.addStringToLine(lightIssue.getStatus());
        line.addStringToLine(lightIssue.getAssignee());
        line.addStringToLine(lightIssue.getResolution());

        for (String currentVersion : lightIssue.getFixVersions()) {

            if (fixVersions.length() != 0) {
                fixVersions.append(", ");
            }

            fixVersions.append(currentVersion);
        }

        line.addStringToLine(fixVersions.toString());

        for (String currentField : lightIssue.getWantedFields()) {
            line.addStringToLine(lightIssue.getFieldByName(currentField));
        }

        return line;
    }

    /**
     * Replaces all Separators within the String with the Separator Replacement. Escapes all commas and Line-breaks.
     *
     * @param string
     * @param separator
     * @return
     */
    public String sanitizeCSVString(String string, char separator) {

        String sanitizedString;
        String separatorString = String.valueOf(separator);

        sanitizedString = StringEscapeUtils.escapeCsv(string);

        //In Case the Separator is not the Default ',' this will sanitize the String if it wasn't already sanitized because of other reasons
        if(string != null && string.contains(separatorString) && sanitizedString.equals(string)) {
            sanitizedString = "\"" + string + "\"";
        }

        return sanitizedString;

    }

    /**
     * Replaces all Separators within the String with the Separator Replacement, escapes all commas and Line-breaks, for every String of the Line.
     *
     * @param line
     * @param separator
     * @return
     */
    public CSVLine sanitizeCSVLine(CSVLine line, char separator) {

        CSVLine sanitizedLine = new CSVLine();

        for(String current : line.getLine()) {
            sanitizedLine.addStringToLine(sanitizeCSVString(current, separator));
        }

        return sanitizedLine;

    }

    /**
     * Replaces all Separators within the String with the Separator Replacement, escapes all commas and Line-breaks, for every String of every Line.
     *
     * @param table
     * @param separator
     * @return
     */
    public CSVTable sanitizeCSVTable(CSVTable table, char separator) {

        CSVTable sanitizedTable = new CSVTable();

        for(CSVLine current : table.getCsvTable()) {
            sanitizedTable.addCSVLineToTable(sanitizeCSVLine(current, separator));
        }

        return sanitizedTable;

    }

}
