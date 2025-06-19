#!/bin/bash

echo "🔧 Correction du projet CliprocoJEE avec SED..."

# 1. Suppression des imports CDI et Jakarta Faces du FrontController
echo "📝 Correction du FrontController..."
sed -i '/import javax.enterprise.context./d' src/main/java/routers/FrontController.java
sed -i '/import javax.inject./d' src/main/java/routers/FrontController.java
sed -i '/import jakarta.faces./d' src/main/java/routers/FrontController.java
sed -i '/import jakarta.enterprise.context./d' src/main/java/routers/FrontController.java
sed -i '/import jakarta.inject./d' src/main/java/routers/FrontController.java

# 2. Remplacement du FrontController par une version simple
cat > src/main/java/routers/FrontController.java << 'EOF'
package routers;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/")
public class FrontController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String cmd = request.getParameter("cmd");
        
        if (cmd == null || cmd.isEmpty()) {
            cmd = "index";
        }
        
        switch (cmd) {
            case "index":
                request.getRequestDispatcher("/WEB-INF/jsp/index.jsp").forward(request, response);
                break;
            case "connexion":
                request.getRequestDispatcher("/WEB-INF/jsp/connexion.jsp").forward(request, response);
                break;
            case "clients":
                request.getRequestDispatcher("/WEB-INF/jsp/clients.jsp").forward(request, response);
                break;
            case "prospects":
                request.getRequestDispatcher("/WEB-INF/jsp/prospects.jsp").forward(request, response);
                break;
            case "contact":
                request.getRequestDispatcher("/WEB-INF/jsp/contact.jsp").forward(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                break;
        }
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
}
EOF

# 3. Suppression des imports CDI des contrôleurs
echo "📝 Nettoyage des contrôleurs..."
find src/main/java/controllers -name "*.java" -exec sed -i '/import javax.enterprise.context./d' {} \;
find src/main/java/controllers -name "*.java" -exec sed -i '/import javax.inject./d' {} \;
find src/main/java/controllers -name "*.java" -exec sed -i '/import jakarta.faces./d' {} \;
find src/main/java/controllers -name "*.java" -exec sed -i '/import jakarta.enterprise.context./d' {} \;
find src/main/java/controllers -name "*.java" -exec sed -i '/import jakarta.inject./d' {} \;

# 4. Suppression des annotations CDI des contrôleurs
find src/main/java/controllers -name "*.java" -exec sed -i '/@Named/d' {} \;
find src/main/java/controllers -name "*.java" -exec sed -i '/@RequestScoped/d' {} \;
find src/main/java/controllers -name "*.java" -exec sed -i '/@SessionScoped/d' {} \;
find src/main/java/controllers -name "*.java" -exec sed -i '/@ApplicationScoped/d' {} \;
find src/main/java/controllers -name "*.java" -exec sed -i '/@Inject/d' {} \;

# 5. Suppression des imports CDI des services
echo "📝 Nettoyage des services..."
find src/main/java/services -name "*.java" -exec sed -i '/import javax.enterprise.context./d' {} \;
find src/main/java/services -name "*.java" -exec sed -i '/import javax.inject./d' {} \;
find src/main/java/services -name "*.java" -exec sed -i '/import jakarta.faces./d' {} \;
find src/main/java/services -name "*.java" -exec sed -i '/import jakarta.enterprise.context./d' {} \;
find src/main/java/services -name "*.java" -exec sed -i '/import jakarta.inject./d' {} \;

# 6. Suppression des annotations CDI des services
find src/main/java/services -name "*.java" -exec sed -i '/@Named/d' {} \;
find src/main/java/services -name "*.java" -exec sed -i '/@RequestScoped/d' {} \;
find src/main/java/services -name "*.java" -exec sed -i '/@SessionScoped/d' {} \;
find src/main/java/services -name "*.java" -exec sed -i '/@ApplicationScoped/d' {} \;
find src/main/java/services -name "*.java" -exec sed -i '/@Inject/d' {} \;

# 7. Suppression des imports CDI des DAO
echo "📝 Nettoyage des DAO..."
find src/main/java/dao -name "*.java" -exec sed -i '/import javax.enterprise.context./d' {} \;
find src/main/java/dao -name "*.java" -exec sed -i '/import javax.inject./d' {} \;
find src/main/java/dao -name "*.java" -exec sed -i '/import jakarta.faces./d' {} \;
find src/main/java/dao -name "*.java" -exec sed -i '/import jakarta.enterprise.context./d' {} \;
find src/main/java/dao -name "*.java" -exec sed -i '/import jakarta.inject./d' {} \;

