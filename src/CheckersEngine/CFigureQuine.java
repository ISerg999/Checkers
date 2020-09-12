package CheckersEngine;

import CheckersEngine.BaseEngine.ETypeColor;
import CheckersEngine.BaseEngine.ETypeFigure;
import CheckersEngine.BaseEngine.CPair;

import java.util.List;

/**
 * Класс контролирующий возможные ходы для дамки.
 */
public class CFigureQuine extends CFigureParent {
    /**
     * Возможные направления движения.
     */
    protected int[][] BASE_PATH_OPTION = {{-1, -1}, {1, -1}, {-1, 1}, {1, 1}};

    public CFigureQuine(ETypeColor colorFig) {
        super(ETypeFigure.QUINE, colorFig);
    }

    public CFigureQuine(ETypeColor colorFig, int x, int y) {
        super(ETypeFigure.QUINE, colorFig, x, y);
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
