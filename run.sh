#!/bin/bash

# Set Ignite home directory
IGNITE_HOME=/afs/inf.ed.ac.uk/user/b/bfranke/test/ignite/apache-ignite-fabric-1.7.0-bin

# Set Ignite libaries
IGNITE_LIBS=$IGNITE_HOME/libs/h2-1.4.192.jar:$IGNITE_HOME/libs/ignite-core-1.7.0.jar:$IGNITE_HOME/libs/annotations-13.0.jar:$IGNITE_HOME/libs/cache-api-1.0.0.jar:$IGNITE_HOME/libs/ignite-shmem-1.0.0.jar:$IGNITE_HOME/libs/ignite-spring/commons-logging-1.1.1.jar:$IGNITE_HOME/libs/ignite-spring/spring-aop-4.1.0.RELEASE.jar:$IGNITE_HOME/libs/ignite-spring/spring-context-4.1.0.RELEASE.jar:$IGNITE_HOME/libs/ignite-spring/spring-expression-4.1.0.RELEASE.jar:$IGNITE_HOME/libs/ignite-spring/spring-tx-4.1.0.RELEASE.jar:$IGNITE_HOME/libs/ignite-spring/ignite-spring-1.7.0.jar:$IGNITE_HOME/libs/ignite-spring/spring-beans-4.1.0.RELEASE.jar:$IGNITE_HOME/libs/ignite-spring/spring-core-4.1.0.RELEASE.jar:$IGNITE_HOME/libs/ignite-spring/spring-jdbc-4.1.0.RELEASE.jar

# Compile HelloWorld program (assume it's in the current directory)
javac -cp $IGNITE_LIBS HelloWorld.java

# Start a couple of Ignite nodes
xterm -hold -e $IGNITE_HOME/bin/ignite.sh $IGNITE_HOME/examples/config/example-ignite-no-discovery.xml &
xterm -hold -e $IGNITE_HOME/bin/ignite.sh $IGNITE_HOME/examples/config/example-ignite-no-discovery.xml &
xterm -hold -e $IGNITE_HOME/bin/ignite.sh $IGNITE_HOME/examples/config/example-ignite-no-discovery.xml &

# Wait a couple of seconds for Ignite nodes to start and initialise
sleep 7

# Start the HelloWorld programme
java -cp $IGNITE_LIBS:. -Xms512m -Xmx512m -DIGNITE_QUIET=false -DIGNITE_PERFORMANCE_SUGGESTIONS_DISABLED=true Hello
