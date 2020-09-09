package CheckersEngine.BaseEngine;

/**
 * Класс парных данных.
 */
public class Pair<TX, TY> {

    public Pair(TX first, TY second) {
        this.first = first;
        this.second = second;
    }

    public TX getFirst() {
        return first;
    }
    public TY getSecond() {
        return second;
    }
    public void setFirst(TX first) {
        this.first = first;
    }
    public void setSecond(TY second) {
        this.second = second;
    }

    private TX first;
    private TY second;
}
