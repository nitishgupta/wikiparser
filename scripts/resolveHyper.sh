CP="./config/:./target/classes/:./target/dependency/*:"

OPTIONS="-Xmx32g -XX:+UseConcMarkSweepGC -ea -cp $CP"
PACKAGE_PREFIX="edu.illinois.cs.cogcomp"

MAIN="$PACKAGE_PREFIX.wikiparser.unifiedParsing.ResolveHyperlinks"

mvn compile
time nice java $OPTIONS $MAIN

