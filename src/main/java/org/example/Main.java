package org.example;

import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import java.io.File;


public class Main {
    public static void main(String[] args) throws Exception {
        Tomcat tomcat=new Tomcat();
        String webport="8080";
        tomcat.setPort(Integer.parseInt(webport));
        String webdir="src/main/webapp/";
        StandardContext context=(StandardContext) tomcat.addWebapp("",new File(webdir).getAbsolutePath());
        File addWebInfClasses =new File("target/classes");
        WebResourceRoot resources=new StandardRoot(context);
        resources.addPreResources(new DirResourceSet(resources,"/WEB-INF/classes",addWebInfClasses.getAbsolutePath(),"/"));
        context.setResources(resources);
        tomcat.start();
        tomcat.getServer().await();
    }
}
