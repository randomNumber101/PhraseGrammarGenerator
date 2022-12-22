package phrasegrammarcreator.io.print.info;

import phrasegrammarcreator.compute.Derivation;
import phrasegrammarcreator.compute.Occurence;
import phrasegrammarcreator.core.derive.impl.DerivationPath;
import phrasegrammarcreator.core.derive.impl.DerivationPointer;
import phrasegrammarcreator.core.derive.impl.DerivationTree;
import phrasegrammarcreator.core.phrases.Phrase;
import phrasegrammarcreator.core.rules.Rule;
import phrasegrammarcreator.io.print.Util;
import phrasegrammarcreator.io.print.Visual;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class DerivationTreeInfo extends InfoWatcher<DerivationTree> {
    public DerivationTreeInfo(PrintStream out, DerivationTree watched) {
        super(out, watched);
    }

    @Override
    public String toString(DerivationTree watched) {
        return printToString(this::printHeadPathDerivations);
    }

    public void printHeadPathDerivations() {
       DerivationPath path =  watched.getPathOf(watched.getHead());
       List<DerivationPointer> derivationPath = path.getDerivations();
       List<String> header = List.of("FROM", "TO", "RULE", "HEAD");

       ArrayList<String> froms = new ArrayList<>(List.of(""));
       ArrayList<String> tos = new ArrayList<>(List.of(""));
       ArrayList<String> rules = new ArrayList<>(List.of(""));
       ArrayList<String> heads = new ArrayList<>(List.of(watched.getRoot().getData().toString(" ")));

       for (int i = 0; i < derivationPath.size(); i++) {
           Phrase fromPhrase = path.get(i).getData();
           Phrase toPhrase = path.get(i + 1).getData();
           Derivation derivation = derivationPath.get(i).getDerivation();
           Rule rule = derivation.getRule();

           Occurence fromOcc = derivation.getOccurence();
           froms.add(Visual.markOccurrences(fromPhrase, fromOcc));
           tos.add(Visual.markOccurrences(toPhrase, fromOcc.extendBy(rule.getLengthChange())));
           rules.add(rule.getName() + ": " + rule.toString());
           heads.add(toPhrase.toString(" "));
       }

       List<String> rows = Util.printTable(header, List.of(froms, tos, rules, heads));

       for (String row : rows)
           out.println(row);
    }

    public void printSeperatedPhrase(Phrase phrase) {
        out.println(phrase.toString(" "));
    }

}
