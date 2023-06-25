package Couches;

public abstract class Couche {
    private Couche coucheSup;
    private Couche coucheInf;

    protected abstract void recevoirDeCoucheSup(byte[] PDU);
    protected abstract void recevoirDeCoucheInf(byte[] PDU) throws ErreurDeTransmission;
    protected void envoieVersCoucheSup(byte[] PDU) throws ErreurDeTransmission{
        coucheSup.recevoirDeCoucheInf(PDU);
    }
    protected void envoieVersCoucheInf(byte[] PDU){
        coucheInf.recevoirDeCoucheSup(PDU);
    }
    public void setCoucheSup(Couche coucheSup){
        this.coucheSup = coucheSup;
    }
    public void setCoucheInf(Couche coucheInf){
        this.coucheInf = coucheInf;
    }
}
