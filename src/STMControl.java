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
     * Текущее и прошлое состояние программы.
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

    /**
     * Получение текущего состояния.
     * @return текущее состояние
     */
    public TStateGame getCurStateGame() {
        return curStateGame;
    }

    /**
     * Получение предыдущего состояния.
     * @return предыдущее состояние
     */
    public TStateGame getOldStateGame() {
        return oldStateGame;
    }

    /**
     * Сохраняет текущее состоянияе.
     */
    public void saveCruStateGame() { oldStateGame = curStateGame; }

    @Override
    public void makeChangesState(TStateGame curStateGame, TActionGame actionGame) {
        for (IChangeState obj: lstChangeState) {
            obj.makeChangesState(this.curStateGame, actionGame);
        }
        if (this.curStateGame == null) this.curStateGame = TStateGame.BASE;
        else {
            Pair<TStateGame,TActionGame> p = new Pair<>(curStateGame, actionGame);
            if (newStateGame.containsKey(p)){
                this.curStateGame = newStateGame.get(p);
            }
            if (stateAction.containsKey(p)) {
                try {
                    Method method = this.getClass().getDeclaredMethod(stateAction.get(p));
                    method.setAccessible(true);
                    method.invoke(this);
                } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
