package phrasegrammarcreator.core.phrases.variables;

import org.jetbrains.annotations.Nullable;
import phrasegrammarcreator.core.phrases.Phrasable;
import phrasegrammarcreator.core.phrases.Phrase;

import java.util.List;

public abstract class Variable implements Phrasable {

    public String getRegex() {
        return regex;
    }

    public String getName() {
        return name;
    }

    public enum Type {
        NON_TERMINAL,
        TERMINAL,
        WORD
    }

    private String regex;
    private String name;


    public Variable(String regex) {
        this(regex, regex);
    }

    public Variable(String regex, String name) {
        this.name = name;
        this.regex = regex;
    }

    @Override
    public Phrase toPhrase() {
        return new Phrase(List.of(this));
    }

    public String toString() {
        return name;
    }

    public abstract Type getType();
    public abstract VariableInstance<?> createInstance();


    public static Variable ofRegex(Type type, Vocabulary dict, String regex, @Nullable String name) {
        Variable result = dict.getVariable(regex);
        if (result != null)
            return result;

        Variable var = switch (type) {
                case TERMINAL -> (name == null)? new Terminal(regex) : new Terminal(regex, name);
                case NON_TERMINAL -> (name == null)? new NonTerminal(regex) : new NonTerminal(regex, name);
                default -> null;
        };
        dict.registerVariable(regex, var);
        return var;
    }


}
