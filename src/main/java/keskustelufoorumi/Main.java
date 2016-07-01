package keskustelufoorumi;

import keskustelufoorumi.database.Database;
import keskustelufoorumi.database.DaoManager;
import keskustelufoorumi.database.AlueDao;
import keskustelufoorumi.database.LankaDao;
import keskustelufoorumi.database.ViestiDao;
import keskustelufoorumi.database.KayttajaDao;
import keskustelufoorumi.webconfig.SparkConfig;
import static spark.Spark.*;


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
        DaoManager daoManager = DaoManager.getDaoManager(database, DaoManager.Tietokantatyyppi.SQL);
        AlueDao alueDao = daoManager.getAlueDao();
        LankaDao lankaDao = daoManager.getLankaDao();
        ViestiDao viestiDao = daoManager.getViestiDao();
        KayttajaDao kayttajaDao = daoManager.getKayttajaDao();

        /*
         * Spark reititykset käyntiin
         */
        new SparkConfig(database, alueDao, lankaDao, viestiDao, kayttajaDao);

    }
}
