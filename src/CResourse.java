import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.List;

/**
 * Класс получения ресурсов.
 */
public class CResourse {
//        Main.class.getResource("Resource/imgs/checkers.png"); - Доступ к пути ресурсов по url или получить поток ввода - .getResourceAsStream.
    private CResourse() {
        lstProperty = new LinkedList<>();
        cacheImage = new HashMap<>();
        cacheText = new HashMap<>();
        cacheListStr = new HashMap<>();
        cacheInt = new HashMap<>();
        cacheDouble = new HashMap<>();
    }
    private static class CResourseHolder {
        private static final CResourse INSTANCE = new CResourse();
    }
    public static CResourse getInstance() {
        return CResourse.CResourseHolder.INSTANCE;
    }

    /**
     * Класс свойств
     */
    private List<Properties> lstProperty;
    /**
     * Словарь запрашиваемых изображений.
     */
    private Map<String, ImageIcon> cacheImage;
    /**
     * Словарь запрашиваемых строк.
     */
    private Map<String, String> cacheText;
    /**
     * Словарь запрашиваемых списков строк.
     */
    private Map<String, List<String>> cacheListStr;
    /**
     * Словарь запрашиваемых чисел.
     */
    private Map<String, Integer> cacheInt;
    /**
     * Словарь запрашиваемых вещественных чисел.
     */
    private Map<String, Double> cacheDouble;

    /**
     * Перекодирование получаемых строк.
     * @param str порлученная строка
     * @return перекодированная строка
     */
    private String encoding(String str) {
        String res = "";
        try {
            res = new String(str.getBytes("ISO-8859-1"), "UTF-8");
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException("Encoding not supported", e);
        }
        return res;
    }

    /**
     * Поиск ресурса по ключу.
     * @param key ключ ресурса
     * @return строка ресурса
     */
    private String searchResource(String key) {
        String res = null;
        for (Properties pr: lstProperty) {
            if (pr.containsKey(key)) {
                res = pr.getProperty(key);
                break;
            }
        }
        return res;
    }

    /**
     * Очистка ресурсов.
     */
    public void clearResourse() {
        lstProperty.clear();
        cacheImage.clear();
        cacheText.clear();
        cacheListStr.clear();
        cacheInt.clear();
        cacheDouble.clear();
    }

    /**
     * Добавление новых ресурсов.
     * @param nameResource имя добавляемого ресурса
     */
    public void addResourse(String nameResource) {
        Properties property = new Properties();
        try {
            InputStream fis = Main.class.getResourceAsStream(nameResource);
            property.load(fis);
        } catch (IOException e) {
            System.err.println("ОШИБКА: Файл свойств отсуствует!");
        }
        cacheImage.clear();
        cacheText.clear();
        cacheInt.clear();
        cacheDouble.clear();
        lstProperty.add(0, property);
    }

    /**
     * Получение значение строки из файла ресурса по ключу.
     * @param key ключ ресурса
     * @return получаемая строка
     */
    public String getResStr(String key) {
        if (!cacheText.containsKey(key)) {
            String res = searchResource(key);
            if (null == res) return null;
            res = encoding(res);
            cacheText.put(key, res);
        }
        return cacheText.get(key);
    }

    /**
     * Возвращает список строк находящийся уже в памяти.
     * @param key ключ ресурса
     * @return список строк, или null, если в памяти его нет
     */
    public List<String> getListStr(String key) {
        if (!cacheListStr.containsKey(key)) return null;
        return cacheListStr.get(key);
    }

    /**
     * Возвращает список строк.
     * @param key   ключ ресурса
     * @param delim разделитель
     * @return список строк или null
     */
    public List<String> getListStr(String key, String delim) {
        if (!cacheListStr.containsKey(key)) {
            String res = searchResource(key);
            if (null == res) return null;
            res = encoding(res);
            cacheListStr.put(key, Arrays.asList(res.split(delim)));
        }
        return cacheListStr.get(key);
    }

    /**
     * Получение целого числа из файла ресурса по ключу.
     * @param key ключ ресурса
     * @return получаемое число
     */
    public Integer getResInt(String key) {
        if (!cacheInt.containsKey(key)) {
            Integer rI;
            String res = searchResource(key);
            if (null == res) return null;
            res = res.toLowerCase();
            boolean isHex = false;
            if (res.charAt(0) == '#') {
                isHex = true;
                res = res.substring(1);
            } else if (res.charAt(0) == '0' && res.charAt(1) == 'x') {
                isHex = true;
                res = res.substring(2);
            }
            if (isHex) rI = Integer.parseInt(res, 16);
            else rI = Integer.parseInt(res);
            cacheInt.put(key, rI);
        }
        return cacheInt.get(key);
    }

    /**
     * Получение вещественного числа двойной точности по ключу.
     * @param key ключ
     * @return получаемое вещественное число двойной точности
     */
    public Double getResDouble(String key) {
        if (!cacheDouble.containsKey(key)) {
            String res = searchResource(key);
            if (null == res) return null;
            cacheDouble.put(key, Double.parseDouble(res));
        }
        return cacheDouble.get(key);
    }

    /**
     * Возвращает иконку на основе ключа ресурсов.
     * @param nameKey ключ имени иконки
     * @return изображение иконки
     */
    public ImageIcon getImage(String nameKey) {
        if (!cacheImage.containsKey(nameKey)) {
            String fullName = getResStr(nameKey);
            cacheImage.put(nameKey, new ImageIcon(Main.class.getResource(fullName)));
        }
        return cacheImage.get(nameKey);
    }

    /**
     * Создание отдельных еденичных изображений, взятых из одного большого изображения.
     * @param nameMultiImg  ключ ресурсов на основное, большое изображение
     * @param namePreffix   ключ ресурсов на имена ключей отдельных изображений разделенных пробелом
     * @param namePositions ключ ресурсов на прямоугольные области еденичных изображений
     */
    public void createMultiImage(String nameMultiImg, String namePreffix, String namePositions) {
        String fullName = getResStr(nameMultiImg);
        ImageIcon fullImage = new ImageIcon(Main.class.getResource(fullName));
        List<String> fullPreffixs = getListStr(namePreffix, " ");
        if (null == fullPreffixs) return;
        List<String> fullStringRect = getListStr(namePositions, " ");
        if (null == fullStringRect) return;
        int k = 0;
        for (String pref: fullPreffixs) {
            Integer x = Integer.parseInt(fullStringRect.get(k++));
            Integer y = Integer.parseInt(fullStringRect.get(k++));
            Integer w = Integer.parseInt(fullStringRect.get(k++));
            Integer h = Integer.parseInt(fullStringRect.get(k++));
            BufferedImage bImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            Graphics g = bImg.getGraphics();
            g.drawImage(fullImage.getImage(), 0, 0, w, h, x, y, x + w, y + h, null);
            g.dispose();
            cacheImage.put(pref, new ImageIcon(fullImage.getImage()));
        }
    }
}
