package org.ucsc.railboostbackend.repositories;

import org.ucsc.railboostbackend.models.ParcelBooking;
import org.ucsc.railboostbackend.models.ParcelTracking;
import org.ucsc.railboostbackend.utilities.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ParcelTrackingRepo {

    public List<ParcelTracking> getTrackingParcelsByID(String station){
        Connection connection = DBConnection.getConnection();
        List<ParcelTracking> parcelTrackingList = new ArrayList<>();
        List<ParcelTracking> stations = new ArrayList<>();

        String query = "SELECT p.bookingId,p.trackingId, p.recoveringStation, p.item, p.sendingStation,p.status" +
                " FROM parcelbooking p INNER JOIN users u ON p.userId= u.userId " +
                "WHERE p.sendingStation=?";

        try(PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1,station);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                ParcelTracking parcelTracking = new ParcelTracking();

                parcelTracking.setRecoveringStation(resultSet.getString("recoveringStation"));
                parcelTracking.setTrackingId(resultSet.getString("trackingId"));
                parcelTracking.setBookingId(resultSet.getString("bookingId"));
                parcelTracking.setItem(resultSet.getString("item"));
                parcelTracking.setSendingStation(resultSet.getString("sendingStation"));
                

                parcelTrackingList.add(parcelTracking);

            }
//            stations.add(parcelTrackingList.get(0));
//            for (int i = 1; i < parcelTrackingList.size(); i++) {
//                for (int j=0;j < stations.size(); j++)
//               if(stations.get(j)!=parcelTrackingList.get(i));
//                stations.add(parcelTrackingList.get(i));
//            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return parcelTrackingList;
    }
}
