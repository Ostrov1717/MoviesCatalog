package org.example.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DataBaseConnectionManager {
    private final String url;
    private final Properties properties;

    public DataBaseConnectionManager(String host, String dataBaseName, String username, String password){
//    this.url="jdbc:postgresql://127.0.0.1:5432/dvdrental";
        System.out.println(dataBaseName+"-"+host);
        this.url="jdbc:postgresql://"+host+"/"+dataBaseName;
        this.properties=new Properties();
        this.properties.setProperty("user",username);
        this.properties.setProperty("password",password);
    }

    public Connection getConnection() throws SQLException{
        return DriverManager.getConnection(this.url,this.properties);
    }
}
