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

    private int direction;
}
