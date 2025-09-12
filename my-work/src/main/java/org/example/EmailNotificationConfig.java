package org.example;

import java.util.List;
import java.util.function.Predicate;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmailNotificationConfig {

  private boolean systemTriggered;

  private boolean nonWordingRelated;

  private boolean accountLevelControlled;
  private String accountSwitch;

  private boolean documentLevelControlled;
  private String documentSwitch;

  private boolean sendToOperator;
  private boolean sendToSpecificUser;
  private boolean sendToOwnParty;
  private boolean sendToCounterParty;
  private boolean sendToAllParty;

  private boolean useDefaultTpl;
  private boolean useSameTplExceptOperator;
  private boolean tplIncludeProduct;
  private boolean tplIncludeLocationInstitution;
  private boolean tplIncludePartyType;
  private boolean tplIncludeUserCategory;
  private boolean operatorTplIgnoreLinksFlag;
  private boolean specificUserTplIgnoreLinksFlag;
  private boolean ownPartyTplIgnoreLinksFlag;
  private boolean counterPartyTplIgnoreLinksFlag;
  private boolean allPartyTplIgnoreLinksFlag;

  private Predicate<EntitlementDetails> counterPartyPredicate;

  private List<String> imageClassPaths;

  private boolean enableThrowingException;
  private String errorType;
  private String xMattersCode;

  public boolean isControlNotRequired() {
    return !accountLevelControlled && !documentLevelControlled;
  }
}