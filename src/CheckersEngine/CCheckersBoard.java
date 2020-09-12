package CheckersEngine;

import CheckersEngine.BaseEngine.ETypeColor;
import CheckersEngine.BaseEngine.ETypeFigure;
import CheckersEngine.BaseEngine.CEngineBoard;
import CheckersEngine.BaseEngine.IFigureBase;

import java.util.LinkedList;
import java.util.List;

/**
 * Класс управляющий игрой в шашки.
 */
public class CCheckersBoard extends CEngineBoard {

    public CCheckersBoard() {
        super(8, 8);
        placementBoard();
    }

    /**
     * Базовая расстановка фигур на доске.
     */
    public void placementBoard() {
        clearBoard();
        int yi0 = 0, yi1 = 1, yi2 = 2, yi5 = 5, yi6 = 6, yi7 = 7;
        for (int x = 0; x < 8; x++) {
            int xi1 = x + 1;
            setFigure(x, yi0, ETypeFigure.CHECKERS, ETypeColor.WHITE);
            setFigure(xi1, yi1, ETypeFigure.CHECKERS, ETypeColor.WHITE);
            setFigure(x, yi2, ETypeFigure.CHECKERS, ETypeColor.WHITE);
            setFigure(xi1, yi5, ETypeFigure.CHECKERS, ETypeColor.BLACK);
            setFigure(x, yi6, ETypeFigure.CHECKERS, ETypeColor.BLACK);
            setFigure(xi1, yi7, ETypeFigure.CHECKERS, ETypeColor.BLACK);
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
    public List<Integer> getBinaryGame() {
        List<Integer> bytesOut = new LinkedList<>();
        int sizeWC = 0, sizeWQ = 0, sizeBC = 0, sizeBQ = 0;
        List<Integer> lstWC = new LinkedList<>();
        List<Integer> lstWQ = new LinkedList<>();
        List<Integer> lstBC = new LinkedList<>();
        List<Integer> lstBQ = new LinkedList<>();
        Integer tmp;
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if (null != board[y][x]) {
                    tmp = ((y << 4) + x) & 0xff;
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

        tmp = getCurMove() == ETypeColor.WHITE ? 1: 0;
        bytesOut.add(tmp);
        tmp = (getPlayForColor(ETypeColor.WHITE) ? 0x10: 0) + (getPlayForColor(ETypeColor.BLACK) ? 0x1: 0);
        bytesOut.add(tmp);

        return bytesOut;
    }

    @Override
    public int setBinaryGame(List<Integer> binGame, int k) {
        IFigureBase[][] newBoard = new IFigureBase[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                newBoard[y][x] = null;
            }
        }

        int sizeTC;
        int x, y;
        sizeTC = binGame.get(k++);
        if (sizeTC > 0) {
            for (int i = 0; i < sizeTC; i++)
            {
                x = binGame.get(k++);
                y = (x >> 4) & 0xf;
                x = x & 0xf;
                newBoard[y][x] = new CFigureCheckers(ETypeColor.WHITE, x, y);
            }
        }
        sizeTC = binGame.get(k++);
        if (sizeTC > 0) {
            for (int i = 0; i < sizeTC; i++)
            {
                x = binGame.get(k++);
                y = (x >> 4) & 0xf;
                x = x & 0xf;
                newBoard[y][x] = new CFigureQuine(ETypeColor.WHITE, x, y);
            }
        }
        sizeTC = binGame.get(k++);
        if (sizeTC > 0) {
            for (int i = 0; i < sizeTC; i++)
            {
                x = binGame.get(k++);
                y = (x >> 4) & 0xf;
                x = x & 0xf;
                newBoard[y][x] = new CFigureCheckers(ETypeColor.BLACK, x, y);
            }
        }
        sizeTC = binGame.get(k++);
        if (sizeTC > 0) {
            for (int i = 0; i < sizeTC; i++)
            {
                x = binGame.get(k++);
                y = (x >> 4) & 0xf;
                x = x & 0xf;
                newBoard[y][x] = new CFigureQuine(ETypeColor.BLACK, x, y);
            }
        }
        board = newBoard;
        if (binGame.get(k++) > 0) setCurMoveWhite();
        else setCurMoveBlack();
        x = binGame.get(k++);
        y = x & 0xf0;
        x = x & 0xf;
        setPlayerForColor(ETypeColor.WHITE, y > 0);
        setPlayerForColor(ETypeColor.BLACK, x > 0);

        return k;
    }
}
