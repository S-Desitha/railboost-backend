package org.ucsc.railboostbackend.controllers;

import com.google.gson.Gson;
import org.ucsc.railboostbackend.models.ParcelCategory;
import org.ucsc.railboostbackend.repositories.ParcelCategoryRepo;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class ParcelCategoryController extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter writer = resp.getWriter();
        Gson gson = new Gson();
        try {
            List<ParcelCategory> parcelCategoryList;
            ParcelCategoryRepo parcelCategoryRepo = new ParcelCategoryRepo();

            parcelCategoryList = parcelCategoryRepo.getAllCategory();
            writer.write(gson.toJson(parcelCategoryList));


        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        writer.flush();
        writer.close();


    }
}
