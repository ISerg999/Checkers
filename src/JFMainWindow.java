import CheckersEngine.BaseEngine.CPair;
import CheckersEngine.BaseEngine.ETypeColor;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.Map;

public class JFMainWindow extends JFrame implements IChangeState {

    /**
     * Список изполняемых функций для текущего объекта
     */
    private static final Map<CPair<ETStateGame, ETActionGame>, String> stateAction;
    static {
        stateAction = new Hashtable<>();
        stateAction.put(new CPair<>(ETStateGame.NONE, ETActionGame.TOABOUT), "viewDialogAbout");
        stateAction.put(new CPair<>(ETStateGame.BASE, ETActionGame.TOEDITING), "initEditingMode");
        stateAction.put(new CPair<>(ETStateGame.EDITING, ETActionGame.TOBOARDPLACEMANT), "placemantBoard");
        stateAction.put(new CPair<>(ETStateGame.EDITING, ETActionGame.TOBOARDCLEAR), "clearBoard");
        stateAction.put(new CPair<>(ETStateGame.EDITING, ETActionGame.TOBASE), "closeEdition");
        stateAction.put(new CPair<>(ETStateGame.BASE, ETActionGame.TOGAME), "initGameMode");
        stateAction.put(new CPair<>(ETStateGame.GAME, ETActionGame.TONEXTSTEPGAMEWIN), "nextStepGame");
        stateAction.put(new CPair<>(ETStateGame.NONE, ETActionGame.TOREPAIN), "repainWindow");
        stateAction.put(new CPair<>(ETStateGame.GAME, ETActionGame.TOBASEGAMESTOP), "endGameFromStop");
        stateAction.put(new CPair<>(ETStateGame.BASE, ETActionGame.TOCONTINUEGAME), "initGameMode");
        stateAction.put(new CPair<>(ETStateGame.GAME, ETActionGame.TOBASEFROMGAMEDRAW), "endGameFromDraw");
        stateAction.put(new CPair<>(ETStateGame.GAME, ETActionGame.TOBASEFROMGAMEWHILE), "endGameFromWinWhile");
        stateAction.put(new CPair<>(ETStateGame.GAME, ETActionGame.TOBASEFROMGAMEBLACK), "endGameFromWinBlack");
        stateAction.put(new CPair<>(ETStateGame.EDITING, ETActionGame.SELECTEDSTEPWHITE), "stepToWhite");
        stateAction.put(new CPair<>(ETStateGame.EDITING, ETActionGame.SELECTEDSTEPBLACK), "stepToBlack");
//        stateAction.put(new CPair<>(ETStateGame.NONE, ETActionGame.TOSAVE), "viewDialogSave");
//        stateAction.put(new CPair<>(ETStateGame.NONE, ETActionGame.TOLOAD), "viewDialogLoad");
//        stateAction.put(new CPair<>(ETStateGame.NONE, ETActionGame.TOSAVEOK), "viewSaveFileOK");
//        stateAction.put(new CPair<>(ETStateGame.NONE, ETActionGame.TOLOADOK), "viewLoadFileOk");
//        stateAction.put(new CPair<>(ETStateGame.NONE, ETActionGame.TOWHITEPLAYER), "playWhiteFromPlayer");
//        stateAction.put(new CPair<>(ETStateGame.NONE, ETActionGame.TOBLACKPLAYER), "playBlackFromPlayer");
//        stateAction.put(new CPair<>(ETStateGame.NONE, ETActionGame.TOWHITECOMP), "playWhiteFromComp");
//        stateAction.put(new CPair<>(ETStateGame.NONE, ETActionGame.TOBLACKCOMP), "playBlackFromComp");
    }

