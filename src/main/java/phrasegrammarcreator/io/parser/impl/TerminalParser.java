package phrasegrammarcreator.io.parser.impl;

import phrasegrammarcreator.core.phrases.variables.Terminal;
import phrasegrammarcreator.core.phrases.variables.Variable;
import phrasegrammarcreator.core.phrases.variables.Vocabulary;
import phrasegrammarcreator.io.parser.core.SingleValueParser;

public class TerminalParser extends SingleValueParser<Terminal> {
    private Vocabulary vocabulary;

    public TerminalParser(Vocabulary vocabulary) {
        this.vocabulary = vocabulary;
    }
    @Override
    public Terminal parse(String regex) throws Exception {
        return (Terminal) Variable.ofRegex(Variable.Type.TERMINAL, vocabulary, regex, null);
    }
}
