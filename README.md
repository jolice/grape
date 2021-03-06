# Grape

Basic proof of concept implementation of the IoC container.

[![Build Status](https://travis-ci.org/jolice/Grape.svg?branch=master)](https://travis-ci.org/jolice/Grape)
[![codecov](https://codecov.io/gh/jolice/Grape/branch/master/graph/badge.svg)](https://codecov.io/gh/jolice/Grape)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/11310c3df3894caf9be6b01212f88977)](https://www.codacy.com/manual/jolice/Grape?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=jolice/Grape&amp;utm_campaign=Badge_Grade)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=riguron_Grape&metric=alert_status)](https://sonarcloud.io/dashboard?id=riguron_Grape)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=riguron_Grape&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=riguron_Grape)

## Dependency

This project is distributed via JitPack. Register a JitPack repository at your pom.xml:

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

And add the following dependency:

```xml
<dependency>
    <groupId>com.github.jolice</groupId>
    <artifactId>Grape</artifactId>
    <version>v1.0</version>
</dependency>
```

## Usage

```java
Grape grape = new Grape(new GrapeConfiguration()
                        .scan("your.packages.to.scan")
                        .configurations(Collections.singletonList(new TestConfiguration()))
                        .classes(Arrays.asList(Sample.class, Component.class))
);
                        
Context context = grape.createContext();
Sample sample = context.getBean(Sample.class);
```

### Defining a bean

There are three ways to register a component in the Grape container. 


#### Component scanning

Annotate a class with ```@Component``` so it will be detected by the component scanning

```java
package com.example.beans;

@Component
public class SomeComponent {
```

And instruct Grape to scan a certain package and its subpackages:

```java
Grape grape = new Grape(new GrapeConfiguration().scan("com.example.beans"));

Context context = grape.createContext();
```

#### Manual registration

```java
public class SomeComponent {
```

Register a class in the configuration

```java
Grape grape = new Grape(new GrapeConfiguration().classes(
     Collections.singletonList(SomeComponent.class)
));

Context context = grape.createContext();
```

#### Configuration

A configuration is intended for defining beans through the factory methods. This is also known as a Java Configuration in Spring 
Framework. 

To define a bean, add an appropriate factory method in the configuration and annotate it with ```@Produces```:

```java
public class SomeConfiguration implements Configuration {

    @Produces
    public Bean bean() {
        return new Bean();
    }

}
```

#### Lifecycle callbacks

Grape supports standard ```@PostConstruct``` and ```@PreDestroy``` annotations.

### Injection point

#### Constructor injection


```java
@Inject
public YourComponent(DependencyA dependencyA, DependencyB dependencyB) {
    this.dependencyA = dependencyA;
    this.dependencyB = dependencyB;
}
```    
   
#### Setter injection

```java
@Inject
public void setDependency(Dependency dependency) {
    this.dependency = dependency;
}
```   

#### Field injection

```java
@Inject
private Dependency dependency;
```  

## Qualifying bean

If there are multiple beans of the same type defined, the injection point must qualify the bean to be injected
by specifying its name using ```@Named``` annotation.

```java
@Inject
public YourComponent(@Named("ImplementationOne") Interface someInterface) {
    this.someInterface = someInterface;
}
```

This annotations is supported by setters and fields as well.

In turn, the named bean must be defined as follows:

```java
@Named("ImplementationOne")
public class ImplementationOne {
```

Or add the bean annotation to the configuration method:

```java
@Produces
@Named("ImplementationOne")
public Interface implementationOne() {
    return new ImplementationOne();
}
```

The named bean may manually obtained from the context as follows:

```java
Interface i = context.getBean(Interface.class, new NamedLookup("ImplementationOne"));
```

#### Bean priority

```@Primary``` annotation is another way to resolve the bean ambiguity. If there are multiple bean candidates for the 
injection point and one of them is defined with this annotation, it should be given a preference. That is, this annotation
makes a bean a priority one.


```java
@Produces
public Interface one() {
    return new One();
}

@Primary
@Produces
public Interface two() {
     return new Two();
}
```

### Troubleshooting


#### AmbiguousDependencyException

This exception is fired when there are multiple candidates for the injection point and none of them is
qualified at this point. Specify a bean to be injected through the ```@Named``` mark one of the candidates
as a priority one using ```@Primary``` annotation.

#### CircularDependencyException

This exception indicates a circular constructor dependency between two beans. It means that constructor
of bean A accepts bean of type B as a parameter and constructor of bean B accepts bean of type A as a parameter.
This may be eliminated by injecting one of dependencies involved in a loop through the setter. 

#### UnsatisfiedDependencyException

This exception is raised when there are no candidates for the injection in the conatiner, i.e Grape is unable
to resolve the dependency for some reason. It may happen because there are no beans of the dependency type
defined or there is no bean with the specified name. Examine the exception message for more details.

#### InvocationException

This is a reflection-related exception indicating that an invocation of a reflective component is failed due to some 
reason. As usual, this exception is caused by insufficient access privileges or by a error raised within the invoked
constructor or method.
