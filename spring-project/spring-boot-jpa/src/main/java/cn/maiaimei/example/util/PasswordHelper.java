package cn.maiaimei.example.util;

import java.util.UUID;

public class PasswordHelper {
    public static String getRandomPassword() {
        String uuid = UUID.randomUUID().toString();
        String[] uuids = uuid.split("-");
        return uuids[uuids.length - 1];
    }

    private PasswordHelper() {
        throw new UnsupportedOperationException();
    }
}
