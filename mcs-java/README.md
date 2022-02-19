# mcs
Finding maximum common subgraph and minimum common supergraph of two graphs

## Generate jar executable
Project requires at least version 8 of Java, and Maven to generate jar executable.

The following command will generate `mcs-1.0-SNAPSHOT-jar-with-dependencies.jar` in `./target` folder:
```bash
mvn clean compile assembly:single
```

## Run program with jar
Make sure you have a jar file from previous step, and run:
```bash
java -jar target/mcs-1.0-SNAPSHOT-jar-with-dependencies.jar
```
