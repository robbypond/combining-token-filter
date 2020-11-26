package com.bti360.lucene;

import org.apache.lucene.analysis.BaseTokenStreamTestCase;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.miscellaneous.WordDelimiterGraphFilterFactory;
import org.apache.lucene.analysis.standard.ClassicTokenizer;
import static com.bti360.lucene.EmailUsernameCombinerTokenFilter.EMAIL_TYPE;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

public class EmailUsernameCombinerTokenFilterTest extends BaseTokenStreamTestCase {

    public void testEmailTokenFilter() throws Exception {
        ClassicTokenizer classicTokenizer = new ClassicTokenizer();
        classicTokenizer.setReader(new StringReader("robby.pond@bti.com"));
        Map<String, String> args = new HashMap<>();
        args.put("generateWordParts", "1");
        args.put("generateNumberParts", "1");
        args.put("catenateWords", "1");
        args.put("catenateNumbers", "1");
        args.put("catenateAll", "0");
        args.put("splitOnCaseChange", "1");
        WordDelimiterGraphFilterFactory wordDelimiterGraphFilterFactory = new WordDelimiterGraphFilterFactory(args);
        TokenStream tokenStream = wordDelimiterGraphFilterFactory.create(classicTokenizer);

        tokenStream = new EmailUsernameCombinerTokenFilter(tokenStream);

        assertTokenStreamContents(tokenStream,
                new String[]{"robbypondbticom", "robby",  "pond", "robbypond", "bti", "pondbti", "com", "bticom"},
                new int[] {0, 0, 6, 6, 11, 11, 15, 15}, new int[] {18, 5, 10, 10, 14, 14, 18, 18},
                new String[]{ EMAIL_TYPE, EMAIL_TYPE, EMAIL_TYPE, EMAIL_TYPE, EMAIL_TYPE, EMAIL_TYPE, EMAIL_TYPE, EMAIL_TYPE},
                new int[] {1, 0, 1, 0, 1, 0, 1, 0});
    }

}