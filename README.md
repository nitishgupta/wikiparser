**WIKIPARSER**

This is a tool that extracts text from a Wikipedia database dump.  It uses an external WikiExtractor script to extract files from the compressed database dump.  The files are extracted into folders with document texts.  Once this is done, the wikiparser will parse the document texts to extract relevant information such as the title and hyperlinks in a wikipage.  This library is written in Java.

**DETAILS**

The WikiParser tool consists of 3 components.  The first component is a wikiextractor.  The second component is the JWPL DataMachine.  The third component is the WikiParser itself. 

The python file wikiextractor.py is used to extract and clean text from a Wikipedia Database dump.  The dump is a compressed XML file that contains the entire encyclopedia.  The WikiExtractor tool parses the XML file and generates plain text while discarding information and annotation found in Wikipedia pages such as images and tables.  It takes a compressed Wikipedia Database dump as an input and produces a number of files in a given directory as output.  Each of these files contains several documents in the .txt format. 

The wikiparser tool uses the Data Machine framework to obtain information such as categories, articles and link structure.  To run the Data Machine framework, the following 3 files are needed.  These files can be downloaded from the Wikimedia Download site.
1) [LANGCODE]wiki-[DATE]-pages-articles.xml.bz2 or [LANGCODE]wiki-[DATE]-pages-meta-current.xml.bz2
2) [LANGCODE]wiki-[DATE]-pagelinks.sql.gz
3) [LANGCODE]wiki-[DATE]-categorylinks.sql.gz

This will generate 11 files in a folder called output.  This includes files such as PageMapLine.txt and Category.txt.

3 files from the output of the JWPL DataMachine are parsed to create relevant data structures.  These files are PageMapLine.txt, Category.txt and category_pages.txt.  The data structures being created are written to text files as shown below.  In this README, resolved pages refer to Wikipedia pages that are the original articles and unresolved pages refer to articles that will be redirected to the resolved pages when you try to access them. 

PageMapLine.txt:

1) allPageIds.txt - List of all page ids
2) resPageIds.txt - List of all resolved page ids
3) pageIds2PageTitles.txt.txt - Map from page ids (first column) to page titles (second column)
4) pageTitles2resPageTitles.txt - Map from page titles (first column) to resolved page titles (second column)
5) resListPages.txt - List of page Ids which belong to list pages
    
Category.txt:

1) categoryIds2CategoryTitles.txt - Map from category id (first column) to category title (second column)

category_pages.txt:

1) resPageIds2CategoryTitles.txt - Map from resolved page ids to set of category titles
2) resDisambPageIds.txt - List of resolved page ids of disambiguation pages
3) resNonDisambPageIds.txt - List of resolved page ids of non-disambiguation pages

Finally, the WikiParser will parse the documents extracted by the WikiExtractor.py script in a multi-threaded way to speed up processing.  It will create a serialized WikiPage object for each wiki document.  They will be saved to a folder that you can specify.  It takes the output of the WikiExtractor, a directory of files, and produces serialized WikiPage objects in the given directory as its output.

**USAGE**

4 shell scripts are provided to run the various parts of this tool.  The first script "runwikiextractor.sh" runs the wikiextractor tool.  The second script "runjwpl" runs the JWPL datamachine code.  The third script "runwikiparser" runs the java wikiparser code.  The last script "runjwplparser" runs the code that parses the JWPL output files as mentioned above.
The runwikiextractor script takes 3 arguments as follows:

1) Number of processors to be used
2) File path of the wiki dump to be parsed
3) File path of your desired location for your output for the wikiextractor.py

Run it using:
    `sh runwikiextractor.sh "Number of Processors" "Wiki Dump File Path" "Directory for output of WikiExtractor.py"`

The runjwpl script takes 1 argument as follows:

1) File path to directory containing source files required for JWPL

Run it using:
    `sh runjwpl.sh "Directory to files required for JWPL"`

The runwikiparser script takes 2 arguments as follows:

1) File path to directory containing extracted wiki pages to be parsed
2) File path of desired output for the java wiki parser

Run it using:
    `sh runwikiparser.sh "Directory of WikiExtractor output" "Directory for output of WikiParser"`
    
The runjwplparser script takes 1 argument as follows:

1) File path to output directory

Run it using:
    `sh runjwplparser.sh "Directory for output of JWPLParser"`
