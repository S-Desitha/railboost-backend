package org.ucsc.railboostbackend.repositories;

import org.ucsc.railboostbackend.models.ParcelCategory;
import org.ucsc.railboostbackend.utilities.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ParcelCategoryRepo {

    public List<ParcelCategory> getAllCategory(){
        Connection connection = DBConnection.getConnection();
        List<ParcelCategory> parcelCategoryList = new ArrayList<>();

        String query = "SELECT * FROM parcelcategory ";
        try(PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                ParcelCategory parcelCategory = new ParcelCategory();
                parcelCategory.setItemId((resultSet.getInt("itemId")));
                parcelCategory.setSpecialItem(resultSet.getString("specialItem"));
                parcelCategoryList.add(parcelCategory);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return parcelCategoryList;
    }
}
