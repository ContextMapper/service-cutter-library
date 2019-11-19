# Service Cutter Library
[![Build Status](https://travis-ci.com/ContextMapper/service-cutter-library.svg?branch=master)](https://travis-ci.com/ContextMapper/service-cutter-library) [![codecov](https://codecov.io/gh/ContextMapper/service-cutter-library/branch/master/graph/badge.svg)](https://codecov.io/gh/ContextMapper/service-cutter-library) [![Maven Central](https://img.shields.io/maven-central/v/org.contextmapper/service-cutter-library.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22org.contextmapper%22%20AND%20a:%22service-cutter-library%22) [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0) 

The [Service Cutter tool](https://github.com/ServiceCutter/ServiceCutter) provides a structured way to service decomposition. It is based on the [Bachelor Thesis](https://eprints.hsr.ch/476/) by [Lukas Kölbener](https://github.com/koelbener) and [Michael Gysel](https://github.com/gysel).

The original tool repository is located under https://github.com/ServiceCutter/ServiceCutter.

This fork aims to provide the Service Cutter engine as a library to be used within other tools such as [Context Mapper](https://contextmapper.org).

# Usage 
The library is published to the [Maven central repository](https://search.maven.org/artifact/org.contextmapper/service-cutter-library/), so you can easily include it to your Gradle or Maven build:

**Gradle**:
```Gradle
implementation 'org.contextmapper:service-cutter-library:1.1.3'
```
**Maven**:
```xml
<dependency>
  <groupId>org.contextmapper</groupId>
  <artifactId>service-cutter-library</artifactId>
  <version>1.1.3</version>
</dependency>
```
**Eclipse**:

Since we use the library inside our [Context Mapper](https://contextmapper.org) Eclipse plugin, we also need it as Eclipse feature (OSGi bundle) in a P2 repository. Thus, we provide the following two Eclipse update sites for snapshots (master builds) and releases:
 * https://dl.bintray.com/contextmapper/service-cutter-library-snapshots/ (latest snapshot: [ ![Download](https://api.bintray.com/packages/contextmapper/service-cutter-library-snapshots/updatesites/images/download.svg) ](https://dl.bintray.com/contextmapper/service-cutter-library-snapshots))
 * https://dl.bintray.com/contextmapper/service-cutter-library-releases/ (latest release: [ ![Download](https://api.bintray.com/packages/contextmapper/service-cutter-library-releases/updatesites/images/download.svg) ](https://dl.bintray.com/contextmapper/service-cutter-library-releases))

## Input
As in the original Service Cutter tool there are two input models which you need to generate service decompositions with the library:
 * Domain Model (Entity Relation Model); **required**
 * User Representations; **optional**
 
Originally these input models are provided as JSON files. You find explanations and examples on the [Service Cutter](http://servicecutter.github.io/) page. You also find the examples in our test resources, [here](https://github.com/ContextMapper/service-cutter-library/tree/master/src/test/resources). However, if you use this library you just have to provide the models with the classes offered in the package _ch.hsr.servicecutter.api.model_. Thereby you can create the models manually in Java or however you want to create it.

### Solver Configuration
In the original Service Cutter tool you can provide parameters and priorities on the coupling criteria via the user interface. This configuration has to be provided with the _ch.hsr.servicecutter.solver.SolverConfiguration_ class here in the library version. The configuration can be created with the _ch.hsr.servicecutter.api.SolverConfigurationFactory_ which gives you the default configuration which can then be changed (factory also provides JSON import).

## Usage Example
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

If you want to create the SolverConfiguration and change priorities on coupling criteria it can be done as follows:
```java
// create configuration
SolverConfiguration configuration = new SolverConfigurationFactory().createDefaultConfiguration();
        
// change parameters
configuration.setPriority(CouplingCriterion.PREDEFINED_SERVICE, SolverPriority.XS);
        
// build solver context (user representations are optional)
ServiceCutterContext context = new ServiceCutterContextBuilder(erd)
    .withUserRepresentations(userRepresentations)
    .withCustomSolverConfiguration(configuration)
    .build();
```
**Note:** If you don't pass a configuration to the _ServiceCutterContextBuilder_, it will use the default configuration.

With the _ch.hsr.servicecutter.api.SolverConfigurationFactory_ you can also create the Configuration from a JSON file. The **default configuration** looks as follows: (JSON)

```json
{
  "algorithmParams": {
    "inflation": 2,
    "power": 1,
    "prune": 0,
    "extraClusters": 0,
    "numberOfClusters": 3,
    "leungM": 0.1,
    "leungDelta": 0.55
  },
  "priorities": {
    "Identity & Lifecycle Commonality": "M",
    "Semantic Proximity": "M",
    "Shared Owner": "M",
    "Structural Volatility": "XS",
    "Latency": "M",
    "Consistency Criticality": "XS",
    "Availability Criticality": "XS",
    "Content Volatility": "XS",
    "Consistency Constraint": "M",
    "Storage Similarity": "XS",
    "Predefined Service Constraint": "M",
    "Security Contextuality": "M",
    "Security Criticality": "XS",
    "Security Constraint": "M"
  },
  "algorithm": "Leung"
}
```

### Priorities
The solver currently supports the following priorities: IGNORE, XS, S, M, L, XL, XXL

### Algorithms
We currently only support the "Epidemic Label Propagation" by [Leung et al](http://arxiv.org/pdf/0808.2633.pdf). The "Girvan-Newman" algorithm by [M. Girvan and M. E. J. Newman](http://arxiv.org/abs/cond-mat/0112110) has been supported by Service Cutter but we haven't included it into the library due to its license. We want to add new algorithms in the future.

# Build
If you want to checkout the library and build it by yourself you can do that with the following Gradle command: (prerequisite: JDK 1.8)

```
./gradlew clean build
```

## Contributing
Contribution is always welcome! Here are some ways how you can contribute:
 * Create Github issues if you find bugs or just want to give suggestions for improvements.
 * This is an open source project: if you want to code, [create pull requests](https://help.github.com/articles/creating-a-pull-request/) from [forks of this repository](https://help.github.com/articles/fork-a-repo/). Please refer to a Github issue if you contribute this way. 

## Licence
Service Cutter and this library version are released under the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0).
