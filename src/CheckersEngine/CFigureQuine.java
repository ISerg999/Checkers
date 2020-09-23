package CheckersEngine;

import CheckersEngine.BaseEngine.ETypeColor;
import CheckersEngine.BaseEngine.ETypeFigure;
import CheckersEngine.BaseEngine.CPair;
import CheckersEngine.BaseEngine.IFigureBase;

import javax.swing.*;
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
            while (null == board.getBoard().getOrDefault(new CPair<>(new_x, new_y), null) && tstCoord(new_x, new_y)) {
                x = new_x;
                y = new_y;
                new_x = x + BASE_PATH_OPTION[is][0];
                new_y = y + BASE_PATH_OPTION[is][1];
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
            isTest = true;
            for (int[] delta: BASE_PATH_OPTION) {
                nextPos = new CPair<>(curPos.getFirst() + delta[0], curPos.getSecond() + delta[1]);
                while (board.aField(nextPos) && null == curBoard.getBoard().getOrDefault(nextPos, null))
                {
                    int tx = nextPos.getFirst() + delta[0];
                    int ty = nextPos.getSecond() + delta[1];
                    nextPos.setFirst(tx);
                    nextPos.setSecond(ty);
                }
                if (board.aField(nextPos) && null != curBoard.getBoard().getOrDefault(nextPos, null) && board.getCurMove() != curBoard.getBoard().get(nextPos).getColorType()) {
                    endPos = new CPair<>(nextPos.getFirst() + delta[0], nextPos.getSecond() + delta[1]);
                    if (board.aField(endPos) && null == curBoard.getBoard().getOrDefault(endPos, null)) {
                        isTest = false;
                        tmpBoard = new CCheckersBoard(curBoard);
                        tmpBoard.setFigure(curPos.getFirst(), curPos.getSecond(), null, null);
                        tmpBoard.setFigure(nextPos.getFirst(), nextPos.getSecond(), null, null);
                        tmpBoard.setFigure(endPos.getFirst(), endPos.getSecond(), typeFigure, colorFigure);
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
        return lstSteps;
    }

}
