package phrasegrammarcreator.core.derive.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DerivationPath extends ArrayList<DerivationNode> {

    public DerivationPath() {}
    public DerivationPath(Collection<DerivationNode> nodes) {
        super(nodes);
    }
    public List<DerivationPointer> getDerivations() {
        ArrayList<DerivationPointer> pointers = new ArrayList<>();
        for (int i = 0; i < size() - 1; i ++) {
            DerivationNode current = get(i);
            DerivationNode next = get(i + 1);

            pointers.add(current.getPointer().stream()
                    .filter(dp -> dp.isInitialized() && dp.getPointingTo().equals(next))
                    .findFirst().orElseThrow());
        }
        return pointers;
    }
}
