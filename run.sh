#!/bin/bash

WIKI_IGNITE=target/ignite-example-1.0-SNAPSHOT.jar
PACKAGE=wikipedia.top.ten

case "$1" in
    compile)
        # Compile using Maven and Java 1.8.0
        JAVA_HOME=/usr/lib/jvm/java-1.8.0 mvn package
        echo Built into "target" folder!
        ;;
    partA)
        xterm -hold -e java -cp $WIKI_IGNITE $PACKAGE.DataNode &
        sleep 5
        xterm -hold -e java -cp $WIKI_IGNITE -Xms512m -Xmx512m $PACKAGE.parta.QueryNode &
        xterm -e java -cp $WIKI_IGNITE -Xms512m -Xmx512m $PACKAGE.parta.StreamNode $2 &
        echo Done.
        ;;
    partB)
        xterm -hold -e java -cp $WIKI_IGNITE $PACKAGE.DataNode &
        sleep 5
        xterm -hold -e java -cp $WIKI_IGNITE -Xms512m -Xmx512m $PACKAGE.partb.QueryNode &
        xterm -e java -cp $WIKI_IGNITE -Xms512m -Xmx512m $PACKAGE.partb.StreamNode $2 &
        xterm -e java -cp $WIKI_IGNITE -Xms512m -Xmx512m $PACKAGE.partb.StreamNode $3 &
        xterm -e java -cp $WIKI_IGNITE -Xms512m -Xmx512m $PACKAGE.partb.StreamNode $4 &
        echo Done.
        ;;
    partC)
        xterm -hold -e java -cp $WIKI_IGNITE $PACKAGE.DataNode &
        sleep 5
        xterm -hold -e java -cp $WIKI_IGNITE -Xms512m -Xmx512m $PACKAGE.partc.QueryNode &
        xterm -e java -cp $WIKI_IGNITE -Xms512m -Xmx512m $PACKAGE.partc.StreamNode $2 &
        xterm -e java -cp $WIKI_IGNITE -Xms512m -Xmx512m $PACKAGE.partc.StreamNode $3 &
        xterm -e java -cp $WIKI_IGNITE -Xms512m -Xmx512m $PACKAGE.partc.StreamNode $4 &
        ;;
    *)
        echo $"Usage: $0 {compile|partA <file1>|partB <file1> <file2> <file3>|partC <file1> <file2> <file3>}"
        exit 1
        ;;

esac
