import CheckersEngine.BaseEngine.Pair;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
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
    private static final Map<Pair<TStateGame,TActionGame>, String> stateAction;
    static {
        stateAction = new Hashtable<>();
//        stateAction.put(new Pair<>(TStateGame.BASE, TActionGame.TOBASE), "function");
    }

    /**
     * Доступ к классу контроллера.
     */
    protected STMControl stmControl;

    /**
     * Список выбираемых элементов меню, состояние которых может изменяться.
     */
    protected Map<String, JMenuItem> mActionMenu;

    public JFMainWindow() throws HeadlessException {
        stmControl = STMControl.getInstance();
        stmControl.addActionGame(this);
        mActionMenu = new Hashtable<>();
        createAndShowGUI();
    }

    /**
     * Создание интерфейса окна.
     */
    private void createAndShowGUI() {
        // -------- Базовая настройка. --------

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        ImageIcon icon = new ImageIcon(Main.class.getResource("Resource/icons/icon_game.png"));
        setIconImage(icon.getImage());
        int curWidth = stmControl.getResInt("Window.Width");
        int curHeight = stmControl.getResInt("Window.Height");
        setResizable(false);
        setTitle(stmControl.getResStr("Window.Title"));
        setMinimumSize(new Dimension(curWidth, curHeight));
        setMinimumSize(new Dimension(curWidth, curHeight));
        setFont(new Font("Arial", Font.PLAIN, 10));
        setVisible(true);

        // -------- Создание меню. --------

        JMenuBar mb = new JMenuBar();
        mb.add(createMenuFile());
        mb.add(createMenuGame());
        mb.add(createMenuState());
        mb.add(createMenuEditing());
        mb.add(createMenuInfo());
        mb.setBounds(1, 1, curWidth, 24);
        setJMenuBar(mb);
        mb.setVisible(true);
        mb.repaint();

    }

    private JMenu createMenuFile() {
        JMenu mFile = new JMenu(stmControl.getResStr("MenuName.File"));
        JMenuItem miOpen = new JMenuItem(stmControl.getResStr("MenuName.File.Open"));
        miOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        // miOpen.addActionListener(this);
        mFile.add(miOpen);
        JMenuItem miSave = new JMenuItem(stmControl.getResStr("MenuName.File.Save"));
        miSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        // miSave.addActionListener(this);
        mFile.add(miSave);
        mActionMenu.put("MenuName.File.Save", miSave);
        mActionMenu.put("MenuName.File.Open", miOpen);
        mFile.addSeparator();
        JMenuItem miExit = new JMenuItem(stmControl.getResStr("MenuName.File.Exit"));
        miExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.ALT_MASK + ActionEvent.CTRL_MASK));
        // miExit.addActionListener(this);
        mFile.add(miExit);
        return mFile;
    }

    private JMenu createMenuGame() {
        JMenu mGame = new JMenu(stmControl.getResStr("MenuName.Game"));
        JMenuItem miStart = new JMenuItem(stmControl.getResStr("MenuName.Game.Start"));
        miStart.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        // miStart.addActionListener(this);
        mGame.add(miStart);
        mActionMenu.put("MenuName.Game.Start", miStart);
        JMenuItem miContinue = new JMenuItem(stmControl.getResStr("MenuName.Game.Continue"));
        miContinue.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        // miContinue.addActionListener(this);
        mGame.add(miContinue);
        mActionMenu.put("MenuName.Game.Continue", miContinue);
        JMenuItem miStop = new JMenuItem(stmControl.getResStr("MenuName.Game.Stop"));
        miStop.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
        // miStop.addActionListener(this);
        mGame.add(miStop);
        mActionMenu.put("MenuName.Game.Stop", miStop);
        mGame.addSeparator();
        JMenuItem miBack = new JMenuItem(stmControl.getResStr("MenuName.Game.Back"));
        miBack.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, ActionEvent.CTRL_MASK));
        // miBack.addActionListener(this);
        mGame.add(miBack);
        mActionMenu.put("MenuName.Game.Back", miBack);
        return mGame;
    }

    private JMenu createMenuState() {
        JMenu mState = new JMenu(stmControl.getResStr("MenuName.State"));
        JMenuItem miWhitePlayer = new JMenuItem(stmControl.getResStr("MenuName.State.While.Player"));
        miWhitePlayer.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, ActionEvent.ALT_MASK + ActionEvent.CTRL_MASK));
        // miWhitePlayer.addActionListener(this);
        mState.add(miWhitePlayer);
        mActionMenu.put("MenuName.State.While.Player", miWhitePlayer);
        JMenuItem miWhiteComp = new JMenuItem(stmControl.getResStr("MenuName.State.While.Comp"));
        miWhiteComp.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, ActionEvent.ALT_MASK + ActionEvent.CTRL_MASK));
        // miWhiteComp.addActionListener(this);
        mState.add(miWhiteComp);
        mActionMenu.put("MenuName.State.While.Comp", miWhiteComp);
        mState.addSeparator();
        JMenuItem miBlackPlayer = new JMenuItem(stmControl.getResStr("MenuName.State.Black.Player"));
        miBlackPlayer.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_J, ActionEvent.ALT_MASK + ActionEvent.CTRL_MASK));
        // miBlackPlayer.addActionListener(this);
        mState.add(miBlackPlayer);
        mActionMenu.put("MenuName.State.Black.Player", miBlackPlayer);
        JMenuItem mBlackComp = new JMenuItem(stmControl.getResStr("MenuName.State.Black.Comp"));
        mBlackComp.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_K, ActionEvent.ALT_MASK + ActionEvent.CTRL_MASK));
        // mBlackComp.addActionListener(this);
        mState.add(mBlackComp);
        mActionMenu.put("MenuName.State.Black.Comp", mBlackComp);
        return mState;
    }

    private JMenu createMenuEditing() {
        JMenu mEditing = new JMenu(stmControl.getResStr("MenuName.Editing"));
        JMenuItem miBeginEdit = new JMenuItem(stmControl.getResStr("MenuName.Editing.Begin"));
        miBeginEdit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, ActionEvent.ALT_MASK + ActionEvent.CTRL_MASK));
        // miBeginEdit.addActionListener(this);
        mEditing.add(miBeginEdit);
        mActionMenu.put("MenuName.Editing.Begin", miBeginEdit);
        JMenuItem miEndEdit = new JMenuItem(stmControl.getResStr("MenuName.Editing.End"));
        miEndEdit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.ALT_MASK + ActionEvent.CTRL_MASK));
        // miEndEdit.addActionListener(this);
        mEditing.add(miEndEdit);
        mActionMenu.put("MenuName.Editing.End", miEndEdit);
        mEditing.addSeparator();
        JMenuItem miPlacemantBoard = new JMenuItem(stmControl.getResStr("MenuName.Editing.Placemant"));
        // miPlacemantBoard.addActionListener(this);
        mEditing.add(miPlacemantBoard);
        mActionMenu.put("MenuName.Editing.Placemant", miPlacemantBoard);
        JMenuItem miClearBoard = new JMenuItem(stmControl.getResStr("MenuName.Editing.Clear"));
        // miClearBoard.addActionListener(this);
        mEditing.add(miClearBoard);
        mActionMenu.put("MenuName.Editing.Clear", miClearBoard);
        return mEditing;
    }

    private JMenu createMenuInfo() {
        JMenu mInfo = new JMenu(stmControl.getResStr("MenuName.Info"));
        JMenuItem miInfoAbout = new JMenuItem(stmControl.getResStr("MenuName.Info.About"));
        // miInfoAbout.addActionListener(this);
        mInfo.add(miInfoAbout);
        return mInfo;
    }

    public void makeChangesState(TStateGame newState) {

    }

    @Override
    public void makeChangesState(TStateGame curStateGame, TActionGame actionGame) {
        if (curStateGame == null) {
            stepToBase();
        } else {
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
        }
    }

    // ---------------------------- Методы обрабатывающиеся контроллером переходов состояний ---------------------------

    /**
     * Действия, необходимые для перехода в состояние базового для основного окна.
     */
    private void stepToBase() {
        String[] deactivate = {"MenuName.File.Save", "MenuName.File.Open", "MenuName.Game.Continue", "MenuName.Game.Stop",
                "MenuName.Game.Back", "MenuName.State.While.Player", "MenuName.State.While.Comp", "MenuName.State.Black.Player",
                "MenuName.State.Black.Comp", "MenuName.Editing.Begin", "MenuName.Editing.End", "MenuName.Editing.Placemant",
                "MenuName.Editing.Clear"
        };
        for (String nm: deactivate) {
            mActionMenu.get(nm).setEnabled(false);
        }
    }

}
