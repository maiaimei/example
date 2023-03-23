package cn.maiaimei.example;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
public class OgnlWrapperTest {

    @Test
    void test() {
        final Map<String, Object> response = getResponse();
        final OgnlWrapper ognlWrapper = new OgnlWrapper(response);
        final Integer code = ognlWrapper.getInt("code");
        final Map<String, Object> data = ognlWrapper.get("data");
        List<HashMap<String, Object>> users = ognlWrapper.get("data.users");
        assertEquals(200, code);
        assertTrue(data.containsKey("users"));
        assertEquals(2, users.size());
        log.info("before modify users");
        log.info("{}", users);
        log.info("after modify users");
        ognlWrapper.set("data.users", new HashMap<String, Object>() {{
            put("id", 3);
            put("username", "amy");
            put("password", "12345");
        }});
        List<HashMap<String, Object>> afterModifyUsers = ognlWrapper.get("data.users");
        log.info("{}", afterModifyUsers);
    }

    Map<String, Object> getResponse() {
        return new HashMap<String, Object>() {{
            put("code", 200);
            put("message", null);
            put("data", new HashMap<String, Object>() {{
                put("users", new ArrayList<HashMap<String, Object>>() {{
                    add(new HashMap<String, Object>() {{
                        put("id", 1);
                        put("username", "admin");
                        put("password", "12345");
                    }});
                    add(new HashMap<String, Object>() {{
                        put("id", 2);
                        put("username", "guest");
                        put("password", "67890");
                    }});
                }});
            }});
        }};
    }

}
