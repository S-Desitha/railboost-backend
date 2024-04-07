package org.ucsc.railboostbackend.repositories;
import org.ucsc.railboostbackend.models.Notification;
import org.ucsc.railboostbackend.utilities.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class NotificationRepoImpl implements NotificationRepo {
    private final Connection connection;

    public NotificationRepoImpl(Connection connection) {
        this.connection = connection;
    }

    public NotificationRepoImpl() {
        this.connection = DBConnection.getConnection();
    }

    @Override
    public void save(Notification notification) {
        String sql = "INSERT INTO notifications (message, timestamp) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, notification.getMessage());
            statement.setTimestamp(2, Timestamp.valueOf(notification.getTimestamp()));
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Notification> findAll() {
        List<Notification> notifications = new ArrayList<>();
        String sql = "SELECT * FROM notifications";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String message = resultSet.getString("message");
                LocalDateTime timestamp = resultSet.getTimestamp("timestamp").toLocalDateTime();
                notifications.add(new Notification(id, message, timestamp));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notifications;
    }
}
