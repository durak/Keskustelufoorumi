package keskustelufoorumi.domain;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class Lanka {

    private int id;
    private String lankanimi;
    private Alue alue;
    private int lankaviestimaara;
    private Timestamp viimeisinAika;
    private String viimeisinAikaFormatted;

    public Lanka() {
    }

    public Lanka(int id, String lankanimi, Alue alue, int lankaviestimaara, Timestamp viimeisinAika) {
        this.id = id;
        this.lankanimi = lankanimi;
        this.alue = alue;
        this.lankaviestimaara = lankaviestimaara;
        this.viimeisinAika = viimeisinAika;
        if (!viimeisinAika.equals(new Timestamp(0))) {
            this.viimeisinAikaFormatted = new SimpleDateFormat("yyyy-MM-dd @ HH:mm").format(viimeisinAika);
        } else {
            this.viimeisinAikaFormatted = "-";
        }
    }

    public Lanka(String lankanimi, Alue alue) {
        this.lankanimi = lankanimi;
        this.alue = alue;
        this.lankaviestimaara = 0;
        this.viimeisinAika = new Timestamp(0);
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

    public String getViimeisinAikaFormatted() {
        return viimeisinAikaFormatted;
    }

    /*
    *SETTERIT
     */
    public void setId(int id) {
        this.id = id;
    }

    public void setViimeisinAika(Timestamp viimeisinAika) {
        this.viimeisinAika = viimeisinAika;
        if (!viimeisinAika.equals(new Timestamp(0))) {
            this.viimeisinAikaFormatted = new SimpleDateFormat("yyyy-MM-dd @ HH:mm").format(viimeisinAika);
        } else {
            this.viimeisinAikaFormatted = "-";
        }
    }

    public void lisaaLankaviestimaaraa() {
        this.lankaviestimaara++;
    }

    public void lisaaViesti(Viesti viesti) {
        this.lankaviestimaara++;
        setViimeisinAika(viesti.getLahetysaika());
    }

}
