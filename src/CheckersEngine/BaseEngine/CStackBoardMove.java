package CheckersEngine.BaseEngine;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Стек для проверки возможных ходов.
 */
public class CStackBoardMove {

    protected List<Map<CPair<Integer, Integer>, CPair<ETypeFigure, ETypeColor>>> stackBoard;
    protected List<CPair<Integer, Integer>> stackPos;
    protected List<List<CPair<Integer, Integer>>> stackOneStep;

    public CStackBoardMove() {
        stackBoard = new LinkedList<>();
        stackPos = new LinkedList<>();
        stackOneStep = new LinkedList<>();
    }

    public int size() {
        return stackPos.size();
    }

    public void push(Map<CPair<Integer, Integer>, CPair<ETypeFigure, ETypeColor>> board, CPair<Integer, Integer> pos, List<CPair<Integer, Integer>> oneStep) {
        stackBoard.add(0, board);
        stackPos.add(0, pos);
        stackOneStep.add(0, oneStep);
    }

    public Map<CPair<Integer, Integer>, CPair<ETypeFigure, ETypeColor>> getBoard() {
        if (stackBoard.size() == 0) return null;
        return stackBoard.get(0);
    }

    public CPair<Integer, Integer> getPos() {
        if (stackPos.size() == 0) return null;
        return stackPos.get(0);
    }

    public List<CPair<Integer, Integer>> getOneStep() {
        if (stackOneStep.size() == 0) return null;
        return stackOneStep.get(0);
    }

    public void pop() {
        stackBoard.remove(0);
        stackPos.remove(0);
        stackOneStep.remove(0);
    }
}
