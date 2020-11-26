package com.bti360.lucene;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.util.TokenFilterFactory;

import java.util.Map;

public class EmailUsernameCombinerTokenFilterFactory extends TokenFilterFactory {

    /**
     * Initialize this factory via a set of key-value pairs.
     *
     * @param args
     */
    public EmailUsernameCombinerTokenFilterFactory(Map<String, String> args) {
        super(args);
    }

    @Override
    public TokenStream create(TokenStream input) {
        return new EmailUsernameCombinerTokenFilter(input);
    }
}
