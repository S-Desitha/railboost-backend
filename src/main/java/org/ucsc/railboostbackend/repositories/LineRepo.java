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
        String query = "INSERT INTO line(line_id, line_name) VALUES (?,?)";

        try(PreparedStatement statement = connection.prepareStatement(query)){
            statement.setString(1,line.getLineId());
            statement.setString(2, line.getLineName());

            statement.executeUpdate();
        }catch (SQLException e){
            System.out.println("Error when inserting new entry in line table: "+e.getMessage());

        }


    }

    public Line getLineById(String lineId) throws SQLException {
        Connection connection = DBConnection.getConnection();
        Line line = new Line();
        String query = "SELECT line_id, line_name FROM line  WHERE line_id=?";
        PreparedStatement pst = null;
        ResultSet resultSet = null;

        //pst = connection.prepareStatement(query);


        try(PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, lineId);
            resultSet = statement.executeQuery();

            if (resultSet.next())
                line.setLineId(resultSet.getString("line_id"));
                line.setLineName((resultSet.getString("line_name")));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return line;
        }
        public List<Line> getAllLine(){
        Connection connection = DBConnection.getConnection();
        List<Line> lineList = new ArrayList<>();

        String query = "SELECT * FROM `line` ";

        try(PreparedStatement statement = connection.prepareStatement(query)){
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                Line line = new Line();
                line.setLineId(resultSet.getString("line_id"));
                line.setLineName(resultSet.getString("line_name"));

                lineList.add(line);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return lineList;

    }
        public void updateLine(Line line) throws SQLException {
        Connection connection = DBConnection.getConnection();
        String query ="UPDATE `line` SET `line_name`=? WHERE line_id=?;";
        PreparedStatement statement =connection.prepareStatement(query);
        statement.setString(1,line.getLineName());
        statement.setString(2,line.getLineId());
        statement.executeUpdate();

        }

        public void deleteLine(Line line){
        Connection connection = DBConnection.getConnection();
        String query = "DELETE FROM `line` WHERE line_id=?";

        try(PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1,line.getLineId());
            statement.executeUpdate();
        } catch (SQLException e){
            System.out.println("Error occurred while executing delete query for train table");
        }
        }
    }

