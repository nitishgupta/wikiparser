mvn clean compile -e
mvn exec:java -Dexec.mainClass="edu.illinois.cs.cogcomp.wikiparser.jwpl.jwplparsers.JWPLParser" -Dexec.args="$1 $2"
