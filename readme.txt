1) Improper encoding produced after parsing by wikiextractor.py
	eg) <a href="%s">%s</a>
		This causes an error by the java.net.URLDecoder.decode function since there is an illegal trailing % character
	Solution:  Use java.net.URLEncoder.encode to properly encode the string first before attempting to decode it.  This will remove all illegal characters.
	
2) Also in _cleanHyperLink function, certain strings that are not hyperlinks will get passed into this function due to the implementation of pattern matching in the cleanDocText function.
	eg) <NAK> This is a serious problem.
		This causes an error in the _cleanHyperLink function.
	Solution:  Check for the following substring "<a href" that should exist at the beginning of the input string to _cleanHyperLink function.

