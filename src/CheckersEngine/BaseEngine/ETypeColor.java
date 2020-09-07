package CheckersEngine.BaseEngine;

public enum ETypeColor {
    WHITE((short)1),
    BLACK((short)-1);

    ETypeColor(short color) {
        this.color = color;
    }

    public short getColor() {
        return color;
    }

    private short color;
}
