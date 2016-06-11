package keskustelufoorumi.ui;

import java.util.*;
import java.sql.*;
import keskustelufoorumi.database.AlueDao;
import keskustelufoorumi.database.Database;
import keskustelufoorumi.database.KayttajaDao;
import keskustelufoorumi.database.LankaDao;
import keskustelufoorumi.database.ViestiDao;
import keskustelufoorumi.domain.Alue;
import keskustelufoorumi.domain.Lanka;
import keskustelufoorumi.domain.Viesti;

public class TekstiUi {

    private Scanner lukija;
    private Database database;
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
                + "\n5 Lopeta");
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
            ohjelmaPaalla = false;
        }
    }

    private void tulostaAlueet() throws SQLException {
        System.out.println("Alue : Viestimäärä");
        for (int i = 0; i < alueDao.findAll().size(); i++) {
            Alue juokseva = alueDao.findAll().get(i);
            System.out.println(juokseva.getAluenimi() + " : " + juokseva.getAlueviestimaara());
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
                System.out.println(viesti.getKayttaja().getTunnus() + " : " + viesti.getSisalto() + " : " + viesti.getLahetysaika());
            }
        }
    }

    private void valitseAlue() throws SQLException {
        System.out.print("Valitse alue: ");
        String aluenimi = lukija.nextLine();

        this.alue = alueDao.findOne(aluenimi);
    }

    private void valitseLanka() throws SQLException {
        System.out.println("Valitse lanka: ");
        String lankanimi = lukija.nextLine();

        this.lanka = lankaDao.findOne(lankanimi);
    }

}
