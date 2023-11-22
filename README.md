# Wipro Technical Test
## Prerequisites
- Java JDK (version 11.0.18)
- Maven (version 3.9.5)

## Setup

#### Build the project:
```
mvn clean install
```

#### Copy the dependencies
```
mvn dependency:copy-dependencies
```

#### Compile
```
mvn compile
```

#### Run the application:

```
mvn exec:java -Dexec.mainClass="com.example.App"
```

#### To generate a new DataSet:

- The 1st arg -generatedDataset is to generate a new Dataset and use it
- 2nd arg is for the Number of Instruments
- 3rd arg is for the Number of Entries per Instrument  

If no args are given the sample input.txt that was given with the test will be used instead

```
set MAVEN_OPTS=-Xmx12g
mvn exec:java -Dexec.mainClass="com.example.App" -Dexec.args="-generatedDataset 1000 1000"
```

In order to generate a new Dataset it is necessary to first manually delete, if existent, large_dataset.csv

#### Testing:

The following JUnit Tests files are included in the project:

```
FileParserTest.java
InstrumentProcessorTest.java
```

To run the tests:
```
mvn test
```


## Additional Notes
If you encounter any issues, check the error messages for more information.