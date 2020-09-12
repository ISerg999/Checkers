package CheckersEngine.BaseEngine;

import CheckersEngine.CFigureCheckers;
import CheckersEngine.CFigureQuine;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Пул объектов фигур.
 */
public class CPoolFigures {

    /**
     * Словарь доступных объектов.
     */
    protected Map<Integer, List<IFigureBase>> publicFigures;
    /**
     * Словарь скрытых объектов.
     */
    protected Map<Integer, List<IFigureBase>> hiddenFigures;

    public CPoolFigures() {
        publicFigures = new HashMap<>();
        hiddenFigures = new HashMap<>();
        for (ETypeFigure tf: ETypeFigure.values()) {
            for (ETypeColor cf: ETypeColor.values()) {
                int code = tf.getDirection() + cf.getDirection();
                publicFigures.put(code, new LinkedList<>());
                hiddenFigures.put(code, new LinkedList<>());
            }
        }
    }

    /**
     * Получение фигуры.
     * @param tf тип ифигуры
     * @param cf цвет фигуры
     * @return получаемая фигура
     */
    public IFigureBase get(ETypeFigure tf, ETypeColor cf) {
        IFigureBase res;
        int code = tf.getDirection() + cf.getDirection();
        if (hiddenFigures.get(code).size() > 0) {
            res = hiddenFigures.get(code).remove(0);
        } else {
            if (ETypeFigure.CHECKERS == tf) {
                res = new CFigureCheckers(cf);
            } else {
                res = new CFigureQuine(cf);
            }
        }
        publicFigures.get(code).add(res);
        return res;
    }

    /**
     * Удаляет фигуру.
     * @param ifb объект фигуры
     */
    public void remove(IFigureBase ifb) {
        ETypeFigure tf = ifb.getTypeFigure();
        ETypeColor cf = ifb.getColorType();
        int code = tf.getDirection() + cf.getDirection();
        publicFigures.get(code).remove(ifb);
        hiddenFigures.get(code).add(ifb);
    }
}
