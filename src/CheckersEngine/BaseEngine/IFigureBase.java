package CheckersEngine.BaseEngine;

import java.util.List;
import java.util.Map;

/**
 * Интерфейс классов игровых фигур.
 */
public interface IFigureBase {
    /**
     * Алгоритм поиска возможных ходов данной фигурой.
     * @param board объект игровой доски
     * @param pos   позиция фигуры
     * @return список списоков ходов (по 2 параметра: 1-й: конечные координаты, 2-й: если null, то фигура не изменилась, иначе координаты изменения)
     */
    List<List<CPair<Integer, Integer>>> searchMove(Map<CPair<Integer, Integer>, CPair<ETypeFigure, ETypeColor>> board, CPair<Integer, Integer> pos);

    /**
     * Алгоритм поиска возможных атак данной фигурой.
     * @param board объект игровой доски
     * @param pos   позиция фигуры
     * @return список списокв атак (по 2 + n параметров: 1-й: конечные координаты, 2-й: если null, то фигура не изменилась, иначе координаты изменения,
     * остальные по 2 координаты: первая из них - координаты снимаемой фигуры, вторые - координаты за ней)
     */
    List<List<CPair<Integer, Integer>>> searchAttack(Map<CPair<Integer, Integer>, CPair<ETypeFigure, ETypeColor>> board, CPair<Integer, Integer> pos);
}
