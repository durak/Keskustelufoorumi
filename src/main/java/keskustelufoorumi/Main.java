package keskustelufoorumi;

import java.util.*;
import java.util.Date;
import java.sql.*;
import java.text.DateFormat;
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
//import keskustelufoorumi.ui.TekstiUi;

public class Main {

    public static void main(String[] args) throws Exception {
        Kayttaja kayttaja = new Kayttaja("KumiJ");
        Alue alue = new Alue(1, "Yleinen keskustelu", 0, null);
        Lanka lanka = new Lanka(1, "Timantit on ikuisia", alue, 0, null);
        Date date = new Date();
        Viesti viesti = new Viesti(1, "tadaa-tadaa", kayttaja, lanka, new Timestamp(date.getTime()));
        
        lanka.lisaaViesti(viesti);
        alue.lisaaViesti(viesti);
        
        DaoManager dm = new DaoManager();
        dm.teePaivitys(viesti);
        
        
        
        
        
        
//        Database database = new Database("jdbc:sqlite:forum2.db");
//        KayttajaDao kayttajaDao = new KayttajaDao(database);
//        AlueDao alueDao = new AlueDao(database);
//        LankaDao lankaDao = new LankaDao(database, alueDao);
//        ViestiDao viestiDao = new ViestiDao(database, kayttajaDao, lankaDao);
//        Scanner lukija = new Scanner(System.in);
        
//        Kayttaja uusi = new Kayttaja("KumiJ");
//        kayttajaDao.insertNewKayttaja(uusi);
//        tulostaKayttajat(kayttajaDao);
//        
//        Alue alue = new Alue(1, "testialue", 0);
//        Lanka lanka = new Lanka(1, "testilanka", alue, 0);
//        lankaDao.insertNewLanka(lanka);
//        
        
//        tulostaKayttajat(kayttajaDao);
//        tulostaAlueet(alueDao);
//        tulostaLangat(lankaDao);
//        tulostaViesti(viestiDao);

        //TekstiUi tekstiUi = new TekstiUi(lukija, database, alueDao, lankaDao, viestiDao, kayttajaDao);
        //tekstiUi.kaynnista();
    }

    public static void tulostaKayttajat(KayttajaDao kayttajaDao) throws SQLException {
        System.out.println("Käyttäjät:");
        for (Kayttaja kayttaja : kayttajaDao.findAll()) {
            System.out.println(kayttaja.getId());
        }
        System.out.println("");
    }

    public static void tulostaAlueet(AlueDao alueDao) throws SQLException {
        System.out.println("Alueet:");
        for (Alue alue : alueDao.findAll()) {
            System.out.println(alue.getAluenimi() + " | " + alue.getAlueviestimaara());
        }
        System.out.println("");
    }

    public static void tulostaLangat(LankaDao lankaDao) throws SQLException {
        System.out.println("Langat:");
        for (Lanka lanka : lankaDao.findAll()) {
            System.out.println(lanka.getAlue().getAluenimi() + " | " + lanka.getLankanimi() + " | " + lanka.getLankaviestimaara());
        }
        System.out.println("");
    }

    public static void tulostaViesti(ViestiDao viestiDao) throws SQLException {
        System.out.println("Viestit:");
        for (Viesti viesti : viestiDao.findAll()) {
            if (viesti.getLanka().getLankanimi().equals("UCL finaali")) {
                System.out.println(viesti.getViestitunnus()
                        + " | " + viesti.getLanka().getLankanimi()
                        + " | " + viesti.getKayttaja().getId()
                        + " | " + viesti.getSisalto()
                        + " | " + viesti.getLahetysaika());
            }
        }
        System.out.println("");
    }

}
