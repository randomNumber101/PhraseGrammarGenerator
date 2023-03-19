package phrasegrammarcreator.main;

import java.util.Random;

public class Randomizer extends Random{

    private static Randomizer randomizer;
    private long seed;

    private Randomizer(){
        super();
        seed = nextLong();
        setSeed(seed);
    };

    public static Randomizer getInstance() {
        if (randomizer == null)
            randomizer = new Randomizer();
        return randomizer;
    }

    public long getSeed() {
        return seed;
    }

}
