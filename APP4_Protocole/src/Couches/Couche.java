package Couches;

public abstract class Couche {
    private Couche coucheSup;
    private Couche coucheInf;

    protected abstract void recevoirDeSup(byte[] PDU);
    protected abstract void recevoirDeInf(byte[] PDU);
    protected void envoieVersSup(byte[] PDU){
        coucheSup.recevoirDeInf(PDU);
    }
    protected void envoieVersInf(byte[] PDU){
        coucheInf.recevoirDeSup(PDU);
    }
    public void setCoucheSup(Couche coucheSup){
        this.coucheSup = coucheSup;
    }
    public void setCoucheInf(Couche coucheInf){
        this.coucheInf = coucheInf;
    }
}
