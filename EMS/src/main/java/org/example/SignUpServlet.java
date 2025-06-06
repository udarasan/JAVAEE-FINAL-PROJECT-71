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
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;

@WebServlet("/api/v1/signup")
public class SignUpServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //read json obj and write in to hashmap
        ObjectMapper mapper = new ObjectMapper();
        Map<String,String> user = mapper.readValue(req.getInputStream(), Map.class);

        //save user in database
        ServletContext sc = req.getServletContext();
        BasicDataSource dataSource= (BasicDataSource) sc.getAttribute("ds");
        try {
            Connection connection=dataSource.getConnection();
            PreparedStatement pstm=
                    connection.prepareStatement("INSERT INTO systemusers" +
                            "(uid,uname,upassword,uemail) Values (?,?,?,?)");
            pstm.setString(1, UUID.randomUUID().toString());
            pstm.setString(2, user.get("uname"));
            pstm.setString(3,user.get("upassword"));
            pstm.setString(4,user.get("uemail"));

            int executed=pstm.executeUpdate();
            PrintWriter out=resp.getWriter();
            resp.setContentType("application/json");
            if(executed>0){
                resp.setStatus(200);
            }else {
                resp.setStatus(500);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
}