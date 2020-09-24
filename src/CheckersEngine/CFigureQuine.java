package CheckersEngine;

import CheckersEngine.BaseEngine.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Класс контролирующий возможные ходы для дамки.
 */
public class CFigureQuine extends CFigureParent {

    @Override
    public List<List<CPair<Integer, Integer>>> searchMove(Map<CPair<Integer, Integer>, CPair<ETypeFigure, ETypeColor>> board, CPair<Integer, Integer> pos) {
        List<List<CPair<Integer, Integer>>> lstSteps = new LinkedList<>();
        List<CPair<Integer, Integer>> oneStep;
        int x, y, new_x, new_y;
        for (int is = 0; is < BASE_PATH_OPTION.length; is++) {
            x = pos.getFirst();
            y = pos.getSecond();
            new_x = x + BASE_PATH_OPTION[is][0];
            new_y = y + BASE_PATH_OPTION[is][1];
            while (CFactoryFigure.getInstance().aPosition(new_x, new_y) && null == board.getOrDefault(new CPair<>(new_x, new_y), null)) {
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
    public List<List<CPair<Integer, Integer>>> searchAttack(Map<CPair<Integer, Integer>, CPair<ETypeFigure, ETypeColor>> board, CPair<Integer, Integer> pos) {
        ETypeColor tc = board.get(pos).getSecond();
        CStackBoardMove stack = new CStackBoardMove();
        List<List<CPair<Integer, Integer>>> lstSteps = new LinkedList<>();
        Map<CPair<Integer, Integer>, CPair<ETypeFigure, ETypeColor>> curBoard, tmpBoard;
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
            for (int[] delta: BASE_PATH_OPTION) {
                nextPos = new CPair<>(curPos.getFirst() + delta[0], curPos.getSecond() + delta[1]);
                while (CFactoryFigure.getInstance().aPosition(nextPos) && null == curBoard.getOrDefault(nextPos, null)) {
                    nextPos.setFirst(nextPos.getFirst() + delta[0]);
                    nextPos.setSecond(nextPos.getSecond() + delta[1]);
                }
                if (CFactoryFigure.getInstance().aPosition(nextPos) && null != curBoard.getOrDefault(nextPos, null) && tc != curBoard.get(nextPos).getSecond()) {
                    endPos = new CPair<>(nextPos.getFirst() + delta[0], nextPos.getSecond() + delta[1]);
                    if (CFactoryFigure.getInstance().aPosition(endPos) && null == curBoard.getOrDefault(endPos, null)) {
                        isTest = false;
                        tmpBoard = new HashMap<>(curBoard);
                        tmpBoard.remove(curPos);
                        tmpBoard.remove(nextPos);
                        tmpBoard.put(endPos, new CPair<>(ETypeFigure.QUINE, tc));
                        tmpOneStep = new LinkedList<>(oneStep);
                        tmpOneStep.add(nextPos);
                        tmpOneStep.add(endPos);
                        stack.push(tmpBoard, endPos, tmpOneStep);
                    }
                }
            }
            if (isTest) {
                if (oneStep.size() > 0) {
                    tmpOneStep = new LinkedList<>();
                    tmpOneStep.add(oneStep.get(oneStep.size() - 1));
                    tmpOneStep.add(null);
                    tmpOneStep.addAll(oneStep);
                    lstSteps.add(tmpOneStep);
                }
            }
        }
        return lstSteps;
    }

}
