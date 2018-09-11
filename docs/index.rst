====================================
Welcome to JASSISTANT documentation!
====================================

Welcome to JASSISTANT, a helper service for Atlassian JIRA for exporting
various frequently requested statistics. What you get is basically a standalone
webserver that queries your JIRA instance through its API and presents the
statistics as JSON or CSV.


General
=======

+----------------------------+----------------------------------------------------------------+
| Topic                      | Description                                                    |
+============================+================================================================+
| :ref:`general-local-setup` | This guide will help you configure a server locally.           |
+----------------------------+----------------------------------------------------------------+
| :ref:`general-config`      | All available configuration settings                           |
+----------------------------+----------------------------------------------------------------+
| :ref:`general-kubernetes`  | Quickstart on how to set up JASSISTANT in a Kubernetes cluster |
+----------------------------+----------------------------------------------------------------+

.. toctree::
    :caption: General
    :hidden:
    :maxdepth: 1

    ./local-setup.rst
    ./configuration.rst
    ./kubernetes.rst

Modules
=======

JASSISTANT's functionality is split into multiple task-specific modules. The
user interacts with them through a unified web-interface provided by the
restservices module.

+------------------------------------+-----------------------------------------------------------------------------------+
| Module                             | Description                                                                       |
+====================================+===================================================================================+
| :ref:`module-account-progress`     | Statistics about the progress of issues associated with one or multiple accounts. |
+------------------------------------+-----------------------------------------------------------------------------------+
| :ref:`module-billing`              | Billing-specific data                                                             |
+------------------------------------+-----------------------------------------------------------------------------------+
| :ref:`module-estimationstatistics` | Accuracy of story point and time estimations                                      |
+------------------------------------+-----------------------------------------------------------------------------------+
| :ref:`module-kanbananalysis`       | Kanban KPIs, Lead Time, Cycle Time etc.                                           |
+------------------------------------+-----------------------------------------------------------------------------------+
| :ref:`module-linksearch`           | Cross-project issue-links                                                         |
+------------------------------------+-----------------------------------------------------------------------------------+
| :ref:`module-projectsetup`         | Helper for creating and configuring new JIRA projects                             |
+------------------------------------+-----------------------------------------------------------------------------------+
| :ref:`module-projectstatus`        | Status of projects within JIRA                                                    |
+------------------------------------+-----------------------------------------------------------------------------------+
| :ref:`module-reopenfactor`         | Reopen-factor of issues within a project                                          |
+------------------------------------+-----------------------------------------------------------------------------------+
| :ref:`module-sprintanalysis`       | Various statistics about the status of sprints                                    |
+------------------------------------+-----------------------------------------------------------------------------------+
| :ref:`module-sprintforecast`       | Forecasts the number of sprint necessary to complete the current backlog          |
+------------------------------------+-----------------------------------------------------------------------------------+
| :ref:`module-supportanalysis`      | Customer support-related statistics                                               |
+------------------------------------+-----------------------------------------------------------------------------------+


.. toctree::
    :caption: Modules
    :hidden:
    :maxdepth: 1
    
    ./modules/accountprogress/index.rst
    ./modules/billing/index.rst
    ./modules/estimationstatistics/index.rst
    ./modules/kanbananalysis/index.rst
    ./modules/linksearch/index.rst
    ./modules/projectsetup/index.rst
    ./modules/projectstatus/index.rst
    ./modules/reopenfactor/index.rst
    ./modules/sprintanalysis/index.rst
    ./modules/sprintforecast/index.rst
    ./modules/supportanalysis/index.rst
