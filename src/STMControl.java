import CheckersEngine.BaseEngine.*;
import CheckersEngine.CheckersBoard;

import java.awt.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.List;

/**
 * Класс управляющий игрой на основе конечных автоматов.
 */
public class STMControl{

    private STMControl() {
        resourse = CResourse.getInstance();
        resourse.addResourse("Resource/gameres.properties");
        lstChangeState = new ArrayList<>();
        checkersBoard = new CheckersBoard();
        curStateGame = null;
        oldStateGame = null;
        isEdition = false;
        fileName = null;
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
    protected static final Map<Pair<TStateGame,TActionGame>,TStateGame> newStateGame;
    static {
        newStateGame = new Hashtable<>();
//        newStateGame.put(new Pair<>(TStateGame.BASE, TActionGame.TOBASE), TStateGame.BASE);
    }
    /**
     * Список изполняемых функций для текущего объекта
     */
    protected static final Map<Pair<TStateGame,TActionGame>, String> stateAction;
    static {
        stateAction = new Hashtable<>();
        stateAction.put(new Pair<>(TStateGame.NONE, TActionGame.TOSAVE), "saveBoard");
        stateAction.put(new Pair<>(TStateGame.NONE, TActionGame.TOLOAD), "loadBoard");
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
     * Имя файла для записи ли чтения.
     */
    private String fileName;

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
     * Устанавливает имя файла.
     * @param fileName полное имя файла.
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
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

    /**
     * Базовый метод управления работой программы.
     * @param actionGame происходящий переход
     * @param allState   зависимость перехода от состояния, true - не зависит, false - зависит
     */
    public void makeChangesState(TActionGame actionGame, boolean allState) {
        TStateGame selectedSG = allState ? TStateGame.NONE: curStateGame;
        Pair<TStateGame,TActionGame> psg = new Pair<>(selectedSG, actionGame);
        if (null != curStateGame) {
            if (TActionGame.TORETURN == actionGame) curStateGame = oldStateGame;
            else {
                oldStateGame = curStateGame;
                if (newStateGame.containsKey(psg)) curStateGame = newStateGame.get(psg);
            }
        }
        else {
            oldStateGame = TStateGame.BASE;
            curStateGame = TStateGame.BASE;
        }
        if (TActionGame.TORETURN != actionGame) {
            for (IChangeState objSt: lstChangeState) {
                objSt.makeChangesState(psg);
            }
            String funName = stateAction.getOrDefault(psg, null);
            if (null != funName) {
                try {
                    Method method = this.getClass().getDeclaredMethod(funName);
                    method.setAccessible(true);
                    method.invoke(this);
                }  catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // ---------------------------- Методы обрабатывающиеся контроллером переходов состояний ---------------------------

    protected void saveBoard() {
        if (null != fileName) {
            List<Short> bin = new LinkedList<>();
            String title = resourse.getResStr("Board.File.Title");
            bin.addAll(checkersBoard.getBinaryGame());
            bin.addAll(checkersBoard.getLstMoves().getListPack());
            try (OutputStream os = new FileOutputStream(fileName);) {
                for (byte cb: title.getBytes()) {
                    os.write(cb);
                }
                for (Short code: bin) {
                    os.write(code);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            makeChangesState(TActionGame.TOSAVEOK, true);
        }
    }

    protected void loadBoard() {
        if (null != fileName) {
            int k = 0;
            List<Short> bin = new LinkedList<>();
            String title = resourse.getResStr("Board.File.Title");
            try (InputStream is = new FileInputStream(fileName);) {
                int byteRead;
                while (((byteRead = is.read()) != -1) && k < title.length()) {
                    if (byteRead != title.charAt(k)) {
                        throw new IOException("Ошибка в заголовке файла");
                    }
                    else k++;
                }
                bin.add((short) byteRead);
                while ((byteRead = is.read()) != -1) {
                    bin.add((short) byteRead);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            k = checkersBoard.setBinaryGame(bin, 0);
            checkersBoard.getLstMoves().setListPack(bin, k);
            makeChangesState(TActionGame.TOLOADOK, true);
        }
    }
}
