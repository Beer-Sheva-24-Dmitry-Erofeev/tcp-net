package telran.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicBoolean;

public class TcpClientServerSession implements Runnable {

    private static final int MAX_ERRORS = 5;
    private static final int MAX_REQUESTS = 10;
    private static final int TIMEOUT = 30000;

    private final Protocol protocol;
    private final Socket socket;
    private final AtomicBoolean isShutdown;

    public TcpClientServerSession(Protocol protocol, Socket socket, AtomicBoolean isShutdown) {
        this.protocol = protocol;
        this.socket = socket;
        this.isShutdown = isShutdown;
    }

    @Override
    public void run() {
        int errorResponses = 0;
        int requestsPerSecond = 0;
        Instant lastRequestTime = Instant.now();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream())); PrintStream writer = new PrintStream(socket.getOutputStream())) {

            socket.setSoTimeout(TIMEOUT);

            String request;
            while ((request = reader.readLine()) != null) {
                // Проверка на завершение работы сервера
                if (isShutdown.get()) {
                    break;
                }

                // Проверка на количество запросов
                Instant now = Instant.now();
                if (now.isAfter(lastRequestTime.plusSeconds(1))) {
                    requestsPerSecond = 0;
                    lastRequestTime = now;
                }
                requestsPerSecond++;

                // Ответ на запрос
                String response = protocol.getResponseWithJSON(request);
                writer.println(response);

                if (response.contains("WRONG_DATA") || response.contains("WRONG_TYPE")) {
                    errorResponses++;
                }

                if (requestsPerSecond > MAX_REQUESTS || errorResponses > MAX_ERRORS) {
                    socket.close();
                    break;
                }
            }
        } catch (SocketTimeoutException e) {
            System.out.println("Session timeout: " + socket.getRemoteSocketAddress());
        } catch (Exception e) {
            System.out.println("Error in session: " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println("Error closing socket: " + e.getMessage());
            }
        }
    }

}
