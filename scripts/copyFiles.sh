# This shell script takes 2 arguments.
# First argument: File path to directory containing curIds2Title.tsv and resCurId2Redirects.tsv
# Second argument: File path to output directory

mvn clean compile
mvn exec:java -Dexec.mainClass="edu.illinois.cs.cogcomp.wikiparser.unifiedParsing.CopyFiles" -Dexec.args="$1 $2"
