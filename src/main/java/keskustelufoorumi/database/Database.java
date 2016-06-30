package keskustelufoorumi.database;

import java.net.URI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private String databaseAddress;

    public Database() throws ClassNotFoundException {
        this.databaseAddress = "jdbc:sqlite:forum.db";
    }

// toimiva plain luokka = konstruktori plus alla oleva metodi    
//    public Connection getConnection() throws SQLException {
//        return DriverManager.getConnection(databaseAddress);
//    }
    public Database(String databaseAddress) throws ClassNotFoundException {
        this.databaseAddress = databaseAddress;
    }

    private void init() {
        List<String> lauseet = null;
        if (this.databaseAddress.contains("postgres")) {
            lauseet = postgreLauseet();
        } else {
            lauseet = sqliteLauseet();
        }

        // "try with resources" sulkee resurssin automaattisesti lopuksi
        try (Connection conn = getConnection()) {
            Statement st = conn.createStatement();

            // suoritetaan komennot
            for (String lause : lauseet) {
                System.out.println("Running command >> " + lause);
                st.executeUpdate(lause);
            }

        } catch (Throwable t) {
            // jos tietokantataulu on jo olemassa, ei komentoja suoriteta
            System.out.println("Error >> " + t.getMessage());
        }
    }

    public Connection getConnection() throws SQLException {
        if (this.databaseAddress.contains("postgres")) {
            try {
                URI dbUri = new URI(databaseAddress);

                String username = dbUri.getUserInfo().split(":")[0];
                String password = dbUri.getUserInfo().split(":")[1];
                String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();

                return DriverManager.getConnection(dbUrl, username, password);
            } catch (Throwable t) {
                System.out.println("Error: " + t.getMessage());
                t.printStackTrace();
            }
        }

        return DriverManager.getConnection(databaseAddress);
    }

    private List<String> postgreLauseet() {
        ArrayList<String> lista = new ArrayList<>();

        // tietokantataulujen luomiseen tarvittavat komennot suoritusjärjestyksessä
        lista.add("CREATE TABLE Kayttaja "
                + "(id varchar(20) PRIMARY KEY);");
        
        lista.add("CREATE TABLE Alue "
                + "("
                + "id integer PRIMARY KEY,"
                + "aluenimi varchar(20),"
                + "viimeisin_aika timestamp NOT NULL,"
                + "alueviestimaara integer NOT NULL"
                + ");");
        
        lista.add("CREATE TABLE Lanka "
                + "("
                + "id integer PRIMARY KEY,"
                + "lankanimi varchar(40) NOT NULL,"
                + "alue_id integer NOT NULL,"
                + "lankaviestimaara integer NOT NULL,"
                + "viimeisin_aika timestamp NOT NULL,"
                + "FOREIGN KEY(alue_id) REFERENCES Alue(id)"
                + ");");
        
        lista.add("CREATE TABLE Viesti "
                + "("
                + "id integer PRIMARY KEY,"
                + "sisalto varchar(140),"
                + "kayttaja_id varchar(20) NOT NULL,"
                + "lanka_id integer NOT NULL,"
                + "lahetysaika timestamp NOT NULL,"
                + "FOREIGN KEY(kayttaja_id) REFERENCES Kayttaja(id),"
                + "FOREIGN KEY(lanka_id) REFERENCES Lanka(id)"
                + ");");

        return lista;
    }

    private List<String> sqliteLauseet() {
        ArrayList<String> lista = new ArrayList<>();

        // tietokantataulujen luomiseen tarvittavat komennot suoritusjärjestyksessä
        lista.add("CREATE TABLE Kayttaja "
                + "(id varchar(20) PRIMARY KEY);");
        
        lista.add("CREATE TABLE Alue "
                + "("
                + "id integer PRIMARY KEY,"
                + "aluenimi varchar(20),"
                + "viimeisin_aika timestamp NOT NULL,"
                + "alueviestimaara integer NOT NULL"
                + ");");
        
        lista.add("CREATE TABLE Lanka "
                + "("
                + "id integer PRIMARY KEY,"
                + "lankanimi varchar(40) NOT NULL,"
                + "alue_id integer NOT NULL,"
                + "lankaviestimaara integer NOT NULL,"
                + "viimeisin_aika timestamp NOT NULL,"
                + "FOREIGN KEY(alue_id) REFERENCES Alue(id)"
                + ");");
        
        lista.add("CREATE TABLE Viesti "
                + "("
                + "id integer PRIMARY KEY,"
                + "sisalto varchar(140),"
                + "kayttaja_id varchar(20) NOT NULL,"
                + "lanka_id integer NOT NULL,"
                + "lahetysaika timestamp NOT NULL,"
                + "FOREIGN KEY(kayttaja_id) REFERENCES Kayttaja(id),"
                + "FOREIGN KEY(lanka_id) REFERENCES Lanka(id)"
                + ");");

        return lista;
    }

}
