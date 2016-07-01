package keskustelufoorumi.webconfig;

import java.util.HashMap;
import java.util.Map;
import keskustelufoorumi.database.AlueDao;
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
import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.halt;
import static spark.Spark.post;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

public class SparkConfig {

    private Database database;
    private AlueDao alueDao;
    private LankaDao lankaDao;
    private ViestiDao viestiDao;
    private KayttajaDao kayttajaDao;

    public SparkConfig(Database database, AlueDao alueDao, LankaDao lankaDao, ViestiDao viestiDao, KayttajaDao kayttajaDao) {
        this.database = database;
        this.alueDao = alueDao;
        this.lankaDao = lankaDao;
        this.viestiDao = viestiDao;
        this.kayttajaDao = kayttajaDao;

        routeSetup();
    }

    private void routeSetup() {
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
            int kerrallaNaytetaanLankoja = 10;

            HashMap map = new HashMap<>();
            map.put("alue", alueDao.findOne(alueId));
            map.put("langat", lankaDao.findLangatInAlue(alueId, kerrallaNaytetaanLankoja, sivu));

            if (lankaDao.findCountOfLankaInAlue(alueId) > (sivu + 1) * kerrallaNaytetaanLankoja) {
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
            lankaDao.insertNewLanka(uusiLanka);

            String paluuOsoite = "/alue/" + alue.getId() + "/" + lankaDao.findLatestId() + "?sivu=0";
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
            int kerrallaNaytetaanViesteja = 15;

            HashMap map = new HashMap<>();
            map.put("alue", alueDao.findOne(alueId));
            map.put("lanka", lankaDao.findOne(lankaId));
            map.put("viestit", viestiDao.findViestitInLanka(lankaId, kerrallaNaytetaanViesteja, sivu));

            if (viestiDao.findCountOfViestiInLanka(lankaId) > (sivu + 1) * kerrallaNaytetaanViesteja) {
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
                kayttajaDao.insertNewKayttaja(kayttaja);
            }

            Viesti uusiViesti = new Viesti(sisalto, kayttaja, lanka);
            lanka.lisaaViesti(uusiViesti);
            alue.lisaaViesti(uusiViesti);

            alueDao.updateInstance(alue);
            lankaDao.updateLanka(lanka);
            viestiDao.insertNewViesti(uusiViesti);

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
            map.put("viestit", viestiDao.findViestitWithKayttajaId(kayttajaId));

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
