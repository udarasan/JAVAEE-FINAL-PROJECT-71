package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import org.apache.commons.dbcp2.BasicDataSource;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@WebServlet("/api/v1/employee")
@MultipartConfig
public class EmployeeServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String eid = req.getParameter("eid");
        String ename = req.getParameter("ename");
        String eemail = req.getParameter("eemail");

        Part filepart = req.getPart("eimage");
        String fileName = filepart.getSubmittedFileName();
        System.out.println("fileName:"+fileName);

        String uploadPath="C:\\Lectures\\Batch\\GDSE71\\AAD\\JavaEE\\Work\\EMS-FN\\assests";
        java.io.File file=new java.io.File(uploadPath);
        if(!file.exists()){
            file.mkdir();
        }
        String fileAbsolutePath=uploadPath+java.io.File.separator+fileName;
        filepart.write(fileAbsolutePath);

    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        resp.setContentType("application/json");

        ServletContext sc = req.getServletContext();
        BasicDataSource ds = (BasicDataSource) sc.getAttribute("ds");

        try (Connection connection = ds.getConnection()) {
            PreparedStatement pstm = connection.prepareStatement(
                    "SELECT eid, ename, eaddress, eemail FROM employee"
            );
            ResultSet rs = pstm.executeQuery();

            // Build a list of employees
            List<Map<String, String>> employees = new ArrayList<>();

            while (rs.next()) {
                Map<String, String> emp = Map.of(
                        "eid", rs.getString("eid"),
                        "ename", rs.getString("ename"),
                        "eaddress", rs.getString("eaddress"),
                        "eemail", rs.getString("eemail")
                );
                employees.add(emp);
            }

            // Write response
            PrintWriter out = resp.getWriter();
            resp.setStatus(HttpServletResponse.SC_OK);
            mapper.writeValue(out, Map.of(
                    "code", "200",
                    "status", "success",
                    "data", employees
            ));

        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            mapper.writeValue(resp.getWriter(), Map.of(
                    "code", "500",
                    "status", "error",
                    "message", "Internal server error!"
            ));
        }
    }


}
