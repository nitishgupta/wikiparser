# This shell script takes 2 arguments.
# First argument: File path to directory containing extracted wiki pages to be parsed
# Second argument: File path of desired output for the java wiki parser

# mvn clean compile
# mvn exec:java -Dexec.mainClass="edu.illinois.cs.cogcomp.wikiparser.wikiparse.WikiExtractParser" -Dexec.args="$1 $2"



CP="./target/classes/:./target/dependency/*:"

OPTIONS="-Xmx32g -XX:+UseConcMarkSweepGC -ea -cp $CP"
PACKAGE_PREFIX="edu.illinois.cs.cogcomp"

MAIN="$PACKAGE_PREFIX.wikiparser.wikiparse.WikiExtractParser"

mvn compile
time nice java -Dfile.encoding=UTF-8 $OPTIONS $MAIN $1 $2 $3

