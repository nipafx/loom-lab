# Project Loom Lab

Experiments with Project Loom's features based on these JEP(draft)s:

* [Structured Concurrency](https://openjdk.java.net/jeps/8277129)
* [Virtual Threads](https://openjdk.java.net/jeps/8277131)

## Experiments

To build and execute these experiments, you need a current [Project Loom EA build](https://jdk.java.net/loom/).
You can build all the examples with `mvn compile` (don't `package` or the module will exist twice in `target` - once as a JAR, once in `classes` folder)

### DiskAnalyzer

Walks over all folders and files in a given directory to gather their respective sizes.
Can be configured to either run as a single thread or with one virtual thread for each file/folder.

To run, execute:

```shell
java --enable-preview -p target/ -m dev.nipafx.lab.loom/dev.nipafx.lab.loom.disk.DiskStats $THREADING $PATH
```

Where:

* `$THREADING` is either "single" or "virtual"
* `$PATH` is the directory to analyze - pick something that takes at least a few seconds

### Echo Client & Server

A client and server that exchange messages via sockets on localhost:8080.
Client protocol:

* sends a single line, terminated by a newline
* waits for a single line (i.e. a string terminated by a newline) to be received

Server protocol:

* reads a single line (i.e. a string terminated by a newline) from that socket
* waits a predetermined amount of time
* replies with the same string, including the newline

To run, execute:

```shell
# in one shell
java --enable-preview -p target/ -m dev.nipafx.lab.loom/dev.nipafx.lab.loom.echo.Echo $OPTIONS
# in another shell
java --enable-preview -p target/ -m dev.nipafx.lab.loom/dev.nipafx.lab.loom.echo.Send $OPTIONS
```

For required and optional `$OPTIONS`,
see [`Echo.java`.](src/main/java/dev/nipafx/lab/loom/echo/server/Echo.java)
and [`Send.java`.](src/main/java/dev/nipafx/lab/loom/echo/client/Send.java),
respectively.
