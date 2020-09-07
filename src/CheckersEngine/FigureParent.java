package CheckersEngine;

import CheckersEngine.BaseEngine.ETypeColor;
import CheckersEngine.BaseEngine.ETypeFigure;
import CheckersEngine.BaseEngine.IFigureBase;
import CheckersEngine.BaseEngine.Pair;

import java.util.List;
import java.util.Map;

public class FigureParent implements IFigureBase {

    public FigureParent(ETypeFigure typeFig, ETypeColor colorFig) {
        setTypeFigure(typeFig);
        setTypeColor(colorFig);
    }

    public FigureParent(ETypeFigure typeFig, ETypeColor colorFig, short x, short y) {
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
    public void setPos(short x, short y) {
        pos = new Pair<>(x, y);
    }

    @Override
    public void setBoard(Map<Pair<Short, Short>, IFigureBase> board) {
        this.board = board;
    }

    @Override
    public List<List<Pair<Short, Short>>> searchMove() {
        return null;
    }

    @Override
    public List<List<Pair<Short, Short>>> searchAttack() {
        return null;
    }

    protected ETypeColor colorFigure = null;
    protected ETypeFigure typeFigure = null;
    protected Pair<Short, Short> pos = null;
    protected Map<Pair<Short, Short>, IFigureBase> board = null;

}
