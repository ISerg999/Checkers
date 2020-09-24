package CheckersEngine.BaseEngine;

import CheckersEngine.CCheckersBoard;

import java.util.*;

/**
 * Базовый класс движка.
 */
public abstract class CEngineBoard {

    /**
     * Размеры игрового поля.
     */
    protected int width, height;
    /**
     * Словарь состояния игрового поля.
     */
    protected Map<CPair<Integer, Integer>, CPair<ETypeFigure, ETypeColor>> board;
    /**
     * Определяет, идет сейчас игра или нет.
     */
    protected boolean gameOn;
    /**
     * Определяет, чей сечас ход.
     */
    protected ETypeColor curMove;
    /**
     * Словарь возможных текущих ходов фигурами во время игры.
     */
    protected Map<CPair<Integer, Integer>, List<List<CPair<Integer, Integer>>>> possibleCurrentMoves;
    /**
     * Метод для обратной связи, указывает об окончании игры.
     */
    protected ICallableStopGame callableStopGame;
    /**
     * Состояние окончания игры. 0 - ничья, 1 - выйграли белые, 2 - выйграли чёрные, -1 - всё остальное.
     */
    protected int endState;
    protected CFactoryFigure factoryFigure;

    /**
     * Конструктор.
     * @param width ширина игрового поля
     * @param height высота игрового поля
     */
    public CEngineBoard(int width, int height) {
        this.width = width;
        this.height = height;
        board = new HashMap<>();
        gameOn = false;
        possibleCurrentMoves = new HashMap<>();
        curMove = ETypeColor.WHITE;
        callableStopGame = null;
        endState = -1;
        factoryFigure = CFactoryFigure.getInstance();
    }

    public CEngineBoard(CEngineBoard old) {
        board = new HashMap<>();
        clearBoard();
        for (CPair<Integer, Integer> oldPos: old.board.keySet()) {
            CPair<Integer, Integer> pos = new CPair<>(oldPos.getFirst(), oldPos.getSecond());
            CPair<ETypeFigure, ETypeColor> figure = new CPair<>(old.board.get(oldPos).getFirst(), old.board.get(oldPos).getSecond());
            board.put(pos, figure);
        }
        width = old.width;
        height = old.height;
        gameOn = old.gameOn;
        curMove = old.curMove;
        possibleCurrentMoves = new HashMap<>();
        callableStopGame = old.callableStopGame;
        endState = old.endState;
        factoryFigure = CFactoryFigure.getInstance();
    }

    /**
     * Возвращает текущее состояние игры.
     * @return true - игра запущена, false - игра остановлена
     */
    public boolean getGameOn() {
        return gameOn;
    }

    /**
     * Очистка всей доски.
     */
    public synchronized void clearBoard() {
        board.clear();
    }

    /**
     * Возвращает словарь доски.
     * @return словарь доски
     */
    public Map<CPair<Integer, Integer>, CPair<ETypeFigure, ETypeColor>> getBoard() {
        return board;
    }

    /**
     * Установить текущий ход белых.
     */
    public void setMoveWhile() {
        if (gameOn) return;
        curMove = ETypeColor.WHITE;
    }

    /**
     * Установить текущий ход чёрных.
     */
    public void setMoveBlack() {
        if (gameOn) return;
        curMove = ETypeColor.BLACK;
    }

    /**
     * Возвращает чей текущий ход.
     * @return текущий ход
     */
    public ETypeColor getCurrentMove() {
        return curMove;
    }

    /**
     * Меняет ход игрока на противоположенный.
     */
    public synchronized void reverseCurrentMove() {
        if (gameOn) {
            curMove = curMove.neg();
        } else {
            gameOn = true;
        }
        testNewStep();
    }

    /**
     * Получение содержимое клетки доски по заданным координатам.
     * @param x координата x
     * @param y координата y
     * @return содержимое клетки доски
     */
    public CPair<ETypeFigure, ETypeColor> getF(int x, int y) {
        return getF(new CPair<>(x, y));
    }

    /**
     * Получение содержимое клетки доски по заданным координатам.
     * @param pos положение на доске
     * @return содержимое клетки доски
     */
    public CPair<ETypeFigure, ETypeColor> getF(CPair<Integer, Integer> pos) {
        return board.getOrDefault(pos,null);
    }

