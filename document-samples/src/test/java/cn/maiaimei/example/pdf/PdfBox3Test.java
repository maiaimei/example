package cn.maiaimei.example.pdf;

import cn.maiaimei.example.BaseTest;
import cn.maiaimei.example.FileUtils;
import java.io.IOException;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.jupiter.api.Test;

public class PdfBox3Test extends BaseTest {

  @Test
  public void html2Pdf() {

  }

  @Test
  public void pdf2HTML() {

  }

  @Test
  public void pdf2PlainText() {
    try (PDDocument document = Loader.loadPDF(
        FileUtils.getClassPathFile("templates/pdfbox/input.pdf"))) {
      // 创建一个PDFTextStripper的子类用于自定义输出
      PDFTextStripper stripper = new PDFTextStripper() {
        @Override
        protected void startPage(PDPage page) throws IOException {
          super.startPage(page);
          writeString(
              "Start a new page. Default implementation is to do nothing. Subclasses may provide "
                  + "additional information.\n");
        }

        @Override
        protected void endPage(PDPage page) throws IOException {
          writeString(
              "End a page. Default implementation is to do nothing. Subclasses may provide "
                  + "additional information. \n");
          super.endPage(page);
        }
      };

      stripper.setStartPage(1);
      stripper.setEndPage(document.getNumberOfPages());

      String content = stripper.getText(document);
      System.out.println(content);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
