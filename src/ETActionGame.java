/**
 * Произведенное действие
 */
public enum ETActionGame {
    TOBASE,                 // Переход к базовому состоянию после запуска игры.
    TOREPAIN,               // Перерисовка изображения.
    TOABOUT,                // Запуск диалогового окна "О программе".
    TOEDITING,              // Переход в режим редактирования.
    TOBOARDPLACEMANT,       // Базовая разстановка фигур на доске.
    TOBOARDCLEAR,           // Очистка доски.
    TOGAME,                 // Переход в режим игры.
    TONEXTSTEPGAME,         // К следующему ходу игры.
    TONEXTSTEPGAMEWIN,      // К следующему ходу игры для основного окна.
    BACKSPACEMOVEGAME,      // Отступ в списке измененений на игровом поле.
    TOBASEGAMESTOP,         // Конец игры. Прерывание по требованию пользователя.
    TOCONTINUEGAME,         // Продолжение остановленной игры.
    TOBASEFROMGAMEDRAW,     // Конец игры. Ничья.
    TOBASEFROMGAMEWHILE,    // Конец игры. Белые выйграли.
    TOBASEFROMGAMEBLACK,    // Конец игры. Чёрные выйграли выйграли.
//    TOSAVE,                 // Сохранение содержимого доски и состояния игры.
//    TOSAVEOK,               // Сообщение о том, что произошло сохранение данных на диск.
//    TOLOAD,                 // Загрузка содержимого доски и состояния игры.
//    TOLOADOK,               // Сообщение о том, что произошло загрузка данных с диска.
//    TOWHITEPLAYER,          // Игрок играет за белых.
//    TOBLACKPLAYER,          // Игрок играет за чёрных.
//    TOWHITECOMP,            // За белых играет компьютер.
//    TOBLACKCOMP,            // За чёрных играет компьютер.
    TORETURN;               // Возвращение назад.
}
