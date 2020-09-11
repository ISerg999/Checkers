package CheckersEngine;

import CheckersEngine.BaseEngine.ETypeColor;
import CheckersEngine.BaseEngine.ETypeFigure;
import CheckersEngine.BaseEngine.EngineBoard;
import CheckersEngine.BaseEngine.IFigureBase;

import java.util.LinkedList;
import java.util.List;

/**
 * Класс управляющий игрой в шашки.
 */
public class CheckersBoard extends EngineBoard {

    public CheckersBoard() {
        super(8, 8);
        placementBoard();
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

    @Override
    public List<Short> getBinaryGame() {
        List<Short> bytesOut = new LinkedList<>();
        short sizeWC = 0, sizeWQ = 0, sizeBC = 0, sizeBQ = 0;
        List<Short> lstWC = new LinkedList<>();
        List<Short> lstWQ = new LinkedList<>();
        List<Short> lstBC = new LinkedList<>();
        List<Short> lstBQ = new LinkedList<>();
        Short tmp;
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if (null != board[y][x]) {
                    tmp = (short) (((y << 4) + x) & 255);
                    if (board[y][x].getColorType() == ETypeColor.WHITE) {
                        if (board[y][x].getTypeFigure() == ETypeFigure.CHECKERS) {
                            sizeWC++;
                            lstWC.add(tmp);
                        } else {
                            sizeWQ++;
                            lstWQ.add(tmp);
                        }
                    } else {
                        if (board[y][x].getTypeFigure() == ETypeFigure.CHECKERS) {
                            sizeBC++;
                            lstBC.add(tmp);
                        } else {
                            sizeBQ++;
                            lstBQ.add(tmp);
                        }
                    }
                }
            }
        }
        bytesOut.add(sizeWC);
        if (sizeWC > 0) bytesOut.addAll(lstWC);
        bytesOut.add(sizeWQ);
        if (sizeWQ > 0) bytesOut.addAll(lstWQ);
        bytesOut.add(sizeBC);
        if (sizeBC > 0) bytesOut.addAll(lstBC);
        bytesOut.add(sizeBQ);
        if (sizeBQ > 0) bytesOut.addAll(lstBQ);

        tmp = (short) (getCurMove() == ETypeColor.WHITE ? 1: 0);
        bytesOut.add(tmp);
        tmp = (short) ((getPlayForColor(ETypeColor.WHITE) ? 16: 0) + (getPlayForColor(ETypeColor.BLACK) ? 1: 0));
        bytesOut.add(tmp);

        return bytesOut;
    }

    @Override
    public int setBinaryGame(List<Short> binGame, int k) {
        IFigureBase[][] newBoard = new IFigureBase[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                newBoard[y][x] = null;
            }
        }

        short sizeTC;
        short x, y;
        sizeTC = binGame.get(k++);
        if (sizeTC > 0) {
            for (int i = 0; i < sizeTC; i++)
            {
                x = binGame.get(k++);
                y = (short) ((x >> 4) & 15);
                x = (short) (x & 15);
                newBoard[y][x] = new FigureCheckers(ETypeColor.WHITE, x, y);
            }
        }
        sizeTC = binGame.get(k++);
        if (sizeTC > 0) {
            for (int i = 0; i < sizeTC; i++)
            {
                x = binGame.get(k++);
                y = (short) ((x >> 4) & 15);
                x = (short) (x & 15);
                newBoard[y][x] = new FigureQuine(ETypeColor.WHITE, x, y);
            }
        }
        sizeTC = binGame.get(k++);
        if (sizeTC > 0) {
            for (int i = 0; i < sizeTC; i++)
            {
                x = binGame.get(k++);
                y = (short) ((x >> 4) & 15);
                x = (short) (x & 15);
                newBoard[y][x] = new FigureCheckers(ETypeColor.BLACK, x, y);
            }
        }
        sizeTC = binGame.get(k++);
        if (sizeTC > 0) {
            for (int i = 0; i < sizeTC; i++)
            {
                x = binGame.get(k++);
                y = (short) ((x >> 4) & 15);
                x = (short) (x & 15);
                newBoard[y][x] = new FigureQuine(ETypeColor.BLACK, x, y);
            }
        }
        board = newBoard;
        if (binGame.get(k++) > 0) setCurMoveWhite();
        else setCurMoveBlack();
        x = binGame.get(k++);
        y = (short) (x & 0xf0);
        x = (short) (x & 15);
        setPlayerForColor(ETypeColor.WHITE, y > 0);
        setPlayerForColor(ETypeColor.BLACK, x > 0);

        return k;
    }
}
