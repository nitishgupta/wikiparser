# This shell script takes 3 arguments.
# First argument: Number of processors to be used
# Second argument: File path of the wiki dump to be parsed
# Third argument: File path of your desired location for your output for the wikiextractor.py

python scripts/wikiextractor.py --filter_disambig_pages --processes=$1 --links -o $3 $2
