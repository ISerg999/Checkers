package CheckersEngine;

import CheckersEngine.BaseEngine.ETypeColor;
import CheckersEngine.BaseEngine.ETypeFigure;
import CheckersEngine.BaseEngine.CPair;

import java.util.List;

/**
 * Класс контролирующий возможные ходы для шашки.
 */
public class CFigureCheckers extends CFigureParent {

    public CFigureCheckers(ETypeColor colorFig) {
        super(ETypeFigure.CHECKERS, colorFig);
    }

    public CFigureCheckers(ETypeColor colorFig, int x, int y) {
        super(ETypeFigure.CHECKERS, colorFig, x, y);
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
