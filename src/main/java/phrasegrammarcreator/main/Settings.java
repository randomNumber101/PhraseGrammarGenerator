package phrasegrammarcreator.main;

import phrasegrammarcreator.io.out.generate.Task;
import phrasegrammarcreator.io.out.generate.WordGenerationPolicy;

public record Settings(
        String name,
        Long seed,
        String outputDir,
        WordGenerationPolicy policy,
        Task task,
        int possibilityCap,
        int depthCap,
        int lengthCap) {}
