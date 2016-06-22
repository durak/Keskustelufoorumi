package keskustelufoorumi;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
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
import keskustelufoorumi.ui.TekstiUi;
import spark.ModelAndView;
import static spark.Spark.get;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import static spark.Spark.get;
import static spark.Spark.get;
import static spark.Spark.get;


public class Main {

    public static void main(String[] args) throws Exception {

        Database database = new Database();
        DaoManager daoManager = new DaoManager(database);


  
        
        /*
         * Aluenäkymä
         */
        get("/", (req, res) -> {
            AlueDao alueDao = daoManager.getAlueDao();

            Map map = new HashMap<>();
            map.put("alueet", alueDao.findAll());
            

            
                    
            return new ModelAndView(map, "indexTable");
        }, new ThymeleafTemplateEngine());

        post("/", (req, res) -> {
            String aluenimi = req.queryParams("aluenimi");
            Alue uusiAlue = new Alue(aluenimi);
            daoManager.getAlueDao().insertNewInstance(uusiAlue);

            return "<meta http-equiv=\"refresh\" content=\"0; url=/\">";
        });

        /*
         * Alueen langat
         */
//        get("/alue/:id", (req, res) -> {
//            AlueDao alueDao = daoManager.getAlueDao();
//            LankaDao lankaDao = daoManager.getLankaDao();
//
//            HashMap map = new HashMap<>();
//            map.put("alue", alueDao.findOne(Integer.parseInt(req.params("id"))));
//            map.put("langat", lankaDao.findAllWithAlueId(Integer.parseInt(req.params("id"))));
//
//            return new ModelAndView(map, "alueTable");
//        }, new ThymeleafTemplateEngine());
        /*
         *
         * Alueen langat 10 kerrallaan
         */
        get("/alue/:id", (req, res) -> {
            AlueDao alueDao = daoManager.getAlueDao();
            LankaDao lankaDao = daoManager.getLankaDao();
            int alueId = Integer.parseInt(req.params("id"));
            int sivu = Integer.parseInt(req.queryParams("sivu"));

            HashMap map = new HashMap<>();
            map.put("alue", alueDao.findOne(alueId));
            map.put("langat", lankaDao.findTenInAlueIdWithOffset(alueId, sivu));

            if (lankaDao.findCountOfLankaInAlue(alueId) > (sivu + 1) * 10) {
                map.put("seuraava", sivu + 1);
            } else {
                map.put("seuraava", null);
            }

            if (sivu > 0) {
                map.put("edellinen", sivu - 1);
            } else {
                map.put("edellinen", null);
            }

            return new ModelAndView(map, "alueTable");
        }, new ThymeleafTemplateEngine());

        post("/alue/:id", (req, res) -> {
            LankaDao lankaDao = daoManager.getLankaDao();
            String lankanimi = req.queryParams("lankanimi");
            int alueId = Integer.parseInt(req.params("id"));

            Alue alue = daoManager.getAlueDao().findOne(alueId);
            Lanka uusiLanka = new Lanka(lankanimi, alue);

            lankaDao.insertNewInstance(uusiLanka);

            String paluuOsoite = "/alue/" + alue.getId() + "/" + lankaDao.getMaxId() + "?sivu=0";
            return "<meta http-equiv=\"refresh\" content=\"0; url=" + paluuOsoite + "\">";
        });

        /*
         * Langan viestit
         */
        get("/alue/:alue.id/:id", (req, res) -> {
            AlueDao alueDao = daoManager.getAlueDao();
            LankaDao lankaDao = daoManager.getLankaDao();
            ViestiDao viestiDao = daoManager.getViestiDao();
            int alueId = Integer.parseInt(req.params("alue.id"));
            int lankaId = Integer.parseInt(req.params("id"));
            int sivu = Integer.parseInt(req.queryParams("sivu"));

            HashMap map = new HashMap<>();
            map.put("alue", alueDao.findOne(alueId));
            map.put("lanka", lankaDao.findOne(lankaId));
//            map.put("viestit", viestiDao.findAllWithLankaId(lankaId));
            map.put("viestit", viestiDao.find15WithLankaId(lankaId, sivu));

            if (viestiDao.findCountOfViestiInLanka(lankaId) > (sivu + 1) * 15) {
                map.put("seuraava", sivu + 1);
            } else {
                map.put("seuraava", null);
            }

            if (sivu > 0) {
                map.put("edellinen", sivu - 1);
            } else {
                map.put("edellinen", null);
            }

            return new ModelAndView(map, "lankaTable");
        }, new ThymeleafTemplateEngine());

        post("/alue/:alue.id/:id", (req, res) -> {
            AlueDao alueDao = daoManager.getAlueDao();
            LankaDao lankaDao = daoManager.getLankaDao();
            ViestiDao viestiDao = daoManager.getViestiDao();
            KayttajaDao kayttajaDao = daoManager.getKayttajaDao();
            int sivu = Integer.parseInt(req.queryParams("sivu"));

            int lankaId = Integer.parseInt(req.params("id"));
            String nimimerkki = req.queryParams("nimimerkki");
            String sisalto = req.queryParams("sisalto");

            Lanka lanka = lankaDao.findOne(lankaId);
            Alue alue = lanka.getAlue();
            Kayttaja kayttaja = kayttajaDao.findOne(nimimerkki);
            if (kayttaja == null) {
                kayttaja = new Kayttaja(nimimerkki);
                kayttajaDao.insertNewInstance(kayttaja);
            }

            Viesti uusiViesti = new Viesti(sisalto, kayttaja, lanka);
            lanka.lisaaViesti(uusiViesti);
            alue.lisaaViesti(uusiViesti);

            alueDao.updateInstance(alue);
            lankaDao.updateInstance(lanka);
            viestiDao.insertNewInstance(uusiViesti);

            String paluuOsoite = "/alue/" + alue.getId() + "/" + lanka.getId() + "?sivu=0";

            return "<meta http-equiv=\"refresh\" content=\"0; url=" + paluuOsoite + "\">";

        });

        /*
         * Käyttäjän viestit
         */
        get("/kayttaja/:id", (req, res) -> {
            AlueDao alueDao = daoManager.getAlueDao();
            LankaDao lankaDao = daoManager.getLankaDao();
            ViestiDao viestiDao = daoManager.getViestiDao();

            String kayttajaId = req.params("id");

            HashMap map = new HashMap<>();

            map.put("kayttaja", daoManager.getKayttajaDao().findOne(kayttajaId));
            map.put("viestit", viestiDao.findAllWithUserId(kayttajaId));

            return new ModelAndView(map, "kayttaja");
        }, new ThymeleafTemplateEngine());

//        Scanner lukija = new Scanner(System.in);
//        TekstiUi tekstiUi = new TekstiUi(lukija, daoManager);
//        tekstiUi.kaynnista();
    }

}
