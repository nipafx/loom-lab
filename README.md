# Project Loom Lab

Experiments with Project Loom's features based on these JEPs:

* [JEP 425: Virtual Threads](https://openjdk.org/jeps/425)
* [JEP 428: Structured Concurrency](https://openjdk.org/jeps/428)

## Experiments

* [Disk Stats](#disk-stats)
* [Echo Client & Server](#echo-client--server)
* [GitHub Crawler](#github-crawler)

For these experiments, you need [Java 19](https://jdk.java.net/19/).
Build the project with `mvn package` to get `target/loom-lab.jar`.
To run it:

```
java --enable-preview -p target/loom-lab.jar -m loom.lab $EXPERIMENT $ARGUMENTS
```

Where:

* `$EXPERIMENT` selects one of the experiments by name
* `$ARGUMENTS` configures the experiment

For details on these, see specific experiments below.

### Disk Stats

Walks over all folders and files in a given directory to gather their respective sizes.
Can be configured to either run as a single thread or with one virtual thread for each file/folder.

* name: `DiskStats`
* arguments: see [`DiskStats.java`.](src/main/java/dev/nipafx/lab/loom/disk/DiskStats.java)
* package: [`dev.nipafx.lab.loom.disk`](src/main/java/dev/nipafx/lab/loom/disk)

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
	* arguments: see [`Echo.java`.](src/main/java/dev/nipafx/lab/loom/echo/server/Echo.java)
* client
    * name: `EchoClient`
    * arguments: see [`Send.java`.](src/main/java/dev/nipafx/lab/loom/echo/client/Send.java), 
* package: [`dev.nipafx.lab.loom.echo`](src/main/java/dev/nipafx/lab/loom/echo)

**Note**:
For a much more thorough experiment with an echo server, check out Elliot Barlas' [project-loom-experiment](https://github.com/ebarlas/project-loom-experiment).

### GitHub Crawler

Starting from a given seed URL, crawls GitHub pages and prints their connections and statistics.
Only runs with virtual threads but also uses/demonstrates some Data-Oriented Programming concepts.

* name: `GitHubCrawl`
* arguments: see [`GitHubCrawl.java`.](src/main/java/dev/nipafx/lab/loom/crawl/GitHubCrawl.java)
* package: [`dev.nipafx.lab.loom.crawl`](src/main/java/dev/nipafx/lab/loom/crawl)
