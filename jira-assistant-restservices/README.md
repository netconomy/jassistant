# Jira Assistant REST Services

This Project combines the Jira Assistant Modules and makes them available via REST API

## Development

### Building

The project uses gradle, docker, phyton and fabric.

Available Gradle jobs:

* ```gradle clean build```
    * to build the Project as a war File

To Deploy to the Live Server:

* ```fab deploy``` (in Docker)
* ```fab updateConfig``` (to only update the Config on Live Server)

For Local Testing:

* ```fab localRun```
* ```fab localDebug``` (with Debug Port 8000 open)

The Version is set in ```gradle.properties```