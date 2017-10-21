# This shell script takes 1 argument.
# First argument: File path to directory for the output files of the JWPL parser

mvn clean compile -e
mvn exec:java -Dexec.mainClass="edu.illinois.cs.cogcomp.wikiparser.jwpl.JWPLParser" -Dexec.args="$1"
