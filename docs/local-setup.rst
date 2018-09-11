.. _general-local-setup:

===========
Local setup
===========

This guide will show you how to setup JASSISTANT locally so that you can get to
know the system before moving it into production.

In order to run JASSISTANT you need to have Oracle's Java SDK in version 1.8
installed.

The system is configured using a single properties file called
jassistant.properties which should be located in the root folder of the
project. This repository already contains a sample configuration that you can
simply copy::
    
    $ cp jassistant_example.properties jassistant.properties

Once you have that file in place, you can build and start JASSISTANT with
Gradle::
    
    $ ./gradlew runDebug

Once you see something like the following text in your terminal, you can reach
JASSISTANT's web interface on http://localhost:8080::
    
    23-Aug-2018 06:09:25.714 INFO [main] org.apache.catalina.startup.Catalina.start Server startup in 15712 ms
    2018-08-23 06:10:06 INFO  o.s.w.s.DispatcherServlet:489 - FrameworkServlet 'dispatcherServlet': initialization started
    2018-08-23 06:10:06 INFO  o.s.w.s.DispatcherServlet:508 - FrameworkServlet 'dispatcherServlet': initialization completed in 73 ms

That's pretty much it!

Under the hood, this command builds a Docker image containing the complete
application and runs it while mounting a local :file:`jassistant.properties`
file into the container.
