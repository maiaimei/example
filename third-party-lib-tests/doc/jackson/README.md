# Jackson Guide

[https://www.baeldung.com/jackson-object-mapper-tutorial](https://www.baeldung.com/jackson-object-mapper-tutorial)

## 1. Overview

This tutorial focuses on understanding the Jackson *ObjectMapper* class and how to serialize Java objects into JSON and deserialize
JSON string into Java objects.

To understand more about the Jackson library in general, the [Jackson Tutorial](https://www.baeldung.com/jackson) is a good place to
start.

## 2. Dependencies

Let’s first add the following dependencies to the *pom.xml*:

```xml

<dependency>
  <groupId>com.fasterxml.jackson.core</groupId>
  <artifactId>jackson-databind</artifactId>
  <version>2.17.2</version>
</dependency>
```

This dependency will also transitively add the following libraries to the classpath:

1. *jackson-annotations*
2. *jackson-core*

Always use the latest versions from the Maven central repository for
*[jackson-databind](https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-databind)*.

## 3. Reading and Writing

### 3.1. Java Object to JSON

```java
ObjectMapper objectMapper=new ObjectMapper();
```

We can use the writeValue API of the ObjectMapper to serialize any Java object as JSON output.

```java
Car car=new Car("yellow","renault");

// serialize car object to File
    objectMapper.writeValue(new File("target/car.json"),car);

// serialize car object to String
    String carAsString=objectMapper.writeValueAsString(car);
```

### 3.2. JSON to Java Object

We can use readValue API of the ObjectMapper to parse or deserialize JSON content into a Java object.

```java
// Below is a simple example of converting a JSON String to a Java object using the ObjectMapper class
String json="{ \"color\" : \"Black\", \"type\" : \"BMW\" }";
    Car car=objectMapper.readValue(json,Car.class);

// The readValue() function also accepts other forms of input, such as a file containing JSON string
    Car car=objectMapper.readValue(new File("src/test/resources/json_car.json"),Car.class);

// or an URL
    Car car=
    objectMapper.readValue(new URL("file:src/test/resources/json_car.json"),Car.class);

// We can parse a JSON in the form of an array into a Java object list using a TypeReference
    String jsonCarArray=
    "[{ \"color\" : \"Black\", \"type\" : \"BMW\" }, { \"color\" : \"Red\", \"type\" : \"FIAT\" }]";
    List<Car> listCar=objectMapper.readValue(jsonCarArray,new TypeReference<List<Car>>(){});

// Similarly, we can parse a JSON into a Java Map
    String json="{ \"color\" : \"Black\", \"type\" : \"BMW\" }";
    Map<String, Object> map
    =objectMapper.readValue(json,new TypeReference<Map<String, Object>>(){});

// Alternatively, a JSON can be parsed into a JsonNode object and used to retrieve data from a specific node
    String json="{ \"color\" : \"Black\", \"type\" : \"FIAT\" }";
    JsonNode jsonNode=objectMapper.readTree(json);
    String color=jsonNode.get("color").asText();
```

## 4. Advanced Features

One of the greatest strengths of the Jackson library is the highly customizable serialization and deserialization process.

### 4.1. Configuring Serialization or Deserialization Feature

```java
// While converting JSON objects to Java classes, in case the JSON string has some new fields, the default process will result in an exception
objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);

// Yet another option is based on the FAIL_ON_NULL_FOR_PRIMITIVES, which defines if the null values for primitive values are allowed
    objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES,false);

// Similarly, FAIL_ON_NUMBERS_FOR_ENUM controls if enum values are allowed to be serialized/deserialized as numbers
    objectMapper.configure(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS,false);
```

You can find the comprehensive list of serialization and deserialization features on
the [official site](https://github.com/FasterXML/jackson-databind/wiki/Serialization-Features).
