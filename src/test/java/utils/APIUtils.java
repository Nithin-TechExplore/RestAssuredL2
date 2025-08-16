package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import config.ConfigManager;

import java.util.HashMap;
import java.util.Map;

public class APIUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static RequestSpecification getBaseRequest() {

        Map<String,String> mp =new HashMap<>();

        mp.put("Content-Type", "application/json");
        mp.put("x-api-key", "reqres-free-v1");
        RequestSpecification request = RestAssured.given()
                .baseUri(ConfigManager.getBaseUrl())
                //.header("Content-Type", "application/json");
                .headers(mp);


        String bearerToken = ConfigManager.getBearerToken();
        if (!bearerToken.isEmpty()) {
            request.header("Authorization", "Bearer " + bearerToken);
        }

        return request;
    }

    public static RequestSpecification addPathParams(RequestSpecification request, Map<String, Object> pathParams) {
        if (pathParams != null && !pathParams.isEmpty()) {
            request.pathParams(pathParams);
        }
        return request;
    }

    public static RequestSpecification addQueryParams(RequestSpecification request, Map<String, Object> queryParams) {
        if (queryParams != null && !queryParams.isEmpty()) {
            request.queryParams(queryParams);
        }
        return request;
    }

    public static String serializeToJson(Object obj) {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize object to JSON", e);
        }
    }

    public static <T> T deserializeFromJson(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize JSON to object", e);
        }
    }
}