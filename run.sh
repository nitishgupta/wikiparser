# This shell script takes 6 arguments.
# First argument: File path to directory containing source files required for JWPL.
# Second argument: Number of processors to be used
# Third argument: File path of the wiki dump to be parsed
# Fourth argument: File path of your desired location for your output for the wikiextractor.py
# Fifth argument: File path of desired output for the java wiki parser
# Sixth argument: Boolean flag to indicate if wikiextractor.py should be run.

if [[ $6 -eq 1 ]]
then
	python wikiextractor.py --filter_disambig_pages --processes=$2 --links -o $4 $3
fi

mvn clean compile
mvn exec:java -Dexec.mainClass="edu.illinois.cs.cogcomp.wikiparser.wikiparse.Runner" -Dexec.args="$1 $4 $5"
