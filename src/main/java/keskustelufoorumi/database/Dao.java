package keskustelufoorumi.database;

import java.sql.*;
import java.util.*;

public interface Dao<T, K> {
    
    /*
      Etsi yksi käsitteen T ilmentymä avaimella K
    */
    T findOne(K key) throws SQLException;
    
    /*
      Etsi kaikki käsitteen T ilmentymät
    */
    List<T> findAll() throws SQLException;
    
    /*
      Etsi käsitteen T ilmentymät annetulla avainkokoelmalla
    */
    List<T> findAllIn(Collection<K> keys) throws SQLException;

    
//    List<T> findAllWhereXIsK(String x, K key) throws SQLException;

    /*
      Poista käsitteen T ilmentymä K
    */
    void delete(K key) throws SQLException;

    /*
      Tee tietokantaan preparedStatement-päivityskysely, jossa kysely on 
      merkkijono updateQuery ja kyselyssä käytettävät arvot parametreina
      Object params...
    */
    void update(String updateQuery, Object... params) throws SQLException;
    
    /*
      Vie tietokantaan uusi käsitteen T ilmentymä t
    */
    void insertNewInstance(T t) throws SQLException;
    
    /*
      Päivitä tietokannassa olevaa käsitteen T ilmentymää t
    */
    void updateInstance(T t) throws SQLException;

}
