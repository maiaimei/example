package org.example;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

public class SpringFactoriesAnalyzer {
    
    public void analyzeSpringFactories() throws IOException {
        Map<String, List<String>> factoriesByJar = new TreeMap<>();
        
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> resources = classLoader.getResources("META-INF/spring.factories");
        
        while (resources.hasMoreElements()) {
            URL url = resources.nextElement();
            String jarName = extractJarName(url);
            
            Properties properties = new Properties();
            try (InputStream inputStream = url.openStream()) {
                properties.load(inputStream);
                
                List<String> factories = new ArrayList<>();
                properties.forEach((key, value) -> {
                    factories.add(String.format("%s = %s", key, value));
                });
                
                factoriesByJar.put(jarName, factories);
            }
        }
        
        // Print analysis results
        printAnalysis(factoriesByJar);
    }
    
    private String extractJarName(URL url) {
        String urlString = url.toString();
        if (urlString.startsWith("jar:file:")) {
            // Extract JAR name from URL
            String jarPath = urlString.substring(9, urlString.indexOf("!/META-INF"));
            return new File(jarPath).getName();
        }
        return "unknown";
    }
    
    private void printAnalysis(Map<String, List<String>> factoriesByJar) {
        System.out.println("=== Spring Factories Analysis ===\n");
        
        factoriesByJar.forEach((jar, factories) -> {
            System.out.println("JAR: " + jar);
            System.out.println("Number of factory definitions: " + factories.size());
            System.out.println("Factories:");
            factories.forEach(factory -> System.out.println("  " + factory));
            System.out.println();
        });
        
        System.out.println("Total number of JARs with spring.factories: " + factoriesByJar.size());
    }

    public static void main(String[] args) throws IOException {
        SpringFactoriesAnalyzer analyzer = new SpringFactoriesAnalyzer();
        analyzer.analyzeSpringFactories();
    }

}
