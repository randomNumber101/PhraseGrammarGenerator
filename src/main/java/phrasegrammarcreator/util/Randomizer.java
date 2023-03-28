package phrasegrammarcreator.util;

import java.util.Random;

public class Randomizer extends Random{

    private long seed;

    public Randomizer(){
        super();
        seed = nextLong();
        setSeed(seed);
    };

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
