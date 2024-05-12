package cn.maiaimei.example;

import cn.maiaimei.commons.lang.utils.FileUtils;
import cn.maiaimei.example.model.Document;
import cn.maiaimei.example.model.Fee;
import cn.maiaimei.example.model.Tax;
import cn.maiaimei.example.model.Transaction;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

@Slf4j
public class JacksonTest extends BaseTest {

  private static final Random random = new Random();

  private static final ObjectMapper mapper = new ObjectMapper();

  private static final String pathname = OUTPUT_FOLDER + "transaction.json";

  @Test
  public void generateJsonFile() throws JsonProcessingException {
    new File(pathname).delete();
    int size = 1;
    List<Transaction> transactions = Lists.newArrayList();
    for (int i = 0; i < size; i++) {
      transactions.add(buildTransaction());
    }
    Map<String, Object> map = Maps.newHashMap();
    Map<String, String> header = Maps.newHashMap();
    header.put("country", "CN");
    header.put("date", "20240415");
    map.put("headers", Lists.newArrayList(header));
    map.put("transactions", transactions);
    final String value = mapper.writeValueAsString(map);
    FileUtils.writeStringToFile(pathname, value);
  }

  /**
   * -Xms100m -Xmx100m
   * <p>
   * java.lang.OutOfMemoryError: Java heap space
   *
   * @throws JsonProcessingException
   */
  @Test
  public void parseJsonFile1() throws JsonProcessingException {
    final File file = new File(pathname);
    DecimalFormat decimalFormat = new DecimalFormat("#.##");
    double fileSize = org.apache.commons.io.FileUtils.sizeOf(file) / 1024.0 / 1024.0;
    log.info("fileSize: {}MB", decimalFormat.format(fileSize));
    final String value = FileUtils.readFileToString(file);
    final long start = System.nanoTime();
    final List<Transaction> transactions = mapper.readValue(value, new TypeReference<>() {
    });
    final long end = System.nanoTime();
    log.info("fileSize: {}MB,  execution of {} ms",
        decimalFormat.format(fileSize),
        TimeUnit.NANOSECONDS.toMillis(end - start));
    log.info("total: {}", transactions.size());
    /**
     * fileSize: 9.75MB,  execution of 317 ms
     * fileSize: 19.54MB,  execution of 371 ms
     * fileSize: 29.4MB,  execution of 410 ms
     * fileSize: 39.2MB,  execution of 495 ms
     * fileSize: 49.14MB,  execution of 568 ms
     * fileSize: 98.15MB,  execution of 956 ms
     * fileSize: 492.15MB,  execution of 3450 ms
     */
  }

