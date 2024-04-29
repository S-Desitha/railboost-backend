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
        String query = "INSERT INTO line (lineName) VALUES (?)";

        try(PreparedStatement statement = connection.prepareStatement(query)){
            statement.setString(1,line.getLineName());
//            statement.setString(2,line.getLineStartStation());
//            statement.setString(3,line.getLineEndStation());


            statement.executeUpdate();
        }catch (SQLException e){
            System.out.println("Error when inserting new entry in line table: "+e.getMessage());

        }


    }


    public List<Line> getAllLines(){
        Connection connection = DBConnection.getConnection();
        List<Line> lineList = new ArrayList<>();
        StationRepo stationRepo = new StationRepo();

        String query = "SELECT * FROM line";
        String stationQuery = "SELECT nextStation FROM station WHERE line = ? AND stationCode = ?";

        try(
                PreparedStatement statement = connection.prepareStatement(query);
                PreparedStatement stationStatement = connection.prepareStatement(stationQuery);
        ){
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                List<String> stationList = new ArrayList<>();
                String lineName = resultSet.getString("lineName");
                String indexStation = stationRepo.getIndexStation(lineName);
                stationStatement.setString(1, lineName);
                stationStatement.setString(2, indexStation);

                stationList.add(indexStation);

                while (true) {
                    ResultSet stationResultSet = stationStatement.executeQuery();
                    if (stationResultSet.next()) {
                        String currentStation = stationResultSet.getString(1);
                        stationList.add(currentStation);

                        stationStatement.setString(2, currentStation);
                    }
                    else
                        break;
                }
/*
                Line line = new Line();
                line.setLineId(resultSet.getInt("lineId"));
                line.setLineName(resultSet.getString("lineName"));

                lineList.add(line);*/

                lineList.add(new Line(lineName, stationList));

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return lineList;

    }

//    public Line updateLine(Line line) {
//        Connection connection = DBConnection.getConnection();
//        String query = "UPDATE `line` SET lineEndStation = ? WHERE lineId = ?";
//
//        try (PreparedStatement statement = connection.prepareStatement(query)) {
//            statement.setString(1, line.getLineEndStation());
//            statement.setInt(2, line.getLineId());
//
//            statement.executeUpdate();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//
//        return line;
//    }

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

