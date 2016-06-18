package keskustelufoorumi.ui;

import java.util.*;
import java.util.Date;
import java.sql.*;
import keskustelufoorumi.database.AlueDao;
import keskustelufoorumi.database.DaoManager;
import keskustelufoorumi.database.Database;
import keskustelufoorumi.database.KayttajaDao;
import keskustelufoorumi.database.LankaDao;
import keskustelufoorumi.database.ViestiDao;
import keskustelufoorumi.domain.Alue;
import keskustelufoorumi.domain.Kayttaja;
import keskustelufoorumi.domain.Lanka;
import keskustelufoorumi.domain.Viesti;

public class TekstiUi {

    private Scanner lukija;
    private Database database;
    private DaoManager daoManager;
    private AlueDao alueDao;
    private LankaDao lankaDao;
    private ViestiDao viestiDao;
    private KayttajaDao kayttajaDao;
    private boolean ohjelmaPaalla;
    private Alue alue;
    private Lanka lanka;

    public TekstiUi(Scanner lukija, Database database, AlueDao alueDao, LankaDao lankaDao, ViestiDao viestiDao, KayttajaDao kayttajaDao) throws SQLException {
        this.lukija = lukija;
        this.database = database;
        this.alueDao = alueDao;
        this.lankaDao = lankaDao;
        this.viestiDao = viestiDao;
        this.kayttajaDao = kayttajaDao;
        this.ohjelmaPaalla = true;
        this.alue = null;
        this.lanka = null;
    }

    public void kaynnista() throws SQLException {
        System.out.println("Tervetuloa keskustelupalstalle!");
        System.out.println("\n--------------------------------\n");

        while (ohjelmaPaalla) {
            tulostaValikko();
            lueKomento();
        }

    }

    private void tulostaValikko() {
        System.out.println("");
        System.out.println("Anne komento: "
                + "\n1 Tulosta alueet"
                + "\n2 Valitse alue"
                + "\n3 Tulosta langat"
                + "\n4 Valitse lanka"
                + "\n5 Uusi alue"
                + "\n6 Uusi lanka"
                + "\n7 Kirjoita viesti"
                + "\n8 Lopeta");
    }

    private void lueKomento() throws SQLException {
        System.out.println("");
        System.out.print("komento: ");
        String komento = lukija.nextLine();

        if (komento.equals("1")) {
            tulostaAlueet();
        } else if (komento.equals("2")) {
            valitseAlue();
            tulostaLangat();
        } else if (komento.equals("3")) {
            tulostaLangat();
        } else if (komento.equals("4")) {
            valitseLanka();
            tulostaViestit();
        } else if (komento.equals("5")) {
            valitseLanka();
            tulostaViestit();
        } else if (komento.equals("6")) {
            valitseLanka();
            tulostaViestit();
        } else if (komento.equals("7")) {
            valitseLanka();
            tulostaViestit();
        } else if (komento.equals("8")) {
            ohjelmaPaalla = false;
        }
    }

    private void tulostaAlueet() throws SQLException {
        System.out.println("Alue : Viestimäärä");
        for (int i = 0; i < alueDao.findAll().size(); i++) {
            Alue juokseva = alueDao.findAll().get(i);
            System.out.println(juokseva.getId() + " : " + juokseva.getAluenimi() + " : " + juokseva.getAlueviestimaara() + " : " + juokseva.getViimeisinAika());
        }
    }

    private void tulostaLangat() throws SQLException {
        System.out.println("Lanka : Viestimäärä");
        for (Lanka lanka : lankaDao.findAll()) {
            if (lanka.getAlue().getAluenimi().equals(alue.getAluenimi())) {
                System.out.println(lanka.getLankanimi() + " : " + lanka.getLankaviestimaara());
            }
        }
    }

    private void tulostaViestit() throws SQLException {
        System.out.println("Kayttaja : Viesti : Lähetysaika");
        for (Viesti viesti : viestiDao.findAll()) {
            if (viesti.getLanka().getLankanimi().equals(lanka.getLankanimi())) {
                System.out.println(viesti.getKayttaja().getId() + " : " + viesti.getSisalto() + " : " + viesti.getLahetysaika());
            }
        }
    }

    private void valitseAlue() throws SQLException {
        System.out.print("Valitse alue: ");
        int alueId = Integer.parseInt(lukija.nextLine());        

        this.alue = alueDao.findOne(alueId);
    }

    private void valitseLanka() throws SQLException {
        System.out.println("Valitse lanka: ");
        int lankaId = Integer.parseInt(lukija.nextLine());        

        this.lanka = lankaDao.findOne(lankaId);
    }

    private void uusiLanka() throws SQLException {
        System.out.print("Anna lankanimi: ");
        String lankanimi = lukija.nextLine();
        
        

    }

    private void kirjoitaViesti() throws SQLException {
        System.out.print("Anna nimimerkki: ");
        String nimimerkki = lukija.nextLine();
        System.out.print("Kirjoita viesti: ");
        String sisalto = lukija.nextLine();

        Kayttaja kayttaja = daoManager.getKayttajaDao().findOne(nimimerkki);
        if (kayttaja == null) {
            kayttaja = new Kayttaja(nimimerkki);
        }
        
        Viesti viesti = new Viesti(0, sisalto, kayttaja, lanka, getTimestamp());
        
        
    }
    
    
    private Timestamp getTimestamp() {
        Date date = new Date();
        return new Timestamp(date.getTime());        
    }

}
