package CheckersEngine.BaseEngine;

import CheckersEngine.CFigureCheckers;
import CheckersEngine.CFigureQuine;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Фабрика игровых фигур.
 */
public class CFactoryFigure {

    /**
     * Список объектов управляемых фигурами.
     */
    protected Map<ETypeFigure, IFigureBase> figures;
    protected Map<CPair<ETypeFigure, ETypeColor>, ImageIcon> imgFigures;

    private CFactoryFigure() {
        // Задаём объекты обрабатывающие логику фигуры.
        figures = new HashMap<>();
        figures.put(ETypeFigure.CHECKERS, new CFigureCheckers());
        figures.put(ETypeFigure.QUINE, new CFigureQuine());
        // Создаём места для изображения фигур.
        imgFigures = new HashMap<>();
        imgFigures.put(new CPair<>(ETypeFigure.CHECKERS, ETypeColor.WHITE), null);
        imgFigures.put(new CPair<>(ETypeFigure.QUINE, ETypeColor.WHITE), null);
        imgFigures.put(new CPair<>(ETypeFigure.CHECKERS, ETypeColor.BLACK), null);
        imgFigures.put(new CPair<>(ETypeFigure.QUINE, ETypeColor.BLACK), null);
    }
    private static class CFactoryFigureHolder {
        private static final CFactoryFigure INSTANCE = new CFactoryFigure();
    }
    public static CFactoryFigure getInstance() {
        return CFactoryFigureHolder.INSTANCE;
    }

    /**
     * Установка рисунка фигуры.
     * @param tf  тип фигуры
     * @param tc  цвет фигуры
     * @param img изображение фигуры
     */
    public void setImageFigure(ETypeFigure tf, ETypeColor tc, ImageIcon img) {
        imgFigures.replace(new CPair<>(tf, tc), img);
    }

    /**
     * Получение рисунка фигуры.
     * @param fg параметры фигуры
     * @return рисунок фигуры
     */
    public ImageIcon getImageFigure(CPair<ETypeFigure, ETypeColor> fg) {
        return imgFigures.get(fg);
    }

    /**
     * Получение рисунка фигуры.
     * @param tf тип фигуры
     * @param tc цвет фигуры
     * @return рисунок фигуры
     */
    public ImageIcon getImageFigure(ETypeFigure tf, ETypeColor tc) {
        return getImageFigure(new CPair<ETypeFigure, ETypeColor>(tf, tc));
    }

    /**
     * Получаем объект управления фигурой по его типу.
     * @param tf тип фигуры
     * @return объект управления фигурой
     */
    public IFigureBase getFigure(ETypeFigure tf) {
        return figures.get(tf);
    }

    /**
     * Проверка любых допустимых координат.
     * @param x координата x
     * @param y координата y
     * @return допустимось координат, true - допустимые координаты, false - не допустимые координаты
     */
    public boolean aPosition(int x, int y) {
        if (x < 0 || x > 7 || y < 0 || y > 7) return false;
        return true;
    }

    /**
     * Проверка любых допустимых координат.
     * @param pos позиция
     * @return допустимось координат, true - допустимые координаты, false - не допустимые координаты
     */
    public boolean aPosition(CPair<Integer, Integer> pos) {
        return aPosition(pos.getFirst(), pos.getSecond());
    }
}
