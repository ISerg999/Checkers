import javax.swing.*;
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
     * Словарь запрашиваемых чисел.
     */
    private Map<String, Integer> cacheInt;
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

}
