.. _general-kubernetes:

==========
Kubernetes
==========

Running JASSISTANT in a Kubernetes environment is rather simple. In order to
make this process as straight forward as possible, we've provided a Helm chart
inside :file:`helm/jassistant` which you can use to automate all the necessary
steps. All that's left to do is to create a custom values file and apply the
chart. If you want to deploy JASSISTANT in Kubernetes without using Helm, the
files inside :file:`helm/jassistant/templates` should provide you with all the
necessary details.

Custom values file
==================

Through the configuration you can set the target hostname an ingress should be
bound to as well as everything you'd normally put into the
:file:`jassistant.properties` file::
    
    ingress:
      enabled: true
      hosts: 
        - jassistant.company.com
      annotations:
        # If you're using Traefik:
        kubernetes.io/ingress.class: traefik
        traefik.frontend.rule.type: PathPrefixStrip
    config: |
      #Jira Server
      jiraUri=https://jira.company.com/
      
      #Username
      user=jassistant
      
      #Password
      password=some-password
      …

Installation and upgrade
========================

To install the chart for the first time, run the following command inside the
:file:`helm/jassistant` folder::
    
    $ helm install --values path/to/config.yaml \
        --name jassistant \
        --namespace jassistant \
        .

Subsequent updates can be done using the `upgrade` command::
    
    $ helm upgrade --values path/to/config.yaml jassistant-test .
