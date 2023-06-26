package Couches;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import static java.lang.System.arraycopy;

/**
 * Implementation de couche au niveau application.
 */
public class CoucheApplication extends Couche {
    private static CoucheApplication instance;
    public static CoucheApplication getInstance(){
        return instance == null ? instance = new CoucheApplication() : instance;
    }

    /**
     * Recois un PDU de la couche au dessus
     * S'il n'y a pas de couche au dessus, le programme principale appel la methode @EnvoieFichier.
     */
    @Override
    protected void recevoirDeCoucheSup(byte[] PDU) {
        // Aucune couche superieur
    }

    /**
     * Receive packet from the Transport Layer. Then creates a file in the
     * `dest` directory, and dumps the content of the packet in it.
     * @param PDU   Contains the name of the file in the first 188 bytes,
     *              and the data from the file on the other bytes.
     */
    @Override
    protected void recevoirDeCoucheInf(byte[] PDU) {
        System.out.println("Receiving");
        String title = new String(Arrays.copyOfRange(PDU, 0, 188), StandardCharsets.US_ASCII).trim();
        byte[] data_bytes = Arrays.copyOfRange(PDU, 188, PDU.length);
        try {
            String filePath = new File("").getAbsolutePath();
            File file = new File(filePath + "/dest/" + title);
            if(file.exists())
                file.delete();
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
            } else {
                System.out.println("File already exists.");
            }
            try (FileOutputStream fos = new FileOutputStream(file.getPath())) {
                System.out.println("Writing stream.");
                fos.write(data_bytes);
                System.out.println("Done writing.");
            }
        } catch (Exception e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    /**
     * Send a file to the transport layer.
     * @param path  File to transmit.
     */
    public void EnvoieFichier(String path) throws IOException, InterruptedException {
        File fichier = new File(path);
        byte[] APDU;
        byte[] nomFichier = fichier.getName().getBytes();
        Path pathFichier = fichier.toPath();
        byte[] bytesFichier = Files.readAllBytes(pathFichier);
        APDU = new byte[188 + bytesFichier.length];
        arraycopy(nomFichier, 0, APDU, 0, nomFichier.length);
        arraycopy(bytesFichier, 0, APDU, 188, bytesFichier.length);
        envoieVersCoucheInf(APDU);
        Thread.sleep(1000);
        System.exit(0);
    }
}
