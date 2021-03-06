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
public class CSMControl implements ICallableStopGame {

    private CSMControl() {
        resourse = CResourse.getInstance();
        resourse.addResourse("Resource/gameres.properties");
        resourse.createMultiImage("Path.Image.Figures", "Info.Image.Figures.Name", "Info.Image.Figures.PlacePosition");
        CFactoryFigure.getInstance().setImageFigure(ETypeFigure.CHECKERS, ETypeColor.WHITE, resourse.getImage("Path.Image.Figure.WC"));
        CFactoryFigure.getInstance().setImageFigure(ETypeFigure.QUINE, ETypeColor.WHITE, resourse.getImage("Path.Image.Figure.WQ"));
        CFactoryFigure.getInstance().setImageFigure(ETypeFigure.CHECKERS, ETypeColor.BLACK, resourse.getImage("Path.Image.Figure.BC"));
        CFactoryFigure.getInstance().setImageFigure(ETypeFigure.QUINE, ETypeColor.BLACK, resourse.getImage("Path.Image.Figure.BQ"));
        cMoveGame = new CControlMoveGame(new CCheckersBoard());
        cMoveGame.getBoard().setCallableStopGame(this);
        saveBoardGame();
        curStateGame = null;
        oldStateGame = null;
        lstChangeState = new ArrayList<>();
        isEdition = false;
        whosPlaying = new HashMap<>();
        for (ETypeColor it: ETypeColor.values()) {
            whosPlaying.put(it, true);
        }
        fileName = null;
//        computerGame = new CComputerGame(cMoveGame);
//        new Thread(computerGame).start();
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
        newStateGame.put(new CPair<>(ETStateGame.GAME, ETActionGame.TOBASEGAMESTOP), ETStateGame.BASE);
        newStateGame.put(new CPair<>(ETStateGame.BASE, ETActionGame.TOCONTINUEGAME), ETStateGame.GAME);
        newStateGame.put(new CPair<>(ETStateGame.GAME, ETActionGame.TOBASEFROMGAMEDRAW), ETStateGame.BASE);
        newStateGame.put(new CPair<>(ETStateGame.GAME, ETActionGame.TOBASEFROMGAMEWHILE), ETStateGame.BASE);
        newStateGame.put(new CPair<>(ETStateGame.GAME, ETActionGame.TOBASEFROMGAMEBLACK), ETStateGame.BASE);
    }
    /**
     * Список изполняемых функций для текущего объекта
     */
    protected static final Map<CPair<ETStateGame, ETActionGame>, String> stateAction;
    static {
        stateAction = new Hashtable<>();
        stateAction.put(new CPair<>(ETStateGame.BASE, ETActionGame.TOEDITING), "initEditingMode");
        stateAction.put(new CPair<>(ETStateGame.EDITING, ETActionGame.TOBASE), "closeEdition");
        stateAction.put(new CPair<>(ETStateGame.BASE, ETActionGame.TOGAME), "launchGameMode");
        stateAction.put(new CPair<>(ETStateGame.NONE, ETActionGame.BACKSPACEMOVEGAME), "backspaceMoveGame");
        stateAction.put(new CPair<>(ETStateGame.BASE, ETActionGame.TOCONTINUEGAME), "continueGameMode");
        stateAction.put(new CPair<>(ETStateGame.EDITING, ETActionGame.SELECTEDSTEPWHITE), "stepToWhite");
        stateAction.put(new CPair<>(ETStateGame.EDITING, ETActionGame.SELECTEDSTEPBLACK), "stepToBlack");
        stateAction.put(new CPair<>(ETStateGame.NONE, ETActionGame.TOSAVE), "saveBoard");
        stateAction.put(new CPair<>(ETStateGame.NONE, ETActionGame.TOLOAD), "loadBoard");
//        stateAction.put(new CPair<>(ETStateGame.NONE, ETActionGame.TOWHITEPLAYER), "playWhiteFromPlayer");
//        stateAction.put(new CPair<>(ETStateGame.NONE, ETActionGame.TOBLACKPLAYER), "playBlackFromPlayer");
//        stateAction.put(new CPair<>(ETStateGame.NONE, ETActionGame.TOWHITECOMP), "playWhiteFromComp");
//        stateAction.put(new CPair<>(ETStateGame.NONE, ETActionGame.TOBLACKCOMP), "playBlackFromComp");
    }

