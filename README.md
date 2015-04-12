# UISpec4J

[![Join the chat at https://gitter.im/UISpec4J/UISpec4J](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/UISpec4J/UISpec4J?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

UISpec4J is an Open Source functional and/or unit testing library for Swing-based Java applications.

If you are writing a Swing application, you will appreciate UISpec4J above all for its simplicity: UISpec4J's APIs are 
designed to hide as much as possible the complexity of Swing, resulting in easy to write and easy to read test scripts. 
This is especially true when comparing UISpec4J tests with those produced using Swing or low-level, event-based 
testing libraries.

Checkout the official website at [http://www.uispec4j.org](http://www.uispec4j.org) for more information.

## Window Interception failures

UISpec4J version 2.4 may fail tests that involve intercepting windows. This is due to a change in a couple of 
function related to modal windows. These appear on the JDK 1.6.0 update 38. If you are using newer JDKs and 
intercept windows you will need to use the 2.5-SNAPSHOT builds (see below). 

## JDK 7 and 8 support

The MASTER branch now include support for both JDK7 and JDK8. All current test are passing. The jdk7 branch has been 
removed. Please submit update and push to the MASTER branch. To use UISpec4J you should use 2.5-SNAPSHOT builds (see 
below).

## Building

Recent JDK updated have been changing several internal swing implementations that UISpec4J depends on. We are trying 
to keep the source up to date. Please report issues if you fail to build on a specific version. While the official 2.5 
release is not out, you can build the project as follows:

| JDK   | Command                                   |
| ----- | ----------------------------------------- |
| jdk6  |  mvn -Pjdk6 -Dgpg.skip=true clean install |
| jdk7  |  mvn -Pjdk7 -Dgpg.skip=true clean install |
| jdk8  |  mvn -Pjdk8 -Dgpg.skip=true clean install |

## Running JDK7 on Linux

As of JDK 7 *MToolkit* is no long available on Linux (see 
[Java SE 7 and JDK 7 Compatibility](http://www.oracle.com/technetwork/java/javase/compatibility-417013.html)).
This is still the default Toolkit on Linux for UISpec4J. To make you tests run on Linux run you builds as follow:

       mvn -Dawt.toolkit=sun.awt.X11.XToolkit clean test

## Experimental Headless mode

If you build from MASTER you may be able to run your test on a headless machine using 
[Caciocavallo](http://rkennke.wordpress.com/2012/05/02/caciocavallo-1-1-released/) this is a Toolkit and Graphical 
engine that do not require a real display. This makes it possible to run your tests in a fully headless environment.
The base test on Linux worked fine (on MacOS there is a platform specific UISpec4J test that fails).

To run your tests with Caciocavallo you need to run on JDK7 and add the following dependency:
```xml
      <dependency>
          <groupId>net.java.openjdk.cacio</groupId>
          <artifactId>cacio-tta</artifactId>
          <version>1.3</version>
          <scope>test</scope>
      </dependency>
```

Then add the following variables to your Maven command line.

```
   -Djava.awt.headless=false
   -Dawt.toolkit=net.java.openjdk.cacio.ctc.CTCToolkit
   -Djava.awt.graphicsenv=net.java.openjdk.cacio.ctc.CTCGraphicsEnvironment
```
Before attempting to use this option, make sure your test work correctly on the standard toolkit. Remember this is 
still experimental, and your mileage may vary.