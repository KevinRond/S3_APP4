package Couches;

public class CoucheReseau extends Couche {
    private static CoucheReseau instance;
    public static CoucheReseau getInstance(){
        return instance == null ? instance = new CoucheReseau() : instance;
    }

    @Override
    protected void recevoirDeSup(byte[] PDU) {
        envoieVersInf(PDU);
    }

    @Override
    protected void recevoirDeInf(byte[] PDU) throws ErreurDeTransmission {
        envoieVersSup(PDU);
    }
}
