package keskustelufoorumi;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import keskustelufoorumi.database.AlueDao;
import keskustelufoorumi.database.DaoManager;
import keskustelufoorumi.database.Database;
import keskustelufoorumi.database.LankaDao;
import keskustelufoorumi.ui.TekstiUi;
import spark.ModelAndView;
import static spark.Spark.get;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
//import keskustelufoorumi.ui.TekstiUi;

public class Main {

    public static void main(String[] args) throws Exception {

        Database database = new Database();
        DaoManager daoManager = new DaoManager(database);

        get("/", (req, res) -> {
            HashMap map = new HashMap<>();
            
            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());

        get("/alueet", (req, res) -> {
            AlueDao alueDao = daoManager.getAlueDao();
            
            Map map = new HashMap<>();            
            map.put("alueet", alueDao.findAll());

            return new ModelAndView(map, "alueet");
        }, new ThymeleafTemplateEngine());

        get("/alueet/:id", (req, res) -> {            
            AlueDao alueDao = daoManager.getAlueDao();
            LankaDao lankaDao = daoManager.getLankaDao();
            
            HashMap map = new HashMap<>();
            map.put("aluenimi", alueDao.findOne(Integer.parseInt(req.params("id"))).getAluenimi());
            map.put("langat", lankaDao.findAllWhereXIsK(Integer.parseInt(req.params("id"))));
            
            return new ModelAndView(map, "alue");
        }, new ThymeleafTemplateEngine());

              
        Scanner lukija = new Scanner(System.in);        
        TekstiUi tekstiUi = new TekstiUi(lukija, daoManager);
        tekstiUi.kaynnista();    
         
    }

}
