# This shell script takes 2 arguments.
# First argument: File path to input files for wiki parser
# Second argument: File path of desired output for the java wiki parser

mvn clean compile
mvn exec:java -Dexec.mainClass="edu.illinois.cs.cogcomp.wikiparser.wikiparse.Runner" -Dexec.args="$1 $2"