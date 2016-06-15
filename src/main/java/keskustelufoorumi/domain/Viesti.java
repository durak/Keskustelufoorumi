package keskustelufoorumi.domain;
import java.util.*;
import java.sql.*;

public class Viesti {
    
    private int id;
    private String sisalto;
    private Kayttaja kayttaja;
    private Lanka lanka;
    private Timestamp lahetysaika;
    
    public Viesti(int id, String sisalto, Kayttaja kayttaja, Lanka lanka, Timestamp lahetysaika) {
        this.id = id;
        this.sisalto = sisalto;
        this.kayttaja = kayttaja;
        this.lanka = lanka;
        this.lahetysaika = lahetysaika;
    }
    
    
    // GETTERIT
    
    public int getViestitunnus() {
        return id;
    }
    
    public String getSisalto() {
        return sisalto;
    }
    
    public Kayttaja getKayttaja() {
        return kayttaja;
    }
    
    public Lanka getLanka() {
        return lanka;
    }
    
    public Timestamp getLahetysaika() {
        return lahetysaika;
    }
    
    // SETTERIT
    
    public void setViestitunnus(int viestitunnus) {
        this.id = viestitunnus;                
    }
    
    public void setSisalto(String sisalto) {
        this.sisalto = sisalto;                
    }
    
    public void setKayttaja(Kayttaja kayttaja) {
        this.kayttaja = kayttaja;
    }
    
    public void setLanka(Lanka lanka) {
        this.lanka = lanka;
    }
    
    public void setLahetysaika(Timestamp lahetysaika) {
        this.lahetysaika = lahetysaika;
    }
    
}
