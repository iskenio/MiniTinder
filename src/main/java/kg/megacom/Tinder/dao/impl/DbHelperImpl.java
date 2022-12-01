package kg.megacom.Tinder.dao.impl;

import kg.megacom.Tinder.dao.DbHelper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DbHelperImpl implements DbHelper {
    @Override
    public PreparedStatement getStmt(String sql) throws SQLException {
        Connection connection = DriverManager.getConnection
                ("jdbc:postgresql://localhost:5432/Tinder", "postgres", "root");
        return connection.prepareStatement(sql);
    }
}
