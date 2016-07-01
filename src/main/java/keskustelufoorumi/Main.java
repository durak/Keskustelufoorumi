package keskustelufoorumi;

import java.util.HashMap;
import java.util.Map;
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
import org.jsoup.Jsoup;
import spark.ModelAndView;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

public class Main {

    public static void main(String[] args) throws Exception {

        /*
         * Valmistelut Herokua varten + SQLite/PostgreSql valinta
         * asetetaan portti jos heroku antaa PORT-ympäristömuuttujan
         */
        if (System.getenv("PORT") != null) {
            port(Integer.valueOf(System.getenv("PORT")));
        }

        String jdbcOsoite = "jdbc:sqlite:forum.db";
        if (System.getenv("DATABASE_URL") != null) {
            jdbcOsoite = System.getenv("DATABASE_URL");
        }

        /*
         * Alustetaan oliot
         */
        Database database = new Database(jdbcOsoite);
        DaoManager daoManager = new DaoManager(database);
        AlueDao alueDao = daoManager.getAlueDao();
        LankaDao lankaDao = daoManager.getLankaDao();
        ViestiDao viestiDao = daoManager.getViestiDao();
        KayttajaDao kayttajaDao = daoManager.getKayttajaDao();

        /*
         * Etusivu / Aluevalikko
         */
        get("/", (req, res) -> {
            Map map = new HashMap<>();
            map.put("alueet", alueDao.findAll());

            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());
        
        /*
         * Uusi keskustelualue
         */
        post("/", (req, res) -> {
            String aluenimi = stripHtml(req.queryParams("aluenimi"));
            if (aluenimi.equals("") || aluenimi.length() > 20) {
                halt(400, "Virheellinen aluenimi: sallittu pituus 1-20 merkkiä.");
            }

            Alue uusiAlue = new Alue(aluenimi);
            alueDao.insertNewInstance(uusiAlue);
            res.redirect("/");
            return null;
        });

        /*
         * Valitun alueen langat
         */
        get("/alue/:id", (req, res) -> {
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

            return new ModelAndView(map, "alue");
        }, new ThymeleafTemplateEngine());
        
        /*
         * Virheellinen alueId -> 404
         */
        before("/alue/:id", (req, res) -> {
            boolean error = false;
            int alueId = Integer.parseInt(req.params("id"));
            if (alueDao.findOne(alueId) == null) {
                error = true;
            }
            try {
                int sivu = Integer.parseInt(req.queryParams("sivu"));
            } catch (Exception e) {
                error = true;
            }

            if (error) {
                halt(404, "Aluetta ei löytynyt");
            }
        });
        
        /*
         * Uusi lanka
         */
        post("/alue/:id", (req, res) -> {
            String lankanimi = stripHtml(req.queryParams("lankanimi"));
            int alueId = Integer.parseInt(req.params("id"));
            if (lankanimi.equals("") || lankanimi.length() > 40) {
                halt(400, "Virheellinen lankanimi: sallittu pituus 1-40 merkkiä.");
            }

            Alue alue = alueDao.findOne(alueId);
            Lanka uusiLanka = new Lanka(lankanimi, alue);
            lankaDao.insertNewInstance(uusiLanka);

            String paluuOsoite = "/alue/" + alue.getId() + "/" + lankaDao.getMaxId() + "?sivu=0";
            res.redirect(paluuOsoite);
            return null;
        });

        /*
         * Valitun langan viestit
         */
        get("/alue/:alue.id/:id", (req, res) -> {
            int alueId = Integer.parseInt(req.params("alue.id"));
            int lankaId = Integer.parseInt(req.params("id"));
            int sivu = Integer.parseInt(req.queryParams("sivu"));

            HashMap map = new HashMap<>();
            map.put("alue", alueDao.findOne(alueId));
            map.put("lanka", lankaDao.findOne(lankaId));
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

            return new ModelAndView(map, "lanka");
        }, new ThymeleafTemplateEngine());

        /*
         * Virheellinen lankavalinta -> 404
         */
        before("/alue/:alue.id/:id", (req, res) -> {
            boolean error = false;
            int lankaId = Integer.parseInt(req.params("id"));
            if (lankaDao.findOne(lankaId) == null) {
                error = true;
            }
            try {
                int sivu = Integer.parseInt(req.queryParams("sivu"));
            } catch (Exception e) {
                error = true;
            }

            if (error) {
                halt(404, "Lankaa ei löytynyt");
            }
        });
        
        /*
         * Uusi viesti        
         */
        post("/alue/:alue.id/:id", (req, res) -> {
            int sivu = Integer.parseInt(req.queryParams("sivu"));
            int lankaId = Integer.parseInt(req.params("id"));
            String nimimerkki = stripHtml(req.queryParams("nimimerkki"));
            String sisalto = stripHtml(req.queryParams("sisalto"));
            
            if (nimimerkki.equals("") || nimimerkki.length() > 20) {
                halt(400, "Virheellinen nimimerkki: sallittu pituus 1-20 merkkiä.");
            }
            if (sisalto.equals("") || sisalto.length() > 140) {
                halt(400, "Virheellinen viestisisalto: sallittu pituus 1-140 merkkiä.");
            }

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
            res.redirect(paluuOsoite);
            return null;
        });

        /*
         * Käyttäjän viestit
         */
        get("/kayttaja/:id", (req, res) -> {
            String kayttajaId = req.params("id");
            HashMap map = new HashMap<>();
            map.put("kayttaja", kayttajaDao.findOne(kayttajaId));
            map.put("viestit", viestiDao.findAllWithUserId(kayttajaId));

            return new ModelAndView(map, "kayttaja");
        }, new ThymeleafTemplateEngine());

        before("/kayttaja/:id", (req, res) -> {
            String kayttajaId = req.params("id");
            if (kayttajaDao.findOne(kayttajaId) == null) {
                halt(404, "Käyttäjää ei löytynyt");
            }
        });

    }

    public static String stripHtml(String html) {
        return Jsoup.parse(html).text();
    }

}
