package CheckersEngine;

import CheckersEngine.BaseEngine.ETypeColor;
import CheckersEngine.BaseEngine.ETypeFigure;
import CheckersEngine.BaseEngine.Pair;

import java.util.List;

/**
 * Класс контролирующий возможные ходы для дамки.
 */
public class FigureQuine extends FigureParent {
    /**
     * Возможные направления движения.
     */
    protected short[][] BASE_PATH_OPTION = {{-1, -1}, {1, -1}, {-1, 1}, {1, 1}};

    public FigureQuine(ETypeColor colorFig) {
        super(ETypeFigure.QUINE, colorFig);
    }

    public FigureQuine(ETypeColor colorFig, short x, short y) {
        super(ETypeFigure.QUINE, colorFig, x, y);
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
