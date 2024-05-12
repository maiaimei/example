package cn.maiaimei.example;

import cn.maiaimei.commons.lang.constants.FileConstants;
import cn.maiaimei.commons.lang.constants.NumberConstants;
import cn.maiaimei.commons.lang.utils.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;

public abstract class BaseTest {

  protected static final String OUTPUT_FOLDER = "C:\\Users\\lenovo\\Desktop\\tmp\\";

  protected String getRandomPdfName() {
    return FileUtils.getFileName(
        FileConstants.PDF,
        RandomStringUtils.randomAlphanumeric(NumberConstants.TWELVE),
        OUTPUT_FOLDER);
  }
}
