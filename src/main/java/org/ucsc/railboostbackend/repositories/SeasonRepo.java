package org.ucsc.railboostbackend.repositories;

import org.ucsc.railboostbackend.models.Season;
import org.ucsc.railboostbackend.services.EmailService;
import org.ucsc.railboostbackend.utilities.DBConnection;
import org.ucsc.railboostbackend.utilities.QRCodeGenerator;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SeasonRepo {
    public void ApplySeason(Season season, Object id, String filename){
        Connection connection = DBConnection.getConnection();

        try{
            String query = "INSERT INTO season (userId, startStation, endStation, passengerType, startDate, duration, endDate, trainClass, totalPrice, fileName) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try(PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                statement.setObject(1, id);
                statement.setString(2, season.getStartStation());
                statement.setString(3, season.getEndStation());
                statement.setString(4, season.getPassengerType());
                statement.setObject(5, season.getStartDate());
                statement.setString(6, season.getDuration());
                statement.setObject(7, season.getEndDate());
                statement.setString(8, season.getTrainClass());
                statement.setDouble(9, season.getTotalPrice());
                statement.setString(10,filename);


                statement.executeUpdate();

            }

        }catch (SQLException e){
            System.out.println("Error occurred during applying season ticket : " + e.getMessage());
        }
    }

    public List<Season> getPendingSeasons(Object userId){
        Connection connection= DBConnection.getConnection();
        String scode = StationCodeById(userId);
        List<Season> seasonList = new ArrayList<>();
        String query = "SELECT " +
                "s.id, " +
                "s.userId, " +
                "s.passengerType, " +
                "s1.name AS endStationName, " +
                "s.endStation, " +
                "s.`startDate`, " +
                "s.`endDate`, " +
                "s.`fileName`, " +
                "s.`status` " +
                "FROM " +
                "season s " +
                "JOIN " +
                "station s1 ON s.endStation COLLATE utf8mb4_unicode_ci = s1.stationCode COLLATE utf8mb4_unicode_ci " +
                "WHERE s.status = 'Pending' && s.startStation = ? " +
                "ORDER BY s.id";




        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, scode);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                Season season = new Season();
                season.setId(resultSet.getInt("id"));
                season.setUserId(resultSet.getInt("userId"));
                season.setPassengerType(resultSet.getString("passengerType")  );
                season.setEndStation(resultSet.getString("endStationName"));
                season.setStartDate(resultSet.getDate("startDate").toLocalDate());
                season.setEndDate(resultSet.getDate("endDate").toLocalDate());
                season.setFileName(resultSet.getString("fileName"));
                season.setStatus(resultSet.getString("status"));

                seasonList.add(season);
            }
        } catch (SQLException e){
            System.out.println("Error in select query for getting pending season: \n"+e.getMessage());
        }
        return seasonList;
    }

    public List<Season> getSeasonsOfUser(Object userId){
        Connection connection= DBConnection.getConnection();
        List<Season> seasonList = new ArrayList<>();
        String query = "SELECT " +
                "s.id, " +
                "s.userId,"+
                "s1.name AS startStationName, " +
                "s.startStation, " +
                "s2.name AS endStationName, " +
                "s.endStation, " +
                "s.`startDate`, " +
                "s.`endDate`, " +
                "s.`trainClass`, " +
                "s.`totalPrice`, " +
                "s.`status` " +
                "FROM " +
                "season s " +
                "JOIN " +
                "station s1 ON s.startStation COLLATE utf8mb4_unicode_ci = s1.stationCode COLLATE utf8mb4_unicode_ci " +
                "JOIN "+
                "station s2 ON s.endStation COLLATE utf8mb4_unicode_ci = s2.stationCode COLLATE utf8mb4_unicode_ci " +
                "WHERE s.userId = ? " +
                "ORDER BY s.id DESC "+
                "LIMIT 5";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setObject(1, userId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                Season season = new Season();
                season.setId(resultSet.getInt("id"));
                season.setUserId(resultSet.getInt("userId"));
                season.setStartStation(resultSet.getString("startStationName"));
                season.setEndStation(resultSet.getString("endStationName"));
                season.setStartDate(resultSet.getDate("startDate").toLocalDate());
                season.setEndDate(resultSet.getDate("endDate").toLocalDate());
                season.setTrainClass(resultSet.getString("trainClass"));
                season.setTotalPrice(resultSet.getDouble("totalPrice"));
                season.setStatus(resultSet.getString("status"));

                seasonList.add(season);
            }
        } catch (SQLException e){
            System.out.println("Error in select query for getting user season: \n"+e.getMessage());
        }
        return seasonList;
    }

    public void updateStatus(Season season){
        Connection connection = DBConnection.getConnection();

        try{
            String query = "UPDATE season SET status = ? WHERE id = ?";
            try(PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, season.getStatus());
                statement.setInt(2, season.getId());

                statement.executeUpdate();

//                notification function call

                if (season.getStatus().equals("Paid")){
                    int seasonId = season.getId();
                    int uid=season.getUserId();
                    byte[] qrCodePath = generateAndSaveQRCode(seasonId);
                    String toEmail = EmailById(uid);
                    String subject = "RailBoost Season Ticket";


                    EmailService emailService = new EmailService();
                    String body = emailService.createSeasonTicketHTML(season);
                    emailService.sendEmailWithQRCode(toEmail, subject, body, qrCodePath);
                }
            }

        }catch (SQLException e){
            System.out.println("Error occurred during updating season ticket status: " + e.getMessage());
        }

    }
    private static String EmailById(Object userId) {
        String mail = null;
        Connection  connection = DBConnection.getConnection();

        String query = "SELECT email FROM users WHERE userId=?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setObject(1, userId);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                mail = resultSet.getString("email");

            }
        } catch (SQLException e){
            System.out.println("Error in select query for users table: \n" + e.getMessage());
        }
        return mail;
    }
    private byte[] generateAndSaveQRCode(int seasonId){
        String data = "Season ID: " + seasonId;
        return QRCodeGenerator.generateQRCode(data);
    }
    private static String StationCodeById(Object userId) {
        String scode = null;
        Connection  connection = DBConnection.getConnection();

        String query = "SELECT stationCode FROM staff WHERE userId=?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setObject(1, userId);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                scode = resultSet.getString("stationCode");

            }
        } catch (SQLException e){
            System.out.println("Error in select query for users table: \n" + e.getMessage());
        }
        return scode;
    }
    public Season getSeasonDetails(String id){
        Season season = new Season();
        Connection  connection = DBConnection.getConnection();

        String query = "SELECT " +
                "s.id, " +
                "s.userId,"+
                "s1.name AS startStationName, " +
                "s.startStation, " +
                "s2.name AS endStationName, " +
                "s.endStation, " +
                "s.`passengerType`, " +
                "s.`startDate`, " +
                "s.`endDate`, " +
                "s.`duration`, " +
                "s.`trainClass`, " +
                "s.`totalPrice`, " +
                "s.`status` " +
                "FROM " +
                "season s " +
                "JOIN " +
                "station s1 ON s.startStation COLLATE utf8mb4_unicode_ci = s1.stationCode COLLATE utf8mb4_unicode_ci " +
                "JOIN "+
                "station s2 ON s.endStation COLLATE utf8mb4_unicode_ci = s2.stationCode COLLATE utf8mb4_unicode_ci "+
                "WHERE id=?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, id);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                season.setId(resultSet.getInt("id"));
                season.setUserId(resultSet.getInt("userId"));
                season.setStartStation(resultSet.getString("startStationName"));
                season.setEndStation(resultSet.getString("endStationName"));
                season.setPassengerType(resultSet.getString("passengerType"));
                season.setStartDate(resultSet.getDate("startDate").toLocalDate());
                season.setEndDate(resultSet.getDate("endDate").toLocalDate());
                season.setDuration(resultSet.getString("duration"));
                season.setTrainClass(resultSet.getString("trainClass"));
                season.setTotalPrice(resultSet.getInt("totalPrice"));
                season.setStatus(resultSet.getString("status"));

            }
        } catch (SQLException e){
            System.out.println("Error in select query for season table: \n" + e.getMessage());
        }
        return season;
    }

    public ArrayList<Integer> getSMIds(Season season) {
        Connection connection = DBConnection.getConnection();
        ArrayList<Integer> userIds = new ArrayList<>();

        try {
            String query = "SELECT userId FROM staff WHERE stationCode = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, season.getStartStation());
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    int userId = resultSet.getInt("userId");
                    userIds.add(userId);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error occurred during getting SM IDs: " + e.getMessage());
        }

        return userIds;
    }
}