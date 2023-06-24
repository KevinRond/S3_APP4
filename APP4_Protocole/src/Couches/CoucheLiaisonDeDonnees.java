package Couches;
import java.util.zip.CRC32;
import static java.lang.System.arraycopy;

public class CoucheLiaisonDeDonnees extends Couche {
    private int erreursCrc = 0;
    private int paquetsRecus = 0;
    private int paquetsTransmis = 0;

    // Singleton
    private static CoucheLiaisonDeDonnees instance;
    private CoucheLiaisonDeDonnees() {}
    static public CoucheLiaisonDeDonnees getInstance() {
        return instance == null ? new CoucheLiaisonDeDonnees() : instance;
    }

    /**
     * Reset the statistics from the singleton
     */
    public void Reset() {
        erreursCrc = 0;
        paquetsRecus = 0;
        paquetsTransmis = 0;
    }


    /**
     * Receive data from the Physical Layer, check the CRC values, dump the
     * packet if the CRC fails, or transmits it to the Network layer if
     * successful.
     * @param PDU   Packet from the Physical Layer
     */
    @Override
    protected void ReceiveFromDown(byte[] PDU) throws ErreurDeTransmission {
        // Extract data from PDU
        byte[] paquet = new byte[PDU.length - 4];
        arraycopy(PDU, 4, paquet, 0, paquet.length);

        // Calculate & Check CRC
        CRC32 crc = new CRC32();
        crc.update(paquet);
        int valeurCrc = (int) crc.getValue();
        int oldCRC = (((int) PDU[0] << 24) & 0xFF000000) | (((int) PDU[1] << 16) & 0x00FF0000) | (((int) PDU[2] << 8) & 0x0000FF00) | ((int) PDU[3] & 0x000000FF);
        if (valeurCrc != oldCRC) {  // Error in CRC
            System.out.println("Error CRC32");
            erreursCrc++;
            return;                 // Drop packet
        }


        // Send PDU to network layer
        paquetsRecus++;
        envoieVersSup(paquet);
    }

    /**
     * Receive a packet to transmit from the Network layer, add a 32-bit CRC
     * to the front of it and transmits it to the Physical layer.
     * @param PDU
     */
    @Override
    protected void recevoirDeSup(byte[] PDU) {
        // Allocate new PDU
        byte[] trame = new byte[PDU.length + 4];

        // Calculate CRC using polynomial 0x82608EDB (default)
        CRC32 crc = new CRC32();
        crc.update(PDU);
        long valeurCrc = crc.getValue();
        byte[] CRCBytes = new byte[] {
                (byte) (valeurCrc >> 24),
                (byte) (valeurCrc >> 16),
                (byte) (valeurCrc >> 8),
                (byte) valeurCrc};
        arraycopy(CRCBytes, 0, trame, 0, CRCBytes.length);

        // Copy PDU into trame
        arraycopy(PDU, 0, trame, 4, PDU.length);

        // Send PDU to physical layer
        paquetsTransmis++;
        envoieVersInf(trame);
    }
}
