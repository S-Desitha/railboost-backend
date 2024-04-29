package org.ucsc.railboostbackend.repositories;

import org.ucsc.railboostbackend.models.Train;
import org.ucsc.railboostbackend.utilities.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class DashRepo {
    public int getTrainCount() {
        Connection connection = DBConnection.getConnection();
        LocalDate currentDate = LocalDate.now();
        int tcount = 0;
        String query = "SELECT COUNT(*) AS tCount " +
                "FROM journey WHERE date=?;";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setObject(1, currentDate);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                
                tcount= resultSet.getInt("tCount");

            }
        } catch (SQLException e) {
            System.out.println("Error occurred while executing delete query for journey table");
            e.printStackTrace();
        }
        return tcount;
    }

    public int getTicketCount(){

    Connection connection = DBConnection.getConnection();
    LocalDate currentDate = LocalDate.now();
    int tktcount = 0;
    String query = "SELECT COUNT(*) AS tktCount " +
            "FROM booking WHERE date=?;";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setObject(1, currentDate);
            ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {

            tktcount= resultSet.getInt("tktCount");

        }
    } catch (SQLException e) {
        System.out.println("Error occurred while executing delete query for booking table");
        e.printStackTrace();
    }
        return tktcount;
    }
}
