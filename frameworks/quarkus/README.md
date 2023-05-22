# code-with-quarkus

This project uses Quarkus, the Supersonic Subatomic Java Framework and runs on the latest JDK21 Early Access Build.

## Extensions and dependencies used

The code was generated from [code.quarkus.io](https://code.quarkus.io/?g=dev.nipafx.lab.loom&e=resteasy-reactive) and has the `quarkus-resteasy-reactive` extension.
Click on the link to get the generated package.

`quarkus-resteasy-reactive` was preferred to `quarkus-resteasy` as per recommendations in the [quarkus resteasy classic guide](https://quarkus.io/guides/resteasy).

Following the [Quarkus virtual threads guide](https://quarkus.io/guides/virtual-threads), I added this dependency to benefit from the performance of virtual threads in Quarkus:

```xml
    <dependency>
      <groupId>io.quarkus</groupId>
      <artifactId>quarkus-netty-loom-adaptor</artifactId>
      <version>${quarkus.platform.version}</version>
    </dependency>
```

## Running the application on Virtual Threads in dev mode 

To make sure that an endpoint will be offloaded to a virtual thread, you can simply add the `@RunOnVirtualThread` annotation to the endpoint.

To run the application on Virtual threads, it is necessary to open the `java.base.lang module` at runtime with the flag
`--add-opens java.base/java.lang=ALL-UNNAMED`. 
You can specify the opening of the `java.lang module` in your `pom.xml` file or you can also specify it as an argument when you start the dev mode.
I preferred the second option, run maven with `-Dopen-lang-package`.


You can run your application in dev mode that enables live coding using:
```shell script
mvn quarkus:dev -Dopen-lang-package
# or
./mvnw compile quarkus:dev -Dopen-lang-package
```
Check that your application runs on Virtual Threads by accessing: http://localhost:8080/api/current-thread

## Running the application without Virtual Threads in dev mode

If you already have dev mode running and already annotated with `@RunOnVirtualThread`, you just need to remove the annotation to have your application running on platform threads.
The context will be automatically reloaded by Quarkus dev mode.


> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.