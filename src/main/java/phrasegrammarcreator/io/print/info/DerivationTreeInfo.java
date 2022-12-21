package phrasegrammarcreator.io.print.info;

import phrasegrammarcreator.core.derive.impl.DerivationNode;
import phrasegrammarcreator.core.derive.impl.DerivationTree;

import java.io.PrintStream;
import java.util.List;

public class DerivationTreeInfo extends InfoWatcher<DerivationTree> {
    public DerivationTreeInfo(PrintStream out, DerivationTree watched) {
        super(out, watched);
    }

    @Override
    public String toString(DerivationTree watched) {
        return null;
    }

    public void printHeadPathDerivations() {
        List<DerivationNode> path =  watched.getPathOf(watched.getHead()).stream().map(n -> (DerivationNode) n).toList();

    }

    public void printDerivationPath() {

    }
}
