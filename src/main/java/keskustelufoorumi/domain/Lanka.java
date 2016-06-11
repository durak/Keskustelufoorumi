package keskustelufoorumi.domain;

public class Lanka {
    
    private String lankanimi;
    private Alue alue;
    private int lankaviestimaara;
    
    public Lanka() {        
    }
    
    public Lanka(String lankanimi, Alue alue, int lankaviestimaara) {
        this.lankanimi = lankanimi;
        this.alue = alue;
        this.lankaviestimaara = lankaviestimaara;        
    }
    
    // GETTERIT
    public String getLankanimi() {
        return lankanimi;
    }
    
    public Alue getAlue() {
        return alue;
    }
    
    public int getLankaviestimaara() {
        return lankaviestimaara;
    }
    
    
    
}
