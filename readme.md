# Checkers

## Decisions

* Simplistic approach of abandoned games - don't include in scores
* A user is limited to single active Game. To change, update anything related to `User.activeGame`
* Lobby per game type
* A user is automatically added to the only lobby we have (the checkers game lobby)
* `Java 18`, seems to work OK
* `Jakarta EE 9.1` because that's the latest version supported by TomEE and Glassfish
* `TomEE` because there is [TomEE Maven Plugin](https://tomee.apache.org/tomee-maven-plugin.html) that does "Easy provisioning of a TomEE server"


## Setup

### Install Java

Version: 18

How to install - whatever works for your platform.

### Install Maven

How to install - whatever works for your platform.

### Install TomEE

Info: Based on https://tomee.apache.org/master/docs/tomee-maven-plugin.html

    mvn tomee:build

### Configure IDEA

* https://tomee.apache.org/tomee-and-intellij (if it doesn't work try this after running with Maven)

## Start TomEE

This also builds the source:

    mvn clean package tomee:start

## Stop TomEE

    mvn tomee:stop

## Browse the App

The app is at http://localhost:8080/checkers-1.0-SNAPSHOT/

As of 2022-07-26, you should see numbers 1,2, and 3, each on a separate line.
