package CheckersEngine;

import CheckersEngine.BaseEngine.ETypeColor;
import CheckersEngine.BaseEngine.ETypeFigure;
import CheckersEngine.BaseEngine.CPair;

import java.util.LinkedList;
import java.util.List;

/**
 * Класс контролирующий возможные ходы для дамки.
 */
public class CFigureQuine extends CFigureParent {
    public CFigureQuine(ETypeColor colorFig) {
        super(ETypeFigure.QUINE, colorFig);
    }
    public CFigureQuine(ETypeColor colorFig, int x, int y) {
        super(ETypeFigure.QUINE, colorFig, x, y);
    }

    @Override
    public List<List<CPair<Integer, Integer>>> searchMove() {
        lstSteps = new LinkedList<>();
        List<CPair<Integer, Integer>> oneStep;
        for (int is = 0; is < BASE_PATH_OPTION.length; is++) {
            int x = pos.getFirst();
            int y = pos.getSecond();
            int new_x = x + BASE_PATH_OPTION[is][0];
            int new_y = y + BASE_PATH_OPTION[is][1];
            int s = 0;
            while (null == board.getOrDefault(new CPair<>(new_x, new_y), null)) {
                x = new_x;
                y = new_y;
                s++;
                new_x = x + BASE_PATH_OPTION[is][0];
                new_y = y + BASE_PATH_OPTION[is][1];
            }
            if (s > 0) {
                oneStep = new LinkedList<>();
                oneStep.add(new CPair<>(x, y));
                oneStep.add(null);
                lstSteps.add(oneStep);
            }
        }
        return lstSteps;
    }

    @Override
    public List<List<CPair<Integer, Integer>>> searchAttack() {
        lstSteps = new LinkedList<>();
        return lstSteps;
    }

}
