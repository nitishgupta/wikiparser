mvn clean compile
mvn exec:java -Dexec.mainClass="edu.illinois.cs.cogcomp.wikiparser.unifiedParsing.KB" -Dexec.args="$1 $2 $3"
