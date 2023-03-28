package phrasegrammarcreator.compute;

import phrasegrammarcreator.core.rules.ContextFreeRule;
import phrasegrammarcreator.core.rules.Rule;
import phrasegrammarcreator.util.Randomizer;

import java.util.*;
import java.util.stream.Collector;

public class DerivationSet extends HashSet<Derivation> {

    public DerivationSet(Collection<? extends Derivation> c) {

        super();
        for (Derivation d : c) {
            this.add(new Derivation(d.getRule(), d.getOccurence()));
        }
    }

    public DerivationSet() {
        super();
    }

    public Derivation getRandom(Random random) {
        Derivation[] derivations = this.getArray();
        return derivations[random.nextInt(derivations.length)];
    }

    public Derivation[] getArray() {
        return this.toArray(new Derivation[0]);
    }

    public DerivationSet deleteAllInSection(Occurrence o) {
        ArrayList<Derivation> list = new ArrayList<>(List.of(getArray()));
        Collections.sort(list);
        for (Derivation d : list) {
            // Elements after occurrence cannot cross section, and as list is sorted break loop for residual
            if (d.getOccurence().from >= o.to)
                break;

            //Elements before occurrence may cross section
            if (d.getOccurence().to > o.from)
                this.remove(d);

        }
        return this;
    }

    public DerivationSet copy() {
        DerivationSet set = new DerivationSet();
        for (Derivation d : this) {
            set.add(new Derivation(d.getRule(), d.getOccurence()));
        }
        return set;
    }

    public DerivationSet shiftedBy(int shift) {
        forEach(d -> d.shiftBy(shift));
        return this;
    }

    public DerivationSet extendedBy(int extension) {
        forEach(d -> d.extendBy(extension));
        return this;
    }

    public DerivationSet getContextFreeSubSet() {
        DerivationSet set =
                this.stream().filter(derivation ->
                        derivation.getRule() instanceof ContextFreeRule).collect(
                                DerivationSet.toSet());
        return set.copy();
    }

    public <R extends Rule, O extends Occurrence> void add(R r, O o) {
        add(new Derivation(r, o));
    }

    public <R extends Rule, O extends Occurrence> void add(Collection<R> rs, O o) {
        for (Rule r : rs)
            add(r, o);
    }

    public <R extends Rule, O extends Occurrence> void add(R r, Collection<O> os) {
        for (Occurrence o : os) {
            add(r, o);
        }
    }
    public static
    Collector<Derivation, ?, DerivationSet> toSet() {
        return Collector.of(DerivationSet::new, DerivationSet::add,
                (left, right) -> {
                    if (left.size() < right.size()) {
                        right.addAll(left); return right;
                    } else {
                        left.addAll(right); return left;
                    }
                },
                Collector.Characteristics.UNORDERED);
    }


}
