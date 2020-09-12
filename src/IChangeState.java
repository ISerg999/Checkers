import CheckersEngine.BaseEngine.CPair;

/**
 * Интерфейс управелние работой.
 */
public interface IChangeState {
    /**
     * Метод обрабатывающий состояния перехеода.
     * @param pStM структура состоящая из текущего состояния и указания перехода
     */
    void makeChangesState(CPair<ETStateGame, ETActionGame> pStM);
}
