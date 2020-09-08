/**
 * Интерфейс для классов упраявляемых контроллером.
 */
public interface IChangeState {
    /**
     * Метод изменяющий состояние на новое/
     */
    void makeChangesState(TStateGame curStateGame, TActionGame actionGame);
}
