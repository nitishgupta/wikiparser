WIKIPARSER

This is a tool that extracts text from a Wikipedia database dump.  It uses an external WikiExtractor script to extract files from the compressed database dump.  The files are extracted into folders with document texts.  Once this is done, the wikiparser will parse the document texts to extract relevant information such as the title and hyperlinks in a wikipage.  This library is written in Java.

DETAILS

The wikiparser tool uses the Data Machine framework to obtain information such as categories, articles and link structure.  To run the Data Machine framework, the following 3 files are needed.  These files can be downloaded from the Wikimedia Download site.
1) [LANGCODE]wiki-[DATE]-pages-articles.xml.bz2 or [LANGCODE]wiki-[DATE]-pages-meta-current.xml.bz2
2) [LANGCODE]wiki-[DATE]-pagelinks.sql.gz
3) [LANGCODE]wiki-[DATE]-categorylinks.sql.gz

This will generate 11 files in a folder called output.  Next, the wikiparser will parse the documents extracted by the WikiExtractor.py script in a multi-threaded way to speed up processing.  It will create a serialized WikiPage object for each wiki document.  They will be saved to a folder that you can specify.

USAGE

A shell script is provided to run the wikiparser too.  The shellscript takes the following 6 arguments in order:

1) File path to directory containing source files required for JWPL
2) Number of processors to be used
3) File path of the wiki dump to be parsed
4) File path of your desired location for your output for the wikiextractor.py
5) File path of desired output for the java wiki parser
6) Boolean flag (0 or 1) to indicate if wikiextractor.py should be run.




