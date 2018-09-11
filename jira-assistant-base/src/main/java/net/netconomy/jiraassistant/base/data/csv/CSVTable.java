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
package net.netconomy.jiraassistant.base.data.csv;

import java.util.ArrayList;
import java.util.List;

public class CSVTable {

    private List<CSVLine> csvLineList = new ArrayList<>();

    public CSVTable() {

    }

    public void addEmptyLineToTable() {
        this.csvLineList.add(new CSVLine());
    }

    public void addCSVLineToTable(CSVLine csvLine) {
        this.csvLineList.add(csvLine);
    }

    public List<CSVLine> getCsvTable() {
        return csvLineList;
    }

    public void setCsvTable(List<CSVLine> csvTable) {
        this.csvLineList = csvTable;
    }

    @Override
    public String toString() {
        return "CSVTable [csvLineList=" + csvLineList + "]";
    }

}