# 8. Suppression des annotations CDI des DAO
find src/main/java/dao -name "*.java" -exec sed -i '/@Named/d' {} \;
find src/main/java/dao -name "*.java" -exec sed -i '/@RequestScoped/d' {} \;
find src/main/java/dao -name "*.java" -exec sed -i '/@SessionScoped/d' {} \;
find src/main/java/dao -name "*.java" -exec sed -i '/@ApplicationScoped/d' {} \;
find src/main/java/dao -name "*.java" -exec sed -i '/@Inject/d' {} \;

# 9. Suppression des imports CDI des modèles
echo "📝 Nettoyage des modèles..."
find src/main/java/models -name "*.java" -exec sed -i '/import javax.enterprise.context./d' {} \;
find src/main/java/models -name "*.java" -exec sed -i '/import javax.inject./d' {} \;
find src/main/java/models -name "*.java" -exec sed -i '/import jakarta.faces./d' {} \;
find src/main/java/models -name "*.java" -exec sed -i '/import jakarta.enterprise.context./d' {} \;
find src/main/java/models -name "*.java" -exec sed -i '/import jakarta.inject./d' {} \;

# 10. Suppression des annotations CDI des modèles
find src/main/java/models -name "*.java" -exec sed -i '/@Named/d' {} \;
find src/main/java/models -name "*.java" -exec sed -i '/@RequestScoped/d' {} \;
find src/main/java/models -name "*.java" -exec sed -i '/@SessionScoped/d' {} \;
find src/main/java/models -name "*.java" -exec sed -i '/@ApplicationScoped/d' {} \;
find src/main/java/models -name "*.java" -exec sed -i '/@Inject/d' {} \;

# 11. Suppression des imports CDI des utilitaires
echo "📝 Nettoyage des utilitaires..."
find src/main/java/utilities -name "*.java" -exec sed -i '/import javax.enterprise.context./d' {} \;
find src/main/java/utilities -name "*.java" -exec sed -i '/import javax.inject./d' {} \;
find src/main/java/utilities -name "*.java" -exec sed -i '/import jakarta.faces./d' {} \;
find src/main/java/utilities -name "*.java" -exec sed -i '/import jakarta.enterprise.context./d' {} \;
find src/main/java/utilities -name "*.java" -exec sed -i '/import jakarta.inject./d' {} \;

# 12. Suppression des annotations CDI des utilitaires
find src/main/java/utilities -name "*.java" -exec sed -i '/@Named/d' {} \;
find src/main/java/utilities -name "*.java" -exec sed -i '/@RequestScoped/d' {} \;
find src/main/java/utilities -name "*.java" -exec sed -i '/@SessionScoped/d' {} \;
find src/main/java/utilities -name "*.java" -exec sed -i '/@ApplicationScoped/d' {} \;
find src/main/java/utilities -name "*.java" -exec sed -i '/@Inject/d' {} \;

# 13. Suppression des imports CDI des builders
echo "📝 Nettoyage des builders..."
find src/main/java/builders -name "*.java" -exec sed -i '/import javax.enterprise.context./d' {} \;
find src/main/java/builders -name "*.java" -exec sed -i '/import javax.inject./d' {} \;
find src/main/java/builders -name "*.java" -exec sed -i '/import jakarta.faces./d' {} \;
find src/main/java/builders -name "*.java" -exec sed -i '/import jakarta.enterprise.context./d' {} \;
find src/main/java/builders -name "*.java" -exec sed -i '/import jakarta.inject./d' {} \;

# 14. Suppression des annotations CDI des builders
find src/main/java/builders -name "*.java" -exec sed -i '/@Named/d' {} \;
find src/main/java/builders -name "*.java" -exec sed -i '/@RequestScoped/d' {} \;
find src/main/java/builders -name "*.java" -exec sed -i '/@SessionScoped/d' {} \;
find src/main/java/builders -name "*.java" -exec sed -i '/@ApplicationScoped/d' {} \;
find src/main/java/builders -name "*.java" -exec sed -i '/@Inject/d' {} \;

# 15. Suppression des imports CDI des exceptions
echo "📝 Nettoyage des exceptions..."
find src/main/java/exceptions -name "*.java" -exec sed -i '/import javax.enterprise.context./d' {} \;
find src/main/java/exceptions -name "*.java" -exec sed -i '/import javax.inject./d' {} \;
find src/main/java/exceptions -name "*.java" -exec sed -i '/import jakarta.faces./d' {} \;
find src/main/java/exceptions -name "*.java" -exec sed -i '/import jakarta.enterprise.context./d' {} \;
find src/main/java/exceptions -name "*.java" -exec sed -i '/import jakarta.inject./d' {} \;

