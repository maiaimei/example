package org.example;

import java.util.*;
import java.util.function.Supplier;

public class EmailTemplateNameGenerator {

  public static String getTemplateName(EmailNotificationConfig config, EmailNotificationRecipientType recipientType,
      String product, String locationInstitution, String partyType, String userCategory, boolean withoutLinks) {
    if (config.isNonWordingRelated()) {
      if (config.isUseDefaultTpl()) {
        return getDefaultTemplateName();
      } else {
        return generateTemplateName(config, recipientType, partyType, userCategory);
      }
    } else {
      return generateTemplateName(config, recipientType, product, locationInstitution, partyType, userCategory, withoutLinks);
    }
  }

  // non-wording related template name
  public static String getDefaultTemplateName() {
    return "default.btl";
  }

  // non-wording related template name
  public static String generateTemplateName(EmailNotificationConfig config, EmailNotificationRecipientType recipientType,
      String partyType, String userCategory) {
    return builder()
        .addPartyType(config, partyType)
        .addUserCategory(config, userCategory)
        .addRecipient(config, recipientType)
        .build();
  }

  // wording related template name
  public static String generateTemplateName(EmailNotificationConfig config, EmailNotificationRecipientType recipientType,
      String product, String locationInstitution, String partyType, String userCategory, boolean withoutLinks) {
    return builder()
        .addProduct(config, product)
        .addLocationInstitution(config, locationInstitution)
        .addPartyType(config, partyType)
        .addUserCategory(config, userCategory)
        .addRecipient(config, recipientType)
        .addWithoutLinks(config, recipientType, withoutLinks)
        .build();
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {

    private final List<String> values;

    public Builder() {
      values = new ArrayList<>();
    }

    public Builder addProduct(EmailNotificationConfig config, String product) {
      if (config.isTplIncludeProduct()) {
        values.add(product);
      }
      return this;
    }

    public Builder addLocationInstitution(EmailNotificationConfig config, String locationInstitution) {
      if (config.isTplIncludeLocationInstitution()) {
        values.add(locationInstitution);
      }
      return this;
    }

    public Builder addPartyType(EmailNotificationConfig config, String partyType) {
      if (!config.isTplIncludeUserCategory() && config.isTplIncludePartyType()) {
        values.add(partyType);
      }
      return this;
    }

    public Builder addUserCategory(EmailNotificationConfig config, String userCategory) {
      if (config.isTplIncludeUserCategory()) {
        values.add(userCategory);
      }
      return this;
    }

    public Builder addRecipient(EmailNotificationConfig config, EmailNotificationRecipientType recipientType) {
      if (config.isUseOthersTplExceptOperator() && !EmailNotificationRecipientType.OPERATOR.equals(recipientType)) {
        values.add("others");
        return this;
      }
      values.add(recipientType.getCode());
      return this;
    }

    public Builder addWithoutLinks(EmailNotificationConfig config, EmailNotificationRecipientType recipientType,
        boolean withoutLinks) {
      // 检查是否需要忽略链接
      if (shouldIgnoreLinks(config, recipientType)) {
        return this;
      }

      // 如果需要去除链接，添加标记
      if (withoutLinks) {
        values.add("without_links");
      }
      return this;
    }


    /**
     * 判断是否需要忽略链接
     */
    private boolean shouldIgnoreLinks(EmailNotificationConfig config, EmailNotificationRecipientType recipientType) {
      // 使用 Map 存储接收者类型和对应的配置检查方法
      Map<EmailNotificationRecipientType, Supplier<Boolean>> ignoreLinksMap = Map.of(
          EmailNotificationRecipientType.OPERATOR, config::isOperatorTplIgnoreLinksFlag,
          EmailNotificationRecipientType.SPECIFIC_USER, config::isSpecificUserTplIgnoreLinksFlag,
          EmailNotificationRecipientType.OWN_PARTY, config::isOwnPartyTplIgnoreLinksFlag,
          EmailNotificationRecipientType.COUNTER_PARTY, config::isCounterPartyTplIgnoreLinksFlag,
          EmailNotificationRecipientType.ALL_PARTY, config::isAllPartyTplIgnoreLinksFlag
      );

      // 获取对应的配置检查方法并执行
      return Optional.ofNullable(ignoreLinksMap.get(recipientType)).map(Supplier::get).orElse(false);
    }

    public String build() {
      return String.format("%s.btl", String.join("_", values).toLowerCase(Locale.US));
    }
  }
}
