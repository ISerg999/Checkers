import CheckersEngine.CheckersBoard;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.Properties;

/**
 * Класс управляющий игрой на основе конечных автоматов.
 */
public class STMControl {

    private STMControl() {
//        Main.class.getResource("Resource/imgs/checkers.png"); - Доступ к пути ресурсов по url или получить поток ввода - .getResourceAsStream.
        property = new Properties();
        try {
            InputStream fis = Main.class.getResourceAsStream("Resource/gameres.properties");
            System.out.println(Main.class.getResource("Resource/gameres.properties"));
            property.load(fis);
        } catch (IOException e) {
            System.err.println("ОШИБКА: Файл свойств отсуствует!");
        }
        checkersBoard = new CheckersBoard();
        curStateGame = TStateGame.BASE;
    }

    private static class STMControlHolder {
        private static final STMControl INSTANCE = new STMControl();
    }

    public static STMControl getInstance() {
        return STMControlHolder.INSTANCE;
    }

    private Properties property;
    /**
     * Класс управления игрой на доске.
     */
    private CheckersBoard checkersBoard;
    /**
     * Текущее состояние программы.
     */
    private TStateGame curStateGame;
    /**
     * Главное окно.
     */
    private JFMainWindow winApp;

    /**
     * Действия необходимые для перехода в базовый режим.
     */
    private TStateGame stepToBase() {
        winApp.stepToBase();
        return TStateGame.BASE;
    }

    public void start() {
        winApp = new JFMainWindow();
        stepToBase();
    }

    /**
     * Получение значение строки из файла ресурса по ключу.
     * @param key ключ ресурса
     * @return получаемая строка
     */
    public String getResStr(String key) {
        String res = "";
        try {
            res = new String(property.getProperty(key).getBytes("ISO-8859-1"), "UTF-8");
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException("Encoding not supported", e);
        }
        return res;
    }

    /**
     * Получение целого числа из файла ресурса по ключу.
     * @param key ключ ресурса
     * @return получаемое число
     */
    public Integer getResInt(String key) {
        String res = property.getProperty(key);
        return Integer.parseInt(res);
    }

    /**
     * Получение вещественного числа двойной точности по ключу.
     * @param key ключ
     * @return получаемое вещественное число двойной точности
     */
    public Double getResDouble(String key) {
        String res = property.getProperty(key);
        return Double.parseDouble(res);
    }
}
