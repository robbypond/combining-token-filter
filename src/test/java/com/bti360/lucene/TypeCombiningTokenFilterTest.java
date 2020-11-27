package com.bti360.lucene;

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
        classicTokenizer.setReader(new StringReader("john.smith@bti.com"));
        TokenStream tokenStream = wordDelimiterGraphFilterFactory(classicTokenizer);

        Map<String, String> args = new HashMap<>();
        args.put(ClassicTokenizer.TOKEN_TYPES[ClassicTokenizer.EMAIL], null);
        tokenStream = new TypeCombiningTokenFilterFactory(args).create(tokenStream);

        assertTokenStreamContents(tokenStream,
                new String[]{"johnsmithbticom", "john",  "smith", "johnsmith", "bti", "smithbti", "com", "bticom"},
                new int[] {0, 0, 5, 5, 11, 11, 15, 15}, new int[] {18, 4, 10, 10, 14, 14, 18, 18},
                new int[] {1, 0, 1, 0, 1, 0, 1, 0});
    }

    public void testTokenFilterWithEmailAndAcronyms() throws Exception {
        ClassicTokenizer classicTokenizer = new ClassicTokenizer();
        classicTokenizer.setReader(new StringReader("john.smith@bti.com works for I.B.M."));
        TokenStream tokenStream = wordDelimiterGraphFilterFactory(classicTokenizer);

        Map<String, String> args = new HashMap<>();
        args.put(ClassicTokenizer.TOKEN_TYPES[ClassicTokenizer.EMAIL], null);
        args.put(ClassicTokenizer.TOKEN_TYPES[ClassicTokenizer.ACRONYM], null);

        tokenStream = new TypeCombiningTokenFilterFactory(args).create(tokenStream);

        assertTokenStreamContents(tokenStream,
                new String[]{"johnsmithbticom", "john",  "smith", "johnsmith", "bti", "smithbti", "com", "bticom",
                    "works", "for", "IBM", "I", "B", "IB", "M", "BM"},
                new int[] {0, 0, 5, 5, 11, 11, 15, 15, 19, 25, 29, 29, 31, 31, 33, 33},
                new int[] {18, 4, 10, 10, 14, 14, 18, 18, 24, 28, 34, 30, 32, 32, 34, 34},
                new int[] {1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0});
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