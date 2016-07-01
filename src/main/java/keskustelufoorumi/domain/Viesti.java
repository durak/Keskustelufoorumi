package keskustelufoorumi.domain;

import java.util.*;
import java.util.Date;
import java.sql.*;
import java.text.SimpleDateFormat;

public class Viesti {

    private int id;
    private String sisalto;
    private Kayttaja kayttaja;
    private Lanka lanka;
    private Timestamp lahetysaika;
    private String lahetysaikaFormatted;

//    public Viesti() {
//
//    }

    /*
     * Konstruktori tietokannasta luettavaa viestiä varten
     */
    public Viesti(int id, String sisalto, Kayttaja kayttaja, Lanka lanka, Timestamp lahetysaika) {
        this.id = id;
        this.sisalto = sisalto;
        this.kayttaja = kayttaja;
        this.lanka = lanka;
        this.lahetysaika = lahetysaika;
        this.lahetysaikaFormatted = new SimpleDateFormat("yyyy-MM-dd @ HH:mm").format(lahetysaika);
    }

    /*
    * Konstruktori, jossa automaattinene timestamp
     */
    public Viesti(String sisalto, Kayttaja kayttaja, Lanka lanka) {
        this.sisalto = sisalto;
        this.kayttaja = kayttaja;
        this.lanka = lanka;
        this.lahetysaika = new Timestamp(new Date().getTime());
        this.lahetysaikaFormatted = new SimpleDateFormat("yyyy-MM-dd @ HH:mm").format(this.lahetysaika);
    }

    // GETTERIT
    public int getId() {
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

    public String getLahetysaikaFormatted() {
        return lahetysaikaFormatted;
    }

    // SETTERIT
    public void setId(int id) {
        this.id = id;
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
