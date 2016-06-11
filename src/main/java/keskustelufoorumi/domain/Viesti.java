package keskustelufoorumi.domain;
import java.util.*;
import java.sql.*;

public class Viesti {
    
    private int viestitunnus;
    private String sisalto;
    private Kayttaja kayttaja;
    private Lanka lanka;
    private String lahetysaika;
    
    public Viesti(int viestitunnus, String sisalto, Kayttaja kayttaja, Lanka lanka, String lahetysaika) {
        this.viestitunnus = viestitunnus;
        this.sisalto = sisalto;
        this.kayttaja = kayttaja;
        this.lanka = lanka;
        this.lahetysaika = lahetysaika;
    }
    
    
    // GETTERIT
    
    public int getViestitunnus() {
        return viestitunnus;
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
    
    public String getLahetysaika() {
        return lahetysaika;
    }
    
    // SETTERIT
    
    public void setViestitunnus(int viestitunnus) {
        this.viestitunnus = viestitunnus;                
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
    
    public void setLahetysaika(String lahetysaika) {
        this.lahetysaika = lahetysaika;
    }
    
}
