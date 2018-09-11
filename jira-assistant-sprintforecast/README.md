# Jira Assistant Sprint Forecast

Module to forecast a number of Sprints based on the current Backlog and a estimated Velocity

## Development

### Building

The project uses gradle.

Available Gradle jobs:

* ```gradle clean build```
    * to build the Project
* ```gradle uploadArchives```
    * to load it to Nexus, depending on the Version Name xxx-SNAPSHOT will be loaded to the snapshot Repository
* ```gradle clean build executableJar```
    * to build the executable jar with all the Dependencies inside so the CLI can be used locally. -help will list all CLI Options

The Version is set in ```gradle.properties```