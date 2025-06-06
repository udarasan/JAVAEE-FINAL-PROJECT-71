package org.example;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.SQLException;

@WebListener
public class DataSource implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        //initialized the database here
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName("com.mysql.jdbc.Driver");
        ds.setUrl("jdbc:mysql://localhost:3306/ems71");
        ds.setUsername("root");
        ds.setPassword("12345678");
        ds.setInitialSize(5);
        ds.setMaxTotal(5);

        ServletContext sc = sce.getServletContext();
        sc.setAttribute("ds", ds);
    }
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        //close the datasource here
        try {
            ServletContext sc = sce.getServletContext();
            BasicDataSource ds = (BasicDataSource) sc.getAttribute("ds");
            ds.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
