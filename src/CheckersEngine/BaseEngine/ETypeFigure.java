package CheckersEngine.BaseEngine;

public enum ETypeFigure {
    CHECKERS(4),
    QUINE(8);

    ETypeFigure(int direction) {
        this.direction = direction;
    }

    public int getDirection() {
        return direction;
    }

    private int direction;
}
