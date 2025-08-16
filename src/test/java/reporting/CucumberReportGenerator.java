package reporting;

import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CucumberReportGenerator {

    public static void generateReport() {
        File reportOutputDirectory = new File("target");
        List<String> jsonFiles = new ArrayList<>();
        jsonFiles.add("target/cucumber-reports/cucumber.json");

        String buildNumber = System.getProperty("build.number", "1");
        String projectName = "REST Assured API Automation Framework";

        Configuration configuration = new Configuration(reportOutputDirectory, projectName);

        // Additional configurations
        configuration.setBuildNumber(buildNumber);
        configuration.addClassifications("Platform", "API");
        configuration.addClassifications("Environment", System.getProperty("environment", "qa"));
        configuration.addClassifications("Browser", "REST API");

        // Set additional metadata
        configuration.addClassifications("Java Version", System.getProperty("java.version"));
        configuration.addClassifications("OS", System.getProperty("os.name"));

        ReportBuilder reportBuilder = new ReportBuilder(jsonFiles, configuration);
        reportBuilder.generateReports();

        System.out.println("Cucumber HTML Report generated at: " + reportOutputDirectory.getAbsolutePath() + "/cucumber-html-reports");
    }

    public static void main(String[] args) {
        generateReport();
    }
}