  /**
   * 在使用Jackson解析大文件时，主要的挑战在于内存的使用效率和解析性能。
   * <p>
   * Jackson默认会将整个JSON文件加载到内存中，这可能会导致内存溢出错误。
   * <p>
   * 为了解决这个问题，可以使用JsonParser来逐个读取和处理JSON的片段，而不是一次性加载整个文件到内存中。在处理每个片段时尽快完成逻辑处理，避免将数据缓存过长时间。
   */
  @Test
  public void parseJsonFile2() {
    final File file = new File(pathname);
    DecimalFormat decimalFormat = new DecimalFormat("#.##");
    double fileSize = org.apache.commons.io.FileUtils.sizeOf(file) / 1024.0 / 1024.0;
    log.info("fileSize: {}MB", decimalFormat.format(fileSize));
    final long start = System.nanoTime();
    // 使用了JsonFactory来创建JsonParser实例
    JsonFactory jsonFactory = new JsonFactory();
    jsonFactory.enable(JsonParser.Feature.AUTO_CLOSE_SOURCE);
    try (JsonParser parser = jsonFactory.createParser(file)) {
      // 移动到第一个记录的开始
      parser.nextToken();
      while (parser.nextToken() != JsonToken.END_ARRAY) {
        // 每次循环处理一个对象
        Transaction transaction = mapper.readValue(parser, Transaction.class);
        log.info("{}", transaction);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    final long end = System.nanoTime();
    log.info("fileSize: {}MB,  execution of {} ms",
        decimalFormat.format(fileSize),
        TimeUnit.NANOSECONDS.toMillis(end - start));
  }


  private Transaction buildTransaction() {
    final Transaction transaction = Transaction.builder()
        .column01(RandomStringUtils.randomAlphanumeric(50))
        .column02(RandomStringUtils.randomAlphanumeric(50))
        .column03(RandomStringUtils.randomAlphanumeric(50))
        .column04(RandomStringUtils.randomAlphanumeric(50))
        .column05(RandomStringUtils.randomAlphanumeric(50))
        .column06(RandomStringUtils.randomAlphanumeric(50))
        .column07(RandomStringUtils.randomAlphanumeric(50))
        .column08(RandomStringUtils.randomAlphanumeric(50))
        .column09(RandomStringUtils.randomAlphanumeric(50))
        .column10(RandomStringUtils.randomAlphanumeric(50))
        .column11(RandomStringUtils.randomAlphanumeric(50))
        .column12(RandomStringUtils.randomAlphanumeric(50))
        .column13(RandomStringUtils.randomAlphanumeric(50))
        .column14(RandomStringUtils.randomAlphanumeric(50))
        .column15(RandomStringUtils.randomAlphanumeric(50))
        .column16(RandomStringUtils.randomAlphanumeric(50))
        .column17(RandomStringUtils.randomAlphanumeric(50))
        .column18(RandomStringUtils.randomAlphanumeric(50))
        .column19(RandomStringUtils.randomAlphanumeric(50))
        .column20(RandomStringUtils.randomAlphanumeric(50))
        .column21(RandomStringUtils.randomAlphanumeric(50))
        .column22(RandomStringUtils.randomAlphanumeric(50))
        .column23(RandomStringUtils.randomAlphanumeric(50))
        .column24(RandomStringUtils.randomAlphanumeric(50))
        .column25(RandomStringUtils.randomAlphanumeric(50))
        .column26(RandomStringUtils.randomAlphanumeric(50))
        .column27(RandomStringUtils.randomAlphanumeric(50))
        .column28(RandomStringUtils.randomAlphanumeric(50))
        .column29(RandomStringUtils.randomAlphanumeric(50))
        .column30(RandomStringUtils.randomAlphanumeric(50))
        .build();

    final int feeSize = random.nextInt(1, 5);
    List<Fee> fees = Lists.newArrayList();
    for (int i = 0; i < feeSize; i++) {
      fees.add(buildFee());
    }
    transaction.setFees(fees);

    final int documentSize = random.nextInt(1, 5);
    List<Document> documents = Lists.newArrayList();
    for (int i = 0; i < documentSize; i++) {
      documents.add(buildDocument());
    }
    transaction.setDocuments(documents);
    return transaction;
  }

  private Fee buildFee() {
    final Fee fee = Fee.builder()
        .column01(RandomStringUtils.randomAlphanumeric(50))
        .column02(RandomStringUtils.randomAlphanumeric(50))
        .column03(RandomStringUtils.randomAlphanumeric(50))
        .column04(RandomStringUtils.randomAlphanumeric(50))
        .column05(RandomStringUtils.randomAlphanumeric(50))
        .column06(RandomStringUtils.randomAlphanumeric(50))
        .column07(RandomStringUtils.randomAlphanumeric(50))
        .column08(RandomStringUtils.randomAlphanumeric(50))
        .column09(RandomStringUtils.randomAlphanumeric(50))
        .column10(RandomStringUtils.randomAlphanumeric(50))
        .build();
    final int taxSize = random.nextInt(1, 5);
    List<Tax> taxes = Lists.newArrayList();
    for (int i = 0; i < taxSize; i++) {
      taxes.add(buildTax());
    }
    fee.setTaxes(taxes);
    return fee;
  }

  private Tax buildTax() {
    return Tax.builder()
        .column01(RandomStringUtils.randomAlphanumeric(50))
        .column02(RandomStringUtils.randomAlphanumeric(50))
        .column03(RandomStringUtils.randomAlphanumeric(50))
        .column04(RandomStringUtils.randomAlphanumeric(50))
        .column05(RandomStringUtils.randomAlphanumeric(50))
        .column06(RandomStringUtils.randomAlphanumeric(50))
        .column07(RandomStringUtils.randomAlphanumeric(50))
        .column08(RandomStringUtils.randomAlphanumeric(50))
        .column09(RandomStringUtils.randomAlphanumeric(50))
        .column10(RandomStringUtils.randomAlphanumeric(50))
        .build();
  }

  private Document buildDocument() {
    return Document.builder()
        .column01(RandomStringUtils.randomAlphanumeric(50))
        .column02(RandomStringUtils.randomAlphanumeric(50))
        .column03(RandomStringUtils.randomAlphanumeric(50))
        .column04(RandomStringUtils.randomAlphanumeric(50))
        .column05(RandomStringUtils.randomAlphanumeric(50))
        .column06(RandomStringUtils.randomAlphanumeric(50))
        .column07(RandomStringUtils.randomAlphanumeric(50))
        .column08(RandomStringUtils.randomAlphanumeric(50))
        .column09(RandomStringUtils.randomAlphanumeric(50))
        .column10(RandomStringUtils.randomAlphanumeric(50))
        .build();
  }

}
