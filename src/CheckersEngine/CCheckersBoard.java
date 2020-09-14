package CheckersEngine;

import CheckersEngine.BaseEngine.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Класс управляющий игрой в шашки.
 */
public class CCheckersBoard extends CEngineBoard {

    protected final static Integer[] keysForFile = {
            ETypeColor.WHITE.getDirection() + ETypeFigure.CHECKERS.getDirection(),
            ETypeColor.WHITE.getDirection() + ETypeFigure.QUINE.getDirection(),
            ETypeColor.BLACK.getDirection() + ETypeFigure.CHECKERS.getDirection(),
            ETypeColor.BLACK.getDirection() + ETypeFigure.QUINE.getDirection()
    };

    public CCheckersBoard() {
        super(8, 8);
    }

    /**
     * Базовая расстановка фигур на доске.
     */
    @Override
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
    public List<Integer> getBinaryBoardFigure() {
        int tmp;
        List<Integer> bytesOut = new LinkedList<>();
        Map<Integer, List<CPair<Integer, Integer>>> mFigures = new HashMap<>();
        mFigures.put(keysForFile[0], new LinkedList<>());
        mFigures.put(keysForFile[1], new LinkedList<>());
        mFigures.put(keysForFile[2], new LinkedList<>());
        mFigures.put(keysForFile[3], new LinkedList<>());

        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if (null != board[y][x]) {
                    tmp = board[y][x].getColorType().getDirection() + board[y][x].getTypeFigure().getDirection();
                    mFigures.get(tmp).add(new CPair<>(x, y));
                }
            }
        }

        for (int y = 0; y < 4; y++) {
            List<CPair<Integer, Integer>> lstCoord = mFigures.get(keysForFile[y]);
            bytesOut.add(lstCoord.size());
            for (int x = 0; x < lstCoord.size(); x++) {
                tmp = ((lstCoord.get(x).getSecond() << 4) + lstCoord.get(x).getFirst()) & 0xff;
                bytesOut.add(tmp);
            }
        }

        return bytesOut;
   }

   @Override
   public int setBinaryBoardFigure(List<Integer> binGame, int k) {
        int x, y, lenLst;
       List<CPair<Integer, Integer>> lstCoord;
       Map<Integer, List<CPair<Integer, Integer>>> mFigures = new HashMap<>();
       mFigures.put(keysForFile[0], new LinkedList<>());
       mFigures.put(keysForFile[1], new LinkedList<>());
       mFigures.put(keysForFile[2], new LinkedList<>());
       mFigures.put(keysForFile[3], new LinkedList<>());

       for (int j = 0; j < 4; j++) {
           lstCoord = mFigures.get(keysForFile[j]);
           for (lenLst = binGame.get(k++); lenLst > 0; lenLst--) {
               x = binGame.get(k++);
               y = (x >> 4) & 0xf;
               x = x & 0xf;
               lstCoord.add(new CPair<>(x, y));
           }
       }

       clearBoard();
       lstCoord = mFigures.get(keysForFile[0]);
       for (CPair<Integer, Integer> p: lstCoord) {
           setFigure(p.getFirst(), p.getSecond(), ETypeFigure.CHECKERS, ETypeColor.WHITE);
       }
       lstCoord = mFigures.get(keysForFile[1]);
       for (CPair<Integer, Integer> p: lstCoord) {
           setFigure(p.getFirst(), p.getSecond(), ETypeFigure.QUINE, ETypeColor.WHITE);
       }
       lstCoord = mFigures.get(keysForFile[2]);
       for (CPair<Integer, Integer> p: lstCoord) {
           setFigure(p.getFirst(), p.getSecond(), ETypeFigure.CHECKERS, ETypeColor.BLACK);
       }
       lstCoord = mFigures.get(keysForFile[3]);
       for (CPair<Integer, Integer> p: lstCoord) {
           setFigure(p.getFirst(), p.getSecond(), ETypeFigure.QUINE, ETypeColor.BLACK);
       }

       return k;
   }

   @Override
   public List<Integer> getBinaryBoardState() {
       List<Integer> bytesOut = new LinkedList<>();
       int tmp = getCurMove() == ETypeColor.WHITE ? 1: 0;
       bytesOut.add(tmp);
       tmp = (getPlayForColor(ETypeColor.WHITE) ? 0x10: 0) + (getPlayForColor(ETypeColor.BLACK) ? 0x1: 0);
       bytesOut.add(tmp);

       return bytesOut;
   }

   @Override
   public int setBinaryBoardState(List<Integer> binGame, int k) {
       if (binGame.get(k++) > 0) setCurMoveWhite();
       else setCurMoveBlack();
       int x = binGame.get(k++);
       int y = x & 0xf0;
       x = x & 0xf;
       setPlayerForColor(ETypeColor.WHITE, y > 0);
       setPlayerForColor(ETypeColor.BLACK, x > 0);

       return k;
   }

}
