package keskustelufoorumi.database;

import java.sql.*;
import java.util.*;

public interface Dao<T, K> {

    T findOne(K key) throws SQLException;

    List<T> findAll() throws SQLException;

    List<T> findAllIn(Collection<K> keys) throws SQLException;

    List<T> findAllWhereXIsK(String x, K key) throws SQLException;

    void delete(K key) throws SQLException;

    void update(String updateQuery, Object... params) throws SQLException;

}
