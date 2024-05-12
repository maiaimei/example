package cn.maiaimei.example.pdf.itextpdf;

import cn.maiaimei.commons.lang.constants.FileConstants;
import cn.maiaimei.commons.lang.utils.FileUtils;
import cn.maiaimei.example.BaseTest;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import org.junit.jupiter.api.Test;

public class Image2PdfTest extends BaseTest {

  private static final String ORIG = "templates/itextpdf/input.jpg";

  @Test
  public void image2Pdf() throws FileNotFoundException, MalformedURLException {
    ImageData imageData = ImageDataFactory.create(FileUtils.getClassPathFilename(ORIG));

    PdfDocument pdfDocument = new PdfDocument(
        new PdfWriter(FileUtils.getRandomFilename(OUTPUT_FOLDER, FileConstants.PDF)));
    Document document = new Document(pdfDocument);

    Image image = new Image(imageData);
    image.setWidth(pdfDocument.getDefaultPageSize().getWidth() - 50);
    image.setAutoScaleHeight(true);

    document.add(image);
    pdfDocument.close();
  }
}

