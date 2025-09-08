package org.example;

import java.io.File;
import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class EmailNotificationParams {

  private List<String> specificUserEmailAddressList;
  private Map<String, String> templateBindings;
  private File attachment;
}
