package CheckersEngine.BaseEngine;

public enum ETypeFigure {
    CHECKERS((short)1),
    QUINE((short)2);

    ETypeFigure(short type) {
        this.type = type;
    }

    public short getType() {
        return type;
    }

    private short type;
}
