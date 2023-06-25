package Couches;

public class CoucheReseau extends Couche {
    private static CoucheReseau instance;
    public static CoucheReseau getInstance(){
        return instance == null ? instance = new CoucheReseau() : instance;
    }

    @Override
    protected void recevoirDeCoucheSup(byte[] PDU) {
        envoieVersCoucheInf(PDU);
    }

    @Override
    protected void recevoirDeCoucheInf(byte[] PDU) throws ErreurDeTransmission {
        envoieVersCoucheSup(PDU);
    }
}
