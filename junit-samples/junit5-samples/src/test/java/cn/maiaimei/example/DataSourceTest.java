package cn.maiaimei.example;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.yaml.snakeyaml.Yaml;

@Slf4j
public class DataSourceTest {

  @ValueSource(strings = {
      "test.csv",
      "C:\\Users\\lenovo\\Desktop\\tmp\\test.csv"
  })
  @ParameterizedTest
  public void readCsv(String path) throws IOException {
    List<String[]> records;
    try (InputStreamReader reader = getInputStreamReader(path)) {
      CSVParser parser = CSVFormat.DEFAULT.parse(reader);
      final List<CSVRecord> csvRecords = parser.getRecords();
      records = Lists.newArrayList();
      for (final CSVRecord csvRecord : csvRecords) {
        String[] values = new String[csvRecord.size()];
        for (int j = 0; j < csvRecord.size(); j++) {
          values[j] = csvRecord.get(j);
        }
        records.add(values);
      }
    }
    for (String[] values : records) {
      log.info("{}", String.join(",", values));
    }
  }

  @ValueSource(strings = {
      "test.xls",
      "test.xlsx",
      "C:\\Users\\lenovo\\Desktop\\tmp\\test.xls",
      "C:\\Users\\lenovo\\Desktop\\tmp\\test.xlsx"
  })
  @ParameterizedTest
  public void readExcel(String path) throws IOException {
    try (InputStream is = getInputStream(path)) {
      Workbook wb = WorkbookFactory.create(is);
      Sheet sheet = wb.getSheetAt(0);
      final int lastRowNum = sheet.getLastRowNum();
      for (int i = 0; i <= lastRowNum; i++) {
        final Row row = sheet.getRow(i);
        final int lastCellNum = row.getLastCellNum();
        for (int j = 0; j < lastCellNum; j++) {
          final Cell cell = row.getCell(j);
          log.info("{}", cell.getStringCellValue());
        }
      }
    }
  }

  @ValueSource(strings = {
      "test.properties",
      "C:\\Users\\lenovo\\Desktop\\tmp\\test.properties"
  })
  @ParameterizedTest
  public void readProperties(String path) throws IOException {
    Map<String, String> result = Maps.newHashMap();
    final Properties properties = new Properties();
    try (InputStream is = getInputStream(path)) {
      properties.load(is);
    }
    for (String key : properties.stringPropertyNames()) {
      result.put(key, properties.getProperty(key));
    }
    for (Entry<String, String> entry : result.entrySet()) {
      log.info("{}={}", entry.getKey(), entry.getValue());
    }
  }

  @ValueSource(strings = {
      "test.yml",
      "C:\\Users\\lenovo\\Desktop\\tmp\\test.yml"
  })
  @ParameterizedTest
  public void readYaml(String path) throws IOException {
    Map<String, String> result;
    final Yaml yaml = new Yaml();
    try (InputStream is = getInputStream(path)) {
      result = yaml.load(is);
    }
    for (Entry<String, String> entry : result.entrySet()) {
      log.info("{}={}", entry.getKey(), entry.getValue());
    }
  }

  private InputStream getInputStream(String path) throws IOException {
    final File file = new File(path);
    if (file.isAbsolute()) {
      return Files.newInputStream(Paths.get(path));
    } else {
      return Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream(path));
    }
  }

  private InputStreamReader getInputStreamReader(String path) throws IOException {
    final File file = new File(path);
    if (file.isAbsolute()) {
      return new InputStreamReader(Files.newInputStream(Paths.get(path)));
    } else {
      return new InputStreamReader(
          Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream(path)));
    }
  }
}
