package CheckersEngine.BaseEngine;

import java.util.LinkedList;
import java.util.List;

/**
 * Класс контролирующий проделанные ходы и изменения на игровой доске.
 * Класс очищается если начинается новая игра.
 */
public class CControlMoveGame {

    static protected final String CMD_REMOVE = "r";     // Удаление фигуры на доске по координатам x, y.
    static protected final String CMD_SET = "s";        // Установка фигуры на доске по координатам x, y. Фигура по её типу и цвету.
    static protected final String CMD_MOVE = "m";       // Ход игрока: [[x1, y1], [x2, y2], [xc, yc]]
    static protected final String CMD_ATTACK = "a";     // Атака игрока [[x1, y1], [x2, y2], [xc, yc], ...[[x, y], [xt, yt]]... .

    /**
     * Объект доски.
     */
    protected CEngineBoard board;
    /**
     * Список игровых действий с игрой.
     */
    protected List<String> lstGameAction;
    /**
     * Указатель на текущее изменение.
     */
    protected int curGameAction;

    public CControlMoveGame(CEngineBoard board) {
        this.board = board;
        clearControlMove();
        placementBoard();
    }

    /**
     * Очистка списка произведённых действий.
     */
    public void clearControlMove() {
        lstGameAction = new LinkedList<>();
        curGameAction = -1;
    }

    /**
     * Возвращает объект игровой доски.
     * @return объект игровой доски
     */
    public CEngineBoard getBoard() {
        return board;
    }

    /**
     * Очистка игровой доски.
     */
    public void clearBoard() {
        board.clearBoard();
        curGameAction = -1;
        lstGameAction.clear();
    }

    /**
     * Базовая разстановка фигур на доске.
     */
    public void placementBoard() {
        board.placementBoard();
        curGameAction = -1;
        lstGameAction.clear();
    }

    /**
     * Удаление фигуры с игровой доски в заданных координатах.
     * @param x координата x клетки
     * @param y координата y клетки
     */
    public void removeSpaceBoard(int x, int y) {
        String cmd = "" + CMD_REMOVE + coordIntToStr(x, true) + coordIntToStr(y,false);
        cmd = cmd + convertFigureToStr(board.getF(x, y));
        board.setF(x, y, null);
        add(cmd);
    }

    /**
     * Помещает фигуру в клетку доски с заданными координатами.
     * @param x      координата x клетка доски
     * @param y      координата y клетка доски
     * @param figure помещаемая фигура
     */
    public void setSpaceBoard(int x, int y, CPair<ETypeFigure, ETypeColor> figure) {
        String cmd = "" + CMD_SET + coordIntToStr(x, true) + coordIntToStr(y,false);
        cmd = cmd + convertFigureToStr(figure);
        cmd = cmd + convertFigureToStr(board.getF(x, y));
        board.setF(x, y, figure);
        add(cmd);
    }

    /**
     * Ход фигурой. ((x1, y1), (x2, y2), [(xc, yc)], ...[ (xf, yf), (xn, yn)). ]...): (x1, y1) - начальная позиция,
     * (x2, y2) - конечная позиция, позиция, [(xc, yc)] - в которой фигура становиться дамкой, если null или отсутствует,
     * то становление дамкой не происходит, (xf, tf) - координаты убиваемой фигуры, (xn, yn) - координаты куда помещается
     * фигура во время убийства другой фигуры.
     * @param lstFullMove список данных по атаке игрока.
     */
    public synchronized void gameMoveFigure(List<CPair<Integer, Integer>> lstFullMove) {
        if (lstFullMove.size() < 2) return;
        String cmd = "" + (lstFullMove.size() > 3 ? CMD_ATTACK: CMD_MOVE);
        cmd = cmd + movePlayer(lstFullMove);
        if (lstFullMove.size() > 3) cmd = cmd + atackPlayer(lstFullMove);
        add(cmd);
    }

    /**
     * Возвращает длину списка ходов.
     * @return размер списка ходов
     */
    public int size() {
        return curGameAction + 1;
    }

