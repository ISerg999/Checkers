package CheckersEngine.BaseEngine;

/**
 * Класс парных данных.
 */
public class CPair<TX, TY> {

    private TX first;
    private TY second;

    public CPair(TX first, TY second) {
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
        if (!(obj instanceof CPair)) return false;
        CPair<TX, TY> newP = (CPair<TX, TY>) obj;
        return  this.getFirst() == newP.getFirst() && this.getSecond() == newP.getSecond();
    }

    @Override
    public int hashCode() {
        int a = first == null ? 0: first.hashCode();
        int b = second == null ? 0: second.hashCode();
        return a ^ b;
    }
}
