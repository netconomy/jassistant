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

import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

@Service
public class DataConversionService {

    /**
     * Converts a Date Time Object into an JQL compatible Date String
     * 
     * @param date
     * @return
     */
    public String convertDateTimeToJQLDate(DateTime date) {

        return date.getYear() + "/" + date.getMonthOfYear() + "/" + date.getDayOfMonth();

    }

    /**
     * Converts a Date Time Object into an JQL compatible Date and Time String
     * 
     * @param date
     * @return
     */
    public String convertDateTimeToJQLDateTime(DateTime date) {

        return date.getYear() + "/" + date.getMonthOfYear() + "/" + date.getDayOfMonth() + " " + date.getHourOfDay()
                + ":" + date.getMinuteOfHour();

    }

}
