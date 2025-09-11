package org.example;

public class EmailNotificationConstants {

  public static final String EMAIL_TEMPLATE_PATH_FORMAT = "classpath:template/email/%s/%s";

  // 带链接的模板常量
  public static final String OPERATOR_EMAIL_TEMPLATE = "operator.btl";
  public static final String OWN_PARTY_EMAIL_TEMPLATE = "own_party.btl";
  public static final String COUNTER_PARTY_EMAIL_TEMPLATE = "counter_party.btl";
  public static final String ALL_PARTY_EMAIL_TEMPLATE = "all_party.btl";

  // 不带链接的模板常量
  public static final String OPERATOR_NO_LINKS_EMAIL_TEMPLATE = "operator_no_links.btl";
  public static final String OWN_PARTY_NO_LINKS_EMAIL_TEMPLATE = "own_party_no_links.btl";
  public static final String COUNTER_PARTY_NO_LINKS_EMAIL_TEMPLATE = "counter_party_no_links.btl";
  public static final String ALL_PARTY_NO_LINKS_EMAIL_TEMPLATE = "all_party_no_links.btl";

  // 默认模板
  public static final String DEFAULT_EMAIL_TEMPLATE = "default.btl";
  public static final String NO_LINKS_IN_EMAIL_TEMPLATE = "no_links.btl";
}
