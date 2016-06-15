package keskustelufoorumi.database.generic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import keskustelufoorumi.database.AlueDao;
import keskustelufoorumi.database.LankaDao;
import keskustelufoorumi.database.ViestiDao;

public class DaoManagerBS {

    protected String databaseAddress = null;
    protected Connection connection = null;
    protected KayttajaDao2 kayttajaDao = null;
    protected AlueDao alueDao = null;
    protected LankaDao lankaDao = null;
    protected ViestiDao viestiDao = null;

    public DaoManagerBS() {
        this.databaseAddress = "jdbc:sqlite:forum.db";

    }

    public KayttajaDao2 kayttajaDao() {
        if (this.kayttajaDao == null) {
            this.kayttajaDao = new KayttajaDao2(this.connection);
        }

        return this.kayttajaDao;
    }

    protected Connection getConnection() throws SQLException {
        if (this.connection == null) {
            this.connection = DriverManager.getConnection(databaseAddress);
        }
        return this.connection;
    }

    protected Connection getTxConnection() throws SQLException {
        getConnection().setAutoCommit(false);
        
        return this.connection;
    }

    public Object transaction(DaoCommand command) throws SQLException {
        try {
            Object returnValue = command.execute(this);
            getConnection().commit();
            return returnValue;
        } catch (Exception e) {
            getConnection().rollback();
            throw e; //or wrap it before rethrowing it
        } finally {
            getConnection().setAutoCommit(true);
        }
    }

    public Object executeAndClose(DaoCommand command) throws SQLException {
        try {
            return command.execute(this);
        } finally {
            getConnection().close();
        }
    }

//    public Object transactionAndClose(DaoCommand command) throws SQLException {
//        executeAndClose(new DaoCommand() {
//            public Object execute(DaoManager manager) {
//                try {
//                    manager.transaction(command);
//                } catch (SQLException ex) {
//                    Logger.getLogger(DaoManager.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//        });
//    }
}
