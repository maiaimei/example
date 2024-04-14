package cn.maiaimei.example.pdf.itextpdf;

import cn.maiaimei.example.BaseTest;
import cn.maiaimei.example.constants.NumericConstants;
import cn.maiaimei.example.itextpdf.CommonPdfConfig;
import cn.maiaimei.example.utils.FileUtils;
import cn.maiaimei.example.utils.ITextPdfUtils;
import com.google.common.collect.Maps;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.BaseFont;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.beetl.core.BeetlKit;
import org.junit.jupiter.api.Test;

@Slf4j
public class ITextPdfUtilsTest extends BaseTest {

  @Test
  public void test1() throws Exception {
    String headerText = "iText - The world's most downloaded and developer-friendly PDF library";
    String footerText = "https://itextpdf.com";

    final String template = FileUtils.readClassPathFileToString("templates/itextpdf/report.btl");
    Map<String, Object> paras = Maps.newHashMap();
    String content = BeetlKit.render(template, paras);

    final CommonPdfConfig config = CommonPdfConfig.builder()
        .pageSize(PageSize.A4)
        .charsetName(StandardCharsets.UTF_8.name())
        .fontName("STSong-Light")
        .fontEncoding("UniGB-UCS2-H")
        .hasHeaderFooter(true)
        .headerAlignment(Element.ALIGN_LEFT)
        .headerText(headerText)
        .headerOffsetTop(30)
        .footerAlignment(Element.ALIGN_LEFT)
        .footerText(footerText)
        .footerOffsetBottom(30)
        .hasPageNumber(true)
        .pageNumberAlignment(Element.ALIGN_RIGHT)
        .pageNumberOffsetBottom(30)
        .pageNumberReservedOffset(28)
        .currentPageFormat("第 %s 页 / 共 ")
        .totalPageFormat("%s 页")
        .totalPageBoxWidth(50)
        .totalPageBoxHeight(50)
        .baseFont(BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", false))
        .fontSize(NumericConstants.TEN)
        .fontColor(BaseColor.GRAY)
        .build();

    String pathname = FileUtils.getRandomFilename(OUTPUT_FOLDER, FileUtils.PDF);
    ITextPdfUtils.html2Pdf(pathname, content, config);
  }

  @Test
  public void test2() throws IOException {
    final String template = FileUtils.readClassPathFileToString("templates/itextpdf/report.btl");
    Map<String, Object> paras = Maps.newHashMap();
    String content = BeetlKit.render(template, paras);
    String pathname = FileUtils.getRandomFilename(OUTPUT_FOLDER, FileUtils.PDF);
    ITextPdfUtils.html2Pdf(pathname, content);
  }

}
