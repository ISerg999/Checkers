package CheckersEngine;

import CheckersEngine.BaseEngine.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Класс контролирующий возможные ходы для шашки.
 */
public class CFigureCheckers extends CFigureParent {

    /**
     * Линии превращения шашаек в дамки.
     */
    protected static final int BOARD_TOP = 7;
    protected static final int BOARD_BOTTOM = 0;

    /**
     * Провекра положения для шашки.
     * @param pos   позиция шашки
     * @param board игровая доска
     * @param tc    текущий цвет фигуры
     * @return результат проверки, если отрицательное, то поле занято, если 0, то позиция свободна, шашка остаётся шашкой, если 1, то позиция свободна, шашка становится дамкой.
     */
    protected int testMove(CPair<Integer, Integer> pos, Map<CPair<Integer, Integer>, CPair<ETypeFigure, ETypeColor>> board, ETypeColor tc) {
        if (!CFactoryFigure.getInstance().aPosition(pos)) return -1;
        int maxp = tc == ETypeColor.BLACK ? BOARD_BOTTOM: BOARD_TOP;
        if (null == board.getOrDefault(pos, null)) {
            return maxp == pos.getSecond() ? 1: 0;
        }
        return -1;
    }

    @Override
    public List<List<CPair<Integer, Integer>>> searchMove(Map<CPair<Integer, Integer>, CPair<ETypeFigure, ETypeColor>> board, CPair<Integer, Integer> pos) {
        ETypeColor tc = board.get(pos).getSecond();
        List<List<CPair<Integer, Integer>>> lstSteps = new LinkedList<>();
        CPair<Integer, Integer> newPos;
        List<CPair<Integer, Integer>> oneStep;
        int x, y, r;
        int is = tc == ETypeColor.BLACK ? 0: 2;
        for (int tis = 0; tis < 2; tis++) {
            x = pos.getFirst() + BASE_PATH_OPTION[is + tis][0];
            y = pos.getSecond() + BASE_PATH_OPTION[is + tis][1];
            r = testMove(new CPair<>(x, y), board, tc);
            if (r >= 0) {
                newPos = new CPair<>(x, y);
                oneStep = new LinkedList<>();
                oneStep.add(newPos);
                oneStep.add(r > 0 ? newPos: null);
                lstSteps.add(oneStep);
            }
        }
        return lstSteps;
    }

    @Override
    public List<List<CPair<Integer, Integer>>> searchAttack(Map<CPair<Integer, Integer>, CPair<ETypeFigure, ETypeColor>> board, CPair<Integer, Integer> pos) {
        ETypeColor tc = board.get(pos).getSecond();
        CStackBoardMove stack = new CStackBoardMove();
        List<List<CPair<Integer, Integer>>> tmpLstSteps, lstSteps = new LinkedList<>();
        Map<CPair<Integer, Integer>, CPair<ETypeFigure, ETypeColor>> curBoard, tmpBoard;
        ETypeFigure tf;
        CPair<Integer, Integer> curPos, nextPos, endPos;
        List<CPair<Integer, Integer>> oneStep, tmpOneStep;
        boolean isTest;
        stack.push(board, pos, new LinkedList<>());
        while (stack.size() > 0) {
            curBoard = stack.getBoard();
            curPos = stack.getPos();
            oneStep = stack.getOneStep();
            stack.pop();
            isTest = true;
            tmpLstSteps = null;
            if (ETypeFigure.QUINE == curBoard.get(curPos).getFirst()) {
                // Атака фигуры как дамки.
                tmpLstSteps = CFactoryFigure.getInstance().getFigure(ETypeFigure.QUINE).searchAttack(curBoard, curPos);
            } else {
                for (int[] delta : BASE_PATH_OPTION) {
                    nextPos = new CPair<>(curPos.getFirst() + delta[0], curPos.getSecond() + delta[1]);
                    if (CFactoryFigure.getInstance().aPosition(nextPos) && null != curBoard.getOrDefault(nextPos, null) && tc != curBoard.get(nextPos).getSecond()) {
                        endPos = new CPair<>(nextPos.getFirst() + delta[0], nextPos.getSecond() + delta[1]);
                        if (CFactoryFigure.getInstance().aPosition(endPos) && null == curBoard.getOrDefault(endPos, null)) {
                            isTest = false;
                            tmpBoard = new HashMap<>(curBoard);
                            tf = 1 == testMove(endPos, tmpBoard, tc) ? ETypeFigure.QUINE : ETypeFigure.CHECKERS;
                            tmpBoard.remove(curPos);
                            tmpBoard.remove(nextPos);
                            tmpBoard.put(endPos, new CPair<>(tf, tc));
                            tmpOneStep = new LinkedList<>(oneStep);
                            tmpOneStep.add(nextPos);
                            tmpOneStep.add(endPos);
                            stack.push(tmpBoard, endPos, tmpOneStep);
                        }
                    }
                }
            }
            if (isTest) {
                if (oneStep.size() == 0) continue;
                if (null == tmpLstSteps || 0 == tmpLstSteps.size()) {
                    tmpOneStep = new LinkedList<>();
                    tmpOneStep.add(oneStep.get(oneStep.size() - 1));
                    tmpOneStep.add(null == tmpLstSteps ? null: tmpOneStep.get(0));
                    tmpOneStep.addAll(oneStep);
                    lstSteps.add(tmpOneStep);
                } else {
                    for (List<CPair<Integer, Integer>> elOneStep: tmpLstSteps) {
                        if (null == elOneStep || 0 == elOneStep.size()) continue;
                        tmpOneStep = new LinkedList<>();
                        tmpOneStep.add(elOneStep.get(elOneStep.size() - 1));
                        tmpOneStep.add(oneStep.get(oneStep.size() - 1));
                        tmpOneStep.addAll(oneStep);
                        tmpOneStep.addAll(elOneStep.subList(2, elOneStep.size()));
                        lstSteps.add(tmpOneStep);
                    }
                }
            }
        }
        return lstSteps;
    }


}
