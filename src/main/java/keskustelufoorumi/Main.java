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

            return new ModelAndView(map, "index");
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
        get("/alue/:id", (req, res) -> {
            AlueDao alueDao = daoManager.getAlueDao();
            LankaDao lankaDao = daoManager.getLankaDao();

            HashMap map = new HashMap<>();
            map.put("alue", alueDao.findOne(Integer.parseInt(req.params("id"))));
            map.put("langat", lankaDao.findAllWhereXIsK(Integer.parseInt(req.params("id"))));

            return new ModelAndView(map, "alue");
        }, new ThymeleafTemplateEngine());

        post("/alue/:id", (req, res) -> {
            LankaDao lankaDao = daoManager.getLankaDao();
            String lankanimi = req.queryParams("lankanimi");
            Alue alue = daoManager.getAlueDao().findOne(Integer.parseInt(req.params("id")));
            Lanka uusiLanka = new Lanka(lankanimi, alue);
            lankaDao.insertNewInstance(uusiLanka);

            String paluuOsoite = "/alue/" + alue.getId() + "/" + lankaDao.getMaxId();

            return "<meta http-equiv=\"refresh\" content=\"0; url=" + paluuOsoite + "\">";
        });

        /*
         * Langan viestit
         */
        get("/alue/:alue.id/:id", (req, res) -> {
            AlueDao alueDao = daoManager.getAlueDao();
            LankaDao lankaDao = daoManager.getLankaDao();
            ViestiDao viestiDao = daoManager.getViestiDao();

            HashMap map = new HashMap<>();
            map.put("alue", alueDao.findOne(Integer.parseInt(req.params("alue.id"))));
            map.put("lanka", lankaDao.findOne(Integer.parseInt(req.params("id"))));
            map.put("viestit", viestiDao.findAllWhereXIsK(Integer.parseInt(req.params("id"))));

            return new ModelAndView(map, "lanka");
        }, new ThymeleafTemplateEngine());

        post("/alue/:alue.id/:id", (req, res) -> {
            AlueDao alueDao = daoManager.getAlueDao();
            LankaDao lankaDao = daoManager.getLankaDao();
            ViestiDao viestiDao = daoManager.getViestiDao();
            KayttajaDao kayttajaDao = daoManager.getKayttajaDao();

            String nimimerkki = req.queryParams("nimimerkki");
            String sisalto = req.queryParams("sisalto");            
            Lanka lanka = lankaDao.findOne(Integer.parseInt(req.params("id")));
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

            String paluuOsoite = "/alue/" + alue.getId() + "/" + lanka.getId();
            
            return "<meta http-equiv=\"refresh\" content=\"0; url=" + paluuOsoite + "\">";
            
        }) ;


//        Scanner lukija = new Scanner(System.in);
//        TekstiUi tekstiUi = new TekstiUi(lukija, daoManager);
//        tekstiUi.kaynnista();

    }

}
