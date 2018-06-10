# tree
In a concurrent environment, one job is building (expanding) a tree data structure, where all other jobs are dumping periodically the current number of nodes.

### To compile:

    $ mvn clean package

### To run:

    $ java -jar target/tree-0.0.1-SNAPSHOT.jar -Xmx512m
