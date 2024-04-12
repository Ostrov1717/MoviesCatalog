package org.example.listener;

import org.example.db.DataBaseConnectionManager;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.Connection;
import java.sql.SQLException;

@WebListener
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context= sce.getServletContext();
        String username=context.getInitParameter("username");
        String password=context.getInitParameter("password");
        String dbhost=context.getInitParameter("dbhost");
        String dbName=context.getInitParameter("dbname");

        try {
            DataBaseConnectionManager dbManager=new DataBaseConnectionManager(dbhost,dbName,username,password);
            Connection connection=dbManager.getConnection();
            context.setAttribute("dbconnection",connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            ((Connection)sce.getServletContext().getAttribute("dbconnection")).close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
