package phrasegrammarcreator.io.out.generate;

public enum Task {
    MASKING,
    TREE_BRACKETING;

    public static Task parse(String s) {
        switch (s.toLowerCase()) {
            case "masking" -> {
                return MASKING;
            }
            case "tree-bracketing" -> {
                return TREE_BRACKETING;
            }
            default -> throw new IllegalArgumentException("Invalid task: " + s);
        }
    }
}
