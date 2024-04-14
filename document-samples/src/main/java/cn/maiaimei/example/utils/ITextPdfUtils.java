package cn.maiaimei.example.utils;

import cn.maiaimei.example.itextpdf.CommonPdfConfig;
import cn.maiaimei.example.itextpdf.CommonPdfPageEvent;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.Pipeline;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.html.CssAppliersImpl;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.net.FileRetrieve;
import com.itextpdf.tool.xml.net.ReadingProcessor;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ITextPdfUtils {

  public static void html2Pdf(String pathname, String content) throws IOException {
    ConverterProperties converterProperties = new ConverterProperties();
    HtmlConverter.convertToPdf(
        content,
        Files.newOutputStream(Paths.get(pathname)),
        converterProperties
    );
  }


  /**
   * https://blog.csdn.net/zhong_jianyu/article/details/96147949
   */
  public static void html2Pdf(String pathname, String content, CommonPdfConfig config)
      throws Exception {
    Document document = new Document(config.getPageSize());
    // 设置文档边距
    // document.setMargins(30, 30, 30, 30);
    OutputStream outputStream = Files.newOutputStream(Paths.get(pathname));
    PdfWriter pdfWriter = PdfWriter.getInstance(document, outputStream);
    pdfWriter.setPageEvent(CommonPdfPageEvent.getInstance(config));
    document.open();
    HtmlPipelineContext htmlContext = getHtmlContext(config.getFontName(),
        config.getFontEncoding());
    PdfWriterPipeline pdfWriterPipeline = new PdfWriterPipeline(document, pdfWriter);
    HtmlPipeline htmlPipeline = new HtmlPipeline(htmlContext, pdfWriterPipeline);
    Pipeline<?> pipeline = new CssResolverPipeline(getCssResolver(config.getCharsetName()),
        htmlPipeline);
    XMLWorker worker = new XMLWorker(pipeline, Boolean.TRUE);
    XMLParser parser = new XMLParser(Boolean.TRUE, worker,
        Charset.forName(config.getCharsetName()));
    try (InputStream inputStream = new ByteArrayInputStream(content.getBytes())) {
      parser.parse(inputStream, Charset.forName(config.getCharsetName()));
    }
    document.close();
  }

  public static HtmlPipelineContext getHtmlContext(String fontName, String fontEncoding) {
    HtmlPipelineContext htmlContext =
        new HtmlPipelineContext(new CssAppliersImpl(new XMLWorkerFontProvider() {
          @Override
          public Font getFont(String name, String encoding, float size, final int style) {
            if (name == null) {
              name = fontName;
              encoding = fontEncoding;
            }
            Font font;
            try {
              font = new Font(BaseFont.createFont(name, encoding, BaseFont.NOT_EMBEDDED), size,
                  style);
            } catch (DocumentException | IOException e) {
              throw new RuntimeException(e);
            }
            return font;
          }
        }));
    htmlContext.setAcceptUnknown(true).autoBookmark(true)
        .setTagFactory(Tags.getHtmlTagProcessorFactory());
    return htmlContext;
  }

  public static CSSResolver getCssResolver(String charsetName) {
    CSSResolver cssResolver = XMLWorkerHelper.getInstance().getDefaultCssResolver(Boolean.TRUE);
    cssResolver.setFileRetrieve(new FileRetrieve() {
      @Override
      public void processFromStream(InputStream in, ReadingProcessor processor) {
        try (InputStreamReader reader = new InputStreamReader(in, charsetName)) {
          int i;
          while (-1 != (i = reader.read())) {
            processor.process(i);
          }
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }

      @Override
      public void processFromHref(String href, ReadingProcessor processor) {
        InputStream is = new ByteArrayInputStream(href.getBytes());
        try {
          InputStreamReader reader = new InputStreamReader(is, charsetName);
          int i;
          while (-1 != (i = reader.read())) {
            processor.process(i);
          }
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
    });
    return cssResolver;
  }

}
