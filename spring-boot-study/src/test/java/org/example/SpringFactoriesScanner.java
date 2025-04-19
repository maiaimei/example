package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.jar.JarFile;
import org.springframework.boot.BootstrapRegistryInitializer;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.SpringFactoriesLoader;

public class SpringFactoriesScanner {

    // Method 1: Using Spring's PathMatchingResourcePatternResolver
    public void findSpringFactoriesUsingSpring() throws IOException {
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("classpath*:META-INF/spring.factories");
        
        for (Resource resource : resources) {
            System.out.println("Found spring.factories in: " + resource.getURL());
            
            // Read the content of spring.factories
            Properties properties = new Properties();
            try (InputStream inputStream = resource.getInputStream()) {
                properties.load(inputStream);
                properties.forEach((key, value) -> {
                    System.out.printf("%s=%s\n",key,value);
                });
            }
            System.out.println();
        }
    }

    // Method 2: Using ClassLoader
    public void findSpringFactoriesUsingClassLoader() throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> resources = classLoader.getResources("META-INF/spring.factories");
        
        while (resources.hasMoreElements()) {
            URL url = resources.nextElement();
            System.out.println("Found spring.factories in: " + url);
            
            // Read the content
            Properties properties = new Properties();
            try (InputStream inputStream = url.openStream()) {
                properties.load(inputStream);
                properties.forEach((key, value) -> {
                    System.out.printf("%s=%s\n",key,value);
                });
            }
            System.out.println();
        }
    }

    // Method 3: Using Spring's SpringFactoriesLoader (internal implementation)
    public void printLoadedFactories() {
        // Get all BootstrapRegistryInitializer implementations as an example
        List<BootstrapRegistryInitializer> bootstrapRegistryInitializers = SpringFactoriesLoader
            .loadFactories(BootstrapRegistryInitializer.class, getClass().getClassLoader());
        System.out.println("Found BootstrapRegistryInitializers:");
        bootstrapRegistryInitializers.forEach(listener ->
            System.out.println(listener.getClass().getName()));
        System.out.println("Total number of BootstrapRegistryInitializers: " + bootstrapRegistryInitializers.size());
        
        // Get all ApplicationContextInitializer implementations
        List<ApplicationContextInitializer> applicationContextInitializers = SpringFactoriesLoader
            .loadFactories(ApplicationContextInitializer.class, getClass().getClassLoader());
        System.out.println("\nFound ApplicationContextInitializers:");
        applicationContextInitializers.forEach(initializer ->
            System.out.println(initializer.getClass().getName()));
        System.out.println("Total number of ApplicationContextInitializers: " + applicationContextInitializers.size());

        // Get all ApplicationListener implementations as an example
        List<ApplicationListener> applicationListeners = SpringFactoriesLoader
            .loadFactories(ApplicationListener.class, getClass().getClassLoader());
        System.out.println("\nFound ApplicationListeners:");
        applicationListeners.forEach(listener ->
            System.out.println(listener.getClass().getName()));
        System.out.println("Total number of ApplicationListeners: " + applicationListeners.size());
    }

    // Utility method to print JAR file information
    private void printJarInfo(URL url) throws IOException {
        if (url.getProtocol().equals("jar")) {
            String jarPath = url.getPath().substring(5, url.getPath().indexOf("!"));
            JarFile jarFile = new JarFile(URLDecoder.decode(jarPath, StandardCharsets.UTF_8));
            System.out.println("JAR file: " + jarFile.getName());
        }
    }

    // Main method to demonstrate usage
    public static void main(String[] args) throws IOException {
        SpringFactoriesScanner scanner = new SpringFactoriesScanner();
        
        System.out.println("=== Using Spring's PathMatchingResourcePatternResolver ===");
        scanner.findSpringFactoriesUsingSpring();
        
        System.out.println("\n=== Using ClassLoader ===");
        scanner.findSpringFactoriesUsingClassLoader();
        
        System.out.println("\n=== Using SpringFactoriesLoader ===");
        scanner.printLoadedFactories();
    }
}
