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
        String query = "INSERT INTO `railway_lines`(`line`) VALUES (?)";

        try(PreparedStatement statement = connection.prepareStatement(query)){
            statement.setString(1,line.getLineName());


            statement.executeUpdate();
        }catch (SQLException e){
            System.out.println("Error when inserting new entry in line table: "+e.getMessage());

        }


    }


        public List<Line> getAllLine(){
        Connection connection = DBConnection.getConnection();
        List<Line> lineList = new ArrayList<>();

        String query = "SELECT * FROM `railway_lines`";

        try(PreparedStatement statement = connection.prepareStatement(query)){
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                Line line = new Line();
                line.setLineName(resultSet.getString("line"));

                lineList.add(line);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return lineList;

    }

        public void deleteLine(Line line){
        Connection connection = DBConnection.getConnection();
        String query = "DELETE FROM `railway_lines` WHERE line=?;";

        try(PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1,line.getLineName());
            statement.executeUpdate();
        } catch (SQLException e){
            System.out.println("Error occurred while executing delete query for train table");
        }
        }
    }

