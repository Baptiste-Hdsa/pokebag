package fr.bapti.esiea.utils;

public class RandomTools {

    public static float getRandomFloat(float min, float max) {
        return (float) (min + Math.random() * (max - min));
    }

    public static int getRandomIntPair(Pair pair) {
        return (int) (pair.getPair()[0] + Math.random() * (pair.getPair()[1] - pair.getPair()[0] + 1));
    }
}
