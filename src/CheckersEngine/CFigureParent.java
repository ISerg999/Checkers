package CheckersEngine;

import CheckersEngine.BaseEngine.ETypeColor;
import CheckersEngine.BaseEngine.ETypeFigure;
import CheckersEngine.BaseEngine.IFigureBase;
import CheckersEngine.BaseEngine.CPair;

import java.util.List;
import java.util.Map;

public class CFigureParent implements IFigureBase {
    /**
     * Возможные направления движения.
     */
    protected static final int[][] BASE_PATH_OPTION = {{-1, -1}, {1, -1}, {-1, 1}, {1, 1}};
    protected static final int BOARD_TOP = 7;
    protected static final int BOARD_BOTTOM = 0;

    protected ETypeColor colorFigure = null;
    protected ETypeFigure typeFigure = null;
    protected CPair<Integer, Integer> pos = null;
    protected CCheckersBoard board = null;
    protected List<List<CPair<Integer, Integer>>> lstSteps;

    protected boolean tstCoord(int x, int y) {
        if (x < 0 || x > 7 || y < 0 || y > 7) return false;
        return true;
    }

    public CFigureParent(ETypeFigure typeFig, ETypeColor colorFig) {
        setTypeFigure(typeFig);
        setColorType(colorFig);
    }

    public CFigureParent(ETypeFigure typeFig, ETypeColor colorFig, int x, int y) {
        setTypeFigure(typeFig);
        setColorType(colorFig);
        setPos(new CPair<>(x, y));
    }

    @Override
    public void setTypeFigure(ETypeFigure typeFig) {
        typeFigure = typeFig;
    }

    @Override
    public ETypeFigure getTypeFigure() {
        return typeFigure;
    }

    @Override
    public void setColorType(ETypeColor color) {
        colorFigure = color;
    }

    @Override
    public ETypeColor getColorType() {
        return colorFigure;
    }

    @Override
    public void setPos(CPair<Integer, Integer> pos) {
        this.pos = pos;
    }

    @Override
    public int getX() {
        return pos.getFirst();
    }

    @Override
    public int getY() {
        return pos.getSecond();
    }

    @Override
    public void setBoard(CCheckersBoard board) {
        this.board = board;
    }

    @Override
    public List<List<CPair<Integer, Integer>>> searchMove() {
        return null;
    }

    @Override
    public List<List<CPair<Integer, Integer>>> searchAttack() {
        return null;
    }

}
