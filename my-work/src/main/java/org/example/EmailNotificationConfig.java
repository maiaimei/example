package org.example;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmailNotificationConfig {

  private boolean systemTriggered;

  private boolean accountLevelControlled;
  private String accountSwitch;

  private boolean documentLevelControlled;
  private String documentSwitch;

  private boolean sendToOperator;
  private boolean sendToOwnParty;
  private boolean sendToCounterParty;
  private boolean sendToAllParty;
  private boolean sendToSpecificUser;

  private boolean useSpecificTemplates;
  private boolean operatorUseSpecificTemplates;
  private boolean ownPartyUseSpecificTemplates;
  private boolean counterPartyUseSpecificTemplates;
  private boolean allPartyUseSpecificTemplates;
  private boolean specificUserUseSpecificTemplates;

  private Function<Boolean, String> operatorTemplateFunction;

  private Predicate<EntitlementDetails> counterPartyPredicate;

  private String urlFormat;
  private List<String> imageClassPaths;

  private boolean enableThrowingException;
  private String errorType;
  private String xMattersCode;

  public boolean isControlNotRequired() {
    return !accountLevelControlled && !documentLevelControlled;
  }
}