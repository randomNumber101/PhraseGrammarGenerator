package phrasegrammarcreator.io.print.info;

import phrasegrammarcreator.compute.Derivation;
import phrasegrammarcreator.compute.Occurrence;
import phrasegrammarcreator.core.derive.impl.DerivationNode;
import phrasegrammarcreator.core.derive.impl.DerivationPath;
import phrasegrammarcreator.core.derive.impl.DerivationPointer;
import phrasegrammarcreator.core.derive.impl.DerivationTree;
import phrasegrammarcreator.core.derive.tree.Node;
import phrasegrammarcreator.core.phrases.Phrase;
import phrasegrammarcreator.core.rules.Rule;
import phrasegrammarcreator.io.print.Visual;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class DerivationTreeInfo extends InfoWatcher<DerivationTree> {
    public DerivationTreeInfo(PrintStream out, DerivationTree watched) {
        super(out, watched);
    }

    @Override
    public String toString(DerivationTree watched) {
        return printToString(this::printTree, this::printHeadPathDerivations);
    }

    public void printHeadPathDerivations() {
        printPathDerivations(watched.getHead());
    }

    public void printPathDerivations(DerivationNode ofNode) {
        if (!watched.contains(ofNode)) {
            out.printf("%s not part of tree.%n", ofNode.toString());
            return;
        }

        DerivationPath path =  watched.getPathOf(ofNode);
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

            Occurrence fromOcc = derivation.getOccurence();
            froms.add(Visual.markOccurrences(fromPhrase, fromOcc));
            tos.add(Visual.markOccurrences(toPhrase, fromOcc.extendedBy(rule.getLengthChange())));
            rules.add(rule.getName() + ": " + rule.toString());
            heads.add(toPhrase.toString(" "));
        }

        List<String> rows = Visual.printTable(header, List.of(froms, tos, rules, heads));

        for (String row : rows)
            out.println(row);
    }

    public void printSeperatedPhrase(Phrase phrase) {
        out.println(phrase.toString(" "));
    }

    public void printTree() {
        printTree(watched.getRoot(), 0);
        out.println();
    }

    private void printTree(Node<Phrase, DerivationPointer> root, int depth) {
        String rootRule = "ROOT";
        if (root.getParent() != null)
            rootRule = root.getParent().getPointer().stream()
                    .filter(p -> p.isInitialized() && p.getPointingTo().equals(root))
                            .findFirst().orElseThrow().getDerivation().getRule().toString();

        printTreeInfo(root.getData().toString(" ") +" (used "+ rootRule + ")", depth, true);

        List<Node<Phrase, DerivationPointer>> children = root.getChildren();
        ArrayList<DerivationPointer> pointers = root.getPointer().stream()
                .filter(dp -> !dp.isInitialized())
                .sorted(Comparator.comparing(l -> l.getDerivation().getOccurence()))
                .collect(Collectors.toCollection(ArrayList::new));

        int uninitialized = pointers.size();
        for (int i = 0; i < uninitialized; i++) {
            String index = "(" + depth + "." + i + ")";
            Derivation p = pointers.get(i).getDerivation();
            String rule = p.getRule().toString() + " "+ p.getOccurence().toString();
            printTreeInfo(  " ... " + rule, depth + 1, false);
        }
        for (Node<Phrase, DerivationPointer> child : children) {
            printTree(child, depth + 1);
        }
    }

    private void printTreeInfo(String info, int depth, boolean realBranch) {
        out.print("  ".repeat(depth));
        if (realBranch)
            out.println("╚═╦╡| " + info);
        else
            out.println("╟╌╌ " + info);
    }

}
