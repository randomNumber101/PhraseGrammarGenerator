package phrasegrammarcreator.compute;

import phrasegrammarcreator.core.rules.Rule;

import java.util.*;

/**
 * Deprecated.
 * Use DerivationSet instead
 */
@Deprecated

public class PossibleDerivations {

    protected HashMap<Rule, List<Occurrence>> applications;

    public PossibleDerivations() {
        applications = new HashMap<>();
    }

    public PossibleDerivations(PossibleDerivations copyOf) {
        applications = new HashMap<>(copyOf.applications);
    }

    public <R extends Rule> void addOccurrence(R r, Occurrence o) {
        if (!applications.containsKey(r))
            applications.put(r, new ArrayList<>());
        applications.get(r).add(o);
    }

    public <R extends Rule> void  addOccurrences(R r, Collection<Occurrence> o) {
        if (!applications.containsKey(r))
            applications.put(r, new ArrayList<>());
        applications.get(r).addAll(o);
    }

    public <R extends Rule> void addOccurrences(Collection<R> rules, Occurrence o) {
        for (Rule r : rules)
            addOccurrence(r, o);
    }


    public DerivationSet getSet() {
        Set<Rule> rules = applications.keySet();
        DerivationSet derivations = new DerivationSet();
        for (Rule r : rules)
            for (Occurrence o : applications.get(r))
                derivations.add(new Derivation(r, o));

        return derivations;
    }

    public void subtract(DerivationSet set) {

    }

    public void add(DerivationSet set) {

    }


}
