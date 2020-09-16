import CheckersEngine.BaseEngine.*;
import CheckersEngine.CCheckersBoard;

import java.awt.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.List;

/**
 * Класс управляющий игрой на основе конечных автоматов. Оформлен как синглтон.
 */
public class CSMControl {

    private CSMControl() {
        curStateGame = null;
        oldStateGame = null;
        resourse = CResourse.getInstance();
        resourse.addResourse("Resource/gameres.properties");
        lstChangeState = new ArrayList<>();
        cMoveGame = new CControlMoveGame(new CCheckersBoard());
        fileName = null;
    }
    private static class STMControlHolder {
        private static final CSMControl INSTANCE = new CSMControl();
    }
    public static CSMControl getInstance() {
        return STMControlHolder.INSTANCE;
    }

    /**
     * Словарь позволяющий понять новое состояние по текущему состоянию и произведенному действию.
     */
    protected static final Map<CPair<ETStateGame, ETActionGame>, ETStateGame> newStateGame;
    static {
        newStateGame = new Hashtable<>();
        newStateGame.put(new CPair<>(ETStateGame.BASE, ETActionGame.TOEDITING), ETStateGame.EDITING);
        newStateGame.put(new CPair<>(ETStateGame.EDITING, ETActionGame.TOBASE), ETStateGame.BASE);
        newStateGame.put(new CPair<>(ETStateGame.BASE, ETActionGame.TOGAME), ETStateGame.GAME);
        newStateGame.put(new CPair<>(ETStateGame.GAME, ETActionGame.TOBASE), ETStateGame.BASE);
    }
    /**
     * Список изполняемых функций для текущего объекта
     */
    protected static final Map<CPair<ETStateGame, ETActionGame>, String> stateAction;
    static {
        stateAction = new Hashtable<>();
        stateAction.put(new CPair<>(ETStateGame.NONE, ETActionGame.TOSAVE), "saveBoard");
        stateAction.put(new CPair<>(ETStateGame.NONE, ETActionGame.TOLOAD), "loadBoard");
        stateAction.put(new CPair<>(ETStateGame.NONE, ETActionGame.TOWHITEPLAYER), "playWhiteFromPlayer");
        stateAction.put(new CPair<>(ETStateGame.NONE, ETActionGame.TOBLACKPLAYER), "playBlackFromPlayer");
        stateAction.put(new CPair<>(ETStateGame.NONE, ETActionGame.TOWHITECOMP), "playWhiteFromComp");
        stateAction.put(new CPair<>(ETStateGame.NONE, ETActionGame.TOBLACKCOMP), "playBlackFromComp");
    }

    /**
     * Объект ресурсов.
     */
    private CResourse resourse;
    /**
     * Список классов поддерживающих систему конечных автоматов.
     */
    List<IChangeState> lstChangeState;
    /**
     * Состояния программы.
     */
    private ETStateGame curStateGame, oldStateGame;
    /**
     * Имя файла для записи ли чтения.
     */
    private String fileName;
    /**
     * Режим редактирования.
     */
    private boolean isEdition;
    /**
     * Объект контролирующий ходы и изменения на доске.
     */
    private CControlMoveGame cMoveGame;

    public void start() {
        EventQueue.invokeLater(() -> new JFMainWindow());
    }

    /**
     * Добавление классов обрабаотывающих изменение состояния.
     * @param obj класс обрабатывающий изменение состояния
     */
    public void addActionGame(IChangeState obj) {
        lstChangeState.add(obj);
    }

    /**
     * Возвращает объект контролирующий ходы и изменения на доске.
     * @return объект контролирующий ходы и изменения на доске
     */
    public CControlMoveGame getCMoveGame() {
        return cMoveGame;
    }

    /**
     * Получает ссылку на класс игры в шашки.
     * @return
     */
    public CCheckersBoard getBoard() {
        return (CCheckersBoard) cMoveGame.getBoard();
    }

    /**
     * Устанавливает имя файла.
     * @param fileName полное имя файла.
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Получает состояние режима редактирования.
     * @return режим редактирования
     */
    public boolean getIsEdition() {
        return isEdition;
    }

    /**
     * Устанавливает состояние режима редактирования.
     * @param edition режим редактирования
     */
    public void setIsEdition(boolean edition) {
        if (getBoard().getStateGame()) return;
        isEdition = edition;
    }

    /**
     * Базовый метод управления работой программы.
     * @param actionGame происходящий переход
     * @param allState   зависимость перехода от состояния, true - не зависит, false - зависит
     */
    public void makeChangesState(ETActionGame actionGame, boolean allState) {
        ETStateGame selectedSG = allState ? ETStateGame.NONE: curStateGame;
        CPair<ETStateGame, ETActionGame> psg = new CPair<>(selectedSG, actionGame);
        if (null != curStateGame) {
            if (ETActionGame.TORETURN == actionGame) curStateGame = oldStateGame;
            else {
                oldStateGame = curStateGame;
                if (newStateGame.containsKey(psg)) curStateGame = newStateGame.get(psg);
            }
        } else {
            oldStateGame = ETStateGame.BASE;
            curStateGame = ETStateGame.BASE;
        }
        if (ETActionGame.TORETURN != actionGame) {
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


// ------------------------------ Методы обрабатывающиеся контроллером переходов состояний -----------------------------

    /**
     * Запись игрового состояния в файл.
     */
    protected void saveBoard() {
        if (null != fileName) {
            List<Integer> bin = new LinkedList<>();
            String title = resourse.getResStr("Board.File.Title");
            bin.addAll(getBoard().getBinaryBoardFigure());
            bin.addAll(getBoard().getBinaryBoardState());
            bin.addAll(getCMoveGame().getListBin());
            try (OutputStream os = new FileOutputStream(fileName);) {
                for (byte cb: title.getBytes()) {
                    os.write(cb);
                }
                for (Integer code: bin) {
                    os.write(code);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            makeChangesState(ETActionGame.TOSAVEOK, true);
        }
    }

    /**
     * Чтение из файла состояния игры.
     */
    protected void loadBoard() {
        if (null != fileName) {
            int k = 0;
            List<Integer> bin = new LinkedList<>();
            String title = resourse.getResStr("Board.File.Title");
            try (InputStream is = new FileInputStream(fileName);) {
                int byteRead;
                while (((byteRead = is.read()) != -1) && k < title.length()) {
                    if (byteRead != title.charAt(k)) {
                        throw new IOException("Ошибка в заголовке файла");
                    }
                    else k++;
                }
                bin.add(byteRead);
                while ((byteRead = is.read()) != -1) {
                    bin.add(byteRead);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            k = getBoard().setBinaryBoardFigure(bin, 0);
            k = getBoard().setBinaryBoardState(bin, k);
            getCMoveGame().setListBin(bin, k);
            makeChangesState(ETActionGame.TOLOADOK, true);
        }
    }

    /**
     * Установка игры белых за игрока.
     */
    protected void playWhiteFromPlayer() {
        cMoveGame.getBoard().setPlayerForColor(ETypeColor.WHITE, true);
    }

    /**
     * Установка игры белых за компьютер.
     */
    protected void playWhiteFromComp() {
        cMoveGame.getBoard().setPlayerForColor(ETypeColor.WHITE, false);
    }

    /**
     * Установка игры чёрных за игрока.
     */
    protected void playBlackFromPlayer() {
        cMoveGame.getBoard().setPlayerForColor(ETypeColor.BLACK, true);
    }

    /**
     * Установка игры чёрных за компьютер.
     */
    protected void playBlackFromComp() {
        cMoveGame.getBoard().setPlayerForColor(ETypeColor.BLACK, false);
    }
}
