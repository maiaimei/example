package cn.maiaimei.example.pdf.itextpdf;

import cn.maiaimei.example.BaseTest;
import cn.maiaimei.example.utils.FileUtils;
import cn.maiaimei.example.utils.ITextPdfUtils;
import cn.maiaimei.example.utils.MapUtils;
import com.google.common.collect.Lists;
import com.itextpdf.text.PageSize;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.beetl.core.BeetlKit;
import org.junit.jupiter.api.Test;

@Slf4j
public class ITextPdfUtilsTest extends BaseTest {

  @Test
  public void testPdfGeneratePlus() throws Exception {
    String headerHtml = "This is header";
    String footerHtml = "This is footer";

    String htmlFormat = "<html><head></head><body>${body}</body></html>";

    List<String> list = Lists.newArrayList();
    for (int i = 0; i < 1000; i++) {
      list.add("<h1>Your content here...</h1>");
    }
    String content = BeetlKit.render(htmlFormat,
        MapUtils.of("body", String.join("", list)));

    ITextPdfUtils.html2Pdf(content, FileUtils.getRandomFilename(OUTPUT_FOLDER, FileUtils.PDF),
        PageSize.A4, headerHtml, footerHtml);
  }

}
