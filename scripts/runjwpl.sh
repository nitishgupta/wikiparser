# This shell script takes 1 argument.
# First argument: File path to directory containing source files required for JWPL

mvn clean compile
mvn exec:java -Dexec.mainClass="edu.illinois.cs.cogcomp.wikiparser.jwpl.DataMachine" -Dexec.args="$1"