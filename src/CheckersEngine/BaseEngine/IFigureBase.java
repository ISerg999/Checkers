package CheckersEngine.BaseEngine;

import java.util.List;
import java.util.Map;

public interface IFigureBase {

    /**
     * Задаёт тип фигуры.
     * @param typeFig  тип фигуры
     */
    void setTypeFigure(ETypeFigure typeFig);

    /**
     * Возвращает тип текущей фигуры.
     * @return тип текущей фигуры
     */
    ETypeFigure getTypeFigure();

    /**
     * Задаёт цвет фигуры.
     * @param color цвет фигуры
     */
    void setTypeColor(ETypeColor color);

    /**
     * Возвращает цвет текущей фигуры.
     * @return цвет текущей фигуры
     */
    ETypeColor getColorType();

    /**
     * Задание положения фигуры.
     * @param x координата x игрового поля
     * @param y координата y игрового поля
     */
    void setPos(short x, short y);

    /**
     * Задаёт ссылку на доску в которой будет искать возможные ходы и возможные атаки.
     * @param board игровая доска
     */
    void setBoard(Map<Pair<Short, Short>, IFigureBase> board);

    /**
     * Поиск возможных путей движения фигуры.
     * @return список возможных путей движения фигуры, или null, если таковых не найдено.
     */
    List<List<Pair<Short, Short>>> searchMove();

    /**
     * Поиск возможных атак фигуры.
     * @return список возможных атак фигуры, или null, если таковых не найдено.
     */
    List<List<Pair<Short, Short>>> searchAttack();
}
