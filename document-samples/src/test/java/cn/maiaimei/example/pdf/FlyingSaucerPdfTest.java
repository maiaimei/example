package cn.maiaimei.example.pdf;

import cn.maiaimei.example.BaseTest;
import cn.maiaimei.example.FileUtils;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xml.sax.SAXException;

public class FlyingSaucerPdfTest extends BaseTest {

  String htmlContent = "<html><body><h1>Hello, flying-saucer-pdf</h1></body></html>";

  @Test
  public void html2Pdf_setDocumentFromString() throws IOException {
    ITextRenderer renderer = new ITextRenderer();
    // TBC: 设置字体支持，需要指定字体路径
    //renderer.getFontResolver().addFont("", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
    // TBC: 解决图片问题
    //renderer.getSharedContext().setBaseURL("file:///" + new File("").getAbsolutePath() + "/");
    renderer.setDocumentFromString(htmlContent);
    // 展现和输出PDF
    renderer.layout();
    renderer.createPDF(
        Files.newOutputStream(
            Paths.get(FileUtils.getRandomFilename(OUTPUT_FOLDER, FileUtils.PDF))));
  }

  @Test
  public void html2Pdf_setDocument()
      throws IOException, ParserConfigurationException, SAXException {
    ITextRenderer renderer = new ITextRenderer();
    DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    final Document document = builder.parse(new ByteArrayInputStream(htmlContent.getBytes()));
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
    Document document = builder.parse(new ByteArrayInputStream(htmlContent.getBytes()));
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
}
