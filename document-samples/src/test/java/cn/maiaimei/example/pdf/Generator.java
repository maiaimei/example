package cn.maiaimei.example.pdf;

import cn.maiaimei.example.BaseTest;
import cn.maiaimei.example.FileUtils;
import cn.maiaimei.example.MapUtils;
import cn.maiaimei.example.NumericConstants;
import com.google.common.collect.Lists;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
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
import com.itextpdf.tool.xml.pipeline.html.AbstractImageProvider;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;
import com.itextpdf.tool.xml.pipeline.html.ImageProvider;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.beetl.core.BeetlKit;
import org.junit.jupiter.api.Test;
import org.springframework.util.StringUtils;

/**
 * @描述：html/pdf生成器
 * @作者：zhongjy
 * @时间：2019年7月15日 下午12:31:25
 */
@Slf4j
public class Generator extends BaseTest {

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

    pdfGeneratePlus(content, FileUtils.getRandomFilename(OUTPUT_FOLDER, FileUtils.PDF),
        PageSize.A4, headerHtml, footerHtml);
  }

  public void pdfGeneratePlus(String content,
      String targetPdf, Rectangle pageSize, String header, String footer)
      throws Exception {
    final String charsetName = "UTF-8";
    Document document = new Document(pageSize);

    OutputStream out = new FileOutputStream(targetPdf);
    /**
     * 设置边距
     */
    // document.setMargins(30, 30, 30, 30);
    PdfWriter writer = PdfWriter.getInstance(document, out);

    CommonPdfPageEvent.Property property = CommonPdfPageEvent.Property.builder()
        .hasHeaderFooter(Boolean.TRUE)
        .hasPageNumber(Boolean.TRUE)
        .headerString(header)
        //.footerString(footer)
        .baseFont(BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", false))
        .fontSize(NumericConstants.TEN)
        .fontColor(BaseColor.GRAY)
        .build();

    writer.setPageEvent(CommonPdfPageEvent.getInstance(property));

    document.open();

    /**
     * html内容解析
     */
    HtmlPipelineContext htmlContext =
        new HtmlPipelineContext(new CssAppliersImpl(new XMLWorkerFontProvider() {
          @Override
          public Font getFont(String fontname, String encoding, float size, final int style) {
            if (fontname == null) {
              /**
               * 操作系统需要有该字体, 没有则需要安装; 当然也可以将字体放到项目中， 再从项目中读取
               */
              fontname = "STSong-Light";
              encoding = "UniGB-UCS2-H";
            }
            Font font = null;
            try {
              font = new Font(BaseFont.createFont(fontname, encoding, BaseFont.NOT_EMBEDDED), size,
                  style);
            } catch (Exception e) {
              log.error("", e);
            }
            return font;
          }
        })) {
          @Override
          public HtmlPipelineContext clone() throws CloneNotSupportedException {
            HtmlPipelineContext context = super.clone();
            ImageProvider imageProvider = this.getImageProvider();
            context.setImageProvider(imageProvider);
            return context;
          }
        };

    /**
     * 图片解析
     */
    htmlContext.setImageProvider(new AbstractImageProvider() {

      String rootPath = "C:\\Users\\Administrator\\Desktop\\刘亦菲\\";

      @Override
      public String getImageRootPath() {
        return rootPath;
      }

      @Override
      public Image retrieve(String src) {
        if (!StringUtils.hasText(src)) {
          return null;
        }
        try {
          Image image = Image.getInstance(new File(rootPath, src).toURI().toString());
          /**
           * 图片显示位置
           */
          image.setAbsolutePosition(400, 400);
          store(src, image);
          return image;
        } catch (Exception e) {
          log.error("", e);
        }
        return super.retrieve(src);
      }
    });
    htmlContext.setAcceptUnknown(true).autoBookmark(true)
        .setTagFactory(Tags.getHtmlTagProcessorFactory());

    // CSS解析
    CSSResolver cssResolver = XMLWorkerHelper.getInstance().getDefaultCssResolver(true);
    cssResolver.setFileRetrieve(new FileRetrieve() {
      @Override
      public void processFromStream(InputStream in, ReadingProcessor processor) throws IOException {
        try (InputStreamReader reader = new InputStreamReader(in, charsetName)) {
          int i = -1;
          while (-1 != (i = reader.read())) {
            processor.process(i);
          }
        } catch (Throwable e) {
        }
      }

      /**
       * 解析href
       */
      @Override
      public void processFromHref(String href, ReadingProcessor processor) throws IOException {
        InputStream is = new ByteArrayInputStream(href.getBytes());
        try {
          InputStreamReader reader = new InputStreamReader(is, charsetName);
          int i = -1;
          while (-1 != (i = reader.read())) {
            processor.process(i);
          }
        } catch (Exception e) {
          log.error("", e);
        }

      }
    });

    HtmlPipeline htmlPipeline =
        new HtmlPipeline(htmlContext, new PdfWriterPipeline(document, writer));
    Pipeline<?> pipeline = new CssResolverPipeline(cssResolver, htmlPipeline);
    XMLWorker worker = null;
    worker = new XMLWorker(pipeline, true);
    XMLParser parser = new XMLParser(true, worker, Charset.forName(charsetName));
    try (InputStream inputStream = new ByteArrayInputStream(content.getBytes())) {
      parser.parse(inputStream, Charset.forName(charsetName));
    }
    document.close();
  }
}
