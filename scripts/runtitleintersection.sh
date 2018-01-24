# This shell script takes 3 arguments.
# First argument: File path to directory containing the wiki extractor output
# Second argument: File path to directory containing the jwpl output
# Third argument: File path to desired output file

mvn clean compile
mvn exec:java -Dexec.mainClass="edu.illinois.cs.cogcomp.wikiparser.unifiedParsing.titleIntersection" -Dexec.args="$1 $2 $3"
