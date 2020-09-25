import CheckersEngine.BaseEngine.CFactoryFigure;
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
     * Размеры ячейки фигуры.
     */
    int fullW = CResourse.getInstance().getResInt("Board.Space.Width");
    int fullH = CResourse.getInstance().getResInt("Board.Space.Height");

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
    protected CPair<ETypeFigure, ETypeColor> selectedF;

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

    /**
     * Инициализация интерфейса панели.
     */
    protected void initUI() {
        // Базовые настройки.
        csmControl = csmControl.getInstance();
        resourse = CResourse.getInstance();
        selectedF = null;
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
        JLabel lblDown1 = new JLabel(resourse.getResStr("Msg.Right.Edition.Info.Del"));
        lblDown1.setBounds(2, 66 + CResourse.getInstance().getResInt("Board.Space.Height") * 5 / 2, rPW - 4, 48);
        lblDown1.setHorizontalAlignment(JLabel.LEFT);
        add(lblDown1);
        JLabel lblDown2 = new JLabel((resourse.getResStr("Msg.Right.Edition.Info.BackSpace")));
        lblDown2.setBounds(2, 114 + CResourse.getInstance().getResInt("Board.Space.Height") * 5 / 2, rPW - 4, 48);
        add(lblDown2);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                for (int i = 0; i < 4; i++) {
                    if (e.getX() >= posFigures[i][0] && e.getX() < posFigures[i][0] + fullW
                            && e.getY() >= posFigures[i][1] && e.getY() < posFigures[i][1] + fullH) {
                        CPair<ETypeFigure, ETypeColor> newF = getFigure(i);
                        if (null != selectedF && selectedF.equals(newF)) selectedF = null;
                        else selectedF = newF;
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
        CPair<ETypeFigure, ETypeColor> newF = getFigure(i);
        if (null != selectedF && selectedF.equals(newF)) selectedF = null;
        else selectedF = newF;
        repaint();
    }

    public CPair<ETypeFigure, ETypeColor> selectedFigure() {
        return selectedF;
    }

    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        int dd = 3;
        for (int i = 0; i < 4; i++) {
            CPair<ETypeFigure, ETypeColor> newF = getFigure(i);
            ImageIcon img = CFactoryFigure.getInstance().getImageFigure(newF);
            g2.drawImage(img.getImage(), posFigures[i][0] + dd, posFigures[i][1] + dd, null);
            if (null != selectedF && selectedF.equals(newF)) {
                g2.drawRect(posFigures[i][0], posFigures[i][1], fullW, fullH);
            }
        }
    }

    /**
     * Получение фигуры по номеру разположения.
     * @param i номер фигуры
     * @return изображаемая фигура
     */
    protected CPair<ETypeFigure, ETypeColor> getFigure(int i) {
        if (i < 0 || i > 3) return null;
        ETypeFigure tf = i % 2 == 0 ? ETypeFigure.CHECKERS: ETypeFigure.QUINE;
        ETypeColor tc = i < 2 ? ETypeColor.WHITE: ETypeColor.BLACK;
        return new CPair<>(tf, tc);
    }

}
