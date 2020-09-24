import CheckersEngine.BaseEngine.CPair;
import CheckersEngine.BaseEngine.ETypeColor;
import CheckersEngine.BaseEngine.ETypeFigure;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Класс вывода игровой доски и её элементов.
 */
public class CViewBoard extends JPanel implements IChangeState {

    private static final Map<CPair<ETStateGame, ETActionGame>, String> stateAction;
    static {
        stateAction = new Hashtable<>();
        stateAction.put(new CPair<>(ETStateGame.BASE, ETActionGame.TOEDITING), "initEditingMode");
        stateAction.put(new CPair<>(ETStateGame.GAME, ETActionGame.TONEXTSTEPGAME), "nextStepGame");
    }
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
    /**
     * Выбранная клетка при редактировании.
     */
    protected int selectedX, selectedY;
    /**
     * Ссылка на объект правой панели режима редактирования.
     */
    protected CRightPanelEdition rPanelEdition;
    /**
     * Начальные выбранные координаты в режиме игры.
     */
    protected CPair<Integer, Integer> firstPos;

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

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        // Рисуем доску.
        g.drawImage(resourse.getImage(strBoard).getImage(), 0, 0, null);
        g2.drawRect(0, 0, resourse.getImage(strBoard).getImage().getWidth(null) - 1,
                resourse.getImage(strBoard).getImage().getHeight(null) - 1);
//        BufferedImage img = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
        g2.setStroke(new BasicStroke(2));
        int imgW = resourse.getImage(strFigures).getImage().getWidth(null) / 4;
        int imgH = resourse.getImage(strFigures).getImage().getHeight(null);
        // Рисуем квадраты доски и фигуры в них.
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
                if (csmControl.getIsEdition()) {
                    // Рисуем цвет квадратов в режиме редактирования.
                    if (selectedX == rx && selectedY == y) g2.setColor(spaceFrameColor[1]);
                    else g2.setColor(spaceFrameColor[0]);
                } else {
                    // Рисуем цвет квадратов в других режимах.
                    g2.setColor(boardSpacesColor[y][x]);
                }
                g2.drawRect(grX, grY, spaceW - 1, spaceH - 1);
            }
        }
    }

    /**
     * Обработка события нажатий на клавиатуре в режиме редактирования.
     * @param e событие клавиатуры.
     */
    public void keyActionEdition(KeyEvent e) {
        if (KeyEvent.VK_DELETE == e.getKeyCode() && selectedX >= 0 && selectedY >= 0) {
            csmControl.getCMoveGame().setSpaceBoard(selectedX, selectedY, null, null);
            repaint();
        }
    }

    @Override
    public void makeChangesState(CPair<ETStateGame, ETActionGame> pStM) {
        if (null == pStM.getFirst()) return;
        String funName = stateAction.getOrDefault(pStM, null);
        if (null != funName) {
            try {
                Method method = this.getClass().getDeclaredMethod(funName);
                method.setAccessible(true);
                method.invoke(this);
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Начальная инициализация класса.
     */
    protected void initViewBoard() {
        // Базовые настройки.
        csmControl = csmControl.getInstance();
        csmControl.addActionGame(this);
        resourse = CResourse.getInstance();
        boardSpacesColor = new Color[8][4];
        clearBoardSpacesColor();
        offsetBaseX = resourse.getResInt("Board.Margin.Left");
        offsetBaseY = resourse.getResInt("Board.Margin.Top");
        spaceW = resourse.getResInt("Board.Space.Width");
        spaceH = resourse.getResInt("Board.Space.Height");
        imgDX = resourse.getResInt("Board.Space.Margin.Left");
        imgDY = resourse.getResInt("Board.Space.Margin.Top");
        rPanelEdition = null;

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
     * Добавляет объект правой панели редактирования.
     * @param rPanelEdition правая панель редактирования
     */
    protected void setPanelEdition(CRightPanelEdition rPanelEdition) {
        this.rPanelEdition = rPanelEdition;
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
            if (isClick) {
                selectedX = bx;
                selectedY = by;
                if (null != rPanelEdition) {
                    CPair<ETypeFigure, ETypeColor> cp = rPanelEdition.selectedFigure();
                    if (null == cp) return;
                    csmControl.getCMoveGame().setSpaceBoard(bx, by, cp.getFirst(), cp.getSecond());
                }
            }
            repaint();
        } else if (csmControl.getBoard().getStateGame()) {
            if (!csmControl.getPlayForColor(csmControl.getBoard().getCurMove())) return;
            if (!isClick) return;
            if (csmControl.getBoard().lstStartMoveGameField().contains(pos)) {
                firstPos = pos;
                choiceColorFrame();
                repaint();
            } else if (null != firstPos && csmControl.getBoard().lstEndMoveGameField(firstPos.getFirst(), firstPos.getSecond()).contains(pos)) {
                List<CPair<Integer, Integer>> newStep = csmControl.getBoard().lstGameStepInfo(firstPos, pos);
                csmControl.getCMoveGame().gameMoveFigure(newStep);
                csmControl.makeChangesState(ETActionGame.TONEXTSTEPGAME, false);
                if (csmControl.getBoard().getStateGame())
                    csmControl.makeChangesState(ETActionGame.TONEXTSTEPGAMEWIN, false);
            }
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

    /**
     * Выбирает цвет рамки поля игры в режиме игры.
     */
    protected void choiceColorFrame() {
        clearBoardSpacesColor();
        List<CPair<Integer, Integer>> lst = csmControl.getBoard().lstStartMoveGameField();
        if (null == lst) return;
        for (CPair<Integer, Integer> el: lst) {
            if (null == firstPos || (el.equals(firstPos)))
                boardSpacesColor[el.getSecond()][el.getFirst() / 2] = spaceFrameColor[1];
        }
        if (null == firstPos) return;
        lst = csmControl.getBoard().lstIntermediateMoveGameField(firstPos.getFirst(), firstPos.getSecond());
        if (null != lst && lst.size() > 0) {
            for (CPair<Integer, Integer> el: lst) {
                boardSpacesColor[el.getSecond()][el.getFirst() / 2] = spaceFrameColor[2];
            }
        }
        lst = csmControl.getBoard().lstEndMoveGameField(firstPos.getFirst(), firstPos.getSecond());
        if (null != lst) {
            for (CPair<Integer, Integer> el: lst) {
                boardSpacesColor[el.getSecond()][el.getFirst() / 2] = spaceFrameColor[3];
            }
        }
    }

    // ---------------------------- Методы обрабатывающиеся контроллером переходов состояний ---------------------------

    /**
     * Инициализация при переходе в режим редактирования.
     */
    protected void initEditingMode() {
        selectedX = -1;
        selectedY = -1;
    }

    /**
     * Переход к следующему ходу.
     */
    public void nextStepGame() {
        firstPos = null;
        clearBoardSpacesColor();
        csmControl.getBoard().nextStep();
        if (csmControl.getBoard().getStateGame()) {
            if (csmControl.getPlayForColor(csmControl.getBoard().getCurMove())) {
                choiceColorFrame();
            }
        } else {
            int r = csmControl.getBoard().getStateGameStop();
            if (r == 0) csmControl.makeChangesState(ETActionGame.TOBASEFROMGAMEDRAW, false);
            else if (r == 1) csmControl.makeChangesState(ETActionGame.TOBASEFROMGAMEWHILE, false);
            else if (r == 2) csmControl.makeChangesState(ETActionGame.TOBASEFROMGAMEBLACK, false);
        }
        repaint();
    }

    /**
     * Выход из режима игры.
     */
    protected void closeGameMode() {
        clearBoardSpacesColor();
    }

}