    /**
     * Возвращает информацию о проделанных игровых ходах в бинарном виде.
     * @return информация о проделанных игровых ходах в бинарном виде
     */
    public List<Integer> getListBin() {
        String cmd;
        List<Integer> binOut = new LinkedList<>();
        int x = curGameAction + 1;
        int y = (x >> 8) & 0xff;
        x = x & 0xff;
        binOut.add(y);
        binOut.add(x);
        for (int i = 0; i <= curGameAction; i++) {
            String str = lstGameAction.get(i);
            cmd = str.substring(0, 1);
            binOut.add(cmd.codePointAt(0));
            str = str.substring(1);
            if (CMD_REMOVE.equals(cmd)) {
                binOut.add(xyStrToInt(str, 0));
                binOut.add(figureStrToInt(str, 2));
            } else if (CMD_SET.equals(cmd)) {
                binOut.add(xyStrToInt(str, 0));
                binOut.add(figureStrToInt(str, 2));
                binOut.add(figureStrToInt(str, 4));
            } else if (CMD_MOVE.equals(cmd)) {
                binOut.add(xyStrToInt(str, 0));
                binOut.add(xyStrToInt(str, 2));
                binOut.add(str.charAt(4) == '!' ? 1: 0);
                binOut.add(figureStrToInt(str, 5));
            } else if (CMD_ATTACK.equals(cmd)) {
                binOut.add(xyStrToInt(str, 0));
                binOut.add(xyStrToInt(str, 2));
                binOut.add(str.charAt(4) == '!' ? 1: 0);
                binOut.add(figureStrToInt(str, 5));
                binOut.add(0);
                int k = 7;
                int indxS = binOut.size() - 1;
                while (k < str.length()) {
                    binOut.add(xyStrToInt(str, k));
                    binOut.add(xyStrToInt(str, k + 2));
                    binOut.add(figureStrToInt(str, k + 4));
                    k += 6;
                    indxS++;
                }
                binOut.set(k, indxS);
            }
        }
        return binOut;
    }

    /**
     * Восстанавливает информацию о проделанных игровых ходах в бинарном виде.
     * @param inBin информация о проделанныхигровых ходах в бинарном виде
     * @param index индекс с которого будет получать данные
     * @return индекс следующий за полученными данными
     */
    public int setListBin(List<Integer> inBin, int index) {
        String cmd;
        int lnLst = (inBin.get(index++) << 8) + inBin.get(index++);
        clearControlMove();
        for (; lnLst > 0; lnLst--) {
            cmd = new String(Character.toChars(inBin.get(index++)));
            if (CMD_REMOVE.equals(cmd)) {
                cmd = cmd + xyIntToStr(inBin.get(index++)) + figureIntToStr(inBin.get(index++));
            } else if (CMD_SET.equals(cmd)) {
                cmd = cmd + xyIntToStr(inBin.get(index++)) + figureIntToStr(inBin.get(index++));
                cmd = cmd + figureIntToStr(inBin.get(index++));
            } else if (CMD_MOVE.equals(cmd)) {
                cmd = cmd + xyIntToStr(inBin.get(index++)) + xyIntToStr(inBin.get(index++));
                cmd = cmd + (inBin.get(index++) > 0 ? "!": " ") + figureIntToStr(inBin.get(index++));
            } else if (CMD_ATTACK.equals(cmd)) {
                cmd = cmd + xyIntToStr(inBin.get(index++)) + xyIntToStr(inBin.get(index++));
                cmd = cmd + (inBin.get(index++) > 0 ? "!": " ") + figureIntToStr(inBin.get(index++));
                for (int lnS = inBin.get(index++); lnS > 0; lnS--) {
                    cmd = cmd + xyIntToStr(inBin.get(index++)) + xyIntToStr(inBin.get(index++));
                    cmd = cmd + figureIntToStr(inBin.get(index++));
                }
            }
            add(cmd);
        }
        return index;
    }

    /**
     * Возвращает заданное изменение.
     * @param i номер изменения
     * @return содержимое измененния
     */
    public String at(int i) {
        if (i < 0 || i > curGameAction) return null;
        return lstGameAction.get(i);
    }

