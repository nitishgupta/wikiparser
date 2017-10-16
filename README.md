WIKIPARSER

This is a tool that extracts text from a Wikipedia database dump. It uses an external WikiExtractor script to extract files from the compressed database dump. The files are extracted into folders with document texts. Once this is done, the wikiparser will parse the document texts to extract relevant information such as the title and hyperlinks in a wikipage. This library is written in Java.

DETAILS

The WikiParser tool consists of 3 components. The first component is a wikiextractor. The second component is the JWPL DataMachine. The third component is the WikiParser itself.

The python file wikiextractor.py is used to extract and clean text from a Wikipedia Database dump. The dump is a compressed XML file that contains the entire encyclopedia. The WikiExtractor tool parses the XML file and generates plain text while discarding information and annotation found in Wikipedia pages such as images and tables. It takes a compressed Wikipedia Database dump as an input and produces a number of files in a given directory as output. Each of these files contains several documents in the .txt format.

The wikiparser tool uses the Data Machine framework to obtain information such as categories, articles and link structure. To run the Data Machine framework, the following 3 files are needed. These files can be downloaded from the Wikimedia Download site.

[LANGCODE]wiki-[DATE]-pages-articles.xml.bz2 or [LANGCODE]wiki-[DATE]-pages-meta-current.xml.bz2
[LANGCODE]wiki-[DATE]-pagelinks.sql.gz
[LANGCODE]wiki-[DATE]-categorylinks.sql.gz
This will generate 11 files in a folder called output. This includes files such as PageMapLine.txt and Category.txt.

Finally, the WikiParser will parse the documents extracted by the WikiExtractor.py script in a multi-threaded way to speed up processing. It will create a serialized WikiPage object for each wiki document. They will be saved to a folder that you can specify. It takes the output of the WikiExtractor, a directory of files, and produces serialized WikiPage objects in the given directory as its output.

USAGE

3 shell scripts are provided to run the various parts of this tool. The first script "runwikiextractor.sh" runs the wikiextractor tool. The second script "runjwpl" runs the JWPL datamachine code. Finally, the last script "runwikiparser" runs the java wikiparser code. The runwikiextractor script takes 3 arguments as follows:

Number of processors to be used
File path of the wiki dump to be parsed
File path of your desired location for your output for the wikiextractor.py
Run it using: sh runwikiextractor.sh "Number of Processors" "Wiki Dump File Path" "Directory for output of WikiExtractor.py"

The runjwpl script takes 1 argument as follows:

File path to directory containing source files required for JWPL
Run it using: sh runjwpl.sh "Directory to files required for JWPL"

The runwikiparser script takes 2 arguments as follows:

File path to directory containing extracted wiki pages to be parsed
File path of desired output for the java wiki parser
Run it using: sh runwikiparser.sh "Directory of WikiExtractor output" "Directory for output of WikiParser"