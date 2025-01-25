/* package telran.net;

import java.util.Scanner;

public class TcpServerShutdownTest {

    public static void main(String[] args) {
        Protocol protocol = null;
        TcpServer server = new TcpServer(protocol, 8080);

        Thread serverThread = new Thread(server);
        serverThread.start();

        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Type 'shutdown' to stop the server.");
            while (true) {
                String command = scanner.nextLine();
                if ("shutdown".equalsIgnoreCase(command)) {
                    server.shutdown();
                    System.out.println("Server is shutting down...");
                    break;
                }
            }
        }
    }
} */
