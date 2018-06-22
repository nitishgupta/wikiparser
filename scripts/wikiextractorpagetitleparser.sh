# This shell script takes 2 arguments.
# First argument: File path to directory containing extracted wiki pages to be parsed
# Second argument: File path of desired output for the java wiki parser

mvn compile
mvn exec:java -Dexec.mainClass="edu.illinois.cs.cogcomp.wikiparser.wikiparse.PageIdTitleParser" -Dexec.args="$1 $2"
