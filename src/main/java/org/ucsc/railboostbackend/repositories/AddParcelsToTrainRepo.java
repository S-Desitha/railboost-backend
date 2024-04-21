package org.ucsc.railboostbackend.repositories;

import org.ucsc.railboostbackend.models.AddParcelsTOTrain;
import org.ucsc.railboostbackend.utilities.DBConnection;

import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AddParcelsToTrainRepo extends HttpServlet {
    public  List<AddParcelsTOTrain> getParcelsByScheduleId(int scheduleId)throws IOException{
        Connection connection = DBConnection.getConnection();
        List<AddParcelsTOTrain> addParcelsTOTrainList = new ArrayList<>();

        String query = "SELECT bookingId,item ,recoveringStation FROM `parcelbooking` WHERE deliver_status=\"Pending\" AND scheduleId= ?;";
        try(PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1,scheduleId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                AddParcelsTOTrain addParcelsTOTrain = new AddParcelsTOTrain();

                addParcelsTOTrain.setBookingId(resultSet.getInt("bookingId"));
                addParcelsTOTrain.setItem(resultSet.getString("item"));
                addParcelsTOTrainList.add(addParcelsTOTrain);
                System.out.println(addParcelsTOTrainList);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return addParcelsTOTrainList;
    }
    public List<AddParcelsTOTrain> getScheduleByStation(String station) throws IOException, SQLException {
        Connection connection = DBConnection.getConnection();
        List<AddParcelsTOTrain> addParcelsTOTrainList = new ArrayList<>();


        String query ="SELECT DISTINCT scheduleId\n" +
                "FROM parcelbooking\n" +
                "WHERE sendingStation = ? AND scheduleId != 0 AND deliver_status=\"Pending\";";
        try (PreparedStatement statement = connection.prepareStatement(query)){
            statement.setString(1,station);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                AddParcelsTOTrain addParcelsTOTrain = new AddParcelsTOTrain();

                addParcelsTOTrain.setScheduleId(resultSet.getInt("scheduleId"));

                addParcelsTOTrainList.add(addParcelsTOTrain);
                System.out.println(addParcelsTOTrainList);
            }
        }

        return addParcelsTOTrainList;
    }


}
