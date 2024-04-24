package org.ucsc.railboostbackend.repositories;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.ucsc.railboostbackend.models.ResponseType;
import org.ucsc.railboostbackend.models.TicketPrice;
import org.ucsc.railboostbackend.models.Train;
import org.ucsc.railboostbackend.utilities.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RatesRepo {

    public List<TicketPrice> getAllRates(){
        Connection connection= DBConnection.getConnection();
        List<TicketPrice> ratesList = new ArrayList<>();
        String query = "SELECT " +
                "s1.name AS startStationName, " +
                "tp.startCode, " +
                "s2.name AS endStationName, " +
                "tp.endCode, " +
                "tp.`1st Class`, " +
                "tp.`2nd Class`, " +
                "tp.`3rd Class` " +
                "FROM " +
                "ticketprice tp " +
                "JOIN " +
                "station s1 ON tp.startCode COLLATE utf8mb4_unicode_ci = s1.stationCode COLLATE utf8mb4_unicode_ci " +
                "JOIN " +
                "station s2 ON tp.endCode COLLATE utf8mb4_unicode_ci = s2.stationCode COLLATE utf8mb4_unicode_ci " +
                "ORDER BY endStationName";




        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                TicketPrice rate = new TicketPrice();
                rate.setStartStation(resultSet.getString("startStationName"));
                rate.setStartCode(resultSet.getString("startCode")  );
                rate.setEndStation(resultSet.getString("endStationName"));
                rate.setEndCode(resultSet.getString("endCode"));
                rate.setFirstClass(Double.parseDouble(resultSet.getString("1st Class")));
                rate.setSecondClass(Double.parseDouble(resultSet.getString("2nd Class")));
                rate.setThirdClass(Double.parseDouble(resultSet.getString("3rd Class")));

                ratesList.add(rate);
            }
        } catch (SQLException e){
            System.out.println("Error in select query for rates table: \n"+e.getMessage());
        }
        return ratesList;
    }

