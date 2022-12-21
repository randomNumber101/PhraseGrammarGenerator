package phrasegrammarcreator.compute;

import phrasegrammarcreator.core.rules.ContextFreeRule;
import phrasegrammarcreator.core.rules.Rule;

import java.util.*;
import java.util.stream.Collectors;

public class DerivationSet extends HashSet<Derivation> {


    public DerivationSet() {
        super();
    }
    public DerivationSet(Collection<? extends Derivation> c) {
        super(c);
    }

    public Derivation getRandom() {
        Random random = new Random();
        Derivation[] derivations = this.getArray();
        return derivations[random.nextInt(derivations.length)];
    }

    public Derivation[] getArray() {
        return this.toArray(new Derivation[0]);
    }

    public DerivationSet deleteAllInSection(Occurence o) {
        List<Derivation> list = new ArrayList<>(List.of(getArray()));
        Collections.sort(list);
        for (Derivation d : list) {
            //Elements before occurrence may cross section
            if (d.getOccurence().to > o.from)
                this.remove(d);

            // Elements after occurrence cannot cross section
            if (d.getOccurence().from >= o.to)
                break;
        }
        return this;
    }



    public DerivationSet getContextFreeSubSet() {
        Set<Derivation> set =
                this.stream().filter(derivation ->
                        derivation.getRule() instanceof ContextFreeRule).collect(Collectors.toSet()
                );
        return new DerivationSet(set);
    }

    public <R extends Rule, O extends Occurence> void add(R r, O o) {
        add(new Derivation(r, o));
    }

    public <R extends Rule, O extends Occurence> void add(Collection<R> rs, O o) {
        for (Rule r : rs)
            add(r, o);
    }

    public <R extends Rule, O extends Occurence> void add(R r, Collection<O> os) {
        for (Occurence o : os) {
            add(r, o);
        }
    }
}
