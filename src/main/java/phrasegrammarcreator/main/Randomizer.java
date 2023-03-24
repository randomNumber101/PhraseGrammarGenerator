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

    public int sample(double[] distribution) {
        double pick = nextDouble();
        double interval = 0;
        for (int i = 0; i < distribution.length; i++) {
            interval += distribution[i];
            if (pick < interval)
                return i;
        }
        return -1;
    }

    public long getSeed() {
        return seed;
    }

}
