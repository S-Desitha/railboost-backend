package org.ucsc.railboostbackend.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.poi.ss.usermodel.Workbook;
import org.ucsc.railboostbackend.models.ResponseType;
import org.ucsc.railboostbackend.models.TicketPrice;
import org.ucsc.railboostbackend.models.Train;
import org.ucsc.railboostbackend.repositories.RatesRepo;
import org.ucsc.railboostbackend.repositories.StationRepo;
import org.ucsc.railboostbackend.repositories.TicketPriceRepo;
import org.ucsc.railboostbackend.repositories.TrainRepo;
import org.ucsc.railboostbackend.services.*;
import org.ucsc.railboostbackend.utilities.Constants;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;

@MultipartConfig(
        fileSizeThreshold = 1024*1024,
        maxFileSize = 1024*1024*5,
        maxRequestSize = 1024*1024*10
)
public class RatesController extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new Gson();
        RatesRepo ratesRepo = new RatesRepo();

        if (req.getParameter("isTemplate")!=null && req.getParameter("isTemplate").equals("true")) {
            String stationCode = req.getParameter("stationCode");
            FileResponseWrapper responseWrapper = new FileResponseWrapper(req, resp);
            Workbook workbook = ratesRepo.createExcelTemplate(stationCode, new StationRepo().getStationName(stationCode));
            responseWrapper.sendExcelFile(workbook, req.getParameter("stationCode"));
        }
        else {
            List<TicketPrice> ratesList;
            PrintWriter writer = resp.getWriter();
            ratesList=ratesRepo.getAllRates();

            writer.write(gson.toJson(ratesList));
            writer.flush();
            writer.close();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        FileRequestWrapper requestWrapper = new FileRequestWrapper(req);
        PrintWriter writer = resp.getWriter();
        TicketPrice ticketPrice = new TicketPrice();
        RatesRepo ratesRepo = new RatesRepo();
        ResponseType responseType = new ResponseType();
        Gson gson = new Gson();

        try {
            String jsonObj = requestWrapper.getJsonObj();
            ticketPrice = gson.fromJson(jsonObj, TicketPrice.class);

            String filename = requestWrapper.saveFile( Constants.EXCEL_UPLOAD_DIR, ticketPrice.getStartCode());
            responseType = ratesRepo.updateRatesFromExcel(requestWrapper.getFolderPath()+Constants.EXCEL_UPLOAD_DIR+File.separator+filename, ticketPrice.getStartCode());
        }
        catch (ServletException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.setContentType("text/html");
            responseType.setError("Unsupported content-type. Should be \"multipart/form-data\"");
        }
        catch (IllegalStateException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.setContentType("text/html");
            responseType.setError("File size exceeds. Request body is greater than 10 MB or file size is greater than 5 MB");
        }

        writer.write(gson.toJson(responseType));
        writer.flush();
        writer.close();

    }


    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        Gson gson = new Gson();
        RatesRepo rateRepo = new RatesRepo();
        TicketPrice rate;

        rate = gson.fromJson(req.getReader(), TicketPrice.class);
        rateRepo.updateRate(rate);
    }


    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        Gson gson = new Gson();
        RatesRepo rateRepo = new RatesRepo();
        TicketPrice rate;

        rate = gson.fromJson(req.getReader(), TicketPrice.class);
        rateRepo.deleteRate(rate);
    }

}
