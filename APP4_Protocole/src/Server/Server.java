package Server;
import java.io.*;

public class Server {
    public static void main(String[] args) throws IOException {
        new ServerThread().start();
        //System.out.println("Current Working Directory: " + System.getProperty("user.dir"));

    }
}