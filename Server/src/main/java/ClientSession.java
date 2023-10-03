import java.io.IOException;
import java.net.Socket;

public class ClientSession extends Thread {

    private final Socket socket;
    private final Library library;

    public ClientSession(Socket socket, Library library) {
        this.socket = socket;
        this.library = library;
    }

    @Override
    public void run() {
        doWork();
        try {
            socket.close();
        } catch (IOException e) {
            e.getStackTrace();
        }
    }

    private void doWork() {
    }
}
