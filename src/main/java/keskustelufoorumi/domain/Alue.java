package keskustelufoorumi.domain;


public class Alue {
    
    private String aluenimi;
    private int alueviestimaara;
    
    public Alue(String aluenimi, int alueviestimaara) {
        this.aluenimi = aluenimi;
        this.alueviestimaara = alueviestimaara;
        
    }
    
    public String getAluenimi() {
        return aluenimi;
    }
    
    public int getAlueviestimaara() {
        return alueviestimaara;
    }
    
    public void lisaaViestimaaraa() {
        this.alueviestimaara++;
    }
    
}
