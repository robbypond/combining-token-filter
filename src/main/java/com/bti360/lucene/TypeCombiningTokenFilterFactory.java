package com.bti360.lucene;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.util.TokenFilterFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TypeCombiningTokenFilterFactory extends TokenFilterFactory {

    private final List<String> types = new ArrayList<>();
    /**
     * Initialize this factory via a set of key-value pairs.
     *
     * @param args
     */
    public TypeCombiningTokenFilterFactory(Map<String, String> args) {
        super(args);
        while (args.containsKey("type")) {
            String type = args.remove("type");
            types.add(type);
        }
    }

    @Override
    public TokenStream create(TokenStream input) {
        return new TypeCombiningTokenFilter(types, input);
    }
}
