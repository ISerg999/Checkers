package CheckersEngine.BaseEngine;

public enum ETypeColor {
    WHITE(1),
    BLACK(2);

    ETypeColor(int direction) {
        this.direction = direction;
    }

    public int getDirection() {
        return direction;
    }

    public ETypeColor neg() {
        return this == ETypeColor.WHITE ? ETypeColor.BLACK: ETypeColor.WHITE;
    }

    private int direction;
}
