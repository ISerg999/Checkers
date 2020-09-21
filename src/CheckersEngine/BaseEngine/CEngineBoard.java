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
    protected Map<CPair<Integer, Integer>, IFigureBase> board;
    /**
     * Пул объектов фигур.
     */
    protected CPoolFigures pool;
    /**
     * Определяет, идет сейчас игра или нет.
     */
    protected boolean stateGame;
    /**
     * Определяет, чей сечас ход.
     */
    protected ETypeColor curTypeColor;
    /**
     * Состояния окончания игры (-1 - игра не закончена, 0 - ничья, 1 - выйграли белые, 2 - выйграли чёрные
     */
    protected int stateGameStop;
    /**
     * Словарь возможных текущих ходов фигурами во время игры.
     */
    protected Map<CPair<Integer, Integer>, List<List<CPair<Integer, Integer>>>> curGameSteps;
    /**
     * Определяет, являются ли возможные текущии ходы фигуров атакой или ходом.
     */
    protected boolean isAttack;

    /**
     * Конструктор.
     * @param w ширина игрового поля
     * @param h высота игрового поля
     */
    public CEngineBoard(int w, int h) {
        this.width = w;
        this.height = h;
        pool = new CPoolFigures();
        board = new HashMap<>();
        curGameSteps = new HashMap<>();
        setStateGame(false);
        setCurMoveWhite();
        clearBoard();
    }

    /**
     * Возвращает количество полей доски в ширину.
     * @return количество полей доски в ширину
     */
    public int getWidth() {
        return width;
    }

    /**
     * Возвращает количество полей доски в длину.
     * @return количество полей доски в длину
     */
    public int getHeight() {
        return height;
    }

    /**
     * Возвращает текущее состояние игры.
     * @return true - игра запущена, false - игра остановлена.
     */
    public boolean getStateGame() {
        return stateGame;
    }

    /**
     * Задаёт состояние игры.
     * @param stateGame true - игра запущена, false - игра остановлена.
     */
    public synchronized void setStateGame(boolean stateGame) {
        boolean old = stateGame;
        this.stateGame = stateGame;
        if (!old && stateGame) {
            // Переход в режим игры.
            testNewStep();
        }
        else if (old && !stateGame) stopGamePlay();
    }

    /**
     * Меняет ход игрока на противоположенный.
     */
    public synchronized void nextStep() {
        if (!getStateGame()) return;
        if (curTypeColor == ETypeColor.WHITE) curTypeColor = ETypeColor.BLACK;
        else curTypeColor = ETypeColor.WHITE;
        testNewStep();
    }

    /**
     * Очистка всей доски.
     */
    public synchronized void clearBoard() {
        pool.clear();
        board.clear();
        setCurMoveWhite();
    }

    /**
     * Возвращает словарь доски.
     * @return словарь доски
     */
    public Map<CPair<Integer, Integer>, IFigureBase> getBoard() {
        return board;
    }

    /**
     * Устанавливет текущий ход для белых.
     */
    public void setCurMoveWhite() {
        if (stateGame) return;
        curTypeColor = ETypeColor.WHITE;
    }

    /**
     * Устанавливает текущий ход для чёрных.
     */
    public void setCurMoveBlack(){
        if (stateGame) return;
        curTypeColor = ETypeColor.BLACK;
    }

    /**
     * Определяет, чей сейчас ход.
     * @return константа игрового цвета
     */
    public ETypeColor getCurMove() {
        return curTypeColor;
    }

    /**
     * Проверяет на допустимость координат.
     * @param x координата x
     * @param y координата y
     * @return true - допустимые координаты, false - не допустимые координаты
     */
    public boolean aField(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >=height)
            return false;
        return true;
    }

    /**
     * Получение фигуры по её координатам.
     * @param x координата x
     * @param y координата y
     * @return фигура, или null
     */
    public IFigureBase getFigure(int x, int y) {
        return aField(x, y) ? board.get(new CPair<>(x, y)): null;
    }

    /**
     * Установка фигуру на доску.
     * @param x  координата x
     * @param y  координата y
     * @param tf тип устанавливаемой фигуры, или null, если фигуру нужно удалить.
     * @param cf цвет устанавливаемой фигуры
     */
    public synchronized void setFigure(int x, int y, ETypeFigure tf, ETypeColor cf) {
        if (aField(x, y)) {
            CPair<Integer, Integer> pos = new CPair<>(x, y);
            if (null != board.getOrDefault(pos, null)) {
                pool.remove(board.remove(pos));
            }
            if (null == tf) return;
            pool.get(tf, cf, pos, board);
        }
    }

    /**
     * Получение бинарного представления доски.
     * @return список байтов представление доски
     */
    public abstract List<Integer> getBinaryBoardFigure();

    /**
     * Установка представления доски из бинарного списка
     * @param binGame список байтов
     * @param k       индекс, с которого идёт получения данных
     * @return указатель на область, лежащую за пределами полученных данных
     */
    public abstract int setBinaryBoardFigure(List<Integer> binGame, int k);

    /**
     * Получение бинарного представления игры.
     * @return список байтов состояния игры
     */
    public abstract List<Integer> getBinaryBoardState();

    /**
     * Установка бпредставления состояния игры из бинарного списка.
     * @param binGame список байтов
     * @param k       индекс, с какого идёт получение данных
     * @return указатель на область, лежащую за пределами полученных данных
     */
    public abstract int setBinaryBoardState(List<Integer> binGame, int k);

    /**
     * Базовая разстановка фигур.
     */
    public abstract void placementBoard();

    /**
     * Состояние окончание игры. (0 - ничья, 1 - выйграли белые, 2 - выйграли чёрные).
     * @return состояние окончание игры
     */
    public int getStateGameStop() {
        return stateGameStop;
    }

    /**
     * Поиск возможных ходов, а также проверка на окончание игры.
     */
    protected synchronized void testNewStep() {
        analisysPossibleMoves();
        stateGameStop = analisysGameState();
        if (stateGameStop >= 0) {
            setStateGame(false);
        }
    }

    /**
     * Выход из состояния игры.
     */
    protected void stopGamePlay() {
        // TODO: Действия при окончании игры.
    }

    /**
     * Анализ доступных ходов.
     */
    protected synchronized void analisysPossibleMoves() {
        curGameSteps.clear();
        // Анализ возможных атак.
        isAttack = true;
        for (CPair<Integer, Integer> pos: board.keySet()) {
            IFigureBase fb = board.get(pos);
            if (fb.getColorType() == getCurMove()) {
                List<List<CPair<Integer, Integer>>> tmp = fb.searchAttack();
                if (tmp.size() > 0) {
                    curGameSteps.put(new CPair<>(fb.getX(), fb.getY()), tmp);
                }
            }
        }

        if (0 == curGameSteps.size()) {
            // Анализ возможных ходов.
            isAttack = false;
            for (CPair<Integer, Integer> pos: board.keySet()) {
                IFigureBase fb = board.get(pos);
                if (fb.getColorType() == getCurMove()) {
                    List<List<CPair<Integer, Integer>>> tmp = fb.searchMove();
                    if (tmp.size() > 0) {
                        curGameSteps.put(new CPair<>(fb.getX(), fb.getY()), tmp);
                    }
                }
            }
        }

    }

    /**
     * Количество найденных ходов.
     * @return количество найденных ходов
     */
    public int sizeGameSteps() {
        return curGameSteps.size();
    }

    /**
     * Анализ конечного игрового состояния.
     * @return 0 - ничья, 1 - выйграли белые, 2 - выйграли чёрные, меньше нуля - все остальные состояния.
     */
    public synchronized int analisysGameState() {
        if (!getStateGame()) return -1;
        int countW = 0, countB = 0;
        for (CPair<Integer, Integer> pos: board.keySet()) {
            IFigureBase fb = board.get(pos);
            if (fb.getColorType() == ETypeColor.BLACK) countB++;
            else countW++;
        }
        int count = getCurMove() == ETypeColor.BLACK ? countW: countB;
        if (count > 0 && curGameSteps.size() > 0) return -1;
        if (countB == 0) return 1;
        if (countW == 0) return 2;
        CCheckersBoard newState = new CCheckersBoard((CCheckersBoard) this);
        newState.nextStep();
        if (sizeGameSteps() == 0 && newState.sizeGameSteps() == 0) return 0;
        return -1;
    }

    /**
     * Проверяет на допустимость координат в режиме игры при выборе фигуры для текущего хода.
     * @param x координата x
     * @param y координата y
     * @return true - допустимые координаты, false - не допустимые координаты
     */
    public boolean startMoveGameField(int x, int y) {
        if (!getStateGame()) return false;
        if (aField(x, y)) {
            if (curGameSteps.containsKey(new CPair<>(x, y))) return true;
            else return false;
        }
        return false;
    }

    /**
     * Получает список конечных координат для хода заданной фигурой.
     * @param x координата x текущей фигуры
     * @param y координата y текущей фигуры
     * @return список конечных координат или null
     */
    public List<CPair<Integer, Integer>> lstGameEndMoves(int x, int y) {
        if (!getStateGame()) return null;
        if (aField(x, y)) {
            List<List<CPair<Integer, Integer>>> lstAll = curGameSteps.getOrDefault(new CPair<>(x, y), null);
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
     * @param x1 координата x фигуры
     * @param y1 координата y фигуры
     * @param x2 координата x конечного хода
     * @param y2 координата y конечного хода
     * @return список информации по заданному ходу
     */
    public List<CPair<Integer, Integer>> lstGameStepInfo(int x1, int y1, int x2, int y2) {
        List<CPair<Integer, Integer>> res = new LinkedList<>();
        CPair<Integer, Integer> pos = new CPair<>(x1, y1);
        CPair<Integer, Integer> pos2 = new CPair<>(x2, y2);
        res.add(pos);
        if (curGameSteps.containsKey(pos)) {
            List<List<CPair<Integer, Integer>>> lstAll = curGameSteps.get(pos);
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
