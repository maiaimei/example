package cn.maiaimei.example.constant;

public class BaseConstant {
    /**
     * The HTTP header to look for the token when execution per request.
     */
    public static final String X_AUTH_TOKEN_HEADER = "X-Auth-Token";

    /**
     * The HTTP parameter to look for the token when execution per request.
     */
    public static final String TOKEN_PARAMETER = "token";

    /**
     * user:username:uuid
     */
    public static final String USER_REDIS_KEY = "user:%s:%s";

    public static final String JWT_CLAIMS_UID = "uid";
    public static final String JWT_CLAIMS_USN = "usn";

    public static final long EXPIRATION = 3600L;
}
