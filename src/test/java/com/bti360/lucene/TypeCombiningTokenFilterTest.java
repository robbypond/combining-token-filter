package com.bti360.lucene;

import org.apache.lucene.analysis.BaseTokenStreamTestCase;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.miscellaneous.WordDelimiterGraphFilterFactory;
import org.apache.lucene.analysis.standard.ClassicTokenizer;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

public class TypeCombiningTokenFilterTest extends BaseTokenStreamTestCase {

    public void testEmailTokenFilter() throws Exception {
        ClassicTokenizer classicTokenizer = new ClassicTokenizer();
        classicTokenizer.setReader(new StringReader("john.smith@bti.com"));
        Map<String, String> args = new HashMap<>();
        args.put("generateWordParts", "1");
        args.put("generateNumberParts", "1");
        args.put("catenateWords", "1");
        args.put("catenateNumbers", "1");
        args.put("catenateAll", "0");
        args.put("splitOnCaseChange", "1");
        WordDelimiterGraphFilterFactory wordDelimiterGraphFilterFactory = new WordDelimiterGraphFilterFactory(args);
        TokenStream tokenStream = wordDelimiterGraphFilterFactory.create(classicTokenizer);

        args = new HashMap<>();
        args.put("type", ClassicTokenizer.TOKEN_TYPES[ClassicTokenizer.EMAIL]);
        tokenStream = new TypeCombiningTokenFilterFactory(args).create(tokenStream);

        assertTokenStreamContents(tokenStream,
                new String[]{"johnsmithbticom", "john",  "smith", "johnsmith", "bti", "smithbti", "com", "bticom"},
                new int[] {0, 0, 5, 5, 11, 11, 15, 15}, new int[] {18, 4, 10, 10, 14, 14, 18, 18},
                new int[] {1, 0, 1, 0, 1, 0, 1, 0});
    }

}