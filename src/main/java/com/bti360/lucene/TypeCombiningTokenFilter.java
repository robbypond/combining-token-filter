package com.bti360.lucene;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.ClassicTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public final class TypeCombiningTokenFilter extends TokenFilter {

    private final TypeAttribute typeAttribute = addAttribute(TypeAttribute.class);
    private final CharTermAttribute termAttribute = addAttribute(CharTermAttribute.class);
    private final PositionIncrementAttribute posIncAtt = addAttribute(PositionIncrementAttribute.class);

    private final List<String> types;
    private Token previousToken;
    private final LinkedList<String> newTokens = new LinkedList<>();
    private State savedState;

    public TypeCombiningTokenFilter(List<String> types, TokenStream input) {
        super(input);
        this.types = types;
    }

    @Override
    public boolean incrementToken() throws IOException {
        if (!newTokens.isEmpty()) {
            restoreState(savedState);
            posIncAtt.setPositionIncrement(0);
            termAttribute.setEmpty().append(newTokens.remove());
            return true;
        }

        if (input.incrementToken()) {
            final char[] buffer = termAttribute.buffer();
            final int bufferLength = termAttribute.length();
            final String type = typeAttribute.type();

            if (types.contains(type)) {
                Token currentToken = new Token(buffer, bufferLength, type);
                if (previousToken != null && !previousToken.equals(currentToken) && !previousToken.value.contains(currentToken.value) && previousToken.type.equals(currentToken.type)) {
                    concatTokens(previousToken, currentToken);
                }
                previousToken = currentToken;
                savedState = captureState();
            }
            return true;
        }

        return false;
    }

    private void concatTokens(Token token1, Token token2) {
        String newValue = token1.value + token2.value;
        newTokens.add(newValue);
    }

    private static class Token {

        private final String type;
        private final String value;

        public Token(char[] buffer, int bufferLength, String type) {
            this.value = String.valueOf(buffer, 0, bufferLength);
            this.type = type;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Token token = (Token) o;
            return type.equals(token.type) &&
                    value.equals(token.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(type, value);
        }
    }
}
