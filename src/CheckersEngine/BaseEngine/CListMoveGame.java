package CheckersEngine.BaseEngine;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;

/**
 * Класс хранения ходов игры.
 */
public class CListMoveGame implements Iterable<String[]> {

    static protected final List<Character> validSymbol;
    static {
        validSymbol = new LinkedList<>();
        validSymbol.add(' ');
        validSymbol.add('-');
        validSymbol.add(':');
    }

    protected List<String[]> lstMoves;

    public CListMoveGame() {
        lstMoves = new LinkedList<>();
        clear();
    }

    /**
     * Очистка списка хранения ходов игры.
     */
    public void clear() {
        lstMoves.clear();
    }

    /**
     * Возвращает упакованный список.
     * @return упакованный список.
     */
    public List<Integer> getListPack() {
        int sC;
        List<Integer> lstPack = new LinkedList<>();
        int sA = lstMoves.size();
        int sB = sA & 0xff;
        sA = (sA >> 8) & 0xff;
        lstPack.add(sA);
        lstPack.add(sB);
        for (String[] isHalfStep: lstMoves) {
            List<Integer> one = new LinkedList<>();
            sA = isHalfStep.length - 1;
            one.add(sA);

            sA = (coordStrToInt(isHalfStep[0].substring(1, 2)) << 4 + coordStrToInt(isHalfStep[0].substring(0, 1))) & 0xff;
            sB = (coordStrToInt(isHalfStep[0].substring(4, 5)) << 4 + coordStrToInt(isHalfStep[0].substring(3, 4))) & 0xff;
            sC = (isHalfStep[0].length() == 6) && isHalfStep[5] == "!" ? 1: 0;
            one.add(sA);
            one.add(sB);
            one.add(sC);

            for (int i = 1; i < isHalfStep.length; i++) {
                sA = (coordStrToInt(isHalfStep[i].substring(1, 2)) << 4 + coordStrToInt(isHalfStep[i].substring(0, 1))) & 0xff;
                sB = isHalfStep[i].charAt(2) == 'q'? 1: 0;
                one.add(sA);
                one.add(sB);
            }
            lstPack.addAll(one);
        }
        return lstPack;
    }

    /**
     * Установить список ходов из упакованного списка.
     * @param lstPack упакованный список
     * @param k       начало списка ходов
     */
    public void setListPack(List<Integer> lstPack, int k) {
        int sA, sB;
        List<String[]> newLstMoves = new LinkedList<>();
        int lenStr = lstPack.get(k) << 8 + lstPack.get(k + 1);
        k += 2;
        for (int i = 0; i < lenStr; i++) {
            int lenOne = lstPack.get(k++) + 1;
            String[] strLstOne = new String[lenOne];

            String tmp = "";
            sA = lstPack.get(k++);
            sB = sA & 0xf;
            sA = (sA >> 4) & 0xf;
            tmp = tmp + coordIntToStr(sB, true);
            tmp = tmp + coordIntToStr(sA, false);
            tmp = tmp + (lenOne > 1 ? ":": "-");
            sA = lstPack.get(k++);
            sB = sA & 0xf;
            sA = (sA >> 4) & 0xf;
            tmp = tmp + coordIntToStr(sB, true);
            tmp = tmp + coordIntToStr(sA, false);
            sA = lstPack.get(k++);
            if (sA > 0) tmp = tmp + "!";
            strLstOne[0] = tmp;

            for (int j = 1; j < lenOne; j++) {
                tmp = "";
                sA = lstPack.get(k++);
                sB = sA & 0xf;
                sA = (sA >> 4) & 0xf;
                tmp = tmp + coordIntToStr(sB, true);
                tmp = tmp + coordIntToStr(sA, false);
                sA = lstPack.get(k++);
                tmp = tmp + (sA > 0 ? "q": "c");
                strLstOne[j] = tmp;
            }
            newLstMoves.add(strLstOne);
        }
        lstMoves = newLstMoves;
    }

    /**
     * Преобразует строковую координату в числовую.
     * @param s строковая координата
     * @return числовая координата
     */
    protected int coordStrToInt(String s) {
        int tmp;
        s = String.valueOf(s.charAt(0));
        try {
            tmp = Integer.parseInt(s) - 1;
            return tmp;
        } catch (NumberFormatException e) {
            tmp = s.codePointAt(0) - String.valueOf('a').codePointAt(0);
            return tmp;
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
     * Проверка строки на правильность формата.
     * @param str   проверяемая строка
     * @param first если true, то проверяет ход игрока, иначе, если false, проверяет данные об убитых шашках.
     * @return true - проверка прошла успешна, false - проверка провалилась
     */
    protected boolean pareserStrStep(String str, boolean first) {
        if (null == str) return false;

        int ti = coordStrToInt(str.substring(0, 1));
        if (ti < 0 || ti > 7) return false;
        ti = coordStrToInt(str.substring(1, 2));
        if (ti < 0 || ti > 7) return false;

        if (first) {
            if (str.length() < 5 || str.length() > 6) return false;
            if (!validSymbol.contains(str.charAt(2))) return false;
            ti = coordStrToInt(str.substring(3, 4));
            if (ti < 0 || ti > 7) return false;
            ti = coordStrToInt(str.substring(4, 5));
            if (ti < 0 || ti > 7) return false;
            if (str.length() == 6 && str.charAt(5) != '!') return false;
        }
        else {
            if (str.length() != 3) return false;
            if (str.charAt(2) != 'c' && str.charAt(2) != 'q') return false;
        }

        return true;
    }

    /**
     * Добавление нового полухода.
     * @param infoStep Список данных описывающий ход. Каждый элемент состоит из списка строк, где 1-й элемент
     *                 (и возможно единственный) имеет формат "xy*xy?". Где первые xy - начальные координаты хода,
     *                 а вторые xy - конечные координаты хода. * - рзделитель, может иметь значение ' ', '-' или ':'.
     *                 ? - элемент может не быть, если он есть, то знак '!' означающий превращение шашки в дамку.
     *                 Следующие элементы нужны, если произошла атака и имеют формат "xyc", где xy - координаты битой
     *                 шашки, c - тип битой шашки: 'c' - шашка, 'q' - дамка.
     * @return возвращает true, ход добавлен успешно, иначе false
     */
    public boolean addHalfMove(String[] infoStep) {
        String[] newStep = new String[infoStep.length];
        String sTmp = infoStep[0].toLowerCase();
        if (!pareserStrStep(sTmp, true)) return false;
        newStep[0] = sTmp;
        for (int i = 1; i < infoStep.length; i++) {
            sTmp = infoStep[i].toLowerCase();
            if (!pareserStrStep(sTmp, false)) return false;
            newStep[i] = sTmp;
        }
        lstMoves.add(newStep);
        return true;
    }

    /**
     * Возвращает длину списка ходов.
     * @return длина списка ходов
     */
    public int size() {
        return lstMoves.size();
    }

    /**
     * Возвращает заданный полуход
     * @param i номер полухода
     * @return полуход
     */
    public String[] at(int i) {
        return lstMoves.get(i);
    }

    /**
     * Удаляет полуходы, находящиеся после заданной позиции.
     * @param i последняя позиция
     */
    public void clearAfte(int i) {
        while (lstMoves.size() > i + 1) lstMoves.remove(lstMoves.size() - 1);
    }

    @NotNull
    @Override
    public Iterator<String[]> iterator() {
        return lstMoves.iterator();
    }

}
