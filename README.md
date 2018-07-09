# Beanfiller-Tcases

An annotation and reflections based java-library to fill Java beans with values according to combinatorial testing principles.
This is useful for acceptance-testing, integration-testing, and unit-testing of functions with high complexity.

## Short introduction

TODO

## Motivation

Tcases is intended as a separate CLI tool to maintain a set of testcase xml-files over time, possibly maintaining also a set of generated+modified Test sources (in any language) containing expected values for each generated testcase (a.k.a Oracle).

This project is intended as a Java automated testing tool to generate combinatorial tests.
The idea is that on constructing the testcases, enough knowledge about the input is available to simulate expected outputs without having to reimplement the SUT.

## How to use

The library can be used freely in any other Unit testing framework like JUnit or TestNg.
Sample projects show examples.

### In Maven

```
    <dependency>
      <groupId>org.beanfiller</groupId>
      <artifactId>tcases-annotation</artifactId>
      <version>[latestVersion]</version>
    </dependency>
```

### In Gradle

```
dependencies {
    testCompile 'org.beanfiller:tcases-annotation:[latestVersion]'
}
```


## Q & A

### Why not annotate methods instead of Beans?

Most commonly testcases will contain meta-information in addition to raw values, a bean is required to capture this data.

### TODO more questions

# Resources

* The Tcases guide: http://www.cornutum.org/tcases/docs/Tcases-Guide.htm
* Overview of combinatorial testing tools: http://www.pairwise.org/tools.asp
* Overview of Boundary Value testing
* Overview of Property-based testing
* Mention EvoSuite
