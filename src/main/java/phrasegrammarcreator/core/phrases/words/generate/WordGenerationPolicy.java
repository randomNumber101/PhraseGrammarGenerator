package phrasegrammarcreator.core.phrases.words.generate;

public enum WordGenerationPolicy {
    SINGLE_RANDOM,
    ALL_POSSIBILITIES;

    public static WordGenerationPolicy parse(String s) {
        switch (s.toLowerCase()) {
            case "single" -> {
                return SINGLE_RANDOM;
            }
            case "all" -> {
                return ALL_POSSIBILITIES;
            }
            default -> throw new IllegalArgumentException("Invalid word generation policy: " + s);
        }
    }
}
