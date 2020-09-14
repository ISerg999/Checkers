import javax.swing.*;
import java.awt.*;

public class CRightPanelEdition extends JPanel {

    /**
     * Доступ к классу ресурсов.
     */
    protected CResourse resourse;
    /**
     * Доступ к классу контроллера.
     */
    protected CSMControl csmControl;

    public CRightPanelEdition(LayoutManager layout, boolean isDoubleBuffered) {
        super(layout, isDoubleBuffered);
        initUI();
    }
    public CRightPanelEdition(LayoutManager layout) {
        super(layout);
        initUI();
    }
    public CRightPanelEdition(boolean isDoubleBuffered) {
        super(isDoubleBuffered);
        initUI();
    }
    public CRightPanelEdition() {
        super();
        initUI();
    }

    protected void initUI() {
        // Базовые настройки.
        csmControl = csmControl.getInstance();
        resourse = CResourse.getInstance();

        // Создание интерфейса.
        setLayout(null);
        int rPW = resourse.getResInt("Window.RightPanel.Width");
        int rPH = resourse.getResInt("Window.RightPanel.Height");
        setSize(rPW, rPH);
    }

    // TODO: Вывести верхнюю надпись.
    // TODO: Вывести выбираемые фигуры. Создать метод, возвращающий выбранную фигуру.
    // TODO: Создать обработку события мыши для выбора выбираемой фигуры.
    // TODO: Создать метод обрабатывающий клавиатуру для выбора выбираемой фигуры.
}
