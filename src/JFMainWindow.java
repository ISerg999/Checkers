import CheckersEngine.BaseEngine.CPair;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.Map;

// implements ActionListener
public class JFMainWindow extends JFrame implements IChangeState{

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
        stateAction.put(new CPair<>(ETStateGame.BASE, ETActionGame.TOEDITING), "initEditing");
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

    public JFMainWindow() throws HeadlessException {
        resourse = CResourse.getInstance();
        csmControl = CSMControl.getInstance();
        csmControl.addActionGame(this);
        mActionMenu = new Hashtable<>();
        createAndShowGUI();
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                keyAction(e);
            }
        });

        csmControl.makeChangesState(ETActionGame.TOBASE, false);
    }

    /**
     * Создание интерфейса окна.
     */
    private void createAndShowGUI() {
        // -------- Базовая настройка. --------

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
        miExit.addActionListener(actionEvent -> System.exit(0));
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
        // miStart.addActionListener(this);
        mGame.add(miStart);
        mActionMenu.put("MenuName.Game.Start", miStart);

        JMenuItem miContinue = new JMenuItem(resourse.getResStr("MenuName.Game.Continue"));
        miContinue.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        // miContinue.addActionListener(this);
        mGame.add(miContinue);
        mActionMenu.put("MenuName.Game.Continue", miContinue);

        JMenuItem miStop = new JMenuItem(resourse.getResStr("MenuName.Game.Stop"));
        miStop.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
        // miStop.addActionListener(this);
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
        // miWhitePlayer.addActionListener(this);
        mSettings.add(miWhitePlayer);
        mActionMenu.put("MenuName.Settings.While.Player", miWhitePlayer);

        JMenuItem miWhiteComp = new JMenuItem(resourse.getResStr("MenuName.Settings.While.Comp"));
        miWhiteComp.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, ActionEvent.ALT_MASK + ActionEvent.CTRL_MASK));
        // miWhiteComp.addActionListener(this);
        mSettings.add(miWhiteComp);
        mActionMenu.put("MenuName.Settings.While.Comp", miWhiteComp);

        mSettings.addSeparator();

        JMenuItem miBlackPlayer = new JMenuItem(resourse.getResStr("MenuName.Settings.Black.Player"));
        miBlackPlayer.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_J, ActionEvent.ALT_MASK + ActionEvent.CTRL_MASK));
        // miBlackPlayer.addActionListener(this);
        mSettings.add(miBlackPlayer);
        mActionMenu.put("MenuName.Settings.Black.Player", miBlackPlayer);

        JMenuItem mBlackComp = new JMenuItem(resourse.getResStr("MenuName.Settings.Black.Comp"));
        mBlackComp.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_K, ActionEvent.ALT_MASK + ActionEvent.CTRL_MASK));
        // mBlackComp.addActionListener(this);
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
        // miEndEdit.addActionListener(this);
        mEditing.add(miEndEdit);
        mActionMenu.put("MenuName.Editing.End", miEndEdit);

        mEditing.addSeparator();

        JMenuItem miPlacemantBoard = new JMenuItem(resourse.getResStr("MenuName.Editing.Placemant"));
        // miPlacemantBoard.addActionListener(this);
        mEditing.add(miPlacemantBoard);
        mActionMenu.put("MenuName.Editing.Placemant", miPlacemantBoard);

        JMenuItem miClearBoard = new JMenuItem(resourse.getResStr("MenuName.Editing.Clear"));
        // miClearBoard.addActionListener(this);
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
        // TODO: Создать панель для режима редактирования.
        JPanel newPanel = new JPanel();
        newPanel.setBackground(Color.yellow);
        rightPanel.append(newPanel);

        // Создание панели для режима игры.
        // TODO: Создать панель для режима игры.
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
     * Обработка событий нажатий на клавиатуре.
     * @param e событие клавиатуры
     */
    protected void keyAction(KeyEvent e) {
        if (csmControl.getIsEdition()) {
            // TODO: Обработка нажатий клавиш для режима редактирования.
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
        csmControl.setIsEdition(false);
        rightPanel.setSelectedIndex(-1);
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
        viewDialog("", resourse.getResStr("Msg.Base.DlgOK.Load"));
    }

    /**
     * Диалоговое окно, сообщения о том, что сохранение закончилось.
     */
    protected void viewSaveFileOK() {
        viewDialog("", resourse.getResStr("Msg.Base.DlgOK.Save"));
    }

    /**
     * Инициализация при переходе в режим редактирования.
     */
    protected void initEditing() {
        String[] deactivate = {
                "MenuName.File.Save", "MenuName.File.Open", "MenuName.Game.Start", "MenuName.Game.Continue", "MenuName.Game.Stop",
                "MenuName.Game.Back", "MenuName.Settings.While.Player", "MenuName.Settings.While.Comp", "MenuName.Settings.Black.Player",
                "MenuName.Settings.Black.Comp", "MenuName.Editing.Begin"
        };
        selectedViewMenu(deactivate);
        lblBottom.setText(resourse.getResStr("Msg.Editing.Info"));
        csmControl.setIsEdition(true);
        rightPanel.setSelectedIndex(0);
    }
}
