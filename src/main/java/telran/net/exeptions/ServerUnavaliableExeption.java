package telran.net.exeptions;

// Наша кастомная ошибка, просто вызывает суперкласс и передаёт сообщение
public class ServerUnavaliableExeption extends IllegalStateException {

    public ServerUnavaliableExeption(String host, int port) {
        super(String.format("Server %s on port %d is unavaliable, try to reconnect later", host, port));
    }
}
