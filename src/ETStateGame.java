/**
 * Состояние игры.
 */
public enum ETStateGame {
    BASE,       // Базовое состояние, (не игра и не редактирование).
    EDITING,    // Состояние редактирования игрового поля.
    GAME,       // Состояние игры.
    NONE;       // Любое состояние.
}
