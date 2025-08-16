@allTest
Feature: User Management API Testing

  Background:
    Given I have a valid API request

  @get @smoke @test1
  Scenario: Get list of users with query parameters
    When I send a GET request to "/users" with query parameter "page" as "2"
    Then the response status should be 200
    And the response should contain 6 users per page
    And the response should match the user list schema
    And I should be able to deserialize the response to UserListResponse model
    And the response time should be less than 3000 milliseconds

  @get @regression @test2
  Scenario: Get single user with path parameters
    When I send a GET request to "/users/{id}" with path parameter "id" as "2"
    Then the response status should be 200
    And the response should contain user data at index 0 with first name "Janet"

  @post @smoke @test3
  Scenario: Create a new user
    When I send a POST request to "/users" with user data name "John Doe" and job "QE Engineer"
    Then the response status should be 201
    And the response should contain user with name "John Doe"
    And the response should match the user creation schema

  @put @regression @test4
  Scenario: Update user using context data
    When I send a POST request to "/users" with user data name "Jane Smith" and job "Developer"
    Then the response status should be 201
    When I send a PUT request to "/users/{id}" with path parameter "id" from context and user data name "Jane Updated" and job "Senior Developer"
    Then the response status should be 200
    And the response should contain user with name "Jane Updated"

  @delete @regression @test5
  Scenario: Delete user using context data
    When I send a POST request to "/users" with user data name "Test User" and job "Tester"
    Then the response status should be 201
    When I send a DELETE request to "/users/{id}" with path parameter "id" from context
    Then the response status should be 204

  @e2e @smoke @test6
  Scenario: End to end user lifecycle
    When I send a POST request to "/users" with user data name "E2E User" and job "Automation Engineer"
    Then the response status should be 201
    And the response should contain user with name "E2E User"
    When I send a PUT request to "/users/{id}" with path parameter "id" from context and user data name "E2E Updated User" and job "Senior Automation Engineer"
    Then the response status should be 200
    And the response should contain user with name "E2E Updated User"
    When I send a DELETE request to "/users/{id}" with path parameter "id" from context
    Then the response status should be 204