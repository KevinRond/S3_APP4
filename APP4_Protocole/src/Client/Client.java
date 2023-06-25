package Client;
import java.io.*;
import java.net.*;
import java.util.*;

public class Client {
    public static void main(String[] args) throws IOException, InterruptedException {
        String nom_fichier = args[0];
        String ip_destination = args[1];
        String port = args[2];
        boolean ajout_erreurs = Boolean.parseBoolean(args[3]);

        InstanceClient instance = new InstanceClient(nom_fichier, ip_destination, port, ajout_erreurs);
        instance.DemarrageClient();
    }
}
