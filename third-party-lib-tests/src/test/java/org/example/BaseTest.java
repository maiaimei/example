package org.example;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import org.apache.commons.io.FileUtils;

public abstract class BaseTest {

  protected String readFileContent(String pathname) {
    URL url = this.getClass().getClassLoader().getResource(pathname);
    if (url != null) {
      File file = new File(url.getFile());
      try {
        return FileUtils.readFileToString(file, Charset.defaultCharset());
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
    return null;
  }
}
