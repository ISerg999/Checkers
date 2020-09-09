import CheckersEngine.BaseEngine.ETypeColor;
import CheckersEngine.BaseEngine.ETypeFigure;
import CheckersEngine.BaseEngine.IFigureBase;
import CheckersEngine.BaseEngine.Pair;
import CheckersEngine.CheckersBoard;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.List;

/**
 * Класс управляющий игрой на основе конечных автоматов.
 */
public class STMControl implements IChangeState {

    private STMControl() {
        resourse = CResourse.getInstance();
        resourse.addResourse("Resource/gameres.properties");
        lstChangeState = new ArrayList<>();
        checkersBoard = new CheckersBoard();
        curStateGame = null;
        oldStateGame = null;
        isEdition = false;
    }

    private static class STMControlHolder {
        private static final STMControl INSTANCE = new STMControl();
    }

    public static STMControl getInstance() {
        return STMControlHolder.INSTANCE;
    }

    /**
     * Словарь позволяющий понять новое состояние по текущему состоянию и произведенному действию.
     */
    private static final Map<Pair<TStateGame,TActionGame>,TStateGame> newStateGame;
    static {
        newStateGame = new Hashtable<>();
//        newStateGame.put(new Pair<>(TStateGame.BASE, TActionGame.TOBASE), TStateGame.BASE);
    }
    /**
     * Список изполняемых функций для текущего объекта
     */
    private static final Map<Pair<TStateGame,TActionGame>, String> stateAction;
    static {
        stateAction = new Hashtable<>();
//        stateAction.put(new Pair<>(TStateGame.BASE, TActionGame.TOBASE), "function");
    }

    private CResourse resourse;
    /**
     * Список классов поддерживающих систему конечных автоматов.
     */
    List<IChangeState> lstChangeState;
    /**
     * Класс управления игрой на доске.
     */
    private CheckersBoard checkersBoard;
    /**
     * Режим редактирования.
     */
    private boolean isEdition;

    /**
     * Состояния программы.
     */
    private TStateGame curStateGame, oldStateGame;

    public void start() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new JFMainWindow();
            }
        });
    }

    /**
     * Добавление классов обрабаотывающих изменение состояния.
     * @param obj класс обрабатывающий изменение состояния
     */
    public void addActionGame(IChangeState obj) {
        lstChangeState.add(obj);
    }

    public Pair<ETypeFigure, ETypeColor> getFigureForBoard(int x, int y) {
        IFigureBase fb = checkersBoard.getFigure(x, y);
        if (fb == null) return null;
        return new Pair<>(fb.getTypeFigure(), fb.getColorType());
    }

    /**
     * Проверка правильности координат.
     * @param x координата доски x
     * @param y координата доски y
     * @return true - координата правильная, false - не правильная
     */
    public boolean testCoordinateBoard(int x, int y) {
        if (!checkersBoard.getStateGame() && !isEdition) return false;
        return checkersBoard.aField(x, y);
    }

    /**
     * Получение текущего состояния.
     * @return текущее состояние
     */
    public TStateGame getCurStateGame() {
        return curStateGame;
    }

    @Override
    public void makeChangesState(TStateGame cs, TActionGame actionGame) {
        oldStateGame = this.curStateGame;
        Pair<TStateGame,TActionGame> p = new Pair<>(curStateGame, actionGame);
        if (oldStateGame == null) curStateGame = TStateGame.BASE;
        else {
            if (newStateGame.containsKey(p)) {
                curStateGame = newStateGame.get(p);
            }
        }
        for (IChangeState obj: lstChangeState) {
            obj.makeChangesState(oldStateGame, actionGame);
        }
        if (oldStateGame != null) {
            if (stateAction.containsKey(p)) {
                try {
                    Method method = this.getClass().getDeclaredMethod(stateAction.get(p));
                    method.setAccessible(true);
                    method.invoke(this);
                }  catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // ---------------------------- Методы обрабатывающиеся контроллером переходов состояний ---------------------------

    /**
     * Восстанавливает старое состояние.
     */
    protected void recoverStateGame() {
        curStateGame = oldStateGame;
    }

}
