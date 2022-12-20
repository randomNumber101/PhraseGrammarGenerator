package phrasegrammarcreator.io.parser.core;

import phrasegrammarcreator.io.parser.core.SingleValueParser;

public class IntParser extends SingleValueParser<Integer> {
    @Override
    public Integer parse(String value) throws Exception {
        return Integer.parseInt(value);
    }
}