    /**
     * Доступ к классу ресурсов.
     */
    private CResourse resourse;
    /**
     * Доступ к классу контроллера.
     */
    protected CSMControl csmControl;
    /**
     * Список выбираемых элементов меню, состояние которых может изменяться.
     */
    protected Map<String, JMenuItem> mActionMenu;
    /**
     * Нижняя строка информации.
     */
    protected JLabel lblBottom;
    /**
     * Панель изображения и управления игровой доской и фигур на ней.
     */
    protected CViewBoard viewBoard;
    /**
     * Вспомогательная панель.
     */
    CSwitchingPanel rightSwitchingPanel;
    /**
     * Правая панель редактирования.
     */
    CRightPanelEdition rPanelEdition;
    /**
     * Правая игровая панель
     */
    CRightPanelGaming rPanelGaming;
    /**
     * Позволяет узнать игра только началась, или она продолжается.
     */
    boolean isStart;

    public JFMainWindow() throws HeadlessException {
        resourse = CResourse.getInstance();
        csmControl = CSMControl.getInstance();
        csmControl.addActionGame(this);
        mActionMenu = new Hashtable<>();
        createAndShowGUI();
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                keyAction(e);
            }
        });
        isStart = true;
        csmControl.makeChangesState(ETActionGame.TOBASE, false);
//        csmControl.makeChangesState(ETActionGame.TOWHITEPLAYER, true);
//        csmControl.makeChangesState(ETActionGame.TOBLACKPLAYER, true);
    }

    /**
     * Установка содрежримого нижнего текстового поля.
     * @param txtMsgDown текстовое содержимое
     */
    public void setTxtMsgDown(String txtMsgDown) {
        lblBottom.setText(txtMsgDown);
    }

    @Override
    public void makeChangesState(CPair<ETStateGame, ETActionGame> pStM) {
        if (pStM.getFirst() == null) {
            stepToBase();
        } else {
            String funName = stateAction.getOrDefault(pStM, null);
            if (null != funName) {
                try {
                    Method method = this.getClass().getDeclaredMethod(funName);
                    method.setAccessible(true);
                    method.invoke(this);
                } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Создание интерфейса окна.
     */
    protected void createAndShowGUI() {
        // -------- Базовая настройка. --------

//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        ImageIcon icon = resourse.getImage("Path.Icon.Window");
        setIconImage(icon.getImage());
        int curWidth = resourse.getResInt("Window.Width");
        int curHeight = resourse.getResInt("Window.Height");
        setResizable(false);
        setTitle(resourse.getResStr("Window.Title"));
        setMinimumSize(new Dimension(curWidth, curHeight));
        setMinimumSize(new Dimension(curWidth, curHeight));
        setFont(new Font("Arial", Font.PLAIN, 10));
        setVisible(true);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitWindow();
            }
        });

        // -------- Создание меню. --------
        JMenuBar mb = new JMenuBar();
        mb.add(createMenuFile());
        mb.add(createMenuGame());
        mb.add(createMenuSettings());
        mb.add(createMenuEditing());
        mb.add(createMenuInfo());
        mb.setBounds(1, 1, curWidth, 24);
        setJMenuBar(mb);
        mb.repaint();

        // -------- Создание игровой доски. --------
        viewBoard = new CViewBoard();
        add(viewBoard);

        // -------- Создание правой информационной панели. --------
        createRightPanel();

        // -------- Создание нижней информационной строки. --------
        createLabelBottom();
        setTxtMsgDown(resourse.getResStr("Msg.Base.Info"));
    }

    /**
     * Создание элементов меню: Файл.
     * @return меню Файл
     */
    protected JMenu createMenuFile() {
        JMenu mFile = new JMenu(resourse.getResStr("MenuName.File"));

        JMenuItem miOpen = new JMenuItem(resourse.getResStr("MenuName.File.Open"));
        miOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
//        miOpen.addActionListener(actionEvent -> csmControl.makeChangesState(ETActionGame.TOLOAD, true));
        mFile.add(miOpen);
        mActionMenu.put("MenuName.File.Open", miOpen);

        JMenuItem miSave = new JMenuItem(resourse.getResStr("MenuName.File.Save"));
        miSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
//        miSave.addActionListener(actionEvent -> csmControl.makeChangesState(ETActionGame.TOSAVE, true));
        mFile.add(miSave);
        mActionMenu.put("MenuName.File.Save", miSave);

        mFile.addSeparator();

        JMenuItem miExit = new JMenuItem(resourse.getResStr("MenuName.File.Exit"));
        miExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.ALT_MASK + ActionEvent.CTRL_MASK));
        miExit.addActionListener(actionEvent -> exitWindow());
        mFile.add(miExit);

        return mFile;
    }

    /**
     * Создание элементов меню: Игра
     * @return меню Игра
     */
    protected JMenu createMenuGame() {
        JMenu mGame = new JMenu(resourse.getResStr("MenuName.Game"));

        JMenuItem miStart = new JMenuItem(resourse.getResStr("MenuName.Game.Start"));
        miStart.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        miStart.addActionListener(actionEvent -> csmControl.makeChangesState(ETActionGame.TOGAME, false));
        mGame.add(miStart);
        mActionMenu.put("MenuName.Game.Start", miStart);

        JMenuItem miContinue = new JMenuItem(resourse.getResStr("MenuName.Game.Continue"));
        miContinue.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        miContinue.addActionListener(actionEvent -> csmControl.makeChangesState(ETActionGame.TOCONTINUEGAME, false));
        mGame.add(miContinue);
        mActionMenu.put("MenuName.Game.Continue", miContinue);

        JMenuItem miStop = new JMenuItem(resourse.getResStr("MenuName.Game.Stop"));
        miStop.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
        miStop.addActionListener(actionEvent -> csmControl.makeChangesState(ETActionGame.TOBASEGAMESTOP, false));
        mGame.add(miStop);
        mActionMenu.put("MenuName.Game.Stop", miStop);

        return mGame;
    }

    /**
     * Создание элементов меню: Настройки.
     * @return меню Настройки
     */
    protected JMenu createMenuSettings() {
        JMenu mSettings = new JMenu(resourse.getResStr("MenuName.Settings"));

        JMenuItem miWhitePlayer = new JMenuItem(resourse.getResStr("MenuName.Settings.While.Player"));
        miWhitePlayer.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, ActionEvent.ALT_MASK + ActionEvent.CTRL_MASK));
