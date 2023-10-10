package org.ucsc.railboostbackend.utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String host = "localhost";
    private static final String port = "3306";
    private static final String db_name = "RailBoost";
    private static final String username = "root";
    private static final String password = "";

    static String dbUrl = "jdbc:mysql://"+host+":"+port+"/"+db_name;

    private static Connection connection;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Class Error: "+e.getMessage());
        }
    }


    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed())
                connection = DriverManager.getConnection(dbUrl, username, password);
        } catch (SQLException e) {
            System.out.println("Error creating database connection!!\n" + e.getMessage());
        }

        return connection;
    }
}
