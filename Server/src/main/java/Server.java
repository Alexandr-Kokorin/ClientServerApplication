import lombok.extern.log4j.Log4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

@Log4j
public class Server {

    private final ServerConfig config;
    private final Library library;
    private final ServerCommandManager manager;

    public Server() {
        config = new ServerConfig();
        library = new Library();
        manager = new ServerCommandManager(library);
    }

    public void start() {
        new Thread(this::doWork).start();
        try (ServerSocket serverSocket = new ServerSocket(config.getPort())) {
            log.debug("The server has been successfully started!");
            log.debug("Waiting connection on port: " + config.getPort());
            while (!config.isStopFlag()) {
                Socket clientSocket = serverSocket.accept();
                log.debug("New client connected to server!");
                ClientSession clientSession = new ClientSession(clientSocket, library);
                clientSession.start();
            }
        } catch (IOException e) {
            log.error("The server crashed.");
        }
    }

    private void doWork() {
        try (Scanner scanner = new Scanner(System.in)) {
            while (!config.isStopFlag()) {
                String command = scanner.nextLine();
                if (command.equalsIgnoreCase("exit")) {
                    config.setStopFlag(true);
                    System.out.println("Closing...");
                    log.debug("The server is closed.");
                }
                else manager.sortCommand(command);
            }
            System.exit(0);
        }
    }

}
