package phrasegrammarcreator.io.parser.core;

import phrasegrammarcreator.io.parser.core.SingleValueParser;

public class StringParser extends SingleValueParser<String> {
    @Override
    public String parse(String value) throws Exception {
        return value;
    }
}
