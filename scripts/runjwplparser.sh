mvn clean compile -e
mvn exec:java -Dexec.mainClass="edu.illinois.cs.cogcomp.wikiparser.jwpl.JWPLParser" -Dexec.args="$1"
