package CheckersEngine;

import CheckersEngine.BaseEngine.ETypeColor;
import CheckersEngine.BaseEngine.EngineBoard;

/**
 * Класс управляющий игрой в шашки.
 */
public class CheckersBoard extends EngineBoard {

    public CheckersBoard() {
        super(8, 8);
        placementBoard();
    }

    /**
     * Проверяет на допустимость координат.
     * @param x координата x
     * @param y координата y
     * @return true - допустимые координаты, false - не допустимые координаты
     */
    @Override
    public boolean aField(int x, int y) {
        boolean bRes = super.aField(x, y);
        if ((x + y) % 2 == 1) bRes = false;
        return bRes;
    }

    /**
     * Базовая расстановка фигур на доске.
     */
    public void placementBoard() {
        clearBoard();
        short yi0 = 0, yi1 = 1, yi2 = 2, yi5 = 5, yi6 = 6, yi7 = 7;
        for (short x = 0; x < 8; x++) {
            short xi1 = (short) (x + 1);
            setFigure(x, yi0, new FigureCheckers(ETypeColor.WHITE, x, yi0));
            setFigure(xi1, yi1, new FigureCheckers(ETypeColor.WHITE, xi1, yi1));
            setFigure(x, yi2, new FigureCheckers(ETypeColor.WHITE, x, yi2));
            setFigure(xi1, yi5, new FigureCheckers(ETypeColor.BLACK, xi1, yi5));
            setFigure(x, yi6, new FigureCheckers(ETypeColor.BLACK, x, yi6));
            setFigure(xi1, yi7, new FigureCheckers(ETypeColor.BLACK, xi1, yi7));
        }
        setCurMoveWhite();
    }
}
