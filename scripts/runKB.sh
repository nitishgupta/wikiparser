# This shell script takes 4 arguments.
# First argument: File path of curIds2Title.tsv
# Second argument: File path of resCurIdNonDisambig2ResTitle_nonList.tsv
# Third argument: File path of resCurId2Redirects.tsv
# Fourth argument: File path for output RedirectTitle2ResolvedTitleMap

mvn clean compile
mvn exec:java -Dexec.mainClass="edu.illinois.cs.cogcomp.wikiparser.unifiedParsing.KB" -Dexec.args="$1 $2 $3 $4"
