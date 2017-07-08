# This shell script takes 4 arguments.
# First argument: Number of processors to be used
# Second argument: File path of the wiki dump to be parsed
# Third argument: File path of your desired location for your output for the wikiextractor.py
# Fourth argument: File path of desired output for the java wiki parser

python wikiextractor.py --filter_disambig_pages --processes=$1 --links -o $3 $2 
mvn clean compile
mvn exec:java -Dexec.mainClass="edu.illinois.cs.cogcomp.wikiparser.wikiparse.WikiExtractParser" -Dexec.args="$3 $4"
