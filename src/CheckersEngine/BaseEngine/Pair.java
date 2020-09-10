package CheckersEngine.BaseEngine;

/**
 * Класс парных данных.
 */
public class Pair<TX, TY> {

    private TX first;
    private TY second;

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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (null == obj) return false;
        if (!(obj instanceof Pair)) return false;
        Pair<TX, TY> newP = (Pair<TX, TY>) obj;
        return  this.getFirst() == newP.getFirst() && this.getSecond() == newP.getSecond();
    }

    @Override
    public int hashCode() {
        return first.hashCode() ^ second.hashCode();
    }
}
