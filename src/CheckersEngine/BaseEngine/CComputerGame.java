package CheckersEngine.BaseEngine;

/**
 * Класс игры в шашки компьютером.
 */
public class CComputerGame implements Runnable {

    /**
     * Пауза.
     */
    protected static final long TIMING_FPS = 1000000000 / 20;
    /**
     * Объект управляемый игровой доски и её содержимым.
     */
    protected CControlMoveGame controlMoveGame;
    /**
     * Управляющий работой игрового цикла.
     */
    protected boolean isWhile;
    /**
     * Выполняемый шаг.
     */
    protected int step;
    /**
     * Определяет является ли текущий ход компьютера, или нет.
     */
    protected boolean isMove;

    public CComputerGame(CControlMoveGame controlMoveGame) {
        this.controlMoveGame = controlMoveGame;
        isWhile = true;
        step = 0;
        isMove = false;
    }

    /**
     * Устанавливает разрешение на игру.
     * @param isMove true - разрешает игру за компьютер, false - запрещает.
     */
    public void setIsMove(boolean isMove) {
        this.isMove = isMove;
    }

    /**
     * Проверка на разрешение обработки хода.
     * @return true - обработка разрешена, false - обработка запрещена.
     */
    public boolean isMove() {
        if (controlMoveGame.getBoard().getGameOn() && isMove) return true;
        return false;
    }

    /**
     * Пауза.
     * @param timePause период паузы
     */
    public void pause(long timePause) {
        if (timePause <= 0) return;
        try {
            Thread.sleep(timePause);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Завершение работы класа обработки ходов.
     */
    public void close() {
        isWhile = false;
    }

    @Override
    public void run() {
        long delta, now;
        long lastTime = System.nanoTime();
        while (isWhile) {
            if (isMove()) {
                switch (step) {
                    case 0: // Подготовка к поиску хода.
                        break;
                    case 1: // Поиск хода.
                        break;
                    case 2: // Совершения хода.
                        break;
                }
            }
            now = System.nanoTime();
            delta = now - lastTime;
            lastTime = now;
            pause((TIMING_FPS - delta) / 1000000);
        }
    }
}
