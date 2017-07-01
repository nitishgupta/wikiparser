# This shell script takes 2 arguments.
# First argument: File path of the wiki dump to be parsed
# Second argument: File path of your desired location for your output

python wikiextractor.py --links $1 | tr -d '\040\011\012\015'
mvn clean install
mvn exec:java -Dexec.mainClass="edu.illinois.cs.cogcomp.wikiparser.wikiparse.WikiExtractParser" -Dexec.args="$2"
