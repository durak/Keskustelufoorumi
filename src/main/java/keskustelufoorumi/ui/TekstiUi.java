//package keskustelufoorumi.ui;
//
//import java.util.*;
//import java.util.Date;
//import java.sql.*;
//import keskustelufoorumi.database.sql.SqlAlueDao;
//import keskustelufoorumi.database.DaoManager;
//import keskustelufoorumi.database.Database;
//import keskustelufoorumi.database.sql.SqlKayttajaDao;
//import keskustelufoorumi.database.sql.SqlLankaDao;
//import keskustelufoorumi.database.sql.SqlViestiDao;
//import keskustelufoorumi.domain.Alue;
//import keskustelufoorumi.domain.Kayttaja;
//import keskustelufoorumi.domain.Lanka;
//import keskustelufoorumi.domain.Viesti;
//
//public class TekstiUi {
//
//    private Scanner lukija;
////    private Database database;
//    private DaoManager daoManager;
////    private AlueDao alueDao;
////    private LankaDao lankaDao;
////    private ViestiDao viestiDao;
////    private KayttajaDao kayttajaDao;
//    private boolean ohjelmaPaalla;
//    private Alue alue;
//    private Lanka lanka;
//
//    public TekstiUi(Scanner lukija, DaoManager daoManager) throws SQLException {
//        this.lukija = lukija;
//        this.daoManager = daoManager;
////        this.database = database;
////        this.alueDao = alueDao;
////        this.lankaDao = lankaDao;
////        this.viestiDao = viestiDao;
////        this.kayttajaDao = kayttajaDao;
//        this.ohjelmaPaalla = true;
//        this.alue = null;
//        this.lanka = null;
//    }
//
//    public void kaynnista() throws SQLException {
//        System.out.println("Tervetuloa keskustelupalstalle!");
//        System.out.println("\n--------------------------------\n");
//
//        while (ohjelmaPaalla) {
//            tulostaValikko();
//            lueKomento();
//        }
//
//    }
//
//    private void tulostaValikko() {
//        System.out.println("");
//        System.out.println("Anne komento: "
//                + "\n1 Tulosta alueet"
//                + "\n2 Valitse alue"
//                + "\n3 Tulosta langat"
//                + "\n4 Valitse lanka"
//                + "\n5 Uusi alue"
//                + "\n6 Uusi lanka"
//                + "\n7 Kirjoita viesti"
//                + "\n8 Lopeta");
//    }
//
//    private void lueKomento() throws SQLException {
//        System.out.println("");
//        System.out.print("komento: ");
//        String komento = lukija.nextLine();
//
//        if (komento.equals("1")) {
//            tulostaAlueet();
//        } else if (komento.equals("2")) {
//            valitseAlue();
//            tulostaLangat();
//        } else if (komento.equals("3")) {
//            tulostaLangat();
//        } else if (komento.equals("4")) {
//            valitseLanka();
//            tulostaViestit();
//        } else if (komento.equals("5")) {
//            uusiAlue();
//        } else if (komento.equals("6")) {
//            uusiLanka();
//        } else if (komento.equals("7")) {
//            kirjoitaViesti();
//        } else if (komento.equals("8")) {
//            ohjelmaPaalla = false;
//        }
//    }
//
//    private void tulostaAlueet() throws SQLException {
//        System.out.println("Alue : Viestimäärä");
//        for (int i = 0; i < daoManager.getAlueDao().findAll().size(); i++) {
//            Alue juokseva = daoManager.getAlueDao().findAll().get(i);
//            System.out.println(juokseva.getId() + " : " + juokseva.getAluenimi() + " : " + juokseva.getAlueviestimaara() + " : " + juokseva.getViimeisinAika());
//        }
//    }
//
//    private void tulostaLangat() throws SQLException {
//        System.out.println("Lanka : Viestimäärä");
//        for (Lanka lanka : daoManager.getLankaDao().findAll()) {
//            if (lanka.getAlue().getAluenimi().equals(alue.getAluenimi())) {
//                System.out.println(lanka.getId() + " : " + lanka.getLankanimi() + " : " + lanka.getLankaviestimaara());
//            }
//        }
//    }
//
//    private void tulostaViestit() throws SQLException {
//        System.out.println("Kayttaja : Viesti : Lähetysaika");
//        for (Viesti viesti : daoManager.getViestiDao().findAll()) {
//            if (viesti.getLanka().getId() == lanka.getId()) {
//                System.out.println(viesti.getKayttaja().getId() + " : " + viesti.getSisalto() + " : " + viesti.getLahetysaika());
////                System.out.println(viesti.getKayttaja().getId());
//                
//            }
//        }
//    }
//
//    private void valitseAlue() throws SQLException {
//        System.out.print("Valitse alue: ");
//        int alueId = Integer.parseInt(lukija.nextLine());
//
//        this.alue = daoManager.getAlueDao().findOne(alueId);
//    }
//
//    private void valitseLanka() throws SQLException {
//        System.out.println("Valitse lanka: ");
//        int lankaId = Integer.parseInt(lukija.nextLine());
//
//        this.lanka = daoManager.getLankaDao().findOne(lankaId);
//    }
//
//    private void uusiAlue() throws SQLException {
//        System.out.print("Anna aluenimi: ");
//        String aluenimi = lukija.nextLine();
//
//        Alue uusiAlue = new Alue(aluenimi);
//        uusiAlue.setViimeisinAika(getTimestamp());
//        daoManager.getAlueDao().insertNewInstance(uusiAlue);
//        int maxId = daoManager.getAlueDao().getMaxId();
//        uusiAlue.setId(maxId);
//        this.alue = uusiAlue;
//
//        uusiLanka();
//    }
//
//    private void uusiLanka() throws SQLException {
//        System.out.print("Anna lankanimi: ");
//        String lankanimi = lukija.nextLine();
//
//        Lanka uusiLanka = new Lanka(lankanimi, alue);
//        uusiLanka.setViimeisinAika(getTimestamp());
//        daoManager.getLankaDao().insertNewLanka(uusiLanka);
//        int maxId = daoManager.getLankaDao().findLatestId();
//        uusiLanka.setId(maxId);
//        this.lanka = uusiLanka;
//
//        kirjoitaViesti();
//    }
//
//    private void kirjoitaViesti() throws SQLException {
//        System.out.print("Anna nimimerkki: ");
//        String nimimerkki = lukija.nextLine();
//        System.out.print("Kirjoita viesti: ");
//        String sisalto = lukija.nextLine();
//
//        Kayttaja kayttaja = daoManager.getKayttajaDao().findOne(nimimerkki);
//        if (kayttaja == null) {
//            kayttaja = new Kayttaja(nimimerkki);
//            daoManager.getKayttajaDao().insertNewInstance(kayttaja);
//        }
//
//        Viesti viesti = new Viesti(sisalto, kayttaja, lanka, getTimestamp());
//        lanka.lisaaViesti(viesti);
//        alue.lisaaViesti(viesti);
//
//        if (daoManager.getAlueDao().findOne(lanka.getAlue().getId()) == null) {
//            daoManager.getAlueDao().insertNewInstance(alue);
//            
//        } else {            
//            daoManager.getAlueDao().updateInstance(alue);
//        }
//        
//        
//        if (daoManager.getLankaDao().findOne(lanka.getId()) == null) {
//            daoManager.getLankaDao().insertNewLanka(lanka);
//        } else {
//            daoManager.getLankaDao().updateLanka(lanka);
//        }
//        
//        daoManager.getViestiDao().insertNewViesti(viesti);
//
//        
//    }
//
//    private Timestamp getTimestamp() {
//        Date date = new Date();
//        return new Timestamp(date.getTime());
//    }
//
//}