    /**
     * Установка фигуры на игровой доске.
     * @param x      положение по x
     * @param y      положение по y
     * @param figure устанавливаемая фигура, или null, если удалить
     */
    public void setF(int x, int y, CPair<ETypeFigure, ETypeColor> figure) {
        setF(new CPair<>(x, y), figure);
    }

    /**
     * Установка фигуры на игровой доске.
     * @param pos    положение на игровой доске
     * @param figure устанавливаемая фигура, или null, если удалить
     */
    public synchronized void setF(CPair<Integer, Integer> pos, CPair<ETypeFigure, ETypeColor> figure) {
        if (null != board.getOrDefault(pos, null)) board.remove(pos);
        if (null != figure) {
            board.put(pos, figure);
        }
    }

    /**
     * Установка метода обратной связи.
     * @param callableStopGame метод обратной связи
     */
    public void setCallableStopGame(ICallableStopGame callableStopGame) {
        this.callableStopGame = callableStopGame;
    }

    /**
     * Получение бинарного представления доски.
     * @return список байтов представление доски
     */
    public abstract List<Integer> getBinaryRepresentationBoard();

    /**
     * Установка представления доски из бинарного списка
     * @param binGame список байтов
     * @param k       индекс, с которого идёт получения данных
     * @return указатель на область, лежащую за пределами полученных данных
     */
    public abstract int setBinaryRepresentationBoard(List<Integer> binGame, int k);

    /**
     * Получение бинарного представления параметров игры.
     * @return бинарное представление параметров игры
     */
    public abstract List<Integer> getBinaryParametrsGame();

    /**
     * Установка бинарного представления игры.
     * @param binGame список байтов
     * @param k       индекс, с какого идёт получение данных
     * @return указатель на область, лежащую за пределами полученных данных
     */
    public abstract int setBinaryParametrsGame(List<Integer> binGame, int k);

    /**
     * Базовая разстановка фигур.
     */
    public abstract void placementBoard();

    /**
     * Состояние окончание игры. (0 - ничья, 1 - выйграли белые, 2 - выйграли чёрные, -1 - всё остальное).
     * @return состояние окончание игры
     */
    public int getEndState() {
        return endState;
    }

    /**
     * Выход из состояния игры.
     */
    public synchronized void stopGamePlay() {
        gameOn = false;
        // TODO: Действия при окончании игры.
    }

    /**
     * Поиск возможных ходов, а также проверка на окончание игры.
     */
    protected synchronized void testNewStep() {
        analisysPossibleMoves();
        endState = analisysEndState();
        if (endState >= 0) {
            gameOn = false;
            if (null != callableStopGame) {
                ETypeColor tc = null;
                if (endState > 0) tc = endState == 1 ? ETypeColor.WHITE: ETypeColor.BLACK;
                callableStopGame.endStateGame(tc);
            }
        }
    }

    /**
     * Анализ доступных ходов.
     */
    public synchronized void analisysPossibleMoves() {
        possibleCurrentMoves.clear();
        // Анализ возможных атак.
        for (CPair<Integer, Integer> pos: board.keySet()) {
            CPair<ETypeFigure, ETypeColor> figure = board.get(pos);
            if (figure.getSecond() == curMove) {
                List<List<CPair<Integer, Integer>>> tmp = factoryFigure.getFigure(figure.getFirst()).searchAttack(board, pos);
                if (tmp.size() > 0) {
                    possibleCurrentMoves.put(pos, tmp);
                }
            }
        }

        if (0 == possibleCurrentMoves.size()) {
            // Анализ возможных ходов.
            for (CPair<Integer, Integer> pos: board.keySet()) {
                CPair<ETypeFigure, ETypeColor> figure = board.get(pos);
                if (figure.getSecond() == curMove) {
                    List<List<CPair<Integer, Integer>>> tmp = factoryFigure.getFigure(figure.getFirst()).searchMove(board, pos);
                    if (tmp.size() > 0) {
                        possibleCurrentMoves.put(pos, tmp);
                    }
                }
            }
        }

    }

    /**
     * Количество найденных ходов.
     * @return количество найденных ходов
     */
    public int sizePossibleCurrentMoves() {
        return possibleCurrentMoves.size();
    }

