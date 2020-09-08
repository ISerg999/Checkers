import CheckersEngine.BaseEngine.Pair;
import CheckersEngine.CheckersBoard;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Класс управляющий игрой на основе конечных автоматов.
 */
public class STMControl implements IChangeState {

    private STMControl() {
//        Main.class.getResource("Resource/imgs/checkers.png"); - Доступ к пути ресурсов по url или получить поток ввода - .getResourceAsStream.
        property = new Properties();
        try {
            InputStream fis = Main.class.getResourceAsStream("Resource/gameres.properties");
            System.out.println(Main.class.getResource("Resource/gameres.properties"));
            property.load(fis);
        } catch (IOException e) {
            System.err.println("ОШИБКА: Файл свойств отсуствует!");
        }
        lstChangeState = new ArrayList<>();
        checkersBoard = new CheckersBoard();
        curStateGame = null;
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

    private Properties property;

    /**
     * Список классов поддерживающих систему конечных автоматов.
     */
    List<IChangeState> lstChangeState;
    /**
     * Класс управления игрой на доске.
     */
    private CheckersBoard checkersBoard;

    /**
     * Текущее состояние программы.
     */
    private TStateGame curStateGame;

    /**
     * Получение значение строки из файла ресурса по ключу.
     * @param key ключ ресурса
     * @return получаемая строка
     */
    public String getResStr(String key) {
        String res = "";
        try {
            res = new String(property.getProperty(key).getBytes("ISO-8859-1"), "UTF-8");
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException("Encoding not supported", e);
        }
        return res;
    }

    /**
     * Получение целого числа из файла ресурса по ключу.
     * @param key ключ ресурса
     * @return получаемое число
     */
    public Integer getResInt(String key) {
        String res = property.getProperty(key);
        return Integer.parseInt(res);
    }

    /**
     * Получение вещественного числа двойной точности по ключу.
     * @param key ключ
     * @return получаемое вещественное число двойной точности
     */
    public Double getResDouble(String key) {
        String res = property.getProperty(key);
        return Double.parseDouble(res);
    }

    public void start() {
        new JFMainWindow();
        makeChangesState(null, TActionGame.TOBASE);
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

    @Override
    public void makeChangesState(TStateGame curStateGame, TActionGame actionGame) {
        for (IChangeState obj: lstChangeState) {
            obj.makeChangesState(this.curStateGame, actionGame);
        }
        if (this.curStateGame == null) this.curStateGame = TStateGame.BASE;
        else {
            Pair<TStateGame,TActionGame> p = new Pair<>(curStateGame, actionGame);
            if (stateAction.containsKey(p)) {
                try {
                    Method method = this.getClass().getDeclaredMethod(stateAction.get(p));
                    method.setAccessible(true);
                    method.invoke(this);
                } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            if (newStateGame.containsKey(p)) this.curStateGame = newStateGame.get(p);
        }
    }

}
