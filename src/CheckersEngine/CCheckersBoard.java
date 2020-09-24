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
    public CCheckersBoard(CCheckersBoard old) {
        super(old);
    }

    /**
     * Базовая расстановка фигур на доске.
     */
    @Override
    public synchronized void placementBoard() {
        clearBoard();
        int yi0 = 0, yi1 = 1, yi2 = 2, yi5 = 5, yi6 = 6, yi7 = 7;
        for (int x = 0; x < 8; x += 2) {
            int xi1 = x + 1;
            board.put(new CPair<>(x, yi0), new CPair<>(ETypeFigure.CHECKERS, ETypeColor.WHITE));
            board.put(new CPair<>(xi1, yi1), new CPair<>(ETypeFigure.CHECKERS, ETypeColor.WHITE));
            board.put(new CPair<>(x, yi2), new CPair<>(ETypeFigure.CHECKERS, ETypeColor.WHITE));
            board.put(new CPair<>(xi1, yi5), new CPair<>(ETypeFigure.CHECKERS, ETypeColor.BLACK));
            board.put(new CPair<>(x, yi6), new CPair<>(ETypeFigure.CHECKERS, ETypeColor.BLACK));
            board.put(new CPair<>(xi1, yi7), new CPair<>(ETypeFigure.CHECKERS, ETypeColor.BLACK));
        }
        curMove = ETypeColor.WHITE;
    }

    @Override
    public List<Integer> getBinaryRepresentationBoard() {
        List<Integer> bytesOut = new LinkedList<>();
        int tmp;
        Map<Integer, List<CPair<Integer, Integer>>> mFigures = new HashMap<>();
        mFigures.put(keysForFile[0], new LinkedList<>());
        mFigures.put(keysForFile[1], new LinkedList<>());
        mFigures.put(keysForFile[2], new LinkedList<>());
        mFigures.put(keysForFile[3], new LinkedList<>());

        for (CPair<Integer, Integer> pos: board.keySet()) {
            tmp = board.get(pos).getSecond().getDirection() + board.get(pos).getFirst().getDirection();
            mFigures.get(tmp).add(pos);
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
   public synchronized int setBinaryRepresentationBoard(List<Integer> binGame, int k) {
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
           board.put(p, new CPair<>(ETypeFigure.CHECKERS, ETypeColor.WHITE));
       }
       lstCoord = mFigures.get(keysForFile[1]);
       for (CPair<Integer, Integer> p: lstCoord) {
           board.put(p, new CPair<>(ETypeFigure.QUINE, ETypeColor.WHITE));
       }
       lstCoord = mFigures.get(keysForFile[2]);
       for (CPair<Integer, Integer> p: lstCoord) {
           board.put(p, new CPair<>(ETypeFigure.CHECKERS, ETypeColor.BLACK));
       }
       lstCoord = mFigures.get(keysForFile[3]);
       for (CPair<Integer, Integer> p: lstCoord) {
           board.put(p, new CPair<>(ETypeFigure.QUINE, ETypeColor.BLACK));
       }

       return k;
   }

   @Override
   public List<Integer> getBinaryParametrsGame() {
       List<Integer> bytesOut = new LinkedList<>();
       int tmp = curMove.getDirection();
       tmp += gameOn ? 0x10: 0;
       bytesOut.add(tmp);
       tmp = endState < 0 ? 0xff: endState;
       bytesOut.add(tmp);
       return bytesOut;
   }

   @Override
   public synchronized int setBinaryParametrsGame(List<Integer> binGame, int k) {
        int tmp = binGame.get(k++);
        gameOn = tmp >= 0x10;
        tmp = tmp & 0xf;
        curMove = tmp == 1 ? ETypeColor.WHITE: ETypeColor.BLACK;
        tmp = binGame.get(k++);
        endState = tmp == 0xff ? -1 : tmp;
       return k;
   }

}
