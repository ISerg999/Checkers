import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.*;
import java.util.List;

/**
 * Класс получения ресурсов.
 */
public class CResourse {
//        Main.class.getResource("Resource/imgs/checkers.png"); - Доступ к пути ресурсов по url или получить поток ввода - .getResourceAsStream.

    private CResourse() {
        lstProperty = new LinkedList<>();
    }

    private static class CResourseHolder {
        private static final CResourse INSTANCE = new CResourse();
    }
    public static CResourse getInstance() {
        return CResourse.CResourseHolder.INSTANCE;
    }

    private List<Properties> lstProperty;

    /**
     * Очистка ресурсов.
     */
    public void clearResourse() {
        lstProperty.clear();
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
        lstProperty.add(0, property);
    }

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
     * Получение значение строки из файла ресурса по ключу.
     * @param key ключ ресурса
     * @return получаемая строка
     */
    public String getResStr(String key) {
        String res = searchResource(key);
        if (res != null) res = encoding(res);
        return res;
    }

    /**
     * Получение целого числа из файла ресурса по ключу.
     * @param key ключ ресурса
     * @return получаемое число
     */
    public Integer getResInt(String key) {
        String res = searchResource(key);
        if (res != null) return Integer.parseInt(res);
        return null;
    }

    /**
     * Получение вещественного числа двойной точности по ключу.
     * @param key ключ
     * @return получаемое вещественное число двойной точности
     */
    public Double getResDouble(String key) {
        String res = searchResource(key);
        if (res != null) return Double.parseDouble(res);
        return null;
    }

    /**
     * Возвращает иконку на основе ключа ресурсов.
     * @param nameKey ключ имени иконки
     * @return изображение иконки
     */
    public ImageIcon getIcon(String nameKey) {
        String fullName = getResStr(nameKey);
        return new ImageIcon(Main.class.getResource(fullName));
    }

    public Image getImage(String nameKey) {
        String fullName = getResStr(nameKey);
        Image res = null;
        try {
            res = ImageIO.read(new URL(fullName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }
}