# 16. Suppression des annotations CDI des exceptions
find src/main/java/exceptions -name "*.java" -exec sed -i '/@Named/d' {} \;
find src/main/java/exceptions -name "*.java" -exec sed -i '/@RequestScoped/d' {} \;
find src/main/java/exceptions -name "*.java" -exec sed -i '/@SessionScoped/d' {} \;
find src/main/java/exceptions -name "*.java" -exec sed -i '/@ApplicationScoped/d' {} \;
find src/main/java/exceptions -name "*.java" -exec sed -i '/@Inject/d' {} \;

# 17. Suppression des imports CDI des logs
echo "📝 Nettoyage des logs..."
find src/main/java/logs -name "*.java" -exec sed -i '/import javax.enterprise.context./d' {} \;
find src/main/java/logs -name "*.java" -exec sed -i '/import javax.inject./d' {} \;
find src/main/java/logs -name "*.java" -exec sed -i '/import jakarta.faces./d' {} \;
find src/main/java/logs -name "*.java" -exec sed -i '/import jakarta.enterprise.context./d' {} \;
find src/main/java/logs -name "*.java" -exec sed -i '/import jakarta.inject./d' {} \;

# 18. Suppression des annotations CDI des logs
find src/main/java/logs -name "*.java" -exec sed -i '/@Named/d' {} \;
find src/main/java/logs -name "*.java" -exec sed -i '/@RequestScoped/d' {} \;
find src/main/java/logs -name "*.java" -exec sed -i '/@SessionScoped/d' {} \;
find src/main/java/logs -name "*.java" -exec sed -i '/@ApplicationScoped/d' {} \;
find src/main/java/logs -name "*.java" -exec sed -i '/@Inject/d' {} \;

# 19. Suppression des imports CDI des filters
echo "📝 Nettoyage des filters..."
find src/main/java/filters -name "*.java" -exec sed -i '/import javax.enterprise.context./d' {} \;
find src/main/java/filters -name "*.java" -exec sed -i '/import javax.inject./d' {} \;
find src/main/java/filters -name "*.java" -exec sed -i '/import jakarta.faces./d' {} \;
find src/main/java/filters -name "*.java" -exec sed -i '/import jakarta.enterprise.context./d' {} \;
find src/main/java/filters -name "*.java" -exec sed -i '/import jakarta.inject./d' {} \;

# 20. Suppression des annotations CDI des filters
find src/main/java/filters -name "*.java" -exec sed -i '/@Named/d' {} \;
find src/main/java/filters -name "*.java" -exec sed -i '/@RequestScoped/d' {} \;
find src/main/java/filters -name "*.java" -exec sed -i '/@SessionScoped/d' {} \;
find src/main/java/filters -name "*.java" -exec sed -i '/@ApplicationScoped/d' {} \;
find src/main/java/filters -name "*.java" -exec sed -i '/@Inject/d' {} \;

# 21. Nettoyage des lignes vides multiples
echo "🧹 Nettoyage des lignes vides..."
find src/main/java -name "*.java" -exec sed -i '/^$/N;/^\n$/D' {} \;

# 22. Suppression des fichiers CDI
echo "🗑️ Suppression des fichiers CDI..."
rm -f src/main/webapp/WEB-INF/beans.xml

# 23. Nettoyage du web.xml
echo "📝 Nettoyage du web.xml..."
sed -i '/<context-param>/,/<\/context-param>/d' src/main/webapp/WEB-INF/web.xml
sed -i '/<listener>/,/<\/listener>/d' src/main/webapp/WEB-INF/web.xml
sed -i '/<servlet-mapping>/,/<\/servlet-mapping>/d' src/main/webapp/WEB-INF/web.xml

# 24. Remplacement du web.xml par une version simple
cat > src/main/webapp/WEB-INF/web.xml << 'EOF'
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee 
         http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd"
         version="4.0">
    
    <display-name>CliprocoJEE</display-name>
    
    <welcome-file-list>
        <welcome-file>index.jsp</welcome-file>
    </welcome-file-list>
    
    <error-page>
        <error-code>404</error-code>
        <location>/WEB-INF/jsp/error.jsp</location>
    </error-page>
    
    <error-page>
        <error-code>500</error-code>
        <location>/WEB-INF/jsp/error.jsp</location>
    </error-page>
    
