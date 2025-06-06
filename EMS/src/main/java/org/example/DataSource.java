package org.example;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class DataSource implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        //initialized the database here
    }
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        //close the datasource here
    }
}
