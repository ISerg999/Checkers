import CheckersEngine.BaseEngine.ETypeColor;
import CheckersEngine.BaseEngine.ETypeFigure;
import CheckersEngine.BaseEngine.Pair;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Класс вывода игровой доски и её элементов.
 */
public class ViewBoard extends JPanel {

    static final private Color[] spaceFrameColor = {
            Color.black, new Color(255, 255, 0), new Color(0, 255, 255), new Color(255, 0, 255)
    };

    /**
     * Доступ к классу контроллера.
     */
    protected STMControl stmControl;
    /**
     * Доступ к классу ресурсов.
     */
    protected CResourse resourse;
    /**
     * Ключ изображения доски
     */
    protected String strBoard = "Path.Image.Board";
    /**
     * Ключ изображения фигур.
     */
    protected String strFigures = "Path.Image.Figures";
    /**
     * Массив клеток доски.
     */
    protected Pair<Pair<ETypeFigure, ETypeColor>, Integer>[][] boardSpaces;
    /**
     * Смещение рисуемых элементов на доске.
     */
    protected int offsetBaseX, offsetBaseY;
    /**
     * Размеры одной клетки доски.
     */
    protected int spaceW, spaceH;
    /**
     * Смещение вывода фигуры внутри клетки.
     */
    protected int imgDX, imgDY;

    public ViewBoard() {
        stmControl = STMControl.getInstance();
        resourse = CResourse.getInstance();
        offsetBaseX = resourse.getResInt("Board.Margin.Left");
        offsetBaseY = resourse.getResInt("Board.Margin.Top");
        spaceW = resourse.getResInt("Board.Space.Width");
        spaceH = resourse.getResInt("Board.Space.Height");
        imgDX = resourse.getResInt("Board.Space.Margin.Left");
        imgDY = resourse.getResInt("Board.Space.Margin.Top");

        createAndShowGUI();
    }

    /**
     * Создание интерфейса панели доски.
     */
    protected void createAndShowGUI() {
        int width = resourse.getImage(strBoard).getImage().getWidth(null);
        int height = resourse.getImage(strBoard).getImage().getHeight(null);
        setMinimumSize(new Dimension(width, height));
        setMaximumSize(new Dimension(width, height));
        setBounds(0, 0, width, height);
        boardSpaces = new Pair[8][4];
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 4; j++) {
                boardSpaces[i][j] = new Pair<>(null, 0);
            }

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                mouseAction(e, true);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                mouseAction(e, false);
            }
        });
    }

    /**
     * Обработка нажатий клавишы мыши на доске.
     * @param e       событие нажатие мыши
     * @param isClick true - мышь нажата, false - мышь отпущена
     */
    protected void mouseAction(MouseEvent e, boolean isClick) {
        Pair<Integer, Integer> pos = coordImgToBoard(e.getX(), e.getY());
        int bx = pos.getFirst();
        int by = pos.getSecond();
        if (!stmControl.testCoordinateBoard(bx, by)) return;

//        if (isClick) {
//            setTypeColorFrame(1, bx, by);
//        } else {
//            setTypeColorFrame(0, bx, by);
//        }
//        repaint();
    }

    /**
     * Задаёт тип фигуры.
     * @param typeFigure тип фигуры
     * @param x          координата x на игровой доске
     * @param y          координата y на игровой доске
     */
    public void setImgFigure(Pair<ETypeFigure, ETypeColor> typeFigure, int x, int y) {
        boardSpaces[y][x / 2].setFirst(typeFigure);
    }

    /**
     * Задёт тип цвета рамки фигуры.
     * @param typeColorFrame тип цвета
     * @param x              координата x на игровой доске
     * @param y              координата y на игровой доске
     */
    public void setTypeColorFrame(int typeColorFrame, int x, int y) {
        if (typeColorFrame < 0 || typeColorFrame > 3) return;
        boardSpaces[y][x / 2].setSecond(typeColorFrame);
    }

    /**
     * Преобразует координаты экрана в координаты доски.
     * @param x координата x экрана
     * @param y координата y экрана
     * @return Pair с координатами доски
     */
    protected Pair<Integer, Integer> coordImgToBoard(int x, int y) {
        int bx = (x - offsetBaseX) / spaceW;
        int by = 7 - (y - offsetBaseY) / spaceH;
        return new Pair<>(bx, by);
    }

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g.drawImage(resourse.getImage(strBoard).getImage(), 0, 0, null);
        g2.drawRect(0, 0, resourse.getImage(strBoard).getImage().getWidth(null) - 1,
                resourse.getImage(strBoard).getImage().getHeight(null) - 1);
//        BufferedImage img = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
        g2.setStroke(new BasicStroke(2));
        int imgW = 60;
        int imgH = 60;
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 4; x++) {
                Pair<ETypeFigure, ETypeColor> p = boardSpaces[y][x].getFirst();
                Color colorFrame = spaceFrameColor[boardSpaces[y][x].getSecond()];
                g2.setColor(colorFrame);
                int grX = offsetBaseX + (2 * x + y % 2) * spaceW;
                int grY = offsetBaseY + (7 - y) * spaceH;
                if (p != null) {
                    int iDx = p.getSecond() == ETypeColor.WHITE ? 0: 120;
                    iDx += p.getFirst() == ETypeFigure.CHECKERS ? 0: 60;
                    g2.drawImage(resourse.getImage(strFigures).getImage(), grX + imgDX, grY + imgDY, grX + imgDX + imgW, grY + imgDY + imgH, iDx, 0, iDx + imgW, imgH, null);
                }
                g2.drawRect(grX, grY, spaceW - 1, spaceH - 1);
            }
        }
    }
}
