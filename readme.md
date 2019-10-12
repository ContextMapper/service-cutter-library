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

## Licence
Service Cutter and this library version are released under the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0).
