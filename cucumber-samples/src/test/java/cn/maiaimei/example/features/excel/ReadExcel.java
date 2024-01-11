package cn.maiaimei.example.features.excel;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

@Slf4j
public class ReadExcel {

  @When("I am on the mainscreen")
  public void onTheMainscreen() {
    log.info("I am on the mainscreen");
  }

  @Then("I input username and password with excel row{int} dataset")
  public void inputUsernameAndPassword(int rownum) throws IOException {
    log.info("I input username and password with excel row{} dataset", rownum);
    readExcel(rownum);
  }

  private void readExcel(int rownum) throws IOException {
    String path = "C:\\Users\\lenovo\\Desktop\\tmp\\username.xlsx";
    try (InputStream is = Files.newInputStream(Paths.get(path))) {
      Workbook wb = WorkbookFactory.create(is);
      Sheet sheet = wb.getSheetAt(0);
      Row row = sheet.getRow(rownum);
      Cell usernameCell = row.getCell(0);
      Cell passwordCell = row.getCell(1);
      String username = usernameCell.getStringCellValue();
      String password = passwordCell.getStringCellValue();
      log.info("The username on {} is: {}, the password on {} is: {}",
          rownum, username, rownum, password);
    }
  }
}
