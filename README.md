[![Build Status](https://travis-ci.com/captain-proton/db-choice-sample.svg?branch=master)](https://travis-ci.com/captain-proton/db-choice-sample)

# Example application for database selection

This example application is intended to illustrate Dependency Injection by selecting different databases. The implementation used for [JSR330 (Dependency Injection for Java)](https://jcp.org/en/jsr/detail?id=330) is [Google Guice](https://github.com/google/guice). This application is a simple JSE application, so this dependency is necessary. A reference implementation is included in JEE. The application contains classes for handling Sqlite and Mysql. Mysql was tested in version 5.1. For explanations of the code it is helpful to read the classes `Application` and `SqlFooDao`.

## Configuration

To keep the application simple, the `getDbUrl()` method is implemented in the `MysqlFooDaoImpl` and `SqliteFooDaoImpl` classes to provide the entire address to the database, including authentication. In a real application this data should **no** longer be stored there.

## Execution

A build of the project can be executed via the included Gradle Wrapper.

```shell
./gradlew run
```

By default the sqlite database `foo.db` is used. If Mysql is to be used as a database, the custom task `runMysql` must be used.

```shell
./gradlew runMysql
```
