package CheckersEngine.BaseEngine;

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
    protected IFigureBase[][] board;
    /**
     * Определяет, идет сейчас игра или нет.
     */
    protected boolean stateGame;
    /**
     * Определяет, чей сечас ход.
     */
    protected ETypeColor curTypeColor;
    /**
     * Определяет, кто ходи за соответствующий цвет. true - игрок, false - компьютер
     */
    protected Map<ETypeColor, Boolean> whosPlaying;
    /**
     * Пул объектов фигур.
     */
    protected CPoolFigures pool;

    /**
     * Конструктор.
     * @param w ширина игрового поля
     * @param h высота игрового поля
     */
    public CEngineBoard(int w, int h) {
        pool = new CPoolFigures();
        this.width = w;
        this.height = h;
        setStateGame(false);
        setCurMoveWhite();
        board = new IFigureBase[h][w];
        clearBoard();
        whosPlaying = new HashMap<>();
        for (ETypeColor it: ETypeColor.values()) {
            whosPlaying.put(it, true);
        }
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
    public void setStateGame(boolean stateGame) {
        this.stateGame = stateGame;
    }

    /**
     * Базовая инициализация играющих игроков.
     */
    public void initPlayerForColor() {
        for (ETypeColor it: ETypeColor.values()) {
            whosPlaying.replace(it, true);
        }
    }

    /**
     * Определяет, кто играет за заданный цвет
     * @param playColor игровой цвет
     * @return игрок играющий за данный цвет true - игрок, false - компьютер
     */
    public boolean getPlayForColor(ETypeColor playColor) {
        return whosPlaying.get(playColor);
    }

    /**
     * Задаёт игрока за выбранный цвет.
     * @param playerForColor цвет за котороый выставляется игрок
     * @param type           игрок: true - обычный игрок, false - компьютер
     */
    public void setPlayerForColor(ETypeColor playerForColor, boolean type) {
        whosPlaying.replace(playerForColor, type);
    }

    /**
     * Устанавливет текущий ход для белых.
     */
    public void setCurMoveWhite() { curTypeColor = ETypeColor.WHITE; }

    /**
     * Устанавливает текущий ход для чёрных.
     */
    public void setCurMoveBlack() {
        curTypeColor = ETypeColor.BLACK;
    }

    /**
     * Меняет ход игрока на противоположенный.
     */
    public void setCurMoveNext() {
        if (curTypeColor == ETypeColor.WHITE) setCurMoveBlack();
        else setCurMoveWhite();
    }

    /**
     * Определяет, чей сейчас ход.
     * @return константа игрового цвета
     */
    public ETypeColor getCurMove() {
        return curTypeColor;
    }

    /**
     * Подготовка к началу игры.
     */
    public void start() {
        if (stateGame) return;
        // TODO: Настройка для подготовки к началу игры, после stateGame = true
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
     * Очистка всей доски.
     */
    public void clearBoard() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (null != board[y][x]) {
                    pool.remove(board[y][x]);
                    board[y][x] = null;
                }
            }
        }
    }

    /**
     * Получение фигуры по её координатам.
     * @param x координата x
     * @param y координата y
     * @return фигура, или null
     */
    public IFigureBase getFigure(int x, int y) {
        return aField(x, y) ? board[y][x]: null;
    }

    /**
     * Установка фигуру на доску.
     * @param x  координата x
     * @param y  координата y
     * @param tf тип устанавливаемой фигуры, или null, если фигуру нужно удалить.
     * @param cf цвет устанавливаемой фигуры
     */
    public void setFigure(int x, int y, ETypeFigure tf, ETypeColor cf) {
        if (aField(x, y)) {
            if (null != board[y][x]) {
                pool.remove(board[y][x]);
                board[y][x] = null;
            }
            if (null == tf) return;
            board[y][x] = pool.get(tf, cf);
            board[y][x].setPos(x, y);
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
}