//    public Train getRateById(String trainId) {
//        Train train = new Train();
//
//        return train;
//    }


    public ResponseType addRate(TicketPrice rate) {
        ResponseType responseType = new ResponseType();
        boolean isSuccess = false;
        Connection connection = DBConnection.getConnection();
        String query = "INSERT INTO ticketprice (startCode,endCode,`1st Class`,`2nd Class`, `3rd Class`) VALUES (?, ?, ?, ?, ?) ";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, rate.getStartCode());
            statement.setString(2, rate.getEndCode());
            statement.setDouble(3, rate.getFirstClass());
            statement.setDouble(4, rate.getSecondClass());
            statement.setDouble(5, rate.getThirdClass());

            isSuccess = statement.executeUpdate()>0;
        } catch (SQLException e) {
            responseType.setError(e.getMessage());
            System.out.println("Error when inserting new entry in rate table: "+e.getMessage());
        }
        responseType.setISSuccessful(isSuccess);
        return responseType;
    }


    public void updateRate(TicketPrice rate) {
        Connection connection = DBConnection.getConnection();
        String query = "UPDATE ticketprice SET `1st Class`=?,`2nd Class`=?, `3rd Class`=? " +
                "WHERE (startCode=? AND endCode=?) " +
                    "OR (startCode=? AND endCode=?) ";

        try (PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setDouble(1, rate.getFirstClass());
            statement.setDouble(2, rate.getSecondClass());
            statement.setDouble(3, rate.getThirdClass());
            statement.setString(4, rate.getStartCode());
            statement.setString(5, rate.getEndCode());
            statement.setString(6, rate.getEndCode());
            statement.setString(7, rate.getStartCode());

            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error occurred when executing the update query for rate table");
            e.printStackTrace();
        }
    }


    public void deleteRate(TicketPrice rate) {
        Connection connection = DBConnection.getConnection();
        String query = "DELETE FROM ticketprice WHERE startCode=? AND endCode=?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, rate.getStartCode());
            statement.setString(2, rate.getEndCode());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error occurred while executing delete query for rate table");
            e.printStackTrace();
        }
    }


    public void setDefaultRates(String station) {
        Connection connection = DBConnection.getConnection();
        String query = "SELECT stationCode FROM station";
        String batchQuery = "INSERT INTO ticketprice " +
                "(startCode, endCode, `1st Class`, `2nd Class`, `3rd Class`) VALUES " +
                "(?, ?, 0, 0, 0) " +
                "ON DUPLICATE KEY UPDATE " +
                    "`1st Class` = 0, " +
                    "`2nd Class` = 0, " +
                    "`3rd Class` = 0 ";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();

            try (PreparedStatement batchStatement = connection.prepareStatement(batchQuery)) {
                while (resultSet.next()) {
                    batchStatement.setString(1, station);
                    batchStatement.setString(2, resultSet.getString(1));
                    batchStatement.addBatch();

                    batchStatement.setString(1, resultSet.getString(1));
                    batchStatement.setString(2, station);
                    batchStatement.addBatch();
                }
                batchStatement.executeBatch();
            }
        } catch (SQLException e) {
            System.out.println("SQL error in setting default values to inserted station rates. RatesRepo.java: setDefaultRates()");
            System.out.println(e.getMessage());
        }
    }

    public void onStationDelete(String station) {
        Connection connection = DBConnection.getConnection();
        String query = "DELETE FROM ticketprice " +
                "WHERE " +
                    "startCode=? " +
                "OR " +
                    "endCode=? ";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, station);
            statement.setString(2, station);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("SQL error in deleting rates on station delete. RatesRepo.java: onStationDelete()");
            System.out.println(e.getMessage());
        }

    }


    public Workbook createExcelTemplate(String stationCode, String stationName) {
        Connection connection = DBConnection.getConnection();
        Workbook workbook = createBasicTemplate(stationName);
        Sheet sheet = workbook.getSheet(stationName);
        String query = "SELECT DISTINCT t.`3rd Class`, t.`2nd Class`, t.`1st Class`, s.stationCode, s.name FROM ticketprice t " +
                "INNER JOIN station s ON s.stationCode = t.endCode " +
                "WHERE t.startCode = ? ";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, stationCode);

            ResultSet resultSet = statement.executeQuery();
            for (int i=1; resultSet.next(); i++) {
                Row row = sheet.createRow(i);
                row.createCell(0).setCellValue(resultSet.getString(5));
                row.createCell(1).setCellValue(resultSet.getString(4));
                row.createCell(2).setCellValue(resultSet.getString(3));
                row.createCell(3).setCellValue(resultSet.getString(2));
                row.createCell(4).setCellValue(resultSet.getString(1));
            }
        } catch (SQLException e) {
            System.out.println("SQL error on select query for station table: RatesRepo: createExcelTemplate()");
            System.out.println(e.getMessage());
        }

        return workbook;
    }

    private Workbook createBasicTemplate(String stationName) {
        Workbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet(stationName);
        sheet.setColumnWidth(0, 256*40); // station name
        sheet.setColumnWidth(1, 256*10); // station code
        sheet.setColumnWidth(2, 256*20); // 1st class
        sheet.setColumnWidth(3, 256*20); // 2nd class
        sheet.setColumnWidth(4, 256*20); // 3rd class

        Row header = sheet.createRow(0);

        CellStyle headerStyle = workbook.createCellStyle();
//        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
//        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 16);
        font.setBold(true);
        headerStyle.setFont(font);

        Cell headerCell = header.createCell(0);
        headerCell.setCellValue("Station Name");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(1);
        headerCell.setCellValue("Code");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(2);
        headerCell.setCellValue("1st Class");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(3);
        headerCell.setCellValue("2ns Class");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(4);
        headerCell.setCellValue("3rd Class");
        headerCell.setCellStyle(headerStyle);

        return workbook;
    }
}

