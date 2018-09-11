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
package net.netconomy.jiraassistant.supportanalysis.data;

public class ErrorCategoryData {

    private String categoryName = "";
    private Integer categoryCount = 0;
    private Double categoryMinutesSpentAliquot = 0.0;
    private Integer categoryMinutesSpentFull = 0;

    public ErrorCategoryData() {

    }

    public ErrorCategoryData(String categoryName, Integer categoryCount, Double categoryMinutesSpentAliquot,
            Integer categoryMinutesSpentFull) {
        super();
        this.categoryName = categoryName;
        this.categoryCount = categoryCount;
        this.categoryMinutesSpentAliquot = categoryMinutesSpentAliquot;
        this.categoryMinutesSpentFull = categoryMinutesSpentFull;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getCategoryCount() {
        return categoryCount;
    }

    public void setCategoryCount(Integer categoryCount) {
        this.categoryCount = categoryCount;
    }

    public Double getCategoryMinutesSpentAliquot() {
        return categoryMinutesSpentAliquot;
    }

    public void setCategoryMinutesSpentAliquot(Double categoryMinutesSpentAliquot) {
        this.categoryMinutesSpentAliquot = categoryMinutesSpentAliquot;
    }

    public Integer getCategoryMinutesSpentFull() {
        return categoryMinutesSpentFull;
    }

    public void setCategoryMinutesSpentFull(Integer categoryMinutesSpentFull) {
        this.categoryMinutesSpentFull = categoryMinutesSpentFull;
    }

    @Override
    public String toString() {
        return "ErrorCategoryData [categoryName=" + categoryName + ", categoryCount=" + categoryCount
                + ", categoryMinutesSpentAliquot=" + categoryMinutesSpentAliquot + ", categoryMinutesSpentFull="
                + categoryMinutesSpentFull + "]";
    }

}