//        miWhitePlayer.addActionListener(actionEvent -> csmControl.makeChangesState(ETActionGame.TOWHITEPLAYER, true));
        mSettings.add(miWhitePlayer);
        mActionMenu.put("MenuName.Settings.While.Player", miWhitePlayer);

        JMenuItem miWhiteComp = new JMenuItem(resourse.getResStr("MenuName.Settings.While.Comp"));
        miWhiteComp.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, ActionEvent.ALT_MASK + ActionEvent.CTRL_MASK));
//        miWhiteComp.addActionListener(actionEvent -> csmControl.makeChangesState(ETActionGame.TOWHITECOMP, true));
        mSettings.add(miWhiteComp);
        mActionMenu.put("MenuName.Settings.While.Comp", miWhiteComp);

        mSettings.addSeparator();

        JMenuItem miBlackPlayer = new JMenuItem(resourse.getResStr("MenuName.Settings.Black.Player"));
        miBlackPlayer.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_J, ActionEvent.ALT_MASK + ActionEvent.CTRL_MASK));
//        miBlackPlayer.addActionListener(actionEvent -> csmControl.makeChangesState(ETActionGame.TOBLACKPLAYER, true));
        mSettings.add(miBlackPlayer);
        mActionMenu.put("MenuName.Settings.Black.Player", miBlackPlayer);

        JMenuItem miBlackComp = new JMenuItem(resourse.getResStr("MenuName.Settings.Black.Comp"));
        miBlackComp.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_K, ActionEvent.ALT_MASK + ActionEvent.CTRL_MASK));
