package util;

public class Pair {
    /**
     * Stores infomation about a pair, used to store coordinate points.
     */
    private float x;
    private float y;

    public Pair(float x, float y){
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
