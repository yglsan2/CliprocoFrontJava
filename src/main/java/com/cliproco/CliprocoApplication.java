package com.cliproco;

import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

import java.io.File;

/**
 * Classe principale pour démarrer l'application CliprocoJEE avec Tomcat Embedded
 */
public class CliprocoApplication {
    
    public static void main(String[] args) throws Exception {
        // Créer une instance de Tomcat
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);
        
        // Définir le répertoire temporaire
        String tempDir = System.getProperty("java.io.tmpdir");
        tomcat.setBaseDir(tempDir);
        
        // Créer le contexte web
        StandardContext ctx = (StandardContext) tomcat.addWebapp("", 
            new File("src/main/webapp").getAbsolutePath());
        
        // Configurer les ressources
        WebResourceRoot resources = new StandardRoot(ctx);
        resources.addPreResources(
            new DirResourceSet(resources, "/WEB-INF/classes", 
                new File("target/classes").getAbsolutePath(), "/"));
        ctx.setResources(resources);
        
        // Démarrer le serveur
        tomcat.start();
        System.out.println("Application CliprocoJEE démarrée sur http://localhost:8080");
        
        // Attendre que le serveur s'arrête
        tomcat.getServer().await();
    }
} 