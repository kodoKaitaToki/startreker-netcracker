[![Build Status](https://travis-ci.org/EugeneLokotariev/startreker-netcracker.svg?branch=master)](https://travis-ci.org/EugeneLokotariev/startreker-netcracker) [![codecov](https://codecov.io/gh/EugeneLokotariev/startreker-netcracker/branch/master/graph/badge.svg)](https://codecov.io/gh/EugeneLokotariev/startreker-netcracker) 

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=edu.netcracker%3Astartreker-netcracker&metric=alert_status)](https://sonarcloud.io/dashboard?id=edu.netcracker%3Astartreker-netcracker) [![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=edu.netcracker%3Astartreker-netcracker&metric=security_rating)](https://sonarcloud.io/dashboard?id=edu.netcracker%3Astartreker-netcracker) [![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=edu.netcracker%3Astartreker-netcracker&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=edu.netcracker%3Astartreker-netcracker) [![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=edu.netcracker%3Astartreker-netcracker&metric=reliability_rating)](https://sonarcloud.io/dashboard?id=edu.netcracker%3Astartreker-netcracker)
 
 [![Bugs](https://sonarcloud.io/api/project_badges/measure?project=edu.netcracker%3Astartreker-netcracker&metric=bugs)](https://sonarcloud.io/dashboard?id=edu.netcracker%3Astartreker-netcracker)  [![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=edu.netcracker%3Astartreker-netcracker&metric=duplicated_lines_density)](https://sonarcloud.io/dashboard?id=edu.netcracker%3Astartreker-netcracker)    [![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=edu.netcracker%3Astartreker-netcracker&metric=vulnerabilities)](https://sonarcloud.io/dashboard?id=edu.netcracker%3Astartreker-netcracker)

**A short description:**

This project implements a system, which allows to order space trips with services.

**Technology stack:**

_Back-end:_ Spring Boot, Oracle 11 XE, Spring JDBC, Maven, Lombok

_Front-end:_ Angular 2, Bootstrap

**The project is being developed by:**

- Eugene Lokotariev

- Illia Ditkovsky

- Julia Danilyuk

- Pavel Kravets

- Oleh Demydenko

- Viktor Ivanov

- Aleksey Illin.

- Vasilisa Goloborodko

- Roman Bessmertnyi

- Vadym Zakh

**Our mentors are:**

- Igor Akimov

- Volodymyr Lovetsky

- Oksana Protsyk

**To start developing this project**

You need to have in your computer JDK (backend developing) and Node.js (frontend developing)

To start developing frontend, you need to setup Node.js and Angular 2 in your computer

Node.js you can download at `https://nodejs.org/en/` . To check the version on cmd you can do via `node -v`

The node package manager needs to be installed. In your command line write `npm install setup`

To setup angular use `npm install -g @angular/cli` in your cmd or another command line

When you clone the project from GitHub, you need to download all the dependencies using `mvn clean install` on the root directory (or you can you use install goal on the Maven tool window)

If you try to build only backend via Maven, you won't do it because backend part can't find frontend. But if you just start backend via IDE, it work properly

_**BEFORE PUSH TO GITHUB:**_ RUN **`mvn clean install`** IN YOUR ROOT DIRECTORY AND CHECK WHETHER THE PROJECT BUILDS SUCCESSFULLY OR NOT