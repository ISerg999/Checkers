package CheckersEngine.BaseEngine;

/**
 * Интерфейс для вызова события при завершения игры.
 */
public interface ICallableStopGame {
    /**
     * Обратная связь для вызова при завершения игры.
     * @param state победитель, либо null - ничья
     */
    void endStateGame(ETypeColor state);
}
