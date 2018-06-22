# This shell script takes 1 argument.
# First argument: File path to directory containing source files required for JWPL

mvn compile
mvn exec:java -Dexec.mainClass="edu.illinois.cs.cogcomp.wikiparser.jwpl.datamachine.DataMachine" -Dexec.args="$1"
