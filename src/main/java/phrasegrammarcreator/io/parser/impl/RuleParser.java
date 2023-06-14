package phrasegrammarcreator.io.parser.impl;

import phrasegrammarcreator.core.phrases.Phrase;
import phrasegrammarcreator.core.phrases.variables.NonTerminal;
import phrasegrammarcreator.core.phrases.variables.Vocabulary;
import phrasegrammarcreator.core.rules.ContextFreeRule;
import phrasegrammarcreator.core.rules.ContextSensitiveRule;
import phrasegrammarcreator.core.rules.Rule;
import phrasegrammarcreator.io.parser.core.SingleValueParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RuleParser extends SingleValueParser<List<Rule>> {

    private int index = 0;

    public static final String PATTERN_STRING = "([\\w\\s]*)->([\\w\\s\\|]*)";
    public static final Pattern pattern = Pattern.compile(PATTERN_STRING, Pattern.CASE_INSENSITIVE);

    private final Vocabulary vocabulary;

    private final PhraseParser phraseParser;

    public RuleParser(Vocabulary vocabulary) {
        this.vocabulary = vocabulary;
        this.phraseParser = new PhraseParser(vocabulary);
    }

    @Override
    public List<Rule> parse(String rule) throws Exception {
        Matcher matcher = pattern.matcher(rule);
        if (!matcher.find() || matcher.groupCount() == 3)
            throw new IOException("Couldn't parse rule: " + rule);

        // Parse phrases
        Phrase from = phraseParser.parse(matcher.group(1));

        List<Phrase> tos = new ArrayList<>();
        String qs = matcher.group(2);
        String[] splits = matcher.group(2).split("\\|");
        for (String s : splits) {
            Phrase subRuleDerivation = phraseParser.parse(s);
            tos.add(subRuleDerivation);
        }

        List<Rule> parsed = new ArrayList<>();

        if (from.size() == 1)
            for (Phrase to : tos)
                parsed.add(new ContextFreeRule("R" + index, (NonTerminal) from.get(0).getBuilder(), to));
        else
            for (Phrase to : tos)
                parsed.add(new ContextSensitiveRule("R" + index, from, to));

        index++;
        return parsed;
    }
}
