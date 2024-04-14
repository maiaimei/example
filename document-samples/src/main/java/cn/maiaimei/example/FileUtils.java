package cn.maiaimei.example;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

public class FileUtils {

  public static final String TXT = "txt";
  public static final String JSON = "json";
  public static final String DOC = "doc";
  public static final String DOCX = "docx";
  public static final String XLS = "xls";
  public static final String XLSX = "xlsx";
  public static final String PDF = "pdf";
  public static final String ZIP = "zip";

  private FileUtils() {
    throw new UnsupportedOperationException();
  }

  public static File getClassPathFile(String pathname) {
    URL url = FileUtils.class.getClassLoader().getResource(pathname);
    Assert.notNull(url, "classpath file does not exist");
    final File file = new File(url.getFile());
    if (!file.exists()) {
      Assert.notNull(url, "classpath file does not exist");
    }
    return file;
  }

  public static String getClassPathFilename(String pathname) {
    URL url = FileUtils.class.getClassLoader().getResource(pathname);
    Assert.notNull(url, "classpath file does not exist");
    final File file = new File(url.getFile());
    if (!file.exists()) {
      Assert.notNull(url, "classpath file does not exist");
    }
    return file.getAbsolutePath();
  }

  public static File createFile(String pathname) {
    return new File(pathname);
  }

  public static File createRandomFile(String destination, String suffix) {
    return createFile(getRandomFilename(destination, suffix));
  }

  public static String getRandomFilename(String destination, String suffix) {
    return String.format("%s%s.%s", destination,
        RandomStringUtils.randomAlphanumeric(NumericConstants.TWELVE),
        StringUtils.trimLeadingCharacter(suffix,
            StringConstants.DOT.charAt(NumericConstants.ZERO)));
  }

  public static File getOrCreateFile(String pathname) {
    File file = new File(pathname);
    if (!file.exists()) {
      file = createFile(pathname);
    }
    return file;
  }

  public static void writeStringToFile(String pathname, String value) {
    try (Writer writer = new OutputStreamWriter(
        Files.newOutputStream(Paths.get(pathname)),
        StandardCharsets.UTF_8)) {
      writer.write(value);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
