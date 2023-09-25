package org.ucsc.railboostbackend.Utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private final String host = "localhost";
    private final String port = "3306";
    private final String db_name = "RailBoost";
    private final String username = "root";
    private final String password = "";

    private static DBConnection dbConnection;
    private final Connection connection;

    private DBConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String dbUrl = "jdbc:mysql://"+host+":"+port+"/"+db_name;

        connection = DriverManager.getConnection(dbUrl, username, password);
    }

    public static DBConnection getInstance() throws SQLException, ClassNotFoundException {
        if (dbConnection==null)
            dbConnection = new DBConnection();

        return dbConnection;
    }

    public Connection getConnection() {
        return connection;
    }
}
