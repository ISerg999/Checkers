import javax.swing.*;
import java.awt.*;
import java.util.Hashtable;
import java.util.Map;

public class JFMainWindow extends JFrame {

    /**
     * Список выбираемых элементов меню, состояние которых может изменяться.
     */
    protected Map<String, JMenuItem> mActionMenu;
    /**
     * Доступ к классу контроллера.
     */
    protected STMControl stmControl;

    public JFMainWindow() throws HeadlessException {
        stmControl = STMControl.getInstance();
        mActionMenu = new Hashtable<>();
        createAndShowGUI();
    }

    /**
     * Создание интерфейса окна.
     */
    private void createAndShowGUI() {
        // -------- Базовая настройка. --------

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle(stmControl.getResStr("Window.Title"));
        setSize(stmControl.getResInt("Window.Width"), stmControl.getResInt("Window.Height"));
        setVisible(true);

        // -------- Создание меню. --------

        JMenuBar mb = new JMenuBar();
        mb.add(createMenuFile());
        mb.add(createMenuGame());
        mb.add(createMenuState());
        mb.add(createMenuEditing());
        JMenuItem miAbout = new JMenuItem(stmControl.getResStr("Menu.About"));
        mb.add(miAbout);
        mb.setVisible(true);

        stepToBase();
    }

    private JMenu createMenuFile() {
        JMenu mFile = new JMenu(stmControl.getResStr("Menu.Name.File"));
        JMenuItem miSave = new JMenuItem(stmControl.getResStr("MenuName.File.Save"));
        mFile.add(miSave);
        mActionMenu.put("MenuName.File.Save", miSave);
        JMenuItem miLoad = new JMenuItem(stmControl.getResStr("MenuName.File.Load"));
        mFile.add(miLoad);
        mActionMenu.put("MenuName.File.Load", miLoad);
        mFile.addSeparator();
        JMenuItem miExit = new JMenuItem(stmControl.getResStr("MenuName.File.Exit"));
        mFile.add(miExit);
        return mFile;
    }
    private JMenu createMenuGame() {
        JMenu mGame = new JMenu(stmControl.getResStr("MenuName.Game"));
        JMenuItem miStart = new JMenuItem(stmControl.getResStr("MenuName.Game.Start"));
        mGame.add(miStart);
        mActionMenu.put("MenuName.Game.Start", miStart);
        JMenuItem miContinue = new JMenuItem(stmControl.getResStr("MenuName.Game.Continue"));
        mGame.add(miContinue);
        mActionMenu.put("MenuName.Game.Continue", miContinue);
        JMenuItem miStop = new JMenuItem(stmControl.getResStr("MenuName.Game.Stop"));
        mGame.add(miStop);
        mActionMenu.put("MenuName.Game.Stop", miStop);
        mGame.addSeparator();
        JMenuItem miBack = new JMenuItem(stmControl.getResStr("MenuName.Game.Back"));
        mGame.add(miBack);
        mActionMenu.put("MenuName.Game.Back", miBack);
        return mGame;
    }

    private JMenu createMenuState() {
        JMenu mState = new JMenu(stmControl.getResStr("MenuName.State"));
        JMenuItem miWhitePlayer = new JMenuItem(stmControl.getResStr("MenuName.State.While.Player"));
        mState.add(miWhitePlayer);
        mActionMenu.put("MenuName.State.While.Player", miWhitePlayer);
        JMenuItem miWhiteComp = new JMenuItem(stmControl.getResStr("MenuName.State.While.Comp"));
        mState.add(miWhiteComp);
        mActionMenu.put("MenuName.State.While.Comp", miWhiteComp);
        mState.addSeparator();
        JMenuItem miBlackPlayer = new JMenuItem(stmControl.getResStr("MenuName.State.Black.Player"));
        mState.add(miBlackPlayer);
        mActionMenu.put("MenuName.State.Black.Player", miBlackPlayer);
        JMenuItem mBlackComp = new JMenuItem(stmControl.getResStr("MenuName.State.Black.Comp"));
        mState.add(mBlackComp);
        mActionMenu.put("MenuName.State.Black.Comp", mBlackComp);
        return mState;
    }

    private JMenuItem createMenuEditing() {
        JMenu mEditing = new JMenu(stmControl.getResStr("MenuName.Editing"));
        JMenuItem miBeginEdit = new JMenuItem(stmControl.getResStr("MenuName.Editing.Begin"));
        mEditing.add(miBeginEdit);
        mActionMenu.put("MenuName.Editing.Begin", miBeginEdit);
        JMenuItem miEndEdit = new JMenuItem(stmControl.getResStr("MenuName.Editing.End"));
        mEditing.add(miEndEdit);
        mActionMenu.put("MenuName.Editing.End", miEndEdit);
        mEditing.addSeparator();
        JMenuItem miPlacemantBoard = new JMenuItem(stmControl.getResStr("MenuName.Editing.Placemant"));
        mEditing.add(miPlacemantBoard);
        mActionMenu.put("MenuName.Editing.Placemant", miPlacemantBoard);
        JMenuItem miClearBoard = new JMenuItem(stmControl.getResStr("MenuName.Editing.Clear"));
        mEditing.add(miClearBoard);
        mActionMenu.put("MenuName.Editing.Clear", miClearBoard);
        return mEditing;
    }

    /**
     * Действия, необходимые для перехода в состояние базового для основного окна.
     */
    public void stepToBase() {
        String[] deactivate = {"MenuName.File.Save", "MenuName.File.Load", "MenuName.Game.Continue", "MenuName.Game.Stop",
                "MenuName.Game.Back", "MenuName.State.While.Player", "MenuName.State.While.Comp", "MenuName.State.Black.Player",
                "MenuName.State.Black.Comp", "MenuName.Editing.Begin", "MenuName.Editing.End", "MenuName.Editing.Placemant",
                "MenuName.Editing.Clear"
        };
        for (String nm: deactivate) {
            mActionMenu.get(nm).setEnabled(false);
        }
    }
}
