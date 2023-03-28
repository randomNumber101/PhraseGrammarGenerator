package phrasegrammarcreator.main;

import phrasegrammarcreator.core.phrases.words.generate.Task;
import phrasegrammarcreator.core.phrases.words.generate.WordGenerationPolicy;

public record Settings(
        String outputDir,
        WordGenerationPolicy policy,
        Task task,
        int possibilityCap,
        int depthCap) {}
