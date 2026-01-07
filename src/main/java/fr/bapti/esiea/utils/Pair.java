package fr.bapti.esiea.utils;

public class Pair {
    private int min;
    private int max;

    public Pair(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public int[] getPair(){
        return new int[]{min, max};
    }

    public String getPairString(){
        return min + " " + max;
    }

}
