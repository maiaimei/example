package cn.maiaimei.example.utils;

import cn.maiaimei.example.itextpdf.CommonPdfPageEvent;
import cn.maiaimei.example.itextpdf.CommonPdfPageEvent.Config;
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
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ITextPdfUtils {

  /**
   * https://blog.csdn.net/zhong_jianyu/article/details/96147949
   */
  public static void html2Pdf(String pathname, String content, Config config) throws Exception {
    final String charsetName = StandardCharsets.UTF_8.name();
    Document document = new Document(config.getPageSize());
    // 设置文档边距
    // document.setMargins(30, 30, 30, 30);
    OutputStream outputStream = Files.newOutputStream(Paths.get(pathname));
    PdfWriter pdfWriter = PdfWriter.getInstance(document, outputStream);
    pdfWriter.setPageEvent(CommonPdfPageEvent.getInstance(config));
    document.open();
    HtmlPipelineContext htmlContext = getHtmlContext("STSong-Light", "UniGB-UCS2-H");
    PdfWriterPipeline pdfWriterPipeline = new PdfWriterPipeline(document, pdfWriter);
    HtmlPipeline htmlPipeline = new HtmlPipeline(htmlContext, pdfWriterPipeline);
    Pipeline<?> pipeline = new CssResolverPipeline(getCssResolver(charsetName), htmlPipeline);
    XMLWorker worker = new XMLWorker(pipeline, Boolean.TRUE);
    XMLParser parser = new XMLParser(Boolean.TRUE, worker, Charset.forName(charsetName));
    try (InputStream inputStream = new ByteArrayInputStream(content.getBytes())) {
      parser.parse(inputStream, Charset.forName(charsetName));
    }
    document.close();
  }

  private static HtmlPipelineContext getHtmlContext(String font, String encode) {
    HtmlPipelineContext htmlContext =
        new HtmlPipelineContext(new CssAppliersImpl(new XMLWorkerFontProvider() {
          @Override
          public Font getFont(String fontname, String encoding, float size, final int style) {
            if (fontname == null) {
              fontname = font;
              encoding = encode;
            }
            Font font;
            try {
              font = new Font(BaseFont.createFont(fontname, encoding, BaseFont.NOT_EMBEDDED), size,
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

  private static CSSResolver getCssResolver(String charsetName) {
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
