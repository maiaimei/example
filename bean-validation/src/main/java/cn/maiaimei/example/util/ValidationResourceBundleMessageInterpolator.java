package cn.maiaimei.example.util;

import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.hibernate.validator.spi.resourceloading.ResourceBundleLocator;

import java.nio.charset.StandardCharsets;

/**
 * java中properties配置文件默认的编码为：ISO-8859-1，是不支持中文的，所以会乱码，需要做转码
 */
public class ValidationResourceBundleMessageInterpolator extends ResourceBundleMessageInterpolator {
    public ValidationResourceBundleMessageInterpolator(ResourceBundleLocator userResourceBundleLocator) {
        super(userResourceBundleLocator);
    }

    @Override
    public String interpolate(String message, Context context) {
        String result = super.interpolate(message, context);
        try {
            return new String(result.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        } catch (Exception e) {
            return result;
        }
    }
}
