package org.ucsc.railboostbackend.repositories;

import org.ucsc.railboostbackend.models.Notification;
import org.ucsc.railboostbackend.utilities.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class NotificationRepo {

    public List<Notification> getAllNotifications(){
        Connection connection = DBConnection.getConnection();
        List<Notification> notificationList = new ArrayList<>();
        String query = "SELECT * FROM notifications";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                Notification notification = new Notification();
                notification.setId(resultSet.getLong("id"));
                notification.setTitle(resultSet.getString("title"));
                notification.setMessage(resultSet.getString("message"));
                notification.setTimestamp(resultSet.getTimestamp("timestamp").toLocalDateTime());

                notificationList.add(notification);
            }
        } catch (SQLException e){
            System.out.println("Error in select query for notifications table: \n"+e.getMessage());
        }
        return notificationList;
    }

    public void addNotification(Notification notification) {
        Connection connection = DBConnection.getConnection();
        String query = "INSERT INTO notifications (title, message, timestamp) VALUES (?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, notification.getTitle());
            statement.setString(2, notification.getMessage());
            statement.setTimestamp(3, java.sql.Timestamp.valueOf(notification.getTimestamp()));

            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error when inserting new entry in notifications table: "+e.getMessage());
        }
    }
}