    /**
     * Объект ресурсов.
     */
    private CResourse resourse;
    /**
     * Объект контролирующий ходы и изменения на доске.
     */
    private CControlMoveGame cMoveGame;
    /**
     * Состояния программы.
     */
    private ETStateGame curStateGame, oldStateGame;
    /**
     * Состояние доски до битвы.
     */
    protected CCheckersBoard oldBoard;
    /**
     * Список объектов поддерживающих систему конечных автоматов.
     */
    protected List<IChangeState> lstChangeState;
    /**
     * Режим редактирования.
     */
    protected boolean isEdition;
    /**
     * Определяет, кто ходи за соответствующий цвет. true - игрок, false - компьютер
     */
    protected Map<ETypeColor, Boolean> whosPlaying;
    /**
     * Имя файла для записи ли чтения.
     */
    private String fileName;
//    /**
//     * Объект ходов компьютером.
//     */
//    protected CComputerGame computerGame;

    /**
     * Запуск игры.
     */
    public void start() {
        EventQueue.invokeLater(() -> new JFMainWindow());
    }

    /**
     * Завершение игры.
     * @param state победитель, либо null - ничья
     */
    public void endStateGame(ETypeColor state) {
        if (null == state) makeChangesState(ETActionGame.TOBASEFROMGAMEDRAW, false);
        else if (ETypeColor.WHITE == state) makeChangesState(ETActionGame.TOBASEFROMGAMEWHILE, false);
        else makeChangesState(ETActionGame.TOBASEFROMGAMEBLACK, false);
    }

    /**
     * Возвращает объект контролирующий ходы и изменения на доске.
     * @return объект контролирующий ходы и изменения на доске
     */
    public CControlMoveGame getCMoveGame() {
        return cMoveGame;
    }

    /**
     * Сохраняет копию доски.
     */
    public void saveBoardGame() {
        oldBoard = new CCheckersBoard((CCheckersBoard) cMoveGame.getBoard());
    }

    /**
     * Восстанавливает копию доски.
     */
    public void restoreBoardGame() {
        cMoveGame.setBoard(new CCheckersBoard(oldBoard));
        cMoveGame.getBoard().setCallableStopGame(this);
    }

    /**
     * Получает ссылку на текущий объект игры в шашки.
     * @return текущий объект игры в шашки
     */
    public CCheckersBoard getBoard() {
        return (CCheckersBoard) cMoveGame.getBoard();
    }

    /**
     * Добавление классов обрабаотывающих изменение состояния.
     * @param obj класс обрабатывающий изменение состояния
     */
    public void addActionGame(IChangeState obj) {
        lstChangeState.add(obj);
    }

    /**
     * Возвращает текущий режим игры.
     * @return текущий режим игры, -1 - редактирование, 0 - базовый, 1 - режим игры
     */
    public int getStateGame() {
        if (isEdition) return -1;
        if (cMoveGame.getBoard().getGameOn()) return 1;
        return 0;
    }

    /**
     * Включение режима редактирования.
     */
    public void editModeOn() {
        if (getStateGame() > 0) return;
        isEdition = true;
    }

    /**
     * Выключение режима редактирования.
     */
    public void editModeOff() {
        isEdition = false;
    }

    /**
     * Определяет, кто играет за заданный цвет
     * @param playColor игровой цвет
     * @return игрок играющий за данный цвет true - игрок, false - компьютер
     */
    public boolean getPlayForColor(ETypeColor playColor) {
        return whosPlaying.get(playColor);
    }

