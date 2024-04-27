package org.ucsc.railboostbackend.repositories;

import org.ucsc.railboostbackend.models.GetRevenue;
import org.ucsc.railboostbackend.utilities.DBConnection;

import javax.servlet.http.HttpServlet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GetRevenueRepo extends HttpServlet {
    public List<String> getMonths(){
        Connection connection = DBConnection.getConnection();
        GetRevenue getRevenue = new GetRevenue();

        String query = "SELECT MONTH(`date`) AS month , month AS month1\n" +
                "FROM `monthlyrevenue`\n" +
                "WHERE `date` >= CURDATE() - INTERVAL 12 MONTH\n" +
                "ORDER BY `date`;\n";
        List<String> list;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            list = new ArrayList<String>();
            while (resultSet.next()) {
                list.add(resultSet.getString("month1"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return list;

    }

    public List<Integer> getmonthlyTicketRevenue(){
        Connection connection = DBConnection.getConnection();
        GetRevenue getRevenue = new GetRevenue();

        String query = "SELECT MONTH(`date`) AS month,`ticketRevenue` AS total_ticket_revenue\n" +
                "FROM `monthlyrevenue`\n" +
                "WHERE `date` >= CURDATE() - INTERVAL 12 MONTH\n" +
                "ORDER BY `date`;\n";
        List<Integer> list;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            list = new ArrayList<Integer>();
            while (resultSet.next()) {
                list.add(resultSet.getInt("total_ticket_revenue"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return list;

    }
    public List<Integer> getMonthleParcelBookingRevenue() {
        Connection connection = DBConnection.getConnection();
        GetRevenue getRevenue = new GetRevenue();

        String query = "SELECT MONTH(`date`) AS month, `revenue` AS total_revenue\n" +
                "FROM `monthlyrevenue`\n" +
                "WHERE `date` >= CURDATE() - INTERVAL 12 MONTH\n" +
                "ORDER BY `date`;\n";
        List<Integer> list;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            list = new ArrayList<Integer>();
            while (resultSet.next()) {
                list.add(resultSet.getInt("total_revenue"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return list;
    }
    public static int getTotalParcelRevenue(){
        Connection connection = DBConnection.getConnection();
        GetRevenue getRevenue = new GetRevenue();
        int total = 0;

        String query = "SELECT SUM(totalprice) AS totalParcelsum FROM parcelbooking;\n";
        try(PreparedStatement statement = connection.prepareStatement(query)){
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()){
                total = resultSet.getInt("totalParcelSum");
//                getRevenue.setTotalParcelBookingRevenue(resultSet.getInt("totalParcelSum"));
            }



        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return total;
    }

    public int getTotalTicketRevenue(){
        Connection connection = DBConnection.getConnection();
        GetRevenue getRevenue = new GetRevenue();
        int total = 0;

        String query = "SELECT SUM(totalPrice) AS totalTicketsum FROM booking;\n";
        try(PreparedStatement statement = connection.prepareStatement(query)){
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()){
                total = resultSet.getInt("totalTicketsum");
            }



        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return total;

    }


}
