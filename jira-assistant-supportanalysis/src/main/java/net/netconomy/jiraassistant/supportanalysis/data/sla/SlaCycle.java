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
package net.netconomy.jiraassistant.supportanalysis.data.sla;

import org.joda.time.DateTime;

public class SlaCycle implements Comparable<SlaCycle> {

    private DateTime startTime;
    private DateTime breachTime;
    private DateTime stopTime;
    private Boolean breached;
    private Boolean paused;
    private Boolean withinCalendarHours;
    private Double goalDurationMinutes;
    private Double elapsedTimeMinutes;
    private Double remainingTimeMinutes;
    
    public SlaCycle() {

    }

    public DateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(DateTime startTime) {
        this.startTime = startTime;
    }

    public DateTime getBreachTime() {
        return breachTime;
    }

    public void setBreachTime(DateTime breachTime) {
        this.breachTime = breachTime;
    }

    public DateTime getStopTime() {
        return stopTime;
    }

    public void setStopTime(DateTime stopTime) {
        this.stopTime = stopTime;
    }

    public Boolean getBreached() {
        return breached;
    }

    public void setBreached(Boolean breached) {
        this.breached = breached;
    }

    public Boolean getPaused() {
        return paused;
    }

    public void setPaused(Boolean paused) {
        this.paused = paused;
    }

    public Boolean getWithinCalendarHours() {
        return withinCalendarHours;
    }

    public void setWithinCalendarHours(Boolean withinCalendarHours) {
        this.withinCalendarHours = withinCalendarHours;
    }

    public Double getGoalDurationMinutes() {
        return goalDurationMinutes;
    }

    public void setGoalDurationMinutes(Double goalDurationMinutes) {
        this.goalDurationMinutes = goalDurationMinutes;
    }

    public Double getElapsedTimeMinutes() {
        return elapsedTimeMinutes;
    }

    public void setElapsedTimeMinutes(Double elapsedTimeMinutes) {
        this.elapsedTimeMinutes = elapsedTimeMinutes;
    }

    public Double getRemainingTimeMinutes() {
        return remainingTimeMinutes;
    }

    public void setRemainingTimeMinutes(Double remainingTimeMinutes) {
        this.remainingTimeMinutes = remainingTimeMinutes;
    }

    @Override
    public String toString() {
        return "SlaCycle{" +
                "startTime=" + startTime +
                ", breachTime=" + breachTime +
                ", stopTime=" + stopTime +
                ", breached=" + breached +
                ", paused=" + paused +
                ", withinCalendarHours=" + withinCalendarHours +
                ", goalDurationMinutes=" + goalDurationMinutes +
                ", elapsedTimeMinutes=" + elapsedTimeMinutes +
                ", remainingTimeMinutes=" + remainingTimeMinutes +
                '}';
    }

    @Override
    public int compareTo(SlaCycle compareSlaCycle) {

        DateTime thisDateTime = this.getStartTime();
        DateTime compareDate = compareSlaCycle.getStartTime();

        return thisDateTime.compareTo(compareDate);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((startTime == null) ? 0 : startTime.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof SlaCycle)) {
            return false;
        } else {
            SlaCycle other = (SlaCycle) obj;

            return !(startTime == null) && startTime.equals(other.startTime);
        }
    }

}
