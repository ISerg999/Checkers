import CheckersEngine.BaseEngine.CPair;
import CheckersEngine.BaseEngine.ETypeColor;
import CheckersEngine.BaseEngine.ETypeFigure;
import CheckersEngine.BaseEngine.IFigureBase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Класс вывода игровой доски и её элементов.
 */
public class CViewBoard extends JPanel {

    static final protected Color[] spaceFrameColor = {
            Color.black,                                                                         // Стандартный.
            new Color(CResourse.getInstance().getResInt("Board.Space.Color.Selected")),     // Выбранный.
            new Color(CResourse.getInstance().getResInt("Board.Space.Color.Intermediate")), // Промежуточный.
            new Color(CResourse.getInstance().getResInt("Board.Space.Color.End"))           // Конечный.
    };
    /**
     * Ключ изображения доски
     */
    static final protected String strBoard = "Path.Image.Board";
    /**
     * Ключ изображения фигур.
     */
    static final protected String strFigures = "Path.Image.Figures";

    /**
     * Доступ к классу ресурсов.
     */
    protected CResourse resourse;
    /**
     * Доступ к классу контроллера.
     */
    protected CSMControl csmControl;
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
    /**
     * Массив клеток доски.
     */
    protected Color[][] boardSpacesColor;

    public CViewBoard(LayoutManager layout, boolean isDoubleBuffered) {
        super(layout, isDoubleBuffered);
        initViewBoard();
    }

    public CViewBoard(LayoutManager layout) {
        super(layout);
        initViewBoard();
    }

    public CViewBoard(boolean isDoubleBuffered) {
        super(isDoubleBuffered);
        initViewBoard();
    }

    public CViewBoard() {
        super();
        initViewBoard();
    }

    /**
     * Начальная инициализация класса.
     */
    protected void initViewBoard() {
        // Базовые настройки.
        csmControl = csmControl.getInstance();
        resourse = CResourse.getInstance();
        boardSpacesColor = new Color[8][4];
        clearBoardSpacesColor();
        offsetBaseX = resourse.getResInt("Board.Margin.Left");
        offsetBaseY = resourse.getResInt("Board.Margin.Top");
        spaceW = resourse.getResInt("Board.Space.Width");
        spaceH = resourse.getResInt("Board.Space.Height");
        imgDX = resourse.getResInt("Board.Space.Margin.Left");
        imgDY = resourse.getResInt("Board.Space.Margin.Top");

        // Создание интерфейса.
        int width = resourse.getImage(strBoard).getImage().getWidth(null);
        int height = resourse.getImage(strBoard).getImage().getHeight(null);
        setMinimumSize(new Dimension(width, height));
        setMaximumSize(new Dimension(width, height));
        setBounds(0, 0, width, height);

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
     * Преобразует координаты экрана в координаты доски.
     * @param x координата x экрана
     * @param y координата y экрана
     * @return Pair с координатами доски
     */
    protected CPair<Integer, Integer> coordImgToBoard(int x, int y) {
        int bx = (x - offsetBaseX) / spaceW;
        int by = 7 - (y - offsetBaseY) / spaceH;
        return new CPair<>(bx, by);
    }

    /**
     * Обработка нажатий клавишы мыши на доске.
     * @param e       событие нажатие мыши
     * @param isClick true - мышь нажата, false - мышь отпущена
     */
    protected void mouseAction(MouseEvent e, boolean isClick) {
        CPair<Integer, Integer> pos = coordImgToBoard(e.getX(), e.getY());
        int bx = pos.getFirst();
        int by = pos.getSecond();
        if (!csmControl.getBoard().aField(bx, by)) return;

        if (csmControl.getIsEdition()) {
            // TODO: Обработка нажатий мыши для режима редактирования.
            if (isClick) {
                boardSpacesColor[by][bx / 2] = spaceFrameColor[1];
            } else {
                boardSpacesColor[by][bx / 2] = spaceFrameColor[0];
            }
            repaint();
        } else if (csmControl.getBoard().getStateGame()) {
            // TODO: Обработка нажатий мыши для режима игры.
        }
    }

    /**
     * Цвет всех рамок переходит в базовое состояни.
     */
    protected void clearBoardSpacesColor() {
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 4; x++) {
                boardSpacesColor[y][x] = spaceFrameColor[0];
            }
        }
    }

    /**
     * Возвращает смещение в изображении фигур.
     * @param fig объект фигуры
     * @return смещение
     */
    protected int figureOffsetImage(IFigureBase fig) {
        int r = fig.getColorType() == ETypeColor.WHITE ? 0: 120;
        r += fig.getTypeFigure() == ETypeFigure.CHECKERS ? 0: 60;
        return r;
    }

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g.drawImage(resourse.getImage(strBoard).getImage(), 0, 0, null);
        g2.drawRect(0, 0, resourse.getImage(strBoard).getImage().getWidth(null) - 1,
                resourse.getImage(strBoard).getImage().getHeight(null) - 1);
//        BufferedImage img = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
        g2.setStroke(new BasicStroke(2));
        int imgW = resourse.getImage(strFigures).getImage().getWidth(null) / 4;
        int imgH = resourse.getImage(strFigures).getImage().getHeight(null);
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 4; x++) {
                int rx = 2 * x + y % 2;
                int grX = offsetBaseX + rx * spaceW;
                int grY = offsetBaseY + (7 - y) * spaceH;
                IFigureBase fig = csmControl.getBoard().getFigure(rx, y);
                if (fig != null) {
                    int iDx = figureOffsetImage(fig);
                    g2.drawImage(resourse.getImage(strFigures).getImage(), grX + imgDX, grY + imgDY, grX + imgDX + imgW, grY + imgDY + imgH, iDx, 0, iDx + imgW, imgH, null);
                }
                assert (boardSpacesColor[y][x].equals(spaceFrameColor[0]));
                g2.setColor(boardSpacesColor[y][x]);
                g2.drawRect(grX, grY, spaceW - 1, spaceH - 1);
            }
        }
    }
}
