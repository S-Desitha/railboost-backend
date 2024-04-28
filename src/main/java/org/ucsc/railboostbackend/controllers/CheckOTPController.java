package org.ucsc.railboostbackend.controllers;

import com.google.gson.Gson;
import org.ucsc.railboostbackend.models.CheckOTP;
import org.ucsc.railboostbackend.models.ResponseType;
import org.ucsc.railboostbackend.repositories.CheckOTPRepo;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class CheckOTPController extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)throws IOException{
        PrintWriter writer = resp.getWriter();
        Gson gson = new Gson();
        CheckOTPRepo checkOTPRepo = new CheckOTPRepo();
        ResponseType responseType;
        CheckOTP checkOTP = gson.fromJson(req.getReader(),CheckOTP.class);
//        List<ResponseType> responseTypeList = new ArrayList<>();

        try {
           responseType = checkOTPRepo.verifyOTP(checkOTP);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        writer.write(gson.toJson(responseType));
        writer.flush();
        writer.close();

    }
}
