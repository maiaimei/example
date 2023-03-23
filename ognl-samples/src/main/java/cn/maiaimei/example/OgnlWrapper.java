package cn.maiaimei.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import ognl.Ognl;
import ognl.OgnlException;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

@SuppressWarnings("unchecked")
public class OgnlWrapper {

    private static final Logger logger = LoggerFactory.getLogger(OgnlWrapper.class);

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final Map<String, Object> payload;

    public OgnlWrapper(Map<String, Object> payload) {
        Validate.notEmpty(payload, "can not construct with none payload");
        this.payload = payload;
    }

    public OgnlWrapper(Object payload) {
        this.payload = OBJECT_MAPPER.convertValue(payload, Map.class);
    }

    public <T> T get(String expression) {
        try {
            return (T) Ognl.getValue(expression, this.payload);
        } catch (OgnlException e) {
            logger.error("get value with expression:[{}] due to error, return null instead of", expression, e);
            return null;
        }
    }

    public <T> T get(String expression, Class<T> clazz) {
        try {
            if (Long.class.isAssignableFrom(clazz)) {
                return (T) getLong(expression);
            }
            if (Integer.class.isAssignableFrom(clazz)) {
                return (T) getInt(expression);
            }
            return (T) Ognl.getValue(expression, this.payload);
        } catch (OgnlException e) {
            logger.error("get value with expression:[{}] due to error, return null instead of", expression, e);
            return null;
        }
    }

    public Long getLong(String expression) {
        try {
            Object obj = Ognl.getValue(expression, this.payload);
            if (null == obj) {
                return null;
            }
            try {
                return Long.parseLong(obj.toString());
            } catch (NumberFormatException nfe) {
                logger.error("get value with expression:[{}] due to error, return null. value[{}] cannot be cast to java.lang.Long", expression, obj);
                return null;
            }
        } catch (OgnlException e) {
            logger.error("get value with expression:[{}] due to error, return null instead of", expression, e);
            return null;
        }
    }

    public Integer getInt(String expression) {
        try {
            Object obj = Ognl.getValue(expression, this.payload);
            if (null == obj) {
                return null;
            }
            try {
                return Integer.parseInt(obj.toString());
            } catch (NumberFormatException nfe) {
                logger.error("get value with expression:[{}] due to error, return null. value[{}] cannot be cast to java.lang.Integer", expression, obj);
                return null;
            }
        } catch (OgnlException e) {
            logger.error("get value with expression:[{}] due to error, return null instead of", expression, e);
            return null;
        }
    }

    // TODO: set
    public void set(String expression, Object value) {
        try {
            Ognl.setValue(expression, payload, value);
        } catch (OgnlException e) {
            logger.error("set value with expression:[{}] due to error", expression, e);
        }
    }

}
