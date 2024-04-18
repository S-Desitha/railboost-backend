package org.ucsc.railboostbackend.repositories;
import org.ucsc.railboostbackend.models.Line;
import org.ucsc.railboostbackend.utilities.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LineRepo {

    public void addLines(Line line)throws SQLException, ClassNotFoundException{
        Connection connection = DBConnection.getConnection();
        String query = "INSERT INTO line (lineName,lineStartStation,lineEndStation) VALUES (?,?,?)";

        try(PreparedStatement statement = connection.prepareStatement(query)){
            statement.setString(1,line.getLineName());
            statement.setString(2,line.getLineStartStation());
            statement.setString(3,line.getLineEndStation());


            statement.executeUpdate();
        }catch (SQLException e){
            System.out.println("Error when inserting new entry in line table: "+e.getMessage());

        }


    }


        public List<Line> getAllLines(){
        Connection connection = DBConnection.getConnection();
        List<Line> lineList = new ArrayList<>();

        String query = "SELECT * FROM `line`";

        try(PreparedStatement statement = connection.prepareStatement(query)){
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                Line line = new Line();
                line.setLineId(resultSet.getInt("lineId"));
                line.setLineName(resultSet.getString("lineName"));
                line.setLineStartStation(resultSet.getString("lineStartStation"));
                line.setLineEndStation(resultSet.getString("lineEndStation"));


                lineList.add(line);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return lineList;

    }

    public Line updateLine(Line line) {
        Connection connection = DBConnection.getConnection();
        String query = "UPDATE `line` SET lineEndStation = ? WHERE lineId = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, line.getLineEndStation());
            statement.setInt(2, line.getLineId());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return line;
    }

//        public void deleteLine(Line line){
//        Connection connection = DBConnection.getConnection();
//        String query = "DELETE FROM `railway_lines` WHERE line=?;";
//
//        try(PreparedStatement statement = connection.prepareStatement(query)) {
//            statement.setString(1,line.getLineName());
//            statement.executeUpdate();
//        } catch (SQLException e){
//            System.out.println("Error occurred while executing delete query for train table");
//        }
//        }
    }

