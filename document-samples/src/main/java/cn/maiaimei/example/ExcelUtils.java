package cn.maiaimei.example;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.NumberToTextConverter;

@Slf4j
public class ExcelUtils {

  private static final ThreadLocal<DateFormat> DATE_FORMAT_14_THREAD_LOCAL =
      ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy/MM/dd"));

  private ExcelUtils() {
    throw new UnsupportedOperationException();
  }

  public static List<Map<String, String>> readFile(String pathname) {
    List<Map<String, String>> list = new ArrayList<>();
    try (InputStream is = Files.newInputStream(Paths.get(pathname))) {
      Workbook wb = WorkbookFactory.create(is);
      Sheet sheet = wb.getSheetAt(0);
      for (Row row : sheet) {
        for (Cell cell : row) {
          final String cellValue = getCellValue(cell);
          log.info("{}", cellValue);
        }
        list.add(null);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return list;
  }

  /**
   * getCellValue
   * <p>{@link BuiltinFormats}</p>
   *
   * @param cell cell
   * @return cell value
   */
  private static String getCellValue(Cell cell) {
    CellType cellType = cell.getCellType();
    switch (cellType) {
      case STRING: {
        return cell.getStringCellValue();
      }
      case NUMERIC: {
        if (DateUtil.isCellDateFormatted(cell)) {
          final CellStyle cellStyle = cell.getCellStyle();
          final short dataFormat = cellStyle.getDataFormat();
          final String dataFormatString = cellStyle.getDataFormatString();
          final Date date = cell.getDateCellValue();
          return "";
        } else {
          return NumberToTextConverter.toText(cell.getNumericCellValue());
        }
      }
      case BOOLEAN: {
        return String.valueOf(cell.getBooleanCellValue());
      }
      default: {
        return null;
      }
    }
  }

  private static String getDateCellValue(short dataFormat, Date date) {
    switch (dataFormat) {
      case 14: {
        return DATE_FORMAT_14_THREAD_LOCAL.get().format(date);
      }
      default: {
        return "";
      }
    }
  }
}

