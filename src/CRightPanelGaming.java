import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;

public class CRightPanelGaming extends JPanel  {

    /**
     * Доступ к классу ресурсов.
     */
    protected CResourse resourse;
    /**
     * Доступ к классу контроллера.
     */
    protected CSMControl csmControl;

    public CRightPanelGaming(LayoutManager layout, boolean isDoubleBuffered) {
        super(layout, isDoubleBuffered);
        initUI();
    }

    public CRightPanelGaming(LayoutManager layout) {
        super(layout);
        initUI();
    }

    public CRightPanelGaming(boolean isDoubleBuffered) {
        super(isDoubleBuffered);
        initUI();
    }

    public CRightPanelGaming() {
        super();
        initUI();
    }

    protected void initUI() {
        // Базовые настройки.
        csmControl = csmControl.getInstance();
        resourse = CResourse.getInstance();
        int rPW = resourse.getResInt("Window.RightPanel.Width");
        int rPH = resourse.getResInt("Window.RightPanel.Height");

        // Создание интерфейса.
        setLayout(null);
        setSize(rPW, rPH);
        setBorder(BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED),
                BorderFactory.createEmptyBorder(25, 25, 25, 25)));
    }
}
