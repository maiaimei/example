package cn.maiaimei.example.pdf.itextpdf;

import cn.maiaimei.example.BaseTest;
import cn.maiaimei.example.constants.NumericConstants;
import cn.maiaimei.example.constants.StringConstants;
import cn.maiaimei.example.itextpdf.CommonPdfPageEvent.Config;
import cn.maiaimei.example.utils.FileUtils;
import cn.maiaimei.example.utils.ITextPdfUtils;
import com.google.common.collect.Maps;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.BaseFont;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.beetl.core.BeetlKit;
import org.junit.jupiter.api.Test;

@Slf4j
public class ITextPdfUtilsTest extends BaseTest {

  @Test
  public void test1() throws Exception {
    String headerText = "iText PDF";
    String footerText = "https://itextpdf.com";

    final File file = FileUtils.getClassPathFile("templates/itextpdf/report.btl");
    final String template = org.apache.commons.io.FileUtils.readFileToString(file,
        StandardCharsets.UTF_8);
    Map<String, Object> paras = Maps.newHashMap();
    String content = BeetlKit.render(template, paras);

    final Config config = Config.builder()
        .hasHeaderFooter(StringConstants.YES)
        .headerAlignment(Element.ALIGN_LEFT)
        .headerText(headerText)
        .footerAlignment(Element.ALIGN_LEFT)
        .footerText(footerText)
        .footerOffsetBottom(30)
        .hasPageNumber(StringConstants.YES)
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
    File pdfDest = FileUtils.createRandomFile(OUTPUT_FOLDER, FileUtils.PDF);
    final File file = FileUtils.getClassPathFile("templates/itextpdf/report.btl");
    final String template = org.apache.commons.io.FileUtils.readFileToString(file,
        StandardCharsets.UTF_8);
    Map<String, Object> paras = Maps.newHashMap();
    String content = BeetlKit.render(template, paras);
    ConverterProperties converterProperties = new ConverterProperties();
    HtmlConverter.convertToPdf(
        content,
        Files.newOutputStream(pdfDest.toPath()),
        converterProperties
    );
  }

}
