import javax.swing.*;

/**
 * Класс вывода игровой доски и её элементов.
 */
public class ViewBoard extends JWindow {

    /**
     * Доступ к классу контроллера.
     */
    protected STMControl stmControl;

    public ViewBoard() {
        stmControl = STMControl.getInstance();
    }
}
