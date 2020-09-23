package CheckersEngine;

import CheckersEngine.BaseEngine.ETypeColor;
import CheckersEngine.BaseEngine.ETypeFigure;
import CheckersEngine.BaseEngine.CPair;
import CheckersEngine.BaseEngine.IFigureBase;

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

    protected int testMove(int x, int y, CCheckersBoard tmpBoard) {
        if (null == tmpBoard) tmpBoard = board;
        if (!tstCoord(x, y)) return -1;
        int maxp = colorFigure == ETypeColor.BLACK ? BOARD_BOTTOM: BOARD_TOP;
        if (null == tmpBoard.getBoard().getOrDefault(new CPair<>(x, y), null)) {
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
        int r = testMove(x, y, null);
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
        r = testMove(x, y, null);
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
        List<CPair<Integer, Integer>> tmpOneStep, oneStep = new LinkedList<>();
        boolean isTest;
        CPair<Integer, Integer> curPos, nextPos, endPos, tmpPos;
        List<CCheckersBoard> stackBoard = new LinkedList<>(); // Стек досок.
        List<CPair<Integer, Integer>> stackPos = new LinkedList<>(); // Стек текущих позиций.
        List<List<CPair<Integer, Integer>>> stackOneStep = new LinkedList<>(); // Стек возможных атак.
        CCheckersBoard tmpBoard, curBoard = new CCheckersBoard(board);
        stackBoard.add(0, curBoard);
        stackPos.add(0, pos);
        stackOneStep.add(0, oneStep);
        while (stackBoard.size() > 0) {
            curBoard = stackBoard.remove(0);
            curPos = stackPos.remove(0);
            oneStep = stackOneStep.remove(0);
            if (ETypeFigure.QUINE == curBoard.getFigure(curPos.getFirst(), curPos.getSecond()).getTypeFigure()) {
                List<List<CPair<Integer, Integer>>> tmpLstSteps;
                tmpLstSteps = curBoard.getFigure(curPos.getFirst(), curPos.getSecond()).searchAttack();
                if (tmpLstSteps.size() > 0) {
                    // Дальше фигура атакует как дамка.
                    for (List<CPair<Integer, Integer>> elOneStep: tmpLstSteps) {
                        tmpOneStep = new LinkedList<>();
                        tmpOneStep.add(elOneStep.remove(0));
                        elOneStep.remove(0);
                        tmpPos = new CPair<>(elOneStep.get(0).getFirst(), elOneStep.get(0).getSecond());
                        tmpOneStep.add(tmpPos);
                        tmpOneStep.addAll(oneStep);
                        tmpOneStep.addAll(elOneStep);
                        lstSteps.add(tmpOneStep);
                    }
                }
            } else {
                isTest = true;
                for (int[] delta: BASE_PATH_OPTION) {
                    nextPos = new CPair<>(curPos.getFirst() + delta[0], curPos.getSecond() + delta[1]);
                    if (board.aField(nextPos) && null != curBoard.getBoard().getOrDefault(nextPos, null) && board.getCurMove() != curBoard.getBoard().get(nextPos).getColorType()) {
                        endPos = new CPair<>(nextPos.getFirst() + delta[0], nextPos.getSecond() + delta[1]);
                        if (board.aField(endPos) && null == curBoard.getBoard().getOrDefault(endPos, null)) {
                            isTest = false;
                            tmpBoard = new CCheckersBoard(curBoard);
                            ETypeFigure tfg = 1 == testMove(endPos.getFirst(), endPos.getSecond(), tmpBoard) ? ETypeFigure.QUINE: ETypeFigure.CHECKERS;
                            tmpBoard.setFigure(curPos.getFirst(), curPos.getSecond(), null, null);
                            tmpBoard.setFigure(nextPos.getFirst(), nextPos.getSecond(), null, null);
                            tmpBoard.setFigure(endPos.getFirst(), endPos.getSecond(), tfg, colorFigure);
                            stackBoard.add(0, tmpBoard);
                            tmpOneStep = new LinkedList<>(oneStep);
                            tmpOneStep.add(nextPos);
                            tmpOneStep.add(endPos);
                            stackOneStep.add(0, tmpOneStep);
                            stackPos.add(0, endPos);
                        }
                    }
                }
                if (isTest) {
                    if (oneStep.size() > 0) {
                        tmpPos = oneStep.get(oneStep.size() - 1);
                        tmpOneStep = new LinkedList<>();
                        tmpOneStep.add(tmpPos);
                        tmpOneStep.add(null);
                        tmpOneStep.addAll(oneStep);
                        lstSteps.add(tmpOneStep);
                    }
                }
            }
        }
        return lstSteps;
    }

}
