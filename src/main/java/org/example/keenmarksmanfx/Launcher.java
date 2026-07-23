package org.example.keenmarksmanfx;

import javafx.application.Application;

public class Launcher {
    static void main(String[] args) {
        Application.launch(GameApplication.class, args);
    }
}

// browser game?
// TODO: client [local actions] <-> server [update UI]
/*
 *
 * GameWorld -> server
 * [objects, data, UI info]
 *
 * GameApp -> client
 * [commands, UI, Game Interface]
 *
 *                   CLIENT                                              SERVER
 * [receive answer -> input request -> send request] -> [receive request -> handle request -> send answer] |
 * ^                                                                                                       |
 * |_______________________________________________________________________________________________________V
 *
 * клиент запускается
 * если сервер не запущен, то нажатие игровых клавиш ведет к получению пустого ответа (ничего не происходит)
 * если сервер запущен, нажатие клавиш ведет к обработке запроса, и обновлению UI
 *  - спрайты хранятся у клиента
 *  - информация о их положении (метаданные?)- на сервере
 *
 *
 * вместо полноценной клиент-серверной архитектуры - запуск потоков сервера и клиента в одноим процессе,
 * но обмен осуществлется как в КС архитектуре
 */

/*
 * atomicreference
 *
 * обновление графики лучше сделать в одном потоке, в другом - вычисление
 *
 *
 *
 */