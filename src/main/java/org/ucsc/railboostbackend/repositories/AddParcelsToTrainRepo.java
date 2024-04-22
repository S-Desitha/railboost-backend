package org.ucsc.railboostbackend.repositories;

import org.ucsc.railboostbackend.models.AddParcelsTOTrain;
import org.ucsc.railboostbackend.models.SendParcels;
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

    public void updateDeliveryStatusToPending(int scheduleId){
        Connection connection = DBConnection.getConnection();
        String query = "UPDATE parcelbooking SET scheduleId= 0,deliver_status=\"Pending\" WHERE scheduleId=? AND deliver_status=\"Assign\";";
        try (PreparedStatement statement = connection.prepareStatement(query)){
            statement.setInt(1,scheduleId);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    public  void updateDeliveryStatus(SendParcels sendParcels){
        Connection connection = DBConnection.getConnection();

        for (int i = 0; i < sendParcels.getBookingIdList().size(); i++) {
            String query = "UPDATE parcelbooking SET deliver_status=\"Sent\" WHERE bookingId=? ";

            try (PreparedStatement statement = connection.prepareStatement(query)){
                String bid = sendParcels.getBookingIdList().get(i);
                int bookingId = Integer.parseInt(bid);
                statement.setInt(1,bookingId);
                statement.executeUpdate();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }


    }
    public  List<AddParcelsTOTrain> getParcelsByScheduleId(int scheduleId)throws IOException{
        Connection connection = DBConnection.getConnection();
        List<AddParcelsTOTrain> addParcelsTOTrainList = new ArrayList<>();

        String query = "SELECT bookingId,item ,recoveringStation FROM `parcelbooking` WHERE deliver_status=\"Assign\" AND scheduleId= ?;";
        try(PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1,scheduleId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                AddParcelsTOTrain addParcelsTOTrain = new AddParcelsTOTrain();

                addParcelsTOTrain.setBookingId(resultSet.getInt("bookingId"));
                addParcelsTOTrain.setItem(resultSet.getString("item"));
                addParcelsTOTrainList.add(addParcelsTOTrain);

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
                "WHERE sendingStation = ? AND scheduleId != 0 AND deliver_status=\"Assign\";";
        try (PreparedStatement statement = connection.prepareStatement(query)){
            statement.setString(1,station);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                AddParcelsTOTrain addParcelsTOTrain = new AddParcelsTOTrain();

                addParcelsTOTrain.setScheduleId(resultSet.getInt("scheduleId"));

                addParcelsTOTrainList.add(addParcelsTOTrain);

            }
        }

        return addParcelsTOTrainList;
    }


}
