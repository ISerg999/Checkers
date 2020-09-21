import CheckersEngine.BaseEngine.CPair;
import CheckersEngine.BaseEngine.ETypeColor;
import CheckersEngine.BaseEngine.ETypeFigure;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CRightPanelEdition extends JPanel {

    /**
     * Координаты выводимых фигур.
     */
    protected static final Integer[][] posFigures = {
            {8, 64},
            {8, 66 + CResourse.getInstance().getResInt("Board.Space.Height")},
            {128, 64},
            {128, 66 + CResourse.getInstance().getResInt("Board.Space.Height")}
    };

    /**
     * Доступ к классу ресурсов.
     */
    protected CResourse resourse;
    /**
     * Доступ к классу контроллера.
     */
    protected CSMControl csmControl;
    /**
     * Задаёт выбранную фигуру.
     */
    protected int selectedF;
    /**
     * Ключ изображения фигур.
     */
    static final protected String strFigures = "Path.Image.Figures";
    /**
     * Размеры ячейки фигуры.
     */
    int fullW, fullH;

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
        selectedF = -1;
        fullW = resourse.getResInt("Board.Space.Width");
        fullH = resourse.getResInt("Board.Space.Height");
        int rPW = resourse.getResInt("Window.RightPanel.Width");
        int rPH = resourse.getResInt("Window.RightPanel.Height");

        // Создание интерфейса.
        setLayout(null);
        setSize(rPW, rPH);
        setBorder(BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED),
                BorderFactory.createEmptyBorder(25, 25, 25, 25)));

        // Создание верхней надписи.
        JLabel lblUp = new JLabel(resourse.getResStr("Msg.Right.Editing.Info.Title"));
        lblUp.setBounds(0, 0, rPW, 32);
        lblUp.setHorizontalAlignment(JLabel.CENTER);
        add(lblUp);

        // Создание нижней надписи.
        JLabel lblDown = new JLabel(resourse.getResStr("Msg.Right.Edition.Info.Del"));
        lblDown.setBounds(2, 66 + CResourse.getInstance().getResInt("Board.Space.Height") * 5 / 2, rPW - 4, 48);
        lblDown.setHorizontalAlignment(JLabel.LEFT);
        add(lblDown);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                for (int i = 0; i < 4; i++) {
                    if (e.getX() >= posFigures[i][0] && e.getX() < posFigures[i][0] + fullW
                            && e.getY() >= posFigures[i][1] && e.getY() < posFigures[i][1] + fullH) {
                        if (i == selectedF) selectedF = -1;
                        else selectedF = i;
                        repaint();
                        return;
                    }
                }
            }
        });
    }

    /**
     * Обработка события нажатий на клавиатуре.
     * @param e событие клавиатуры.
     */
    public void keyAction(KeyEvent e) {
        int i = -2;
        if (KeyEvent.VK_0 == e.getKeyCode()) i = -1;
        else if (KeyEvent.VK_1 == e.getKeyCode()) i = 0;
        else if (KeyEvent.VK_2 == e.getKeyCode()) i = 1;
        else if (KeyEvent.VK_3 == e.getKeyCode()) i = 2;
        else if (KeyEvent.VK_4 == e.getKeyCode()) i = 3;
        if (-2 == i) return;
        if (i == selectedF) i = -1;
        selectedF = i;
        repaint();
    }

    public CPair<ETypeFigure, ETypeColor> selectedFigure() {
        if (selectedF < 0 || selectedF > 3) return null;
        ETypeColor tc = selectedF < 2 ? ETypeColor.WHITE: ETypeColor.BLACK;
        ETypeFigure tf = selectedF % 2 == 0 ? ETypeFigure.CHECKERS: ETypeFigure.QUINE;
        return new CPair<>(tf, tc);
    }

    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        int imgW = resourse.getImage(strFigures).getImage().getWidth(null) / 4;
        int imgH = resourse.getImage(strFigures).getImage().getHeight(null);
        int dd = 3;
        for (int i = 0; i < 4; i++) {
            g2.drawImage(resourse.getImage(strFigures).getImage(), posFigures[i][0] + dd, posFigures[i][1] + dd,
                    posFigures[i][0] + imgW + dd, posFigures[i][1] + imgH + dd, i * 60, 0, i * 60 + imgW, imgH, null);
            if (i == selectedF) {
                g2.drawRect(posFigures[i][0], posFigures[i][1], fullW, fullH);
            }
        }
    }

}