//        mBlackComp.addActionListener(actionEvent -> csmControl.makeChangesState(ETActionGame.TOBLACKCOMP, true));
        mSettings.add(miBlackComp);
        mActionMenu.put("MenuName.Settings.Black.Comp", miBlackComp);

        mSettings.addSeparator();

        JMenuItem miBack = new JMenuItem(resourse.getResStr("MenuName.Settings.Back"));
        miBack.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0));
        miBack.addActionListener(actionEvent -> csmControl.makeChangesState(ETActionGame.BACKSPACEMOVEGAME, true));
        mSettings.add(miBack);
        mActionMenu.put("MenuName.Settings.Back", miBack);

        return mSettings;
    }

    /**
     * Создание элементов меню Редактирование.
     * @return меню редактирование
     */
    protected JMenu createMenuEditing() {
        JMenu mEditing = new JMenu(resourse.getResStr("MenuName.Editing"));

        JMenuItem miBeginEdit = new JMenuItem(resourse.getResStr("MenuName.Editing.Begin"));
        miBeginEdit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, ActionEvent.ALT_MASK + ActionEvent.CTRL_MASK));
        miBeginEdit.addActionListener(actionEvent -> csmControl.makeChangesState(ETActionGame.TOEDITING, false));
        mEditing.add(miBeginEdit);
        mActionMenu.put("MenuName.Editing.Begin", miBeginEdit);

        JMenuItem miEndEdit = new JMenuItem(resourse.getResStr("MenuName.Editing.End"));
        miEndEdit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.ALT_MASK + ActionEvent.CTRL_MASK));
        miEndEdit.addActionListener(actionEvent -> csmControl.makeChangesState(ETActionGame.TOBASE, false));
        mEditing.add(miEndEdit);
        mActionMenu.put("MenuName.Editing.End", miEndEdit);

        mEditing.addSeparator();

        JMenuItem miPlacemantBoard = new JMenuItem(resourse.getResStr("MenuName.Editing.Placemant"));
        miPlacemantBoard.addActionListener(actionEvent -> csmControl.makeChangesState(ETActionGame.TOBOARDPLACEMANT, false));
        mEditing.add(miPlacemantBoard);
        mActionMenu.put("MenuName.Editing.Placemant", miPlacemantBoard);

        JMenuItem miClearBoard = new JMenuItem(resourse.getResStr("MenuName.Editing.Clear"));
        miClearBoard.addActionListener(actionEvent -> csmControl.makeChangesState(ETActionGame.TOBOARDCLEAR, false));
        mEditing.add(miClearBoard);
        mActionMenu.put("MenuName.Editing.Clear", miClearBoard);

        mEditing.addSeparator();

        JMenuItem miStepToWhite = new JMenuItem(resourse.getResStr("MenuName.Editing.StepWhite"));
        miStepToWhite.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, ActionEvent.CTRL_MASK + ActionEvent.SHIFT_MASK));
        miStepToWhite.addActionListener(actionEvent -> csmControl.makeChangesState(ETActionGame.SELECTEDSTEPWHITE, false));
        mEditing.add(miStepToWhite);
        mActionMenu.put("MenuName.Editing.StepWhite", miStepToWhite);

        JMenuItem miStepToBlack = new JMenuItem(resourse.getResStr("MenuName.Editing.StepBlack"));
        miStepToBlack.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, ActionEvent.CTRL_MASK + ActionEvent.SHIFT_MASK));
        miStepToBlack.addActionListener(actionEvent -> csmControl.makeChangesState(ETActionGame.SELECTEDSTEPBLACK, false));
        mEditing.add(miStepToBlack);
        mActionMenu.put("MenuName.Editing.StepBlack", miStepToBlack);

        return mEditing;
    }

    /**
     * Создание элементов меню: Информация.
     * @return меню Информация
     */
    protected JMenu createMenuInfo() {
        JMenu mInfo = new JMenu(resourse.getResStr("MenuName.Info"));

        JMenuItem miInfoAbout = new JMenuItem(resourse.getResStr("MenuName.Info.About"));
        miInfoAbout.addActionListener(actionEvent -> csmControl.makeChangesState(ETActionGame.TOABOUT, true));
        mInfo.add(miInfoAbout);

        return mInfo;
    }

    /**
     * Создание правых панелей.
     */
    protected void createRightPanel() {
        rightSwitchingPanel = new CSwitchingPanel(viewBoard.getWidth() + 1, 0);
        add(rightSwitchingPanel);

        // Создание панели для режима редактирования.
        rPanelEdition = new CRightPanelEdition();
        rightSwitchingPanel.append(rPanelEdition);
        viewBoard.setPanelEdition(rPanelEdition);

        // Создание панели для режима игры.
        rPanelGaming = new CRightPanelGaming();
        rightSwitchingPanel.append(rPanelGaming);
    }

    /**
     * Нижняя информационная строка.
     */
    protected void createLabelBottom() {
        lblBottom = new JLabel(resourse.getResStr("Msg.Base.Info"));
        lblBottom.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED),
                BorderFactory.createEmptyBorder(25, 25, 25, 25)));
        lblBottom.setBounds(1, viewBoard.getHeight() + 1, resourse.getResInt("Window.Width") - 4, 24);
        add(lblBottom);
    }

    protected void exitWindow() {
//        csmControl.getComputerGame().close();
        System.exit(0);

    }

    /**
     * Обработка событий нажатий на клавиатуре.
     * @param e событие клавиатуры
     */
    protected void keyAction(KeyEvent e) {
        int state = csmControl.getStateGame();
        if (state < 0) {
            rPanelEdition.keyAction(e);
            viewBoard.keyActionEdition(e);
        }
    }

    /**
     * Включение и выключение элементов меню. Обобщённый метод.
     */
    protected void selectedViewMenu(String[] disablMenuItems) {
        for (JMenuItem mi: mActionMenu.values()) {
            mi.setEnabled(true);
        }
        if (ETypeColor.WHITE == csmControl.getBoard().getCurrentMove()) mActionMenu.get("MenuName.Editing.StepWhite").setEnabled(false);
        else mActionMenu.get("MenuName.Editing.StepBlack").setEnabled(false);
        for (String nm: disablMenuItems) mActionMenu.get(nm).setEnabled(false);
    }

    /**
     * Вывод диалоговых окон с простыми сообщениями. Обобщённый метод.
     * @param title заголовок
     * @param text  содержимое
     */
    protected void viewDialog(String title, String text) {
        JOptionPane.showMessageDialog(null, text, title, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Вывод информации о текущем ходе.
     */
    protected void viewInfoCurrentPlayer() {
        ETypeColor tc = csmControl.getBoard().getCurrentMove();
        String txt = "" + (tc == ETypeColor.WHITE ?  resourse.getResStr("Msg.Game.Play.White"): resourse.getResStr("Msg.Game.Play.Black"));
        txt = txt + " " + (csmControl.getPlayForColor(tc) ?  resourse.getResStr("Msg.Game.Play.Player"): resourse.getResStr("Msg.Game.Play.Computer"));
        setTxtMsgDown(txt);
    }

    /**
     * Общий метод для всех видов остановок игры.
     */
    protected void stopGameAll() {
        viewBoard.clearBoardSpacesColor();
        viewBoard.repaint();
        stepToBase();
    }

    // ---------------------------- Методы обрабатывающиеся контроллером переходов состояний ---------------------------

    /**
     * Действия, необходимые для перехода в состояние базового для основного окна.
     */
    protected void stepToBase() {
        String[] deactivate = {
                "MenuName.Game.Stop", "MenuName.Settings.Back", "MenuName.Editing.End", "MenuName.Editing.Placemant",
                "MenuName.Editing.Clear", "MenuName.Editing.StepWhite", "MenuName.Editing.StepBlack",
        };
        selectedViewMenu(deactivate);
        if (isStart) {
            mActionMenu.get("MenuName.Game.Continue").setEnabled(false);
            isStart = false;
        }
        csmControl.editModeOff();
        rightSwitchingPanel.setSelectedIndex(-1);
        setTxtMsgDown(resourse.getResStr("Msg.Base.Info"));
        repaint();
    }

    /**
     * Вывод диалогового окна: О программе.
     */
    protected void viewDialogAbout() {
        viewDialog(resourse.getResStr("MenuName.Info.About"), resourse.getResStr("Mag.Base.DlgAbout.Info"));
    }

    /**
     * Инициализация при переходе в режим редактирования.
     */
    protected void initEditingMode() {
        String[] deactivate = {
                "MenuName.File.Save", "MenuName.File.Open", "MenuName.Game.Start", "MenuName.Game.Continue", "MenuName.Game.Stop",
                "MenuName.Settings.While.Player", "MenuName.Settings.While.Comp", "MenuName.Settings.Black.Player",
                "MenuName.Settings.Black.Comp", "MenuName.Editing.Begin"
        };
        selectedViewMenu(deactivate);
        setTxtMsgDown(resourse.getResStr("Msg.Editing.Info"));
        rightSwitchingPanel.setSelectedIndex(0);
    }

    /**
     * Базовая разстановка фигур на доске.
     */
    protected void placemantBoard() {
        csmControl.getCMoveGame().placementBoard();
        viewBoard.repaint();
    }

    /**
     * Очистка доски.
     */
    protected void clearBoard() {
        csmControl.getCMoveGame().clearBoard();
        viewBoard.repaint();
    }

    /**
     * Выход из режима редактирования.
     */
    protected void closeEdition() {
        stepToBase();
        setTxtMsgDown(resourse.getResStr("Msg.Base.Info"));
    }

    /**
     * Переход в режим игры.
     */
    protected void initGameMode() {
        String[] deactivate = {
                "MenuName.File.Open", "MenuName.Game.Start", "MenuName.Game.Continue", "MenuName.Settings.While.Player",
                "MenuName.Settings.While.Comp", "MenuName.Settings.Black.Player", "MenuName.Settings.Black.Comp", "MenuName.Editing.Begin",
                "MenuName.Editing.End", "MenuName.Editing.Placemant", "MenuName.Editing.Clear", "MenuName.Editing.StepWhite", "MenuName.Editing.StepBlack",
        };
        selectedViewMenu(deactivate);
        viewInfoCurrentPlayer();
        rightSwitchingPanel.setSelectedIndex(1);
    }

    /**
     * Переход к следующему ходу.
     */
    protected void nextStepGame() {
        viewInfoCurrentPlayer();
        repaint();
    }

    /**
     * Перерисовка изображения доски.
     */
    protected void repainWindow() {
        viewInfoCurrentPlayer();
        repaint();
        if (csmControl.getStateGame() > 0) {
            viewBoard.nextStepGame();
            rPanelGaming.repaint();
        }
    }

    /**
     * Остановка игры по требованию игрока.
     */
    protected void endGameFromStop() {
        viewDialog(resourse.getResStr("Msg.GameOver.Title"), resourse.getResStr("Msg.GameOver.FromPlayer"));
        csmControl.getBoard().stopGamePlay();
        stopGameAll();
    }

    /**
     * Остановка игры в результате ничьей.
     */
    protected void endGameFromDraw() {
        viewDialog(resourse.getResStr("Msg.GameOver.Title"), resourse.getResStr("Msg.GameOver.Draw"));
        stopGameAll();
    }

    /**
     * Остановка игры при выйгрыше белых.
     */
    protected void endGameFromWinWhile() {
        viewDialog(resourse.getResStr("Msg.GameOver.Title"), resourse.getResStr("Msg.GameOver.While"));
        stopGameAll();
    }

    /**
     * Остановка игры при выйгрыше чёрных.
     */
    protected void endGameFromWinBlack() {
        viewDialog(resourse.getResStr("Msg.GameOver.Title"), resourse.getResStr("Msg.GameOver.Black"));
        stopGameAll();
    }

    /**
     * Выбор хода за белых.
     */
    protected void stepToWhite() {
        mActionMenu.get("MenuName.Editing.StepWhite").setEnabled(false);
        mActionMenu.get("MenuName.Editing.StepBlack").setEnabled(true);
    }

    /**
     * Выбор хода за чёрных.
     */
    protected void stepToBlack() {
        mActionMenu.get("MenuName.Editing.StepWhite").setEnabled(true);
        mActionMenu.get("MenuName.Editing.StepBlack").setEnabled(false);
    }

//    /**
//     * Диалоговое окно сохранения игры.
//     */
//    protected void viewDialogSave() {
//        String fileName = null;
//        JFileChooser fileChooser = new JFileChooser();
//        fileChooser.setDialogTitle("Сохранение игрового файла");
//        FileNameExtensionFilter filter = new FileNameExtensionFilter(resourse.getResStr("File.Filter.Description"),
//                resourse.getResStr("File.Filter.Extension"));
//        fileChooser.setFileFilter(filter);
//        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
//        int result = fileChooser.showSaveDialog(JFMainWindow.this);
//        if (result == JFileChooser.APPROVE_OPTION) fileName = fileChooser.getSelectedFile().getAbsolutePath();
//        csmControl.setFileName(fileName);
//        csmControl.makeChangesState(ETActionGame.TORETURN, true);
//    }

//    /**
//     * Диалоговое окно загрузки игры.
//     */
//    protected void viewDialogLoad() {
//        String fileName = null;
//        JFileChooser fileChooser = new JFileChooser();
//        fileChooser.setDialogTitle("Загрузка игрового файла");
//        FileNameExtensionFilter filter = new FileNameExtensionFilter(resourse.getResStr("File.Filter.Description"),
//                resourse.getResStr("File.Filter.Extension"));
//        fileChooser.setFileFilter(filter);
//        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
//        int result = fileChooser.showOpenDialog(JFMainWindow.this);
//        if (result == JFileChooser.APPROVE_OPTION) fileName = fileChooser.getSelectedFile().getAbsolutePath();
//        csmControl.setFileName(fileName);
//        csmControl.makeChangesState(ETActionGame.TORETURN, true);
//    }

//    /**
//     * Диалоговое окно, сообщения о том, что загрузка закончилась.
//     */
//    protected void viewLoadFileOk() {
//        viewBoard.repaint();
//        viewDialog("", resourse.getResStr("Msg.Base.DlgOK.Load"));
//    }

//    /**
//     * Диалоговое окно, сообщения о том, что сохранение закончилось.
//     */
//    protected void viewSaveFileOK() {
//        viewBoard.repaint();
//        viewDialog("", resourse.getResStr("Msg.Base.DlgOK.Save"));
//    }

//    /**
//     * Установка игры белых за игрока.
//     */
//    protected void playWhiteFromPlayer() {
//        mActionMenu.get("MenuName.Settings.While.Player").setEnabled(false);
//        mActionMenu.get("MenuName.Settings.While.Comp").setEnabled(true);
//    }

//    /**
//     * Установка игры белых за компьютер.
//     */
//    protected void playWhiteFromComp() {
//        mActionMenu.get("MenuName.Settings.While.Player").setEnabled(true);
//        mActionMenu.get("MenuName.Settings.While.Comp").setEnabled(false);
//    }

//    /**
//     * Установка игры чёрных за игрока.
//     */
//    protected void playBlackFromPlayer() {
//        mActionMenu.get("MenuName.Settings.Black.Player").setEnabled(false);
//        mActionMenu.get("MenuName.Settings.Black.Comp").setEnabled(true);
//    }

//    /**
//     * Установка игры чёрных за компьютер.
//     */
//    protected void playBlackFromComp() {
//        mActionMenu.get("MenuName.Settings.Black.Player").setEnabled(true);
//        mActionMenu.get("MenuName.Settings.Black.Comp").setEnabled(false);
//    }

}
