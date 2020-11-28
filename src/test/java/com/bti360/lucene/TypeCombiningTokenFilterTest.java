package com.gmail360.lucene;

import com.bti360.lucene.TypeCombiningTokenFilterFactory;
import org.apache.lucene.analysis.BaseTokenStreamTestCase;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.miscellaneous.WordDelimiterGraphFilterFactory;
import org.apache.lucene.analysis.standard.ClassicTokenizer;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

public class TypeCombiningTokenFilterTest extends BaseTokenStreamTestCase {

    public void testTokenFilterWithEmail() throws Exception {
        ClassicTokenizer classicTokenizer = new ClassicTokenizer();
        classicTokenizer.setReader(new StringReader("john.smith@gmail.com"));
        TokenStream tokenStream = wordDelimiterGraphFilterFactory(classicTokenizer);

        tokenStream = new TypeCombiningTokenFilterFactory(new HashMap<>()).create(tokenStream);

        assertTokenStreamContents(tokenStream,
                new String[]{"johnsmithgmailcom", "john",  "smith", "johnsmith", "gmail", "smithgmail", "com", "gmailcom"},
                new int[] {0, 0, 5, 5, 11, 11, 17, 17}, new int[] {20, 4, 10, 10, 16, 16, 20, 20},
                new int[] {1, 0, 1, 0, 1, 0, 1, 0});
    }
    
    private TokenStream wordDelimiterGraphFilterFactory(TokenStream tokenStream) {
        Map<String, String> args = new HashMap<>();
        args.put("generateWordParts", "1");
        args.put("generateNumberParts", "1");
        args.put("catenateWords", "1");
        args.put("catenateNumbers", "1");
        args.put("catenateAll", "0");
        args.put("splitOnCaseChange", "1");
        return new WordDelimiterGraphFilterFactory(args).create(tokenStream);
    }

}