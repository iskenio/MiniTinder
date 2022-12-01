package kg.megacom.Tinder.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface DbHelper {
    PreparedStatement getStmt(String sql) throws SQLException;
}
