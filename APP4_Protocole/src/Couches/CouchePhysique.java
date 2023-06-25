package Couches;

import java.io.IOException;
import java.net.*;

public class CouchePhysique extends Couche {
    InetAddress adresse = null;
    int port = 0;
    protected ThreadReception thread;
    public int delai = 0;
    public int erreurDelai = -1;
    public int packetEnvoye = 0;
    private static CouchePhysique instance;
    private CouchePhysique(){}
    static public CouchePhysique getInstance(){
        return instance == null ? new CouchePhysique() : instance;
    }
    public void setAdresseDestination(String adresse){
        try {
            this.adresse = InetAddress.getByName(adresse);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
    public void setPortDestination(int port){
        this.port = port;
    }
    public void start(){
        thread.running = true;
        thread.start();
    }
    public void stop(){
        thread.running = false;
        thread.stop();
    }
    public boolean isThreadRunning() {
        return thread.running;
    }
    @Override
    protected void recevoirDeCoucheSup(byte[] PDU) {
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        packetEnvoye++;
        if (packetEnvoye == erreurDelai)
            PDU[10] <<= 1;

        DatagramPacket packet = new DatagramPacket(PDU, PDU.length, adresse, port);
        try {
            socket.send(packet);
            Thread.sleep(delai);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void recevoirDeCoucheInf(byte[] PDU) throws ErreurDeTransmission {
        envoieVersCoucheSup(PDU);
    }
    public void createThreadReception(int port) throws IOException {
        this.thread = new ThreadReception(port, this);
    }
    public class ThreadReception extends Thread {
        protected DatagramSocket socket = null;
        private CouchePhysique parent;
        public boolean running = true;
        public ThreadReception(int port, CouchePhysique parent) throws SocketException {
            super("FTP Thread Reception " + Math.random());
            socket = new DatagramSocket(port);
            this.parent = parent;
        }
        public void run(){
            while (running) {
                try {
                    byte[] buf = new byte[204];

                    //Recoit requete
                    DatagramPacket packet = new DatagramPacket(buf, buf.length);
                    socket.receive(packet);

                    //Envoie packet de donnee au parent
                    parent.recevoirDeCoucheInf(packet.getData());

                } catch (IOException | ErreurDeTransmission e) {
                    //throw new RuntimeException(e);
                    running = false;
                    socket.close();
                    System.out.println(e.getLocalizedMessage());
                }
            }
        }
    }
}
