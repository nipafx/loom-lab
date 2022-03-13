# Project Loom Lab

Experiments with Project Loom's features based on these JEP(draft)s:

* [Structured Concurrency](https://openjdk.java.net/jeps/8277129)
* [Virtual Threads](https://openjdk.java.net/jeps/8277131)

## Experiments

For these experiments, you need a [Project Loom EA build](https://jdk.java.net/loom/).
As this is a moving target, it might be necessary to use the exact version I used, which was _19-loom+4-115_ from February 13th, 2022.

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

* package: [`dev.nipafx.lab.loom.disk`](src/main/java/dev/nipafx/lab/loom/disk)
* name: `DiskStats`
* arguments: see [`DiskStats.java`.](src/main/java/dev/nipafx/lab/loom/disk/DiskStats.java)

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

* package: [`dev.nipafx.lab.loom.echo`](src/main/java/dev/nipafx/lab/loom/echo)
* server
	* name: `EchoServer`
	* arguments: see [`Echo.java`.](src/main/java/dev/nipafx/lab/loom/echo/server/Echo.java)
* client
    * name: `EchoClient`
    * arguments: see [`Send.java`.](src/main/java/dev/nipafx/lab/loom/echo/client/Send.java), 

**Note**:
For a much more thorough experiment with an echo server, check out Elliot Barlas' [project-loom-experiment](https://github.com/ebarlas/project-loom-experiment).
