package phrasegrammarcreator.io.parser.impl;

import phrasegrammarcreator.core.phrases.Phrase;
import phrasegrammarcreator.core.phrases.variables.Variable;
import phrasegrammarcreator.core.phrases.variables.Vocabulary;
import phrasegrammarcreator.io.parser.core.SingleValueParser;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhraseParser extends SingleValueParser<Phrase> {

    public final String PATTERN_STRING = "\\s*(\\w+)((\\s+\\w+)*)\\s*";
    private Pattern pattern = Pattern.compile(PATTERN_STRING);

    private Vocabulary vocabulary;
    public PhraseParser(Vocabulary vocabulary) {
        this.vocabulary = vocabulary;
    }

    @Override
    public Phrase parse(String phraseString) throws Exception {
        Matcher matcher = pattern.matcher(phraseString);
        if (!matcher.find())
            throw new ParseException("Could not parse phrase:" + phraseString, 0);
        return new Phrase(parseVariablesFromString(phraseString));
    }

    public ArrayList<Variable> parseVariablesFromString(String phrase) throws ParseException {
        String[] regexes = phrase.strip().split("\\s+");
        ArrayList<Variable> list = new ArrayList<>();
        for (String reg : regexes) {

            // Empty phrase if only variable is 'empty"
            if (reg.equals("empty")) {
                continue;
            }

            Variable var = vocabulary.getVariable(reg);
            if (var == null) {
                String error = String.format("No registered variable found with regex \"%s\" in \"%s\"", reg, phrase);
                throw new ParseException(error, 0);
            }
            list.add(var);
        }
        return list;
    }
}
