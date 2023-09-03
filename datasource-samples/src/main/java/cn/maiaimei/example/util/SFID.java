package cn.maiaimei.example.util;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;

public class SFID {

  private static final Snowflake snowflake;

  static {
    snowflake = IdUtil.getSnowflake(1, 1);
  }

  private SFID() {
    throw new UnsupportedOperationException();
  }

  public static long nextId() {
    return snowflake.nextId();
  }
}
