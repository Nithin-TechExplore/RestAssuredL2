package stepDefinitions;

import context.TestContext;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

public class Hooks {

    @Before
    public void setUp(Scenario scenario) {
        TestContext.clearContext();
        System.out.println("Starting scenario: " + scenario.getName());

    }

    @After
    public void tearDown(Scenario scenario) {
        if (scenario.isFailed()) {
            System.out.println("Scenario failed: " + scenario.getName());
            // Add screenshot or additional logging here if needed
        } else {
            System.out.println("Scenario passed: " + scenario.getName());
        }
        TestContext.clearContext();
    }
}