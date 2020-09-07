package CheckersEngine;

import CheckersEngine.BaseEngine.ETypeColor;
import CheckersEngine.BaseEngine.EngineBoard;
import CheckersEngine.BaseEngine.Pair;

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
     * @param pos объект координат
     * @return true - допустимые координаты, false - не допустимые координаты
     */
    @Override
    public boolean aField(Pair<Short, Short> pos) {
        boolean bRes = super.aField(pos);
        if ((pos.getX() + pos.getY()) % 2 == 1) bRes = false;
        return bRes;
    }

    /**
     * Базовая расстановка фигур на доске.
     */
    public void placementBoard() {
        board.clear();
        short x = 0;
        short yi0 = 0, yi1 = 1, yi6 = 6, yi7 = 7;
        for (short i = 0; i < 4; i++) {
            short xi1 = (short)(x + 1);
            board.put(new Pair<>(x, yi0), new FigureCheckers(ETypeColor.WHITE, x, yi0));
            board.put(new Pair<>(xi1, yi1), new FigureCheckers(ETypeColor.WHITE, xi1, yi1));
            board.put(new Pair<>(x, yi6), new FigureCheckers(ETypeColor.BLACK, x, yi6));
            board.put(new Pair<>(xi1, yi7), new FigureCheckers(ETypeColor.BLACK, xi1, yi7));
            x += 2;
        }
        setCurMoveWhite();
    }
}