    /**
     * Установка новой игровой доски.
     * @param newBoard игровая доска
     */
    public void setBoard(CEngineBoard newBoard) {
        if (board == newBoard) return;
        board = newBoard;
    }

    /**
     * Откат назад.
     */
    public void back() {
        if (curGameAction < 0) return;
        int x, y, x2, y2;
        ETypeFigure tf;
        ETypeColor tc;
        String strAction = lstGameAction.get(curGameAction);
        curGameAction--;
        String cmd = strAction.substring(0, 1);
        strAction = strAction.substring(1);
        if (CMD_REMOVE.equals(cmd)) {
            x = coordStrToInt(strAction, 0);
            y = coordStrToInt(strAction, 1);
            if ('-' == strAction.charAt(2)) board.setF(x, y, null);
            else {
                tf = 'q' == strAction.charAt(2) ? ETypeFigure.QUINE: ETypeFigure.CHECKERS;
                tc = 'w' == strAction.charAt(3) ? ETypeColor.WHITE: ETypeColor.BLACK;
                board.setF(x, y, new CPair<>(tf, tc));
            }
        } else if (CMD_SET.equals(cmd)) {
            x = coordStrToInt(strAction, 0);
            y = coordStrToInt(strAction, 1);
            if ('-' == strAction.charAt(4)) board.setF(x, y, null);
            else {
                tf = 'q' == strAction.charAt(4) ? ETypeFigure.QUINE: ETypeFigure.CHECKERS;
                tc = 'w' == strAction.charAt(5) ? ETypeColor.WHITE: ETypeColor.BLACK;
                board.setF(x, y, new CPair<>(tf, tc));
            }
        } else if (CMD_MOVE.equals(cmd) || CMD_ATTACK.equals(cmd)) {
            x = coordStrToInt(strAction, 0);
            y = coordStrToInt(strAction, 1);
            x2 = coordStrToInt(strAction, 3);
            y2 = coordStrToInt(strAction, 4);
            tf = 'q' == strAction.charAt(6) ? ETypeFigure.QUINE: ETypeFigure.CHECKERS;
            tc = 'w' == strAction.charAt(7) ? ETypeColor.WHITE: ETypeColor.BLACK;
            board.setF(x2, y2, null);
            board.setF(x, y, new CPair<>(tf, tc));
            if (CMD_ATTACK.equals(cmd)) {
                strAction = strAction.substring(8);
                while (strAction.length() > 0) {
                    x = coordStrToInt(strAction, 0);
                    y = coordStrToInt(strAction, 1);
                    tf = 'q' == strAction.charAt(4) ? ETypeFigure.QUINE: ETypeFigure.CHECKERS;
                    tc = 'w' == strAction.charAt(5) ? ETypeColor.WHITE: ETypeColor.BLACK;
                    board.setF(x, y, new CPair<>(tf, tc));
                    strAction = strAction.substring(6);
                }
            }
        }
    }

    /**
     * Добавление нового полухода.
     * @param cmd полуход
     */
    protected void add(String cmd) {
        curGameAction++;
        if (curGameAction < lstGameAction.size()) {
            lstGameAction.set(curGameAction, cmd);
        } else {
            lstGameAction.add(cmd);
        }
    }

    /**
     * Преобразует числовую координату в строковую.
     * @param a        числовая координата
     * @param isSymbol тип строки: true - числа, false - буквы
     * @return строковая координата
     */
    protected String coordIntToStr(int a, boolean isSymbol) {
        int[] code = {"1".codePointAt(0), "a".codePointAt(0)};
        int t = isSymbol ? 1: 0;
        return  "" + Character.toString((a + code[t]));
    }

    /**
     * Преобразует пару координат в строку.
     * @param xy байт с координатами
     * @return строка с координатами
     */
    protected String xyIntToStr(int xy) {
        int y = (xy >> 8) & 0xf;
        int x = xy & 0xf;
        return coordIntToStr(x, true) + coordIntToStr(y, false);
    }

