# JASSISTANT

Welcome to JASSISTANT, a collection of tools around Atlassian's issue tracker
JIRA for getting various statistics out of it.

At this point, JASSISTANT is split into multiple modules, each with their own
set of packages and tests. For details, please take a look at the
documentation, which you can find inside the `docs/` folder.


## How to start a local server

First, you have to create a `jassistant.properties` file in the root folder
(here). You can find an example for this with the
`jassistant_example.properties` file. Once this is in place you can start
the server with this command:

```
$ gradle runDebug
```


## How to build a release

```
$ gradle releaseSnapshot
```

This will create a snapshot image on the TAA-internal Docker registry which can
then be fetched from the target server.


## Documentation

The primary documentation for JASSISTANT is stored inside the `docs` folder and
written in RestructuredText using
[Sphinx](http://www.sphinx-doc.org/en/master/index.html). In order to convert
it into HTML run the following command:

```
$ gradle docs
```

The output of this is stored in `docs/_build/html`.


## Credits

Big thanks to Michael Weilbuchner who wrote most of JASSISTANT!
