package keskustelufoorumi.domain;

import java.sql.Timestamp;

public class Alue {

    private int id;
    private String aluenimi;
    private int alueviestimaara;
    private Timestamp viimeisinAika;

    public Alue(int id, String aluenimi, int alueviestimaara, Timestamp viimeisinAika) {
        this.id = id;
        this.aluenimi = aluenimi;
        this.alueviestimaara = alueviestimaara;
        this.viimeisinAika = viimeisinAika;
    }

    public Alue(String aluenimi) {        
        this.aluenimi = aluenimi;
        this.alueviestimaara = 0;        
        this.viimeisinAika = new Timestamp(0);
        
    }

    /*
    GETTERIT
     */
    public int getId() {
        return this.id;
    }

    public String getAluenimi() {
        return aluenimi;
    }

    public int getAlueviestimaara() {
        return alueviestimaara;
    }

    public Timestamp getViimeisinAika() {
        return viimeisinAika;
    }

    /*
    *SETTERIT
     */
    
    public void setId(int id) {
        this.id = id;
    }
    
    public void lisaaViestimaaraa() {
        this.alueviestimaara++;
    }

    public void setViimeisinAika(Timestamp viimeisinAika) {
        this.viimeisinAika = viimeisinAika;
    }

    public void lisaaViesti(Viesti viesti) {
        this.alueviestimaara++;
        setViimeisinAika(viesti.getLahetysaika());
    }

}
