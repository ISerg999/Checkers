package CheckersEngine.BaseEngine;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Класс контролирующий проделанные ходы и изменения на игровой доске.
 * Класс очищается если начинается новая игра.
 */
public class CControlMoveGame implements Iterable<String> {

    static protected final String CMD_REMOVE = "r";     // Удаление фигуры на доске по координатам x, y.
    static protected final String CMD_SET = "s";        // Установка фигуры на доске по координатам x, y. Фигура по её типу и цвету.
    static protected final String CMD_MOVE = "m";       // Ход игрока: x1, y1, x2, y2, convert.
    static protected final String CMD_ATTACK = "a";     // Атака игрока x1, y1, x2, y2, convert, list[(x, y, xt, yt)].

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
    /**
     * Базовое состояние доски.
     * 0 - очищино, 1 - начальная расстановка.
     */
    protected int baseState;

    public CControlMoveGame(CEngineBoard board) {
        this.board = board;
        clearControlMove();
        placementBoard();
        baseState = 1;
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
        baseState = 0;
        lstGameAction.clear();
    }

    /**
     * Базовая разстановка фигур на доске.
     */
    public void placementBoard() {
        board.placementBoard();
        baseState = 1;
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
        IFigureBase oldF = board.getFigure(x, y);
        cmd = cmd + convertFigureToStr(oldF.getTypeFigure(), oldF.getColorType());
        board.setFigure(x, y, null, null);
        curGameAction++;
        lstGameAction.add(cmd);
    }

    /**
     * Помещает фигуру в клетку доски с заданными координатами.
     * @param x          координата x клетка доски
     * @param y          координата y клетка доски
     * @param typeFigure тип помещаемой фигуры
     * @param typeColor  цвет помещаемой фигуры
     */
    public void setSpaceBoard(int x, int y, ETypeFigure typeFigure, ETypeColor typeColor) {
        String cmd = "" + CMD_SET + coordIntToStr(x, true) + coordIntToStr(y,false);
        cmd = cmd + convertFigureToStr(typeFigure, typeColor);
        IFigureBase oldF = board.getFigure(x, y);
        if (null == oldF) cmd = cmd + convertFigureToStr(null, null);
        else cmd = cmd + convertFigureToStr(oldF.getTypeFigure(), oldF.getColorType());
        board.setFigure(x, y, typeFigure, typeColor);
        curGameAction++;
        lstGameAction.add(cmd);
    }

    /**
     * Ход игрока.
     * @param xs        координата x начальная
     * @param ys        координата y начальная
     * @param xe        координата x конечная
     * @param ye        координата y конечная
     * @param isConvert true - шашка становится дамкой, false - изменение не произошло
     */
    public void movePlayer(int xs, int ys, int xe, int ye, boolean isConvert) {
        ETypeFigure tf;
        ETypeColor tc;
        String cmd = "" + CMD_MOVE + coordIntToStr(xs, true) + coordIntToStr(ys, false);
        cmd = cmd + coordIntToStr(xe, true) + coordIntToStr(ye, false);
        cmd = cmd + (isConvert ? "!": " ");
        IFigureBase oldF = board.getFigure(xs, ys);
        if (null == oldF) {
            tf = null;
            tc = null;
        } else {
            tf = oldF.getTypeFigure();
            tc = oldF.getColorType();
        }
        cmd = cmd + convertFigureToStr(tf, tc);
        board.setFigure(xs, ys, null, null);
        if (isConvert) tf = ETypeFigure.QUINE;
        board.setFigure(xe, ye, tf, tc);
        curGameAction++;
        lstGameAction.add(cmd);
    }

    /**
     * Атака игрока.
     * @param xs        координата x начальная
     * @param ys        координата y начальная
     * @param xe        координата x конечная
     * @param ye        координата y конечная
     * @param isConvert true - шашка становится дамкой, false - изменение не произошло
     * @param lstDid    список убитых фигур и следующая клетка (xf, yf, xn, yn).
     */
    public void atackPlayer(int xs, int ys, int xe, int ye, boolean isConvert, List<Integer[]> lstDid) {
        ETypeFigure tf;
        ETypeColor tc;
        String cmd = "" + CMD_ATTACK + coordIntToStr(xs, true) + coordIntToStr(ys, false);
        cmd = cmd + coordIntToStr(xe, true) + coordIntToStr(ye, false);
        cmd = cmd + (isConvert ? "!": " ");
        IFigureBase oldF = board.getFigure(xs, ys);
        if (null == oldF) {
            tf = null;
            tc = null;
        } else {
            tf = oldF.getTypeFigure();
            tc = oldF.getColorType();
        }
        cmd = cmd + convertFigureToStr(tf, tc);
        board.setFigure(xs, ys, null, null);
        if (isConvert) tf = ETypeFigure.QUINE;
        board.setFigure(xe, ye, tf, tc);
        for (Integer[] ifc: lstDid) {
            cmd = cmd + coordIntToStr(ifc[0], true) + coordIntToStr(ifc[1], false);
            cmd = cmd + coordIntToStr(ifc[2], true) + coordIntToStr(ifc[3], false);
            oldF = board.getFigure(ifc[0], ifc[1]);
            cmd = cmd + convertFigureToStr(oldF.getTypeFigure(), oldF.getColorType());
            board.setFigure(ifc[0], ifc[1], null, null);
        }
        curGameAction++;
        lstGameAction.add(cmd);
    }

    /**
     * Возвращает длину списка ходов.
     * @return размер списка ходов
     */
    public int size() {
        return lstGameAction.size();
    }

    /**
     * Возвращает информацию о проделанных игровых ходах в бинарном виде.
     * @return информация о проделанных игровых ходах в бинарном виде
     */
    public List<Integer> getListBin() {
        String cmd;
        List<Integer> binOut = new LinkedList<>();
        binOut.add(baseState);
        int x = lstGameAction.size();
        int y = (x >> 8) & 0xff;
        x = x & 0xff;
        binOut.add(y);
        binOut.add(x);
        for (String str: lstGameAction) {
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
        baseState = inBin.get(index++);
        int lnLst = (inBin.get(index++) << 8) + inBin.get(index++);
        lstGameAction.clear();
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
            lstGameAction.add(cmd);
        }
        return index;
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
     * @param typeFigure тип фигуры, или null если пустой объект
     * @param typeColor  тип цвета
     * @return строковое представление
     */
    protected String convertFigureToStr(ETypeFigure typeFigure, ETypeColor typeColor) {
        if (null == typeFigure) return "--";
        String res = ETypeFigure.CHECKERS == typeFigure ? "c": "q" ;
        return res + (ETypeColor.WHITE == typeColor ? "w": "b");
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
     * Возвращает заданное изменение.
     * @param i номер изменения
     * @return содержимое измененния
     */
    public String at(int i) {
        return lstGameAction.get(i);
    }

    @NotNull
    @Override
    public Iterator<String> iterator() {
        return lstGameAction.iterator();
    }
}
