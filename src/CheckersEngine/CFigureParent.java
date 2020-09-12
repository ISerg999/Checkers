package CheckersEngine;

import CheckersEngine.BaseEngine.ETypeColor;
import CheckersEngine.BaseEngine.ETypeFigure;
import CheckersEngine.BaseEngine.IFigureBase;
import CheckersEngine.BaseEngine.CPair;

import java.util.List;
import java.util.Map;

public class CFigureParent implements IFigureBase {

    protected ETypeColor colorFigure = null;
    protected ETypeFigure typeFigure = null;
    protected CPair<Integer, Integer> pos = null;
    protected Map<CPair<Integer, Integer>, IFigureBase> board = null;

    public CFigureParent(ETypeFigure typeFig, ETypeColor colorFig) {
        setTypeFigure(typeFig);
        setTypeColor(colorFig);
    }

    public CFigureParent(ETypeFigure typeFig, ETypeColor colorFig, int x, int y) {
        setTypeFigure(typeFig);
        setTypeColor(colorFig);
        setPos(x, y);
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
    public void setTypeColor(ETypeColor color) {
        colorFigure = color;
    }

    @Override
    public ETypeColor getColorType() {
        return colorFigure;
    }

    @Override
    public void setPos(int x, int y) {
        pos = new CPair<>(x, y);
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
    public void setBoard(Map<CPair<Integer, Integer>, IFigureBase> board) {
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
