import lombok.Data;

@Data
public class ClientConfig {

    private final String host;
    private final int port;

    public ClientConfig() {
        host = "localhost";
        port = 8084;
    }
}
