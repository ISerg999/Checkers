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
        stateAction.put(new CPair<>(ETStateGame.NONE, ETActionGame.TOSAVE), "viewDialogSave");
        stateAction.put(new CPair<>(ETStateGame.NONE, ETActionGame.TOLOAD), "viewDialogLoad");
        stateAction.put(new CPair<>(ETStateGame.NONE, ETActionGame.TOSAVEOK), "viewSaveFileOK");
        stateAction.put(new CPair<>(ETStateGame.NONE, ETActionGame.TOLOADOK), "viewLoadFileOk");
        stateAction.put(new CPair<>(ETStateGame.BASE, ETActionGame.TOEDITING), "initEditingMode");
        stateAction.put(new CPair<>(ETStateGame.EDITING, ETActionGame.TOBOARDPLACEMANT), "placemantBoard");
        stateAction.put(new CPair<>(ETStateGame.EDITING, ETActionGame.TOBOARDCLEAR), "clearBoard");
        stateAction.put(new CPair<>(ETStateGame.EDITING, ETActionGame.TOBASE), "stepToBaseFromEdition");
        stateAction.put(new CPair<>(ETStateGame.NONE, ETActionGame.TOWHITEPLAYER), "playWhiteFromPlayer");
        stateAction.put(new CPair<>(ETStateGame.NONE, ETActionGame.TOBLACKPLAYER), "playBlackFromPlayer");
        stateAction.put(new CPair<>(ETStateGame.NONE, ETActionGame.TOWHITECOMP), "playWhiteFromComp");
        stateAction.put(new CPair<>(ETStateGame.NONE, ETActionGame.TOBLACKCOMP), "playBlackFromComp");
        stateAction.put(new CPair<>(ETStateGame.BASE, ETActionGame.TOGAME), "initGameMode");
        stateAction.put(new CPair<>(ETStateGame.GAME, ETActionGame.TONEXTSTEPGAMEWIN), "nextStepGame");
        stateAction.put(new CPair<>(ETStateGame.GAME, ETActionGame.TOBASEFROMGAMEDRAW), "endGameFromDraw");
        stateAction.put(new CPair<>(ETStateGame.GAME, ETActionGame.TOBASEFROMGAMEWHILE), "endGameFromWinWhile");
        stateAction.put(new CPair<>(ETStateGame.GAME, ETActionGame.TOBASEFROMGAMEBLACK), "endGameFromWinBlack");
        stateAction.put(new CPair<>(ETStateGame.GAME, ETActionGame.TOBASEGAMESTOP), "endGameFromStop");

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
     * Панель изображения и управления игровой доской и фигур на ней.
     */
    protected CViewBoard viewBoard;
    /**
     * Нижняя строка информации.
     */
    protected JLabel lblBottom;
    /**
     * Вспомогательная панель.
     */
    CSwitchingPanel rightPanel;
    /**
     * Правая панель редактирования.
     */
    CRightPanelEdition rPanelEdition;
    /**
     * Правая игровая панель
     */
    CRightPanelGaming rPanelGaming;

    /**
     * Текст выводимый внизу.
     */
    String txtMsgDown;

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

        csmControl.makeChangesState(ETActionGame.TOBASE, false);
        csmControl.makeChangesState(ETActionGame.TOWHITEPLAYER, true);
        csmControl.makeChangesState(ETActionGame.TOBLACKPLAYER, true);
    }

    /**
     * Создание интерфейса окна.
     */
    private void createAndShowGUI() {
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
    private JMenu createMenuFile() {
        JMenu mFile = new JMenu(resourse.getResStr("MenuName.File"));

        JMenuItem miOpen = new JMenuItem(resourse.getResStr("MenuName.File.Open"));
        miOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        miOpen.addActionListener(actionEvent -> csmControl.makeChangesState(ETActionGame.TOLOAD, true));
        mFile.add(miOpen);
        mActionMenu.put("MenuName.File.Open", miOpen);

        JMenuItem miSave = new JMenuItem(resourse.getResStr("MenuName.File.Save"));
        miSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        miSave.addActionListener(actionEvent -> csmControl.makeChangesState(ETActionGame.TOSAVE, true));
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
    private JMenu createMenuGame() {
        JMenu mGame = new JMenu(resourse.getResStr("MenuName.Game"));
        JMenuItem miStart = new JMenuItem(resourse.getResStr("MenuName.Game.Start"));
        miStart.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        miStart.addActionListener(actionEvent -> csmControl.makeChangesState(ETActionGame.TOGAME, false));
        mGame.add(miStart);
        mActionMenu.put("MenuName.Game.Start", miStart);

        JMenuItem miContinue = new JMenuItem(resourse.getResStr("MenuName.Game.Continue"));
        miContinue.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        // miContinue.addActionListener(this);
        mGame.add(miContinue);
        mActionMenu.put("MenuName.Game.Continue", miContinue);

        JMenuItem miStop = new JMenuItem(resourse.getResStr("MenuName.Game.Stop"));
        miStop.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
        miStop.addActionListener(actionEvent -> csmControl.makeChangesState(ETActionGame.TOBASEGAMESTOP, false));
        mGame.add(miStop);
        mActionMenu.put("MenuName.Game.Stop", miStop);

        mGame.addSeparator();

        JMenuItem miBack = new JMenuItem(resourse.getResStr("MenuName.Game.Back"));
        miBack.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, ActionEvent.CTRL_MASK));
        // miBack.addActionListener(this);
        mGame.add(miBack);
        mActionMenu.put("MenuName.Game.Back", miBack);

        return mGame;
    }

    /**
     * Создание элементов меню: Настройки.
     * @return меню Настройки
     */
    private JMenu createMenuSettings() {
        JMenu mSettings = new JMenu(resourse.getResStr("MenuName.Settings"));

        JMenuItem miWhitePlayer = new JMenuItem(resourse.getResStr("MenuName.Settings.While.Player"));
        miWhitePlayer.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, ActionEvent.ALT_MASK + ActionEvent.CTRL_MASK));
        miWhitePlayer.addActionListener(actionEvent -> csmControl.makeChangesState(ETActionGame.TOWHITEPLAYER, true));
        mSettings.add(miWhitePlayer);
        mActionMenu.put("MenuName.Settings.While.Player", miWhitePlayer);

        JMenuItem miWhiteComp = new JMenuItem(resourse.getResStr("MenuName.Settings.While.Comp"));
        miWhiteComp.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, ActionEvent.ALT_MASK + ActionEvent.CTRL_MASK));
        miWhiteComp.addActionListener(actionEvent -> csmControl.makeChangesState(ETActionGame.TOWHITECOMP, true));
        mSettings.add(miWhiteComp);
        mActionMenu.put("MenuName.Settings.While.Comp", miWhiteComp);

        mSettings.addSeparator();

        JMenuItem miBlackPlayer = new JMenuItem(resourse.getResStr("MenuName.Settings.Black.Player"));
        miBlackPlayer.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_J, ActionEvent.ALT_MASK + ActionEvent.CTRL_MASK));
        miBlackPlayer.addActionListener(actionEvent -> csmControl.makeChangesState(ETActionGame.TOBLACKPLAYER, true));
        mSettings.add(miBlackPlayer);
        mActionMenu.put("MenuName.Settings.Black.Player", miBlackPlayer);

        JMenuItem mBlackComp = new JMenuItem(resourse.getResStr("MenuName.Settings.Black.Comp"));
        mBlackComp.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_K, ActionEvent.ALT_MASK + ActionEvent.CTRL_MASK));
        mBlackComp.addActionListener(actionEvent -> csmControl.makeChangesState(ETActionGame.TOBLACKCOMP, true));
        mSettings.add(mBlackComp);
        mActionMenu.put("MenuName.Settings.Black.Comp", mBlackComp);

        return mSettings;
    }

    /**
     * Создание элементов меню Редактирование.
     * @return меню редактирование
     */
    private JMenu createMenuEditing() {
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

        return mEditing;
    }

    /**
     * Создание элементов меню: Информация.
     * @return меню Информация
     */
    private JMenu createMenuInfo() {
        JMenu mInfo = new JMenu(resourse.getResStr("MenuName.Info"));

        JMenuItem miInfoAbout = new JMenuItem(resourse.getResStr("MenuName.Info.About"));
        miInfoAbout.addActionListener(actionEvent -> csmControl.makeChangesState(ETActionGame.TOABOUT, true));
        mInfo.add(miInfoAbout);

        return mInfo;
    }

    /**
     * Создание правых панелей.
     */
    private void createRightPanel() {
        rightPanel = new CSwitchingPanel(viewBoard.getWidth() + 1, 0);
        add(rightPanel);

        // Создание панели для режима редактирования.
        rPanelEdition = new CRightPanelEdition();
        rightPanel.append(rPanelEdition);
        viewBoard.setPanelEdition(rPanelEdition);

        // Создание панели для режима игры.
        rPanelGaming = new CRightPanelGaming();
        rightPanel.append(rPanelGaming);
    }

    /**
     * Нижняя информационная строка.
     */
    private void createLabelBottom() {
        lblBottom = new JLabel(resourse.getResStr("Msg.Base.Info"));
        lblBottom.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED),
                BorderFactory.createEmptyBorder(25, 25, 25, 25)));
        lblBottom.setBounds(1, viewBoard.getHeight() + 1, resourse.getResInt("Window.Width") - 4, 24);
        add(lblBottom);
    }

    /**
     * Установка содрежримого нижнего текстового поля.
     * @param txtMsgDown текстовое содержимое
     */
    public void setTxtMsgDown(String txtMsgDown) {
        this.txtMsgDown = txtMsgDown;
    }

    public void exitWindow() {
        csmControl.getComputerGame().close();
        System.exit(0);

    }

    /**
     * Обработка событий нажатий на клавиатуре.
     * @param e событие клавиатуры
     */
    protected void keyAction(KeyEvent e) {
        if (csmControl.getIsEdition()) {
            rPanelEdition.keyAction(e);
            viewBoard.keyActionEdition(e);
        } else if (csmControl.getBoard().getStateGame()) {
            // TODO: Обработка нажатий клавиатуры для режима игры.
        }
    }

    @Override
    public void makeChangesState(CPair<ETStateGame, ETActionGame> pStM) {
        if (pStM.getFirst() == null && pStM.getSecond() == ETActionGame.TOBASE) {
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

    // ---------------------------- Методы обрабатывающиеся контроллером переходов состояний ---------------------------

    /**
     * Включение и выключение элементов меню. Обобщённый метод.
     */
    protected void selectedViewMenu(String[] disablMenuItems) {
        for (JMenuItem mi: mActionMenu.values()) {
            mi.setEnabled(true);
        }
        for (String nm: disablMenuItems) mActionMenu.get(nm).setEnabled(false);
    }

    /**
     * Вывод диалоговых окон с простыми сообщениями. Обобщённый метод.
     * @param title заголовок
     * @param text  содержимое
     */
    protected void viewDialog(String title, String text) {
        JOptionPane.showMessageDialog(null, text, title, JOptionPane.INFORMATION_MESSAGE);
        csmControl.makeChangesState(ETActionGame.TORETURN, true);
    }

    /**
     * Вывод диалогового окна: О программе.
     */
    protected void viewDialogAbout() {
        viewDialog(resourse.getResStr("MenuName.Info.About"), resourse.getResStr("Mag.Base.DlgAbout.Info"));
    }

    /**
     * Действия, необходимые для перехода в состояние базового для основного окна.
     */
    protected void stepToBase() {
        String[] deactivate = {
                "MenuName.Game.Continue", "MenuName.Game.Stop", "MenuName.Game.Back",
                "MenuName.Editing.End", "MenuName.Editing.Placemant", "MenuName.Editing.Clear"
        };
        selectedViewMenu(deactivate);
        lblBottom.setText(txtMsgDown);
        csmControl.setIsEdition(false);
        rightPanel.setSelectedIndex(-1);
        repaint();
    }

    /**
     * Диалоговое окно сохранения игры.
     */
    protected void viewDialogSave() {
        String fileName = null;
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Сохранение игрового файла");
        FileNameExtensionFilter filter = new FileNameExtensionFilter(resourse.getResStr("File.Filter.Description"),
                resourse.getResStr("File.Filter.Extension"));
        fileChooser.setFileFilter(filter);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int result = fileChooser.showSaveDialog(JFMainWindow.this);
        if (result == JFileChooser.APPROVE_OPTION) fileName = fileChooser.getSelectedFile().getAbsolutePath();
        csmControl.setFileName(fileName);
        csmControl.makeChangesState(ETActionGame.TORETURN, true);
    }

    /**
     * Диалоговое окно загрузки игры.
     */
    protected void viewDialogLoad() {
        String fileName = null;
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Загрузка игрового файла");
        FileNameExtensionFilter filter = new FileNameExtensionFilter(resourse.getResStr("File.Filter.Description"),
                resourse.getResStr("File.Filter.Extension"));
        fileChooser.setFileFilter(filter);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int result = fileChooser.showOpenDialog(JFMainWindow.this);
        if (result == JFileChooser.APPROVE_OPTION) fileName = fileChooser.getSelectedFile().getAbsolutePath();
        csmControl.setFileName(fileName);
        csmControl.makeChangesState(ETActionGame.TORETURN, true);
    }

    /**
     * Диалоговое окно, сообщения о том, что загрузка закончилась.
     */
    protected void viewLoadFileOk() {
        viewBoard.repaint();
        viewDialog("", resourse.getResStr("Msg.Base.DlgOK.Load"));
    }

    /**
     * Диалоговое окно, сообщения о том, что сохранение закончилось.
     */
    protected void viewSaveFileOK() {
        viewBoard.repaint();
        viewDialog("", resourse.getResStr("Msg.Base.DlgOK.Save"));
    }

    /**
     * Инициализация при переходе в режим редактирования.
     */
    protected void initEditingMode() {
        String[] deactivate = {
                "MenuName.File.Save", "MenuName.File.Open", "MenuName.Game.Start", "MenuName.Game.Continue", "MenuName.Game.Stop",
                "MenuName.Game.Back", "MenuName.Settings.While.Player", "MenuName.Settings.While.Comp", "MenuName.Settings.Black.Player",
                "MenuName.Settings.Black.Comp", "MenuName.Editing.Begin"
        };
        selectedViewMenu(deactivate);
        lblBottom.setText(resourse.getResStr("Msg.Editing.Info"));
        csmControl.setIsEdition(true);
        rightPanel.setSelectedIndex(0);
        csmControl.getCMoveGame().clearControlMove();
    }

    /**
     * Выход из режима редактирования.
     */
    protected void stepToBaseFromEdition() {
        csmControl.saveBoardGame();
        stepToBase();
        csmControl.getCMoveGame().clearControlMove();
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
     * Установка игры белых за игрока.
     */
    protected void playWhiteFromPlayer() {
        mActionMenu.get("MenuName.Settings.While.Player").setEnabled(false);
        mActionMenu.get("MenuName.Settings.While.Comp").setEnabled(true);
    }

    /**
     * Установка игры белых за компьютер.
     */
    protected void playWhiteFromComp() {
        mActionMenu.get("MenuName.Settings.While.Player").setEnabled(true);
        mActionMenu.get("MenuName.Settings.While.Comp").setEnabled(false);
    }

    /**
     * Установка игры чёрных за игрока.
     */
    protected void playBlackFromPlayer() {
        mActionMenu.get("MenuName.Settings.Black.Player").setEnabled(false);
        mActionMenu.get("MenuName.Settings.Black.Comp").setEnabled(true);
    }

    /**
     * Установка игры чёрных за компьютер.
     */
    protected void playBlackFromComp() {
        mActionMenu.get("MenuName.Settings.Black.Player").setEnabled(true);
        mActionMenu.get("MenuName.Settings.Black.Comp").setEnabled(false);
    }

    /**
     * Переход в режим игры.
     */
    protected void initGameMode() {
        String[] deactivate = {
                "MenuName.File.Open", "MenuName.Game.Start", "MenuName.Game.Continue", "MenuName.Settings.While.Player",
                "MenuName.Settings.While.Comp", "MenuName.Settings.Black.Player", "MenuName.Settings.Black.Comp", "MenuName.Editing.Begin",
                "MenuName.Editing.End", "MenuName.Editing.Placemant", "MenuName.Editing.Clear"
        };
        selectedViewMenu(deactivate);
        ETypeColor tc = csmControl.getBoard().getCurMove();
        String txt = "" + (tc == ETypeColor.WHITE ?  resourse.getResStr("Msg.Game.Play.White"): resourse.getResStr("Msg.Game.Play.Black"));
        txt = txt + " " + (csmControl.getPlayForColor(tc) ?  resourse.getResStr("Msg.Game.Play.Player"): resourse.getResStr("Msg.Game.Play.Computer"));
        lblBottom.setText(txt);
        rightPanel.setSelectedIndex(1);
        csmControl.getCMoveGame().clearControlMove();
    }

    protected void nextStepGame() {
        ETypeColor tc = csmControl.getBoard().getCurMove();
        String txt = "" + (tc == ETypeColor.WHITE ?  resourse.getResStr("Msg.Game.Play.White"): resourse.getResStr("Msg.Game.Play.Black"));
        txt = txt + " " + (csmControl.getPlayForColor(tc) ?  resourse.getResStr("Msg.Game.Play.Player"): resourse.getResStr("Msg.Game.Play.Computer"));
        lblBottom.setText(txt);
        repaint();
    }

    protected void endGameFromDraw() {
        viewDialog(resourse.getResStr("Msg.GameOver.Title"), resourse.getResStr("Msg.GameOver.Draw"));
        stepToBase();
    }

    protected void endGameFromWinWhile() {
        viewDialog(resourse.getResStr("Msg.GameOver.Title"), resourse.getResStr("Msg.GameOver.While"));
        stepToBase();
    }

    protected void endGameFromWinBlack() {
        viewDialog(resourse.getResStr("Msg.GameOver.Title"), resourse.getResStr("Msg.GameOver.Black"));
        stepToBase();
    }

    protected void endGameFromStop() {
        viewDialog(resourse.getResStr("Msg.GameOver.Title"), resourse.getResStr("Msg.GameOver.FromPlayer"));
        stepToBase();
    }
}
