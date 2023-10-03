import lombok.Data;

@Data
public class ServerConfig {

    private final int port;
    private boolean stopFlag;

    public ServerConfig() {
        port = 8084;
        stopFlag = false;
    }
}
