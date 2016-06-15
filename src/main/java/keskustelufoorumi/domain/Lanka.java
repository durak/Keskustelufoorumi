package keskustelufoorumi.domain;

import java.sql.Timestamp;

public class Lanka {

    private int id;
    private String lankanimi;
    private Alue alue;
    private int lankaviestimaara;
    private Timestamp viimeisinAika;

    public Lanka() {
    }

    public Lanka(int id, String lankanimi, Alue alue, int lankaviestimaara, Timestamp viimeisinAika) {
        this.id = id;
        this.lankanimi = lankanimi;
        this.alue = alue;
        this.lankaviestimaara = lankaviestimaara;
        this.viimeisinAika = viimeisinAika;
    }

    /* 
    GETTERIT
    */
    public int getId() {
        return this.id;
    }

    public String getLankanimi() {
        return lankanimi;
    }

    public Alue getAlue() {
        return alue;
    }

    public int getLankaviestimaara() {
        return lankaviestimaara;
    }
    
    public Timestamp getViimeisinAika() {
        return viimeisinAika;
    }
    
    /*
    SETTERIT
    */
    public void setViimeisinAika(Timestamp viimeisinAika) {
        this.viimeisinAika = viimeisinAika;
    }
    
    public void lisaaLankaviestimaaraa() {
        this.lankaviestimaara++;
    }
    
    public void lisaaViesti(Viesti viesti) {
        this.lankaviestimaara++;
        setViimeisinAika(viesti.getLahetysaika());
    }

}
