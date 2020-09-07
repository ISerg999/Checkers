package CheckersEngine;

import java.util.Map;

public interface IFigureBase {
    /**
     * Задаёт цвет фигуры.
     * @param colorFig цвет фигуры
     */
    void setFigureColor(short colorFig);

    /**
     * Задаёт тип фигуры.
     * @param typeFig  тип фигуры
     */
    void setFigureType(short typeFig);

    /**
     * Возвращает цвет текущей фигуры.
     * @return цвет текущей фигуры
     */
    short getColorFigure();

    /**
     * Возвращает тип текущей фигуры.
     * @return тип текущей фигуры
     */
    short getTypeFigure();

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

    void searchMove();
    void searchAttack();
}
