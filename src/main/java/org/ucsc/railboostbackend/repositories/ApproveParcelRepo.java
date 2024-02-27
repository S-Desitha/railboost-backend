package org.ucsc.railboostbackend.repositories;

import org.ucsc.railboostbackend.models.ApproveParcel;
import org.ucsc.railboostbackend.utilities.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ApproveParcelRepo {
    public List<ApproveParcel> getParcelDetailsByStation(String station) throws SQLException {
        Connection connection = DBConnection.getConnection();
        ApproveParcel approveParcel= new ApproveParcel();
        List<ApproveParcel> approveParcelsList = new ArrayList<>();

        String ApproveParcelQuery = "SELECT p.bookingId, p.recoveringStation, p.receiverName, p.item, p" +
                ".userId, p.weight, p.status, u.fName FROM parcelbooking p INNER JOIN users u ON p.userId= u.userId " +
                "WHERE p.sendingStation=?";

        try(PreparedStatement statement = connection.prepareStatement(ApproveParcelQuery)) {
            statement.setString(1,station);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                approveParcel.setBookingId(resultSet.getString("bookingId"));
                approveParcel.setSenderName(resultSet.getString("fName"));
                approveParcel.setReceiverName(resultSet.getString("receiverName"));
                approveParcel.setItem(resultSet.getString("item"));
                approveParcel.setRecoveringStation(resultSet.getString("recoveringStation"));
                approveParcel.setWeight(resultSet.getFloat("weight"));
                approveParcel.setStatus(resultSet.getBoolean("status"));

                approveParcelsList.add(approveParcel);

            }
        }


        return approveParcelsList;
    }
}
