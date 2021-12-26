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