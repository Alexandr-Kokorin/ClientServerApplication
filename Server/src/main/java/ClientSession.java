import lombok.extern.log4j.Log4j;

import java.io.IOException;
import java.net.Socket;

@Log4j
public class ClientSession extends Thread {

    private final Socket socket;
    private final ClientCommandManager manager;
    private String name;

    public ClientSession(Socket socket, Library library) {
        this.socket = socket;
        manager = new ClientCommandManager(library);
        name = null;
    }

    @Override
    public void run() {
        doWork();
        try {
            socket.close();
        } catch (IOException e) {
            log.error("Failed to close socket.");
        }
    }

    private void doWork() {
        try (Connector connector = new Connector(socket)) {
            boolean isExit = false;
            while (name == null) {
                String[] command = connector.readLine().split("=");
                if (command.length == 1 && command[0].equalsIgnoreCase("exit")) {
                    isExit = true;
                    break;
                } else if (command.length == 2 && command[0].equalsIgnoreCase("login -u")) {
                    name = command[1];
                    connector.writeLine("Welcome, " + name + "!");
                } else connector.writeLine("It's not possible to use command until you are logged in.");
            }
            String command;
            while (!isExit && !(command = connector.readLine()).equalsIgnoreCase("exit")) {
                manager.sortCommand(command, connector, name);
            }
            connector.writeLine("Closing...");
        } catch (IOException e) {
            log.error("Connection to the client is lost.");
        }
    }
}
