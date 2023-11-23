package org.ucsc.railboostbackend.repositories;

import org.ucsc.railboostbackend.models.Train;
import org.ucsc.railboostbackend.utilities.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TrainRepo {
    public List<Train> getNTrains(int count) {
        List<Train> trains = new ArrayList<>();
        Connection connection = DBConnection.getConnection();
        String query = "SELECT * FROM train LIMIT ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, count);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Train train = new Train();
                train.setTrainId(resultSet.getString("trainId"));
                train.setTrainType(resultSet.getString("trainType"));
                train.setnCompartments(resultSet.getShort("nCompartments"));

                trains.add(train);
            }
        } catch (SQLException e) {
            System.out.println("Error in select query for train table: \n"+e.getMessage());
        }

        return trains;
    }


    public Train getTrainById(String trainId) {
        Train train = new Train();

        return train;
    }


    public void addTrain(Train train) {
        Connection connection = DBConnection.getConnection();
        String query = "INSERT INTO train (trainId, trainType, nCompartments) VALUES (?, ?, ?) ";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, train.getTrainId());
            statement.setString(2, train.getTrainType());
            statement.setShort(3, train.getnCompartments());

            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error when inserting new entry in train table: "+e.getMessage());
        }
    }


    public void updateTrain(Train train) {
        Connection connection = DBConnection.getConnection();
        String query = "UPDATE train SET trainType=?, nCompartments=? WHERE trainId=?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, train.getTrainType());
            statement.setShort(2, train.getnCompartments());
            statement.setString(3, train.getTrainId());

            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error occurred when executing the update query for train table");
        }
    }


    public void deleteTrain(Train train) {
        Connection connection = DBConnection.getConnection();
        String query = "DELETE FROM train WHERE trainId=?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, train.getTrainId());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error occurred while executing delete query for train table");
        }
    }
}
