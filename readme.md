# Service Cutter Library (experimental state) 
[![Build Status](https://travis-ci.com/ContextMapper/service-cutter-library.svg?branch=master)](https://travis-ci.com/ContextMapper/service-cutter-library) [![codecov](https://codecov.io/gh/ContextMapper/service-cutter-library/branch/master/graph/badge.svg)](https://codecov.io/gh/ContextMapper/service-cutter-library) [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0) 

The [Service Cutter tool](https://github.com/ServiceCutter/ServiceCutter) suggests a structured way to service decomposition. It is based on the [Bachelor Thesis](https://eprints.hsr.ch/476/) by [Lukas Kölbener](https://github.com/koelbener) and [Michael Gysel](https://github.com/gysel).

The original tool repository is located under https://github.com/ServiceCutter/ServiceCutter.

This fork aims to provide the Service Cutter engine as a library to be used within other tools such as [Context Mapper](https://contextmapper.org). Currently this is in an **experimental state**. As soon as we have a first version we aim to publish it to Maven central.

# Build

Prerequisite: JDK 1.8

```
./gradlew clean build
```

# Usage Example
Usage of Service Cutter within your application, in case you provide the input files (entity relations and user representations) as JSON files:
```java
public static void main(String[] args) throws IOException {
    // create ERD and user representations from JSON files
    File erdFile = new File("./src/test/resources/booking_1_model.json");
    File urFile = new File("./src/test/resources/booking_2_user_representations.json");
    EntityRelationDiagram erd = new EntityRelationDiagramImporterJSON().createERDFromJSONFile(erdFile);
    UserRepresentationContainer userRepresentations = new UserRepresentationContainerImporterJSON()
        .createUserRepresentationContainerFromJSONFile(urFile);

    // build solver context (user representations are optional)
    ServiceCutterContext context = new ServiceCutterContextBuilder(erd)
        .withUserRepresentations(userRepresentations)
        .build();

    // generate service decompositions
    SolverResult result = new ServiceCutter(context).generateDecomposition();
    
    // analyze and do something with the result ...
}
```
If you don't want to work with JSON files you can construct the models manually with the classes provided in the package _ch.hsr.servicecutter.api.model_.

## Licence
Service Cutter and this library version are released under the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0).
