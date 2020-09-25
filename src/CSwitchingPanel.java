import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CSwitchingPanel extends JPanel {

    /**
     * Доступ к классу ресурсов.
     */
    protected CResourse resourse;
    /**
     * Доступ к классу контроллера.
     */
    protected CSMControl csmControl;
    /**
     * Список панелей.
     */
    protected List<JPanel> lstPanel;
    /**
     * Номер текущей панели.
     */
    protected int curIndPanel;
    /**
     * Ссылка на текущую панель
     */
    protected JPanel curPanel;

    public CSwitchingPanel(LayoutManager layout, boolean isDoubleBuffered, int x, int y) {
        super(layout, isDoubleBuffered);
        initUI(x, y);
    }
    public CSwitchingPanel(LayoutManager layout, int x, int y) {
        super(layout);
        initUI(x, y);
    }
    public CSwitchingPanel(boolean isDoubleBuffered, int x, int y) {
        super(isDoubleBuffered);
        initUI(x, y);
    }
    public CSwitchingPanel(int x, int y) {
        super();
        initUI(x, y);
    }

    /**
     * Инициализация класса.
     * @param x координата x
     * @param y координата y
     */
    protected void initUI(int x, int y) {
        // Базовые настройки.
        csmControl = csmControl.getInstance();
        resourse = CResourse.getInstance();
        lstPanel = new ArrayList<>();
        curIndPanel = -1;
        curPanel = null;

        // Создание интерфейса.
        setLayout(null);
        int rPW = resourse.getResInt("Window.RightPanel.Width");
        int rPH = resourse.getResInt("Window.RightPanel.Height");
        setBorder(BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED),
                BorderFactory.createEmptyBorder(25, 25, 25, 25)));
        setBounds(x, y, rPW, rPH);
    }

    /**
     * Деактивация текущей панели.
     */
    protected void deactivatingPanel() {
        if (null == curPanel) return;
        remove(curPanel);
        curPanel.setVisible(false);
        curPanel = null;
        curIndPanel = -1;
        revalidate();
        repaint();
    }

    /**
     * Активация новой панели.
     * @param ind индекс панели
     */
    protected void activatingPanel(int ind) {
        if (null != curPanel) return;
        curPanel = lstPanel.get(ind);
        curIndPanel = ind;
        add(curPanel);
        curPanel.setVisible(true);
        revalidate();
        repaint();
    }

    /**
     * Добавление новой панели.
     * @param panel отображаемая панель
     */
    public void append(JPanel panel) {
        if (null == panel || lstPanel.contains(panel)) return;
        lstPanel.add(panel);
        panel.setVisible(false);
        panel.setBounds(0, 0, getWidth(), getHeight());
        if (curIndPanel < 0) setSelectedIndex(0);
    }

    /**
     * Очистка списка панелей.
     */
    public void clear() {
        deactivatingPanel();
        lstPanel.clear();
    }

    /**
     * Удаление объекта панели.
     * @param panel объект удаляемой панели
     */
    public void removePanel(JPanel panel) {
        removePanel(lstPanel.indexOf(panel));
    }

    /**
     * Удаление панели по его индексу.
     * @param ind индекс удаляемой панели
     */
    public void removePanel(int ind) {
        if (ind < 0 || ind >= lstPanel.size()) return;
        boolean isNewPanel = false;
        int newInd = curIndPanel >= ind ? curIndPanel - 1: curIndPanel;
        if (newInd < 0) {
            isNewPanel = true;
            deactivatingPanel();
        }
        lstPanel.remove(ind);
        if (isNewPanel && lstPanel.size() > 0) activatingPanel(0);
    }

    /**
     * Выбор текущей панели.
     * @param ind номер текущей панели, если он меньше нуля, то отключает панель
     */
    public void setSelectedIndex(int ind) {
        if (ind < 0) {
            deactivatingPanel();
        } else {
            if (ind >= lstPanel.size() || ind == curIndPanel) return;
            deactivatingPanel();
            activatingPanel(ind);
        }
    }

    /**
     * Возвращает текущую выбранную панель.
     * @return объект текущей выбранной панели, или null, если не выбрана
     */
    public JPanel getCurPanel() {
        return curPanel;
    }

}
