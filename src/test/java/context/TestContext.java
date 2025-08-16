package context;

import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;

public class TestContext {
    private static final ThreadLocal<Map<String, Object>> context = ThreadLocal.withInitial(HashMap::new);

    public static void setContext(String key, Object value) {
        context.get().put(key, value);
    }

    public static Object getContext(String key) {
        return context.get().get(key);
    }

    public static String getContextAsString(String key) {
        Object value = getContext(key);
        return value != null ? value.toString() : null;
    }

    public static Integer getContextAsInteger(String key) {
        Object value = getContext(key);
        return value != null ? Integer.parseInt(value.toString()) : null;
    }

    public static Response getResponse() {
        return (Response) getContext("response");
    }

    public static void setResponse(Response response) {
        setContext("response", response);
    }

    public static void clearContext() {
        context.get().clear();
    }
}
