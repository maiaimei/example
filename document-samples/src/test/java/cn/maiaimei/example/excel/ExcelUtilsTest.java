package cn.maiaimei.example.excel;

import cn.maiaimei.example.BaseTest;
import cn.maiaimei.example.ExcelUtils;
import org.junit.jupiter.api.Test;

public class ExcelUtilsTest extends BaseTest {

  @Test
  public void readFile() {
    String pathname = "C:\\Users\\lenovo\\Desktop\\tmp\\test.xlsx";
    ExcelUtils.readFile(pathname);
  }
}
