# Project Loom Lab

Experiments with Project Loom's features based on these JEPs:

* [JEP 444: Virtual Threads](https://openjdk.org/jeps/444)
* [JEP 453: Structured Concurrency](https://openjdk.org/jeps/453)

This includes experimenting with these features directly as well as activating them in web frameworks.

You need [Java 21](https://jdk.java.net/21/).

## Experiments

* [Disk Stats](#disk-stats)
* [Echo Client & Server](#echo-client--server)
* [GitHub Crawler](#github-crawler)

Change into the project folder `experiments` and build it with `mvn package` to get `target/loom-experiments.jar`.
To run it:

```
java --enable-preview -p target/loom-experiments.jar -m loom.experiments $EXPERIMENT $ARGUMENTS
```

Where:

* `$EXPERIMENT` selects one of the experiments by name
* `$ARGUMENTS` configures the experiment

For details on these, see specific experiments below.

### Disk Stats

Walks over all folders and files in a given directory to gather their respective sizes.
Can be configured to either run as a single thread or with one virtual thread for each file/folder.

* name: `DiskStats`
* arguments: see [`DiskStats.java`.](experiments/src/main/java/dev/nipafx/lab/loom/disk/DiskStats.java)
* package: [`dev.nipafx.lab.loom.disk`](experiments/src/main/java/dev/nipafx/lab/loom/disk)

### Echo Client & Server

A client and server that exchange messages via sockets on localhost:8080.
Client protocol:

* sends a single line, terminated by a newline
* waits for a single line (i.e. a string terminated by a newline) to be received

Server protocol:

* reads a single line (i.e. a string terminated by a newline) from that socket
* waits a predetermined amount of time
* replies with the same string, including the newline

To try this out, run the client and the server in different shells.

* server
	* name: `EchoServer`
	* arguments: see [`Echo.java`.](experiments/src/main/java/dev/nipafx/lab/loom/echo/server/Echo.java)
* client
    * name: `EchoClient`
    * arguments: see [`Send.java`.](experiments/src/main/java/dev/nipafx/lab/loom/echo/client/Send.java),
* package: [`dev.nipafx.lab.loom.echo`](experiments/src/main/java/dev/nipafx/lab/loom/echo)

**Note**:
For a much more thorough experiment with an echo server, check out Elliot Barlas' [project-loom-experiment](https://github.com/ebarlas/project-loom-experiment).

### GitHub Crawler

Starting from a given seed URL, crawls GitHub pages and prints their connections and statistics.
Only runs with virtual threads but also uses/demonstrates some data-oriented programming concepts.

* name: `GitHubCrawl`
* arguments: see [`GitHubCrawl.java`.](experiments/src/main/java/dev/nipafx/lab/loom/crawl/GitHubCrawl.java)
* package: [`dev.nipafx.lab.loom.crawl`](experiments/src/main/java/dev/nipafx/lab/loom/crawl)


## Frameworks

* [Spring Boot](#spring-boot)
* [Quarkus](#quarkus)

### Spring Boot

Change into the project folder `frameworks/spring_boot` and build with `mvn package`, then run it with:

```
java -jar target/loom-spring-boot.jar
```

Once launched, visit http://localhost:8080/api/current-thread.
To switch to virtual threads, append the command line parameter `virtual`.
The switch is implemented in [`SpringBootApplication`](frameworks/spring_boot/src/main/java/dev/nipafx/lab/loom/spring_boot/SpringBootApplication.java), which explicitly replaces the executors used to submit requests to.

As far as I know, the blog post [_Embracing Virtual Threads_](https://spring.io/blog/2022/10/11/embracing-virtual-threads) is the most up-to-date documentation on Spring and virtual threads.

### Quarkus

Change into the project folder `frameworks/quarkus` and build with `mvn package`, then run it with:

```
java --add-opens java.base/java.lang=ALL-UNNAMED -jar target/quarkus-app/quarkus-run.jar
```

Once launched, visit http://localhost:8080/api/current-thread.
To switch to virtual threads, edit [`QuarkusEndpoints`](frameworks/quarkus/src/main/java/dev/nipafx/lab/loom/quarkus/QuarkusEndpoints.java) and uncomment `@RunOnVirtualThread`, which allows configuring virtual threads for individual endpoints.
This annotation requires the `--add-opens`.
The artifact _quarkus-netty-loom-adaptor_ (see [`pom.xml`](frameworks/quarkus/pom.xml)) seems to improve performance of Quarkus on virtual threads.

As far as I know, the blog post [_Writing simpler reactive REST services with Quarkus Virtual Thread support_](https://quarkus.io/guides/virtual-threads) is the most up-to-date documentation on Quarkus and virtual threads.
