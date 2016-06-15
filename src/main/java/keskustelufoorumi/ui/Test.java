///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package keskustelufoorumi.ui;
//
//import java.sql.SQLException;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import keskustelufoorumi.database.generic.DaoCommand;
//import keskustelufoorumi.database.generic.DaoManager;
//import keskustelufoorumi.domain.Kayttaja;
//
///**
// *
// * @author Ilkka
// */
//public class Test {
//
//    public Test() throws SQLException {
//    
//        
//        DaoManager dm = new DaoManager();
//        
//        dm.executeAndClose(new DaoCommand<Void>() {
//            
//            @Override
//            public Void execute(DaoManager manager) {
//                try {
//                    dm.kayttajaDao().findOne("asd");
//                } catch (SQLException ex) {
//                    Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
//                    
//                }
//
//            }
//        });
//    }
//    
//    
//    
//}
