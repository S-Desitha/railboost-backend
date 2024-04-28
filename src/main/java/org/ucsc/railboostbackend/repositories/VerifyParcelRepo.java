package org.ucsc.railboostbackend.repositories;

import org.ucsc.railboostbackend.models.ApproveParcel;
import org.ucsc.railboostbackend.models.VerifyParcel;
import org.ucsc.railboostbackend.utilities.DBConnection;

import javax.servlet.http.HttpServlet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VerifyParcelRepo extends HttpServlet {
    public List<VerifyParcel> getParcelDetailsByStation(String station) throws SQLException {
        Connection connection = DBConnection.getConnection();
        List<VerifyParcel> verifyParcelArrayList = new ArrayList<>();

        String ApproveParcelQuery = "SELECT bookingId, recoveringStation, receiverName,receiverEmail, item," +
                " receiverNIC,receiverAddress FROM parcelbooking WHERE recoveringStation=? " +
                "AND deliver_status=\"Received\"";

        try(PreparedStatement statement = connection.prepareStatement(ApproveParcelQuery)) {
            statement.setString(1,station);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                VerifyParcel verifyParcel= new VerifyParcel();
                verifyParcel.setBookingId(resultSet.getString("bookingId"));
                verifyParcel.setRecoveringStation(resultSet.getString("recoveringStation"));
                verifyParcel.setReceiverName(resultSet.getString("receiverName"));
                verifyParcel.setReceiverEmail(resultSet.getString("receiverEmail"));
                verifyParcel.setItem(resultSet.getString("item"));
                verifyParcel.setReceiverNIC(resultSet.getString("receiverNIC"));
                verifyParcel.setReceiverAddress(resultSet.getString("receiverAddress"));

                verifyParcelArrayList.add(verifyParcel);

            }
        }


        return verifyParcelArrayList;
    }
}
