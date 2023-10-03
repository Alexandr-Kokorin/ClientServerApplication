import lombok.extern.log4j.Log4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

@Log4j
public class Server {

    private final ServerConfig config;
    private final Library library;

    public Server() {
        config = new ServerConfig();
        library = new Library();
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(config.getPort())) {
            log.debug("The server has been successfully started!");
            while (!config.isStopFlag()) {
                log.debug("Waiting connection on port: " + config.getPort());
                Socket clientSocket = serverSocket.accept();
                log.debug("New client connected to server!");
                ClientSession clientSession = new ClientSession(clientSocket, library);
                clientSession.start();
            }
        } catch (IOException e) {
            log.error("The server crashed");
        }
    }
}
