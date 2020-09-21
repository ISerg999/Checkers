package CheckersEngine;

import CheckersEngine.BaseEngine.ETypeColor;
import CheckersEngine.BaseEngine.ETypeFigure;
import CheckersEngine.BaseEngine.CPair;

import java.util.LinkedList;
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

    public int testMove(int x, int y) {
        int maxp = colorFigure == ETypeColor.BLACK ? BOARD_BOTTOM: BOARD_TOP;
        if (null != board.getOrDefault(new CPair<>(x, y), null)) {
            return maxp == y ? 1: 0;
        }
        return -1;
    }

    @Override
    public List<List<CPair<Integer, Integer>>> searchMove() {
        CPair<Integer, Integer> newPos;
        lstSteps = new LinkedList<>();
        List<CPair<Integer, Integer>> oneStep;
        int is = colorFigure == ETypeColor.BLACK ? 0: 2;
        int x = pos.getFirst() + BASE_PATH_OPTION[is][0];
        int y = pos.getSecond() + BASE_PATH_OPTION[is][1];
        int r = testMove(x, y);
        if (r >= 0) {
            newPos = new CPair<>(x, y);
            oneStep = new LinkedList<>();
            oneStep.add(newPos);
            oneStep.add(r > 0 ? newPos: null);
            lstSteps.add(oneStep);
        }
        is++;
        x = pos.getFirst() + BASE_PATH_OPTION[is][0];
        y = pos.getSecond() + BASE_PATH_OPTION[is][1];
        r = testMove(x, y);
        if (r >= 0) {
            newPos = new CPair<>(x, y);
            oneStep = new LinkedList<>();
            oneStep.add(newPos);
            oneStep.add(r > 0 ? newPos: null);
            lstSteps.add(oneStep);
        }
        return lstSteps;
    }

    @Override
    public List<List<CPair<Integer, Integer>>> searchAttack() {
        lstSteps = new LinkedList<>();
        return lstSteps;
    }

}
