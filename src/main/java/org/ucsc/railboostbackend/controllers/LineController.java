package org.ucsc.railboostbackend.controllers;

import com.google.gson.Gson;
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
        Line line;
        System.out.println("test 0");
        String lineId = req.getParameter("lineId");
        System.out.println(lineId);

            System.out.println("test 2");
            lineList = lineRepo.getAllLine();
            writer.write(gson.toJson((lineList)));


        writer.flush();
        writer.close();


    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Gson gson = new Gson();
        LineRepo lineRepo = new LineRepo();
        Line line;

        line = gson.fromJson(req.getReader(), Line.class);
        try {
            lineRepo.addLines(line);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    protected void doDelete(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException{
        Gson gson = new Gson();
        LineRepo lineRepo = new LineRepo();
        Line line;

        line = gson.fromJson(req.getReader(), Line.class);
        lineRepo.deleteLine(line);


    }
}
