package CheckersEngine;

import CheckersEngine.BaseEngine.ETypeColor;
import CheckersEngine.BaseEngine.ETypeFigure;
import CheckersEngine.BaseEngine.Pair;

import java.util.List;

/**
 * Класс контролирующий возможные ходы для шашки.
 */
public class FigureCheckers extends FigureParent {

    public FigureCheckers(ETypeColor colorFig) {
        super(ETypeFigure.CHECKERS, colorFig);
    }

    public FigureCheckers(ETypeColor colorFig, short x, short y) {
        super(ETypeFigure.CHECKERS, colorFig, x, y);
    }

    @Override
    public List<List<Pair<Short, Short>>> searchMove() {
        return null;
    }

    @Override
    public List<List<Pair<Short, Short>>> searchAttack() {
        return null;
    }

}
