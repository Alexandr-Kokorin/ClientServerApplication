import java.io.IOException;
import java.net.Socket;

public class Client {

    private final ClientConfig config;

    public Client() {
        config = new ClientConfig();
    }

    public void start() {
        try (Socket socket = new Socket(config.getHost(), config.getPort())) {
            doWork(socket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void doWork(Socket socket) {
        System.out.println("The client has been successfully launched!");
    }
}
