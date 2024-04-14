package cn.maiaimei.example.pdf;

import cn.maiaimei.example.BaseTest;
import cn.maiaimei.example.FileUtils;
import cn.maiaimei.example.MapUtils;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.beetl.core.BeetlKit;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xml.sax.SAXException;

public class FlyingSaucerPdfTest extends BaseTest {

  /**
   * 指定纸张大小为a4横向排版、并且边距为0的样式
   */
  private static final String CSS_PAGE_SIZE_A4 = "@page{size:297mm 210mm;margin:0;padding:0;"
      + "margin:0}";
  /**
   * 换页样式
   * <p>
   * 在需要换行的位置加入前端代码<div class='pageNext'></div>
   */
  private static final String CSS_PAGE_NEXT = ".pageNext{page-break-after:always;}";

  /**
   * 根路径
   */
  private static final String BASE_URL = "http://localhost:8080";
  private static final String HTML_CONTENT = "<html><body><h1>Hello, "
      + "flying-saucer-pdf</h1></body></html>";

  @Test
  public void html2Pdf_setDocumentFromString() throws IOException {
    ITextRenderer renderer = new ITextRenderer();

    // 解决中文支持问题
    //renderer.getFontResolver().addFont("c:/Windows/Fonts/simsun.ttc", BaseFont.IDENTITY_H, 
    // BaseFont.NOT_EMBEDDED);

    // 设置根路径，解决样式失效问题，前端样式引入可能使用相对路径，PDF转换必须使用绝对路径。
    //renderer.getSharedContext().setBaseURL(baseUrl);
    //renderer.getSharedContext().setBaseURL("file:///" + new File("").getAbsolutePath() + "/");

    // 设置PDF内容
    renderer.setDocumentFromString(HTML_CONTENT);
    //renderer.setDocumentFromString(htmlContent, baseUrl);

    // 渲染PDF
    renderer.layout();
    // 输出PDF
    renderer.createPDF(
        Files.newOutputStream(
            Paths.get(FileUtils.getRandomFilename(OUTPUT_FOLDER, FileUtils.PDF))));
  }

  @Test
  public void html2Pdf_setDocument()
      throws IOException, ParserConfigurationException, SAXException {
    ITextRenderer renderer = new ITextRenderer();
    DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    final Document document = builder.parse(new ByteArrayInputStream(HTML_CONTENT.getBytes()));
    renderer.setDocument(document);
    renderer.layout();
    renderer.createPDF(
        Files.newOutputStream(
            Paths.get(FileUtils.getRandomFilename(OUTPUT_FOLDER, FileUtils.PDF))));
  }

  @Test
  public void html2Pdf_writeNextDocument()
      throws IOException, ParserConfigurationException, SAXException {
    List<String> list = new ArrayList<>();
    list.add("<h2>test 1</h2>");
    list.add("<h2>test 2</h2>");
    list.add("<h2>test 3</h2>");

    ITextRenderer renderer = new ITextRenderer();
    DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    Document document = builder.parse(new ByteArrayInputStream(HTML_CONTENT.getBytes()));
    renderer.setDocument(document);
    renderer.layout();
    renderer.createPDF(
        Files.newOutputStream(Paths.get(FileUtils.getRandomFilename(OUTPUT_FOLDER, FileUtils.PDF))),
        Boolean.FALSE);

    for (String item : list) {
      document = builder.parse(new ByteArrayInputStream(item.getBytes()));
      renderer.setDocument(document, null);
      renderer.layout();
      renderer.writeNextDocument();
    }
    renderer.finishPDF();
  }

  @Test
  public void addHeaderFooter() throws IOException {
    String headerHtml = "<div class='header'><span>This is header</span></div>";
    String footerHtml = "<div class='footer'><span>This is footer</span></div>";

    String htmlFormat = "<html><head>"
        + "<style>"
        + "  @page{size:297mm 210mm;}"
        + "  .header {"
        + "    position: fixed; "
        + "    left: 0px; "
        + "    top: -50px; "
        + "    width: 100%; "
        + "    height: 50px; "
        + "    background-color: lightblue; "
        + "  }"
        + "  .footer {"
        + "    position: fixed; "
        + "    left: 0px; "
        + "    bottom: -50px; "
        + "    width: 100%; "
        + "    height: 50px; "
        + "    background-color: lightblue; "
        + "  }"
        + "</style>"
        + "</head><body>${body}</body></html>";

    String firstPageHtml = BeetlKit.render(htmlFormat,
        MapUtils.of("body", "<h2>Your content here in first page...</h2>"));

    List<String> list = new ArrayList<>();
    list.add(BeetlKit.render(htmlFormat,
        MapUtils.of("body", "<h2>Your content here in second page...</h2>")));
    list.add(BeetlKit.render(htmlFormat,
        MapUtils.of("body", "<h2>Your content here in third page...</h2>")));
    list.add(BeetlKit.render(htmlFormat,
        MapUtils.of("body", "<h2>Your content here in fourth page...</h2>")));

    ITextRenderer renderer = new ITextRenderer();
    renderer.setDocumentFromString(firstPageHtml);
    renderer.layout();
    renderer.createPDF(
        Files.newOutputStream(Paths.get(FileUtils.getRandomFilename(OUTPUT_FOLDER, FileUtils.PDF))),
        Boolean.FALSE);

    // 添加页眉和页脚
//    renderer.getSharedContext().setHeaderRenderer(new ITextRenderer() {{
//      setDocumentFromString(headerHtml);
//    }});
//    renderer.getSharedContext().setFooterRenderer(new ITextRenderer() {{
//      setDocumentFromString(footerHtml);
//    }});

    // 渲染PDF
    for (String item : list) {
      renderer.setDocumentFromString(item, null);
      renderer.layout();
      renderer.writeNextDocument();
    }
    renderer.finishPDF();
  }

}
