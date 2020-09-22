import CheckersEngine.BaseEngine.ETypeColor;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;

public class CRightPanelGaming extends JPanel  {

    /**
     * Шрфит заголовка ходов.
     */
    protected static final Font fontTitle;
    static {
        fontTitle = new Font("Arial", Font.BOLD, 12);
    }

    /**
     * Максимальное количество строк с ходами.
     */
    protected static final int countLine = 16;

    /**
     * Доступ к классу ресурсов.
     */
    protected CResourse resourse;
    /**
     * Доступ к классу контроллера.
     */
    protected CSMControl csmControl;
    protected int rPW, rPH;

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
        rPW = resourse.getResInt("Window.RightPanel.Width");
        rPH = resourse.getResInt("Window.RightPanel.Height");

        // Создание интерфейса.
        setLayout(null);
        setSize(rPW, rPH);
        setBorder(BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED),
                BorderFactory.createEmptyBorder(25, 25, 25, 25)));
    }

    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        Font oldFont = g2.getFont();
        g2.drawLine(rPW / 2, 4, rPW / 2, 345);
        g2.setFont(fontTitle);
        g2.drawString(resourse.getResStr("Msg.Game.Right.Panel.StepWhite"), 10, 20);
        g2.drawString(resourse.getResStr("Msg.Game.Right.Panel.StepBlack"), 10 + rPW / 2, 20);
        g2.setFont(oldFont);
        int stepBlack = ETypeColor.BLACK == csmControl.getBoard().getCurMove() ? 0: 1;
        int halfLine = csmControl.getCMoveGame().size() < countLine * 2 - stepBlack ? 0: csmControl.getCMoveGame().size() - (countLine * 2 - stepBlack);
        int y = 40;
        boolean isLeft = true;
        for (; halfLine < csmControl.getCMoveGame().size(); halfLine++) {
            String tmp = csmControl.getCMoveGame().at(halfLine).substring(0, 5);
            if (isLeft) g2.drawString(tmp, 35, y);
            else
            {
                g2.drawString(tmp, 35 + rPW / 2, y);
                y += 20;
            }
            isLeft = !isLeft;
        }
    }
}
