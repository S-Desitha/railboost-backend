package org.ucsc.railboostbackend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private final String host = "localhost";
    private final String port = "3306";
    private final String db_name = "RailBoost";
    private final String username = "root";
    private final String password = "";

    public Connection getInstance() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String dbUrl = "jdbc:mysql://"+host+":"+port+"/"+db_name;

        return DriverManager.getConnection(dbUrl, username, password);
    }
}
