#! /bin/sh

BASE=`dirname "$0"`
BASE=`cd "$BASE" && pwd`

for i in $BASE/lib/*.jar ; do
  CP="$CP:$i"
done

CMD=java

if [ "$JAVA_HOME" ] ; then
  CMD="$JAVA_HOME/bin/$CMD"
fi

exec "$CMD" -classpath "$CP" org.tigris.aopmetrics.AopMetricsCLI "$@"