</web-app>
EOF

# 25. Nettoyage du pom.xml - suppression des dépendances CDI et Jakarta Faces
echo "📝 Nettoyage du pom.xml..."
sed -i '/<dependency>/,/<\/dependency>/d' pom.xml
sed -i '/<groupId>jakarta.enterprise<\/groupId>/,/<\/dependency>/d' pom.xml
sed -i '/<groupId>jakarta.faces<\/groupId>/,/<\/dependency>/d' pom.xml
sed -i '/<groupId>jakarta.inject<\/groupId>/,/<\/dependency>/d' pom.xml
sed -i '/<groupId>javax.enterprise<\/groupId>/,/<\/dependency>/d' pom.xml
sed -i '/<groupId>javax.inject<\/groupId>/,/<\/dependency>/d' pom.xml

# 26. Remplacement du pom.xml par une version simple
cat > pom.xml << 'EOF'
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    
    <groupId>com.cliproco</groupId>
    <artifactId>CliprocoJEE</artifactId>
    <version>1.0-SNAPSHOT</version>
    <packaging>war</packaging>
    
    <name>CliprocoJEE</name>
    <description>Application de gestion de clients et prospects</description>
    
    <properties>
        <maven.compiler.source>21</maven.compiler.source>
        <maven.compiler.target>21</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>
    
    <dependencies>
        <!-- Servlet API -->
        <dependency>
            <groupId>javax.servlet</groupId>
            <artifactId>javax.servlet-api</artifactId>
            <version>4.0.1</version>
            <scope>provided</scope>
        </dependency>
        
        <!-- JSP API -->
        <dependency>
            <groupId>javax.servlet.jsp</groupId>
            <artifactId>javax.servlet.jsp-api</artifactId>
            <version>2.3.3</version>
            <scope>provided</scope>
        </dependency>
        
        <!-- JSTL -->
        <dependency>
            <groupId>javax.servlet</groupId>
            <artifactId>jstl</artifactId>
            <version>1.2</version>
        </dependency>
        
        <!-- Hibernate Core -->
        <dependency>
            <groupId>org.hibernate.orm</groupId>
            <artifactId>hibernate-core</artifactId>
            <version>6.4.4.Final</version>
        </dependency>
        
        <!-- MySQL Connector -->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>8.0.33</version>
        </dependency>
        
        <!-- SLF4J API -->
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>2.0.9</version>
        </dependency>
        
        <!-- Logback Classic -->
        <dependency>
            <groupId>ch.qos.logback</groupId>
            <artifactId>logback-classic</artifactId>
            <version>1.4.11</version>
        </dependency>
        
        <!-- JUnit 5 pour les tests -->
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
            <version>5.10.0</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
    
    <build>
        <finalName>CliprocoJEE</finalName>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.11.0</version>
                <configuration>
                    <source>21</source>
                    <target>21</target>
                </configuration>
            </plugin>
            
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-war-plugin</artifactId>
                <version>3.4.0</version>
                <configuration>
                    <failOnMissingWebXml>false</failOnMissingWebXml>
                </configuration>
            </plugin>
            
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>3.1.2</version>
                <configuration>
                    <skipTests>true</skipTests>
                </configuration>
            </plugin>
            
            <plugin>
                <groupId>org.codehaus.cargo</groupId>
                <artifactId>cargo-maven3-plugin</artifactId>
                <version>1.10.11</version>
                <configuration>
                    <container>
                        <containerId>tomcat11x</containerId>
                        <artifactInstaller>
                            <groupId>org.apache.tomcat</groupId>
                            <artifactId>tomcat</artifactId>
                            <version>11.0.0-M11</version>
                        </artifactInstaller>
                    </container>
                    <configuration>
                        <type>standalone</type>
                        <properties>
                            <cargo.servlet.port>8080</cargo.servlet.port>
                            <cargo.tomcat.ajp.port>8009</cargo.tomcat.ajp.port>
                        </properties>
                    </configuration>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
EOF

# 27. Nettoyage du target
echo "🧹 Nettoyage du dossier target..."
rm -rf target/

# 28. Compilation du projet
echo "🔨 Compilation du projet..."
mvn clean compile

echo "✅ Corrections terminées !"
echo "🚀 Le projet est maintenant compatible avec Tomcat 11"
echo "📦 Vous pouvez maintenant déployer avec: mvn cargo:run" 