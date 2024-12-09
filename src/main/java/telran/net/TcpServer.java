package telran.net;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

// Runnable - функциональный интерфейс с единственным методом run(),
// Что-то просто может запускаться
public class TcpServer implements Runnable {

    Protocol protocol;
    int port;

    // Ограничение на количество потоков
    private final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    // Флаг завершения работы сервера. Если я правильно понимаю, вместо Atomic 
    // можно использовать модификатор доступа volatile для обычного boolean
    private final AtomicBoolean isShutdown = new AtomicBoolean(false);

    public TcpServer(Protocol protocol, int port) {
        this.protocol = protocol;
        this.port = port;
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on the port " + port);
            serverSocket.setSoTimeout(1000); // Маленький таймаут для graceful shutdown
            while (!isShutdown.get()) {
                try {
                    Socket socket = serverSocket.accept();
                    var session = new TcpClientServerSession(protocol, socket, isShutdown);
                    executor.execute(session); // Используем пул потоков
                } catch (SocketTimeoutException e) {
                    // Периодически проверяем на таймаут
                }
            }
        } catch (Exception e) {
            System.out.println("Error in server: " + e.getMessage());
        } finally {
            shutdownExecutor();
        }

    }

    public void shutdown() {
        isShutdown.set(true);
        shutdownExecutor();
    }

    private void shutdownExecutor() {
        executor.shutdownNow();
    }
}