    /**
     * Анализ конечного игрового состояния.
     * @return 0 - ничья, 1 - выйграли белые, 2 - выйграли чёрные, меньше нуля - все остальные состояния.
     */
    public synchronized int analisysEndState() {
        if (!gameOn) return -1;
        int countW = 0, countB = 0;
        for (CPair<Integer, Integer> pos: board.keySet()) {
            CPair<ETypeFigure, ETypeColor> figure = board.get(pos);
            if (ETypeColor.BLACK == figure.getSecond()) countB++;
            else countW++;
        }
        if (countB == 0) return 1;
        if (countW == 0) return 2;
        int count = ETypeColor.BLACK == curMove ? countW: countB;
        if (count > 0 && possibleCurrentMoves.size() > 0) return -1;
        CEngineBoard nextBoard = new CCheckersBoard((CCheckersBoard) this);
        nextBoard.setCallableStopGame(null);
        nextBoard.reverseCurrentMove();
        if (0 == possibleCurrentMoves.size() && 0 == nextBoard.sizePossibleCurrentMoves()) return 0;
        return -1;
    }

    /**
     * Возвращает массив доступных стартовых ходов.
     * @return массив стартовых ходов, или null
     */
    public List<CPair<Integer, Integer>> lstStartMoveGameField() {
        if (!gameOn) return null;
        if (0 == possibleCurrentMoves.size()) return null;
        CPair<Integer, Integer>[] arraySteps = new CPair[possibleCurrentMoves.size()];
        possibleCurrentMoves.keySet().toArray(arraySteps);
        return Arrays.asList(arraySteps);
    }

    /**
     * Возвращает массив промежуточных координат для хода заданной фигурой.
     * @param x координата x текущей фигуры
     * @param y координата y текущей фигуры
     * @return массив конечных координат или null
     */
    public List<CPair<Integer, Integer>> lstIntermediateMoveGameField(int x, int y) {
        if (!gameOn) return null;
        if (factoryFigure.aPosition(x, y)) {
            List<List<CPair<Integer, Integer>>> lstAll = possibleCurrentMoves.getOrDefault(new CPair<>(x, y), null);
            if (null == lstAll || lstAll.size() == 0) return null;
            List<CPair<Integer, Integer>> res = new LinkedList<>();
            for (List<CPair<Integer, Integer>> m: lstAll) {
                if (m.size() > 2) {
                    for (int k = 3; k < m.size(); k += 2) {
                        res.add(m.get(k));
                    }
                }
            }
            return res;
        }
        return null;
    }

    /**
     * Возвращает массив конечных координат для хода заданной фигурой.
     * @param x координата x текущей фигуры
     * @param y координата y текущей фигуры
     * @return массив конечных координат или null
     */
    public List<CPair<Integer, Integer>> lstEndMoveGameField(int x, int y) {
        if (!gameOn) return null;
        if (factoryFigure.aPosition(x, y)) {
            List<List<CPair<Integer, Integer>>> lstAll = possibleCurrentMoves.getOrDefault(new CPair<>(x, y), null);
            if (null == lstAll || lstAll.size() == 0) return null;
            List<CPair<Integer, Integer>> res = new LinkedList<>();
            for (List<CPair<Integer, Integer>> m: lstAll) {
                res.add(m.get(0));
            }
            return res;
        }
        return null;
    }

    /**
     * Возвращает список информации по текущему сделанному ходу.
     * @param pos1 позиция начальная
     * @param pos2 позиция конечная
     * @return список информации по заданному ходу
     */
    public List<CPair<Integer, Integer>> lstGameStepInfo(CPair<Integer, Integer> pos1, CPair<Integer, Integer> pos2) {
        List<CPair<Integer, Integer>> res = new LinkedList<>();
        res.add(pos1);
        if (possibleCurrentMoves.containsKey(pos1)) {
            List<List<CPair<Integer, Integer>>> lstAll = possibleCurrentMoves.get(pos1);
            for (List<CPair<Integer, Integer>> m: lstAll) {
                if (m.get(0).equals(pos2)) {
                    res.addAll(m);
                    return res;
                }
            }
        }
        return null;
    }
}
