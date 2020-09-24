package CheckersEngine;

import CheckersEngine.BaseEngine.*;

import java.util.List;
import java.util.Map;

public class CFigureParent implements IFigureBase {

    /**
     * Варианты движения фигуры.
     */
    protected static final int[][] BASE_PATH_OPTION = {{-1, -1}, {1, -1}, {-1, 1}, {1, 1}};

    @Override
    public List<List<CPair<Integer, Integer>>> searchMove(Map<CPair<Integer, Integer>, CPair<ETypeFigure, ETypeColor>> board, CPair<Integer, Integer> pos) {
        return null;
    }

    @Override
    public List<List<CPair<Integer, Integer>>> searchAttack(Map<CPair<Integer, Integer>, CPair<ETypeFigure, ETypeColor>> board, CPair<Integer, Integer> pos) {
        return null;
    }

}
