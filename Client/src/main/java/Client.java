import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private final ClientConfig config;

    public Client() {
        config = new ClientConfig();
    }

    public void start() {
        try (Socket socket = new Socket(config.getHost(), config.getPort())) {
            doWork(socket);
        } catch (IOException e) {
            System.out.println("Failed to connect to the server.");
        }
    }

    public void doWork(Socket socket) {
        System.out.println("The connection to the server is established. Welcome!");
        System.out.println("To get started, please log in:");
        try (Connector connector = new Connector(socket);
             Scanner scanner = new Scanner(System.in)) {
            while (!config.isStopFlag()) {
                String command = scanner.nextLine();
                connector.writeLine(command);
                StringBuilder result = new StringBuilder();
                do {
                    result.append(connector.readLine()).append("\n");
                } while (connector.ready());
                if (result.toString().equalsIgnoreCase("Closing...\n")) config.setStopFlag(true);
                System.out.print(result);
            }
        } catch (IOException e) {
            System.out.println("Connection to the server is lost.");
            System.out.println("Closing...");
        }
    }
}
