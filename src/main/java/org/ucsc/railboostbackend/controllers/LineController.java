package org.ucsc.railboostbackend.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.ucsc.railboostbackend.models.Line;
import org.ucsc.railboostbackend.repositories.LineRepo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

public class LineController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req,HttpServletResponse resp)throws ServletException, IOException {
        PrintWriter writer =resp.getWriter();
        Gson gson = new Gson();
        LineRepo lineRepo =  new LineRepo();
        List<Line> lineList;


        lineList = lineRepo.getAllLines();
        writer.write(gson.toJson((lineList),List.class));


        writer.flush();
        writer.close();


    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter writer = resp.getWriter();
        Gson gson = new Gson();
        LineRepo lineRepo = new LineRepo();
        Line line;


        try {
            line = gson.fromJson(req.getReader(), Line.class);
            lineRepo.addLines(line);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            writer.write("Line added successfully");

        } catch (JsonSyntaxException | SQLException | ClassNotFoundException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            writer.write("Error occurred while adding line: " + e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException{
        PrintWriter writer = resp.getWriter();
        Gson gson = new Gson();
        LineRepo lineRepo = new LineRepo();
        Line line;

        line = gson.fromJson(req.getReader(), Line.class);
        lineRepo.updateLine(line);
        writer.write("Line updated successfully");
        writer.flush();
        writer.close();

    }

//    @Override
//    protected void doDelete(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException{
//        Gson gson = new Gson();
//        LineRepo lineRepo = new LineRepo();
//        Line line;
//
//        line = gson.fromJson(req.getReader(), Line.class);
//        lineRepo.deleteLine(line);
//
//
//    }
}
