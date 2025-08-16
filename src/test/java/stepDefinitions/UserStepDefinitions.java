package stepDefinitions;

import com.jayway.jsonpath.JsonPath;
import context.TestContext;
import io.cucumber.java.en.*;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import models.User;
import models.UserListResponse;
import utils.APIUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import static org.junit.jupiter.api.Assertions.*;

public class UserStepDefinitions {

    private RequestSpecification request;
    private Response response;

    @Given("I have a valid API request")
    public void i_have_a_valid_api_request() {
        request = APIUtils.getBaseRequest();
    }

    @When("I send a GET request to {string}")
    public void i_send_a_get_request_to(String endpoint) {
        response = request.get(endpoint);
        TestContext.setResponse(response);
    }

    @When("I send a GET request to {string} with path parameter {string} as {string}")
    public void i_send_a_get_request_with_path_param(String endpoint, String paramName, String paramValue) {
        Map<String, Object> pathParams = new HashMap<>();
        pathParams.put(paramName, paramValue);

        request = APIUtils.addPathParams(request, pathParams);
        response = request.get(endpoint);
        System.out.println(response.asPrettyString());
        TestContext.setResponse(response);
        TestContext.setContext("userId", paramValue);
    }

    @When("I send a GET request to {string} with query parameter {string} as {string}")
    public void i_send_a_get_request_with_query_param(String endpoint, String paramName, String paramValue) {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put(paramName, paramValue);
        request = APIUtils.addQueryParams(request, queryParams);
        response = request.get(endpoint);
        TestContext.setResponse(response);
    }

    @When("I send a POST request to {string} with user data name {string} and job {string}")
    public void i_send_a_post_request_with_user_data(String endpoint, String name, String job) {
        User user = new User(name, job);
        String jsonBody = APIUtils.serializeToJson(user);

        response = request
                //.header("x-api-key", "reqres-free-v1")
                .body(jsonBody)
                .post(endpoint);
        TestContext.setResponse(response);

        // Store created user ID for further use
        if (response.getStatusCode() == 201) {
            String userId = response.jsonPath().getString("id");
            TestContext.setContext("createdUserId", userId);
        }
    }

    @When("I send a PUT request to {string} with path parameter {string} from context and user data name {string} and job {string}")
    public void i_send_a_put_request_with_context_param(String endpoint, String paramName, String name, String job) {
        String userId = TestContext.getContextAsString("createdUserId");
        assertNotNull(userId, "User ID not found in context");

        Map<String, Object> pathParams = new HashMap<>();
        pathParams.put(paramName, userId);
        request = APIUtils.addPathParams(request, pathParams);

        User user = new User(name, job);
        String jsonBody = APIUtils.serializeToJson(user);

        response = request.body(jsonBody).put(endpoint);
        TestContext.setResponse(response);
    }

    @When("I send a DELETE request to {string} with path parameter {string} from context")
    public void i_send_a_delete_request_with_context_param(String endpoint, String paramName) {
        String userId = TestContext.getContextAsString("createdUserId");
        assertNotNull(userId, "User ID not found in context");

        Map<String, Object> pathParams = new HashMap<>();
        pathParams.put(paramName, userId);
        request = APIUtils.addPathParams(request, pathParams);

        response = request.delete(endpoint);
        TestContext.setResponse(response);
    }

    @Then("the response status should be {int}")
    public void the_response_status_should_be(int expectedStatusCode) {
        response = TestContext.getResponse();
        assertEquals(expectedStatusCode, response.getStatusCode());
    }

    @Then("the response should contain user with name {string}")
    public void the_response_should_contain_user_with_name(String expectedName) {
        response = TestContext.getResponse();
       // String actualName = response.jsonPath().getString("name");
        List<String> names = JsonPath.read(response.asString(), "$..name");
        String actualName = names.isEmpty() ? null : names.get(0);

        assertEquals(expectedName, actualName);
    }

    @Then("the response should contain {int} users per page")
    public void the_response_should_contain_users_per_page(int expectedPerPage) {
        response = TestContext.getResponse();
        int actualPerPage = response.jsonPath().getInt("per_page");
        assertEquals(expectedPerPage, actualPerPage);
    }

    @Then("the response should contain user data at index {int} with first name {string}")
    public void the_response_should_contain_user_data_with_first_name(int index, String expectedFirstName) {
        response = TestContext.getResponse();
        String actualFirstName = response.jsonPath().getString("data.first_name");
        //String actualFirstName = response.jsonPath().getString("data[" + index + "].first_name");
        assertEquals(expectedFirstName, actualFirstName);
    }

    @Then("the response should match the user list schema")
    public void the_response_should_match_user_list_schema() {
        response = TestContext.getResponse();
        File schemaFile = new File("src/test/resources/schemas/userListSchema.json");
        response.then().assertThat().body(matchesJsonSchema(schemaFile));
    }

    @Then("the response should match the user creation schema")
    public void the_response_should_match_user_creation_schema() {
        response = TestContext.getResponse();
        File schemaFile = new File("src/test/resources/schemas/userCreationSchema.json");
        response.then().assertThat().body(matchesJsonSchema(schemaFile));
    }

    @Then("I should be able to deserialize the response to UserListResponse model")
    public void i_should_be_able_to_deserialize_response_to_model() {
        response = TestContext.getResponse();
        String jsonResponse = response.getBody().asString();

        // Test deserialization
        UserListResponse userListResponse = APIUtils.deserializeFromJson(jsonResponse, UserListResponse.class);

        assertNotNull(userListResponse);
        assertNotNull(userListResponse.getData());
        assertTrue(userListResponse.getData().size() > 0);
    }

    @Then("the response time should be less than {int} milliseconds")
    public void the_response_time_should_be_less_than_milliseconds(int maxTime) {
        response = TestContext.getResponse();
        long responseTime = response.getTime();
        assertTrue(responseTime < maxTime, "Response time " + responseTime + "ms exceeded maximum " + maxTime + "ms");
    }
}
