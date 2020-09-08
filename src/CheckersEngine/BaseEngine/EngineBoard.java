package CheckersEngine.BaseEngine;

import java.util.Hashtable;
import java.util.Map;

/**
 * Базовый класс движка.
 */
public class EngineBoard {

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
    Map<ETypeColor, Boolean> whosPlaying;

    /**
     * Конструктор.
     * @param w ширина игрового поля
     * @param h высота игрового поля
     */
    public EngineBoard(int w, int h) {
        this.width = w;
        this.height = h;
        stateGame = false;
        setCurMoveWhite();
        board = new Hashtable<>();
        whosPlaying = new Hashtable<>();
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
     * Очистка всей доски.
     */
    public void clear() {
        board.clear();
    }

    /**
     * Устанавливет текущий ход для белых.
     */
    public void setCurMoveWhite() {
        curTypeColor = ETypeColor.WHITE;
    }

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
     * Подготовка к началу игры.
     */
    public void start() {
        if (stateGame) return;
    }

    /**
     * Проверяет на допустимость координат.
     * @param pos объект координат
     * @return true - допустимые координаты, false - не допустимые координаты
     */
    public boolean aField(Pair<Short, Short> pos) {
        if (pos.getX() < 0 || pos.getX() >= width || pos.getY() < 0 || pos.getY() >=height)
            return false;
        return true;
    }

    /**
     * Получение фигуры по её координатам.
     * @param pos координаты фигуры
     * @return фигура, или null
     */
    public IFigureBase getFigure(Pair<Short, Short> pos) {
        if (aField(pos) && board.containsKey(pos)) {
            return board.get(pos);
        }
        return null;
    }

    /**
     * Установка фигуру на доску.
     * @param pos    координаты фигуры
     * @param figure объект устанавливаемой фигуры
     */
    public void setFigure(Pair<Short, Short> pos, IFigureBase figure) {
        if (aField(pos) && getFigure(pos) == null) {
            board.put(pos, figure);
        }
    }

    /**
     * Удаляет фигуру из изгрового поля.
     * @param pos координаты фигуры
     * @return удалённая фигура, или null, елси ошибка.
     */
    public IFigureBase removeFigure(Pair<Short, Short> pos) {
        if (aField(pos) && getFigure(pos) != null) {
            return board.remove(pos);
        }
        return null;
    }

    /**
     * Размеры игрового поля.
     */
    protected int width, height;

    /**
     * Словарь состояния игрового поля.
     */
    protected Map<Pair<Short, Short>, IFigureBase> board;

}
