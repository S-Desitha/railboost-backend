package org.ucsc.railboostbackend.repositories;

import org.ucsc.railboostbackend.models.Announcement;
import org.ucsc.railboostbackend.models.Season;
import org.ucsc.railboostbackend.models.Staff;
import org.ucsc.railboostbackend.services.EmailService;
import org.ucsc.railboostbackend.utilities.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AnnouncementRepo {

    public void AddAnnouncement(Announcement announcement){
        Connection connection = DBConnection.getConnection();

        try{
            String query = "INSERT INTO announcement (title, category, body, date) VALUES (?, ?, ?, ?)";
            try(PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, announcement.getTitle());
                statement.setString(2, announcement.getCategory());
                statement.setString(3, announcement.getBody());
                statement.setObject(4, announcement.getDate());

                statement.executeUpdate();

            }

        }catch (SQLException e){
            System.out.println("Error occurred during adding announcement : " + e.getMessage());
        }
    }

    public List<Announcement> getAnnouncements(){
        Connection connection= DBConnection.getConnection();
        List<Announcement> announcementList = new ArrayList<>();
        String query = "SELECT * FROM announcement ORDER BY id DESC LIMIT 10";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                Announcement announcement = new Announcement();
                announcement.setId(resultSet.getInt("id"));
                announcement.setTitle(resultSet.getString("title"));
                announcement.setCategory(resultSet.getString("category"));
                announcement.setBody(resultSet.getString("body"));
                announcement.setDate(resultSet.getDate("date").toLocalDate());

                announcementList.add(announcement);
            }
        } catch (SQLException e){
            System.out.println("Error in select query for getting announcements: \n"+e.getMessage());
        }
        return announcementList;
    }

    public List<Announcement> getAnnouncementsForDash(){
        Connection connection= DBConnection.getConnection();
        List<Announcement> announcementList = new ArrayList<>();
        String query = "SELECT * FROM announcement ORDER BY id DESC LIMIT 3";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                Announcement announcement = new Announcement();
                announcement.setId(resultSet.getInt("id"));
                announcement.setTitle(resultSet.getString("title"));
                announcement.setCategory(resultSet.getString("category"));
                announcement.setBody(resultSet.getString("body"));
                announcement.setDate(resultSet.getDate("date").toLocalDate());

                announcementList.add(announcement);
            }
        } catch (SQLException e){
            System.out.println("Error in select query for getting announcements: \n"+e.getMessage());
        }
        return announcementList;
    }

    public void updateAnns(Announcement announcement){
        Connection connection = DBConnection.getConnection();

        try{
            String query = "UPDATE announcement SET title = ?, category = ?, body = ? WHERE id = ?";
            try(PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, announcement.getTitle());
                statement.setString(2, announcement.getCategory());
                statement.setString(3, announcement.getBody());
//                statement.setObject(4, announcement.getDate());
                statement.setInt(4, announcement.getId());

                statement.executeUpdate();
            }

        }catch (SQLException e){
            System.out.println("Error occurred during updating announcement: " + e.getMessage());
        }
    }

    public void deleteAnns(Announcement announcement) {
        Connection connection = DBConnection.getConnection();
        String query = "DELETE FROM announcement WHERE id=?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, announcement.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error occurred while executing delete query for announcement table");
        }

    }
}
