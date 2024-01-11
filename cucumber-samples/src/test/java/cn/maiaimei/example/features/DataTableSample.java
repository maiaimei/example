package cn.maiaimei.example.features;

import io.cucumber.java.en.Given;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataTableSample {

  @Given("[List - Map（常用）] add account")
  public void testList2Map(List<Map<String, String>> dataTable) {
    for (Map<String, String> data : dataTable) {
      String username = data.get("username");
      String password = data.get("password");
      String role = data.get("role");
      String projectId = data.get("project_id");
      log.info(
          "execute testList2Map(), fields:[ username = {}, password = {}, role = {}, projectId = "
              + "{} ]",
          username, password, role, projectId);
    }
  }

  @Given("[List - List] add account")
  public void testList2List(List<List<String>> dataTable) {
    for (List<String> data : dataTable) {
      log.info("execute testList2List(), fields:[ data = {} ]", data);
    }
  }

  @Given("[Map - List] add account")
  public void testMap2List(Map<String, List<String>> dataTable) {
    for (Map.Entry<String, List<String>> data : dataTable.entrySet()) {
      String username = data.getKey();
      List<String> infos = data.getValue();
      log.info("execute testMap2List(), fields:[ username = {}, infos = {} ]", username, infos);
    }
  }

}
