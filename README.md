Freedom Travel Deals App
========================

A SmartPhone App for Freedom Travel to advertise the deals of the week

This repository is split into two projects. These are the:

* Android Application
* Content Management Java Tool

There is _currently_ a build system associated with the Content Management tool and Android app individually. This will be expanded upon to build a project complete build system at some point in the future. For the time being the build instructions are below:


Gradle Build Instruction
------------------------
To build a fat jar for the CMS tool, run the below from the Freedom Travel CMS folder:

```
gradle build
```

To create eclipse wrapper project for the CMS tool, run the below:

```
gradle eclipse
```

-
---------------------------


To build the Android unsigned apk, run the below from the Freedom Travel jQM folder:

```
gradle build
```