    /**
     * Задаёт игрока за выбранный цвет.
     * @param playerForColor цвет за котороый выставляется игрок
     * @param type           игрок: true - обычный игрок, false - компьютер
     */
    public void setPlayerForColor(ETypeColor playerForColor, boolean type) {
        whosPlaying.replace(playerForColor, type);
    }

//    /**
//     * Возвращает объект хода за компьютер.
//     * @return объект хода за компьютер
//     */
//    public CComputerGame getComputerGame() {
//        return computerGame;
//    }

    /**
     * Устанавливает имя файла.
     * @param fileName полное имя файла.
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
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
            saveBoardGame();
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
     * Переход в режим редактирования.
     */
    protected void initEditingMode() {
        editModeOn();
        getCMoveGame().clearControlMove();
    }

    /**
     * Выход из режима редактирования.
     */
    protected void closeEdition() {
        saveBoardGame();
        getCMoveGame().clearControlMove();
    }

    /**
     * Переход в состяния игры.
     */
    protected void launchGameMode() {
        restoreBoardGame();
        cMoveGame.clearControlMove();
        makeChangesState(ETActionGame.TONEXTSTEPGAME, false);
    }

    /**
     * Возврат на шаг в объекте сделанных ходов или изменений на игровом поле.
     */
    protected void backspaceMoveGame() {
        int n = 1;
        if (getStateGame() > 0) {
            ETypeColor tc = cMoveGame.getBoard().getCurrentMove().neg();
            if (!getPlayForColor(tc)) n = 2;
        }
        if (cMoveGame.size() < n) return;
        for (; n > 0; n--) {
            getCMoveGame().back();
        }
        makeChangesState(ETActionGame.TOREPAIN, true);
    }

    /**
     * Продолжение остановленной игры.
     */
    protected void continueGameMode() {
        if (getBoard().getEndState() >= 0) launchGameMode();
        else makeChangesState(ETActionGame.TONEXTSTEPGAME, false);
    }

    /**
     * Выбор хода за белых.
     */
    protected void stepToWhite() {
        getBoard().setMoveWhile();
    }

    /**
     * Выбор хода за чёрных.
     */
    protected void stepToBlack() {
        getBoard().setMoveBlack();
    }

    /**
     * Запись игрового состояния в файл.
     */
    protected void saveBoard() {
        if (null != fileName) {
            int i;
            List<Integer> bin = new LinkedList<>();
            String title = resourse.getResStr("Board.File.Title");
            for (i = 0; i < title.length(); i++) {
                bin.add(title.codePointAt(i));
            }
            bin.addAll(getBoard().getBinaryRepresentationBoard());
            bin.addAll(getBoard().getBinaryParametrsGame());
            bin.addAll(getCMoveGame().getListBin());
            try (OutputStream os = new FileOutputStream(fileName);) {
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
                    } else k++;
                }
                bin.add(byteRead);
                while ((byteRead = is.read()) != -1) {
                    bin.add(byteRead);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            k = getBoard().setBinaryRepresentationBoard(bin, 0);
            k = getBoard().setBinaryParametrsGame(bin, k);
            getCMoveGame().setListBin(bin, k);
            makeChangesState(ETActionGame.TOLOADOK, true);
        }
    }

//    /**
//     * Установка игры белых за игрока.
//     */
//    protected void playWhiteFromPlayer() {
//        setPlayerForColor(ETypeColor.WHITE, true);
//    }

//    /**
//     * Установка игры белых за компьютер.
//     */
//    protected void playWhiteFromComp() {
//        setPlayerForColor(ETypeColor.WHITE, false);
//    }

//    /**
//     * Установка игры чёрных за игрока.
//     */
//    protected void playBlackFromPlayer() {
//        setPlayerForColor(ETypeColor.BLACK, true);
//    }

//    /**
//     * Установка игры чёрных за компьютер.
//     */
//    protected void playBlackFromComp() {
//        setPlayerForColor(ETypeColor.BLACK, false);
//    }
}
