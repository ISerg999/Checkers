package CheckersEngine.BaseEngine;

/**
 * Класс парных данных.
 */
public class Pair<TX, TY> {

    public Pair(TX x, TY y) {
        this.x = x;
        this.y = y;
    }

    public TX getX() {
        return x;
    }

    public TY getY() {
        return y;
    }

    private TX x;
    private TY y;
}
