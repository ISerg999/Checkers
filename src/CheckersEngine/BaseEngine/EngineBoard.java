package CheckersEngine.BaseEngine;

import java.util.Hashtable;
import java.util.Map;

/**
 * Базовый класс движка.
 */
public class EngineBoard {

    /**
     * Конструктор.
     * @param w ширина игрового поля
     * @param h высота игрового поля
     */
    public EngineBoard(int w, int h) {
        this.width = w;
        this.height = h;
        board = new Hashtable<>();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void clear() {
        board.clear();
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
