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
    void setColorType(ETypeColor color);

    /**
     * Возвращает цвет текущей фигуры.
     * @return цвет текущей фигуры
     */
    ETypeColor getColorType();

    /**
     * Задание положения фигуры.
     * @param pos позиция фигуры
     */
    void setPos(CPair<Integer, Integer> pos);

    /**
     * Получение текущей заданной координаты x.
     * @return координата x
     */
    int getX();

    /**
     * Получение текущей заданной координаты y.
     * @return координата y
     */
    int getY();

    /**
     * Задаёт ссылку на доску в которой будет искать возможные ходы и возможные атаки.
     * @param board игровая доска
     */
    void setBoard(Map<CPair<Integer, Integer>, IFigureBase> board);

    /**
     * Поиск возможных путей движения фигуры.
     * @return список возможных путей движения фигуры, или null, если таковых не найдено.
     */
    List<List<CPair<Integer, Integer>>> searchMove();

    /**
     * Поиск возможных атак фигуры.
     * @return список возможных атак фигуры, или null, если таковых не найдено.
     */
    List<List<CPair<Integer, Integer>>> searchAttack();
}
