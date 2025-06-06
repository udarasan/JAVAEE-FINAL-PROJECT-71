package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.dbcp2.BasicDataSource;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

@WebServlet("/api/v1/signin")
public class SignInServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //check the login details and response
        ObjectMapper mapper = new ObjectMapper();
        Map<String,String> user =
                mapper.readValue(req.getInputStream(), Map.class);

        ServletContext sc = req.getServletContext();
        BasicDataSource ds= (BasicDataSource) sc.getAttribute("ds");
        //user sign in logic
        String email=user.get("uemail");
        String password=user.get("upassword");
        System.out.println("email:"+email+"password:"+password);

        try {
            Connection connection=ds.getConnection();
            PreparedStatement pstm=connection
                    .prepareStatement("SELECT * FROM systemusers WHERE uemail=? AND upassword=?");
            pstm.setString(1, email);
            pstm.setString(2, password);
            ResultSet rs=pstm.executeQuery();
            resp.setContentType("application/json");
            PrintWriter out=resp.getWriter();
            if(rs.next()){
                resp.setStatus(HttpServletResponse.SC_OK);
                mapper.writeValue(out,Map.of(
                        "code","200",
                        "status","Login Success",
                        "message","You have been logged in successfully"
                ));
            }else {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                mapper.writeValue(out,Map.of(
                        "code","401",
                        "status","Unauthorized",
                        "message","Unauthorized Behaviour"
                ));
            }
            connection.close();
        } catch (SQLException e) {
            PrintWriter out=resp.getWriter();
            mapper.writeValue(out,Map.of(
                    "code","500",
                    "status","Error",
                    "message",e.getMessage()
            ));
            throw new RuntimeException(e);
        }
    }
}
