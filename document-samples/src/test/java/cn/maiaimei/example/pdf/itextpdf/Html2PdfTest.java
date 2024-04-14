package cn.maiaimei.example.pdf.itextpdf;

import cn.maiaimei.example.BaseTest;
import cn.maiaimei.example.utils.FileUtils;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.pdf.PdfWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import org.junit.jupiter.api.Test;

public class Html2PdfTest extends BaseTest {

  private static final String ORIG = "templates/itextpdf/input.html";

  @Test
  public void html2Pdf() throws IOException {
    // IO
    File htmlSource = FileUtils.getClassPathFile(ORIG);
    File pdfDest = FileUtils.createRandomFile(OUTPUT_FOLDER, FileUtils.PDF);
    // pdfHTML specific code
    ConverterProperties converterProperties = new ConverterProperties();
    HtmlConverter.convertToPdf(
        Files.newInputStream(htmlSource.toPath()),
        Files.newOutputStream(pdfDest.toPath()),
        converterProperties
    );
  }

  @Test
  public void htmlString2Pdf() throws FileNotFoundException {
    String html = "<h1>Apache PDFBox® - A Java PDF Library</h1>\n"
        + "<p>\n"
        + "The Apache PDFBox® library is an open source Java tool for working with PDF documents."
        + " This project allows creation of new PDF documents, manipulation of existing documents"
        + " and the ability to extract content from documents. Apache PDFBox also includes "
        + "several command-line utilities. Apache PDFBox is published under the Apache License v2"
        + ".0.\n"
        + "<p>";
    ConverterProperties converterProperties = new ConverterProperties();
    HtmlConverter.convertToPdf(html,
        new PdfWriter(FileUtils.getRandomFilename(OUTPUT_FOLDER, FileUtils.PDF)),
        converterProperties);
  }

}
