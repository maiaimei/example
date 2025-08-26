package cn.maiaimei.exception;

/**
 * 自定义JSON转换异常
 */
public class JsonConvertException extends RuntimeException {

  public JsonConvertException(String message, Throwable cause) {
    super(message, cause);
  }
}