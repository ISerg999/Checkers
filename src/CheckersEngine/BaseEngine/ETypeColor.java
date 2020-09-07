package CheckersEngine.BaseEngine;

public enum ETypeColor {
    WHITE(1),
    BLACK(-1);

    ETypeColor(int direction) {
        this.direction = direction;
    }

    public int getDirection() {
        return direction;
    }

    private int direction;
}
