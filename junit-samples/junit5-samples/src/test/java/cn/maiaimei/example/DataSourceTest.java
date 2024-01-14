package cn.maiaimei.example;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@Slf4j
public class DataSourceTest {

  @ValueSource(strings = "C:\\Users\\lenovo\\Desktop\\tmp\\test.csv")
  @ParameterizedTest
  public void readCsv(String path) throws IOException {
    String[][] result;
    try (InputStreamReader reader = new InputStreamReader(Files.newInputStream(Paths.get(path)))) {
      CSVParser parser = CSVFormat.DEFAULT.parse(reader);
      final List<CSVRecord> records = parser.getRecords();
      result = new String[records.size()][];
      for (int i = 0; i < records.size(); i++) {
        final CSVRecord record = records.get(i);
        String[] tmp = new String[record.size()];
        for (int j = 0; j < record.size(); j++) {
          tmp[j] = record.get(j);
        }
        result[i] = tmp;
      }
    }
    for (int i = 0; i < result.length; i++) {
      for (int j = 0; j < result[i].length; j++) {
        log.info("{}", result[i][j]);
      }
    }
  }
}
