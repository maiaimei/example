package cn.maiaimei.example.util;

import cn.hutool.core.lang.Snowflake;

public class SFID {
    private static final Snowflake snowflake = new Snowflake();

    public static long randomSFID() {
        return snowflake.nextId();
    }

    private SFID() {
        throw new UnsupportedOperationException();
    }
}
