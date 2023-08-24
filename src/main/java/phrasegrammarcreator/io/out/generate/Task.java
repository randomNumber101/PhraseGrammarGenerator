package phrasegrammarcreator.io.out.generate;

public enum Task {
    MASKING,
    CLASS_MASKING,
    TREE_BRACKETING,

    BINARY_CLASSIFICATION;

    public static Task parse(String s) {
        switch (s.toLowerCase()) {
            case "masking" -> {
                return MASKING;
            }
            case "class-masking" -> {
                return CLASS_MASKING;
            }
            case "tree-bracketing" -> {
                return TREE_BRACKETING;
            }
            case "binary-classification" -> {
                return BINARY_CLASSIFICATION;
            }
            default -> throw new IllegalArgumentException("Invalid task: " + s);
        }
    }
}
