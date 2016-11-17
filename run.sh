#!/bin/bash

WIKI_IGNITE=target/ignite-example-1.0-SNAPSHOT.jar
PACKAGE=wikipedia.top.ten

case "$1" in
    compile)
        # Compile using Maven and Java 1.8.0
        mvn clean
        JAVA_HOME=/usr/lib/jvm/java-1.8.0 mvn package
        echo Built into "target" folder!
        ;;
    partA)
        xterm -hold -e java -cp $WIKI_IGNITE $PACKAGE.DataNode &
        sleep 5
        xterm -hold -e java -cp $WIKI_IGNITE -Xms512m -Xmx512m $PACKAGE.parta.QueryPartA &
        xterm -hold -e java -cp $WIKI_IGNITE -Xms512m -Xmx512m $PACKAGE.parta.StreamPartA $2 &
        echo Done.
        ;;
    partB)
        ;;
    partC)
        ;;
    *)
        echo $"Usage: $0 {compile|partA <file1>|partB <file1> <file2> <file3>|partC <file1> <file2> <file3>}"
        exit 1
        ;;

esac