    /**
     * Преобразует тип и цвет фигуры в строку.
     * @param figure фигура, или null если пустой объект
     * @return строковое представление
     */
    protected String convertFigureToStr(CPair<ETypeFigure, ETypeColor> figure) {
        if (null == figure) return "--";
        String res = ETypeFigure.CHECKERS == figure.getFirst() ? "c": "q" ;
        return res + (ETypeColor.WHITE == figure.getSecond() ? "w": "b");
    }

    /**
     * Преобразует числовое представление фигуры в строку.
     * @param a числовое представление
     * @return представление в виде строки
     */
    protected String figureIntToStr(int a) {
        if (a == 0) return "--";
        String res = "" + (a > 31 ? "q": "c");
        a = a & 0xf;
        return res + (a > 1 ? "w": "b");
    }

    /**
     * Преобразует строковую координату в числовую.
     * @param sa строка
     * @param i положение координаты в строке
     * @return числовая координата
     */
    protected int coordStrToInt(String sa, int i) {
        int res;
        String tmp = String.valueOf(sa.charAt(i));
        try {
            res = Integer.parseInt(tmp) - 1;
        } catch (NumberFormatException e) {
            res = tmp.codePointAt(0) - String.valueOf('a').codePointAt(0);
        }
        return res;
    }

    /**
     * Преобразует пар координат в байт.
     * @param s пара координат в строковой форме
     * @param i смещение в строке
     * @return байт с координатами
     */
    protected int xyStrToInt(String s, int i) {
        int x = coordStrToInt(s, i);
        int y = coordStrToInt(s, i + 1);
        return ((y << 8) + x) & 0xff;
    }

    /**
     * Преобразует строковое представление фигуры в байт.
     * @param sa строковое представление фигуры
     * @param i  смещение внутри строки
     * @return байт представляющий собой фигурой
     */
    protected int figureStrToInt(String sa, int i) {
        if ("-".equals(sa.substring(i, i + 1))) return 0;
        return ("c".equals(sa.substring(i, i + 1)) ? 16: 32) + ("b".equals(sa.substring(i + 1, i + 2))? 1: 2);
    }

    /**
     * Преобразование параметров хода.
     * @param lstMove список координат - параметры хода.
     * @return результат преобразования
     */
    protected String movePlayer(List<CPair<Integer, Integer>> lstMove) {
        CPair<ETypeFigure, ETypeColor> fig;
        String cmd = "" + coordIntToStr(lstMove.get(0).getFirst(), true) + coordIntToStr(lstMove.get(0).getSecond(), false);
        cmd = cmd + (lstMove.size() > 3 ? ":": "-");
        cmd = cmd + coordIntToStr(lstMove.get(1).getFirst(), true) + coordIntToStr(lstMove.get(1).getSecond(), false);
        cmd = cmd + (lstMove.size() > 2 && null != lstMove.get(2) ? "!": " ");
        fig = board.getF(lstMove.get(0));
        cmd = cmd + convertFigureToStr(fig);
        board.setF(lstMove.get(0), null);
        if (lstMove.size() > 2 && null != lstMove.get(2)) fig.setFirst(ETypeFigure.QUINE);
        board.setF(lstMove.get(1), fig);
        return cmd;
    }

    /**
     * Преобразование параметров атаки.
     * @param lstAtack список координат - параметры атаки с 3-й позиции.
     * @return результат преобразования
     */
    protected String atackPlayer(List<CPair<Integer, Integer>> lstAtack) {
        String cmd = "";
        CPair<ETypeFigure, ETypeColor> fig;
        for (int ind = 3; ind < lstAtack.size(); ind += 2) {
            cmd = cmd + coordIntToStr(lstAtack.get(ind).getFirst(), true) + coordIntToStr(lstAtack.get(ind).getSecond(), false);
            cmd = cmd + coordIntToStr(lstAtack.get(ind + 1).getFirst(), true) + coordIntToStr(lstAtack.get(ind + 1).getSecond(), false);
            fig = board.getF(lstAtack.get(ind));
            cmd = cmd + convertFigureToStr(fig);
            board.setF(lstAtack.get(ind), null);
        }
        return cmd;
    }

}
