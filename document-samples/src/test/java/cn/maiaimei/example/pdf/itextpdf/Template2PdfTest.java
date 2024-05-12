package cn.maiaimei.example.pdf.itextpdf;

import cn.maiaimei.example.BaseTest;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class Template2PdfTest extends BaseTest {

  @Test
  public void template2Pdf() {
    // PDF模板路径
    String inputFileName = "templates/itextpdf/input.pdf";
    // PDF输出路径
    String outputFileName = getRandomPdfName();

    try (OutputStream os = Files.newOutputStream(new File(outputFileName).toPath())) {
      // 读取PDF表单
      PdfReader reader = new PdfReader(inputFileName);
      // 根据表单生成一个新的PDF
      PdfStamper ps = new PdfStamper(reader, os);
      // 获取PDF表单
      AcroFields form = ps.getAcroFields();
      // 给表单添加中文字体
      BaseFont bf = BaseFont.createFont("Font/SIMYOU.TTF", BaseFont.IDENTITY_H,
          BaseFont.NOT_EMBEDDED);
      form.addSubstitutionFont(bf);
      // 模板数据
      Map<String, Object> data = new HashMap<>();
      data.put("childrenName", "李四");
      data.put("gender", "男");
      data.put("year", "2021");
      data.put("month", "09");
      data.put("day", "23");
      data.put("userName", "张三");
      data.put("address", "河南省案发时发放手动阀是的");
      // 填充模板
      for (String key : data.keySet()) {
        form.setField(key, data.get(key).toString());
      }
      ps.setFormFlattening(true);
      System.out.println("===============PDF导出成功=============");
    } catch (IOException | DocumentException e) {
      System.out.println("===============PDF导出失败=============");
      e.printStackTrace();
    }
  }

}
