package Server;

import java.io.IOException;

public class Server {
    public static void main(String[] args) throws IOException {
        String port = args[0];
        InstanceServer instance = new InstanceServer(port);
        instance.DemarrageServer();
        System.out.println("Terminer");
        //System.out.println("Current Working Directory: " + System.getProperty("user.dir"));

    }
}