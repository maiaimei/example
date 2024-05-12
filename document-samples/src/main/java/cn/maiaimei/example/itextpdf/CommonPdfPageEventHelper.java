package cn.maiaimei.example.itextpdf;

import cn.maiaimei.commons.lang.constants.NumericConstants;
import cn.maiaimei.commons.lang.constants.StringConstants;
import cn.maiaimei.commons.lang.utils.FileUtils;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.IOException;
import java.util.Objects;
import org.springframework.util.StringUtils;

public class CommonPdfPageEventHelper extends PdfPageEventHelper {

  private CommonPdfPageConfig config;
  private PdfTemplate pageNumberTemplate;

  private CommonPdfPageEventHelper() {
  }

  public static CommonPdfPageEventHelper getInstance(CommonPdfPageConfig config) {
    final CommonPdfPageEventHelper event = new CommonPdfPageEventHelper();
    event.config = config;
    return event;
  }

  /**
   * 文档打开时，创建模板
   */
  @Override
  public void onOpenDocument(PdfWriter writer, Document document) {
    if (config.isHasHeaderFooter()) {
      if (config.getBaseFont() == null) {
        try {
          config.setBaseFont(BaseFont.createFont());
        } catch (DocumentException | IOException e) {
          throw new RuntimeException(e);
        }
      }
      if (config.getFontDetail() == null) {
        config.setFontDetail(new Font(config.getBaseFont(), config.getFontSize(),
            config.getFontStyle()));
        config.getFontDetail().setColor(config.getFontColor());
      }
      if (config.isHasPageNumber()) {
        pageNumberTemplate = writer.getDirectContent()
            .createTemplate(config.getTotalPageBoxWidth(), config.getTotalPageBoxHeight());
      }
    }
  }

  /**
   * 关闭每页时，添加页眉页脚和水印
   */
  @Override
  public void onEndPage(PdfWriter writer, Document document) {
    // 添加页眉页脚
    addHeaderFooter(writer, document);
    // 添加水印
    addWatermark(writer);
  }

  /**
   * 关闭文档时，完成页眉页脚的渲染
   */
  @Override
  public void onCloseDocument(PdfWriter writer, Document document) {
    if (config.isHasPageNumber()) {
      // 将模板替换成实际的 Y 值，兼容各种文档size
      pageNumberTemplate.beginText();
      // 设置模版的字体、颜色
      pageNumberTemplate.setFontAndSize(config.getBaseFont(), config.getFontSize());
      pageNumberTemplate.setColorFill(config.getFontColor());
      // 渲染模版
      pageNumberTemplate.showText(
          String.format(config.getTotalPageFormat(), writer.getPageNumber()));
      pageNumberTemplate.endText();
      pageNumberTemplate.closePath();
    }
  }

  /**
   * 添加页眉页脚
   *
   * @param writer   writer
   * @param document document
   */
  private void addHeaderFooter(PdfWriter writer, Document document) {
    if (config.isHasPageNumber()) {
      // 写入页眉
      if (StringUtils.hasText(config.getHeaderText())) {
        addHeader(writer, document);
      }
      // 写入页脚
      if (StringUtils.hasText(config.getFooterText())) {
        addFooter(writer, document);
      }
      if (config.isHasPageNumber()) {
        // 添加页码
        addPageNumber(writer, document);
      }
    }
  }

  /**
   * 写入页眉
   *
   * @param writer   writer
   * @param document document
   */
  private void addHeader(PdfWriter writer, Document document) {
    float x, y;
    switch (config.getHeaderAlignment()) {
      case Element.ALIGN_CENTER: {
        x = calculateXIfAlignCenter(document,
            config.getBaseFont().getWidthPoint(config.getHeaderText(), config.getFontSize()));
        break;
      }
      case Element.ALIGN_RIGHT: {
        x = document.right() - config.getHeaderOffsetRight();
        break;
      }
      default: {
        x = document.left() + config.getHeaderOffsetLeft();
        break;
      }
    }
    y = document.top() + config.getHeaderOffsetTop();
    ColumnText.showTextAligned(writer.getDirectContent(), config.getHeaderAlignment(),
        new Phrase(config.getHeaderText(), config.getFontDetail()), x, y, NumericConstants.ZERO);
  }

  /**
   * 写入页脚
   *
   * @param writer   writer
   * @param document document
   */
  private void addFooter(PdfWriter writer, Document document) {
    float x, y;
    switch (config.getFooterAlignment()) {
      case Element.ALIGN_CENTER: {
        x = calculateXIfAlignCenter(document,
            config.getBaseFont().getWidthPoint(config.getFooterText(), config.getFontSize()));
        break;
      }
      case Element.ALIGN_RIGHT: {
        x = document.right() - config.getFooterOffsetRight();
        break;
      }
      default: {
        x = document.left() + config.getFooterOffsetLeft();
        break;
      }
    }
    y = document.bottom() - config.getFooterOffsetBottom();
    ColumnText.showTextAligned(writer.getDirectContent(), config.getFooterAlignment(),
        new Phrase(config.getFooterText(), config.getFontDetail()), x, y, NumericConstants.ZERO);
  }

  /**
   * 写入页码
   *
   * @param writer   writer
   * @param document document
   */
  private void addPageNumber(PdfWriter writer, Document document) {
    String currentPageText = String.format(config.getCurrentPageFormat(), writer.getPageNumber());
    final float len = config.getBaseFont().getWidthPoint(currentPageText, config.getFontSize());
    PdfContentByte cb = writer.getDirectContent();
    float x1, x2, y;
    switch (config.getPageNumberAlignment()) {
      case Element.ALIGN_CENTER: {
        x1 = calculateXIfAlignCenter(document, len);
        x2 = calculateXIfAlignCenter(document, 0);
        break;
      }
      case Element.ALIGN_RIGHT: {
        x1 = document.right() - config.getPageNumberOffsetRight()
            - config.getPageNumberReservedOffset();
        x2 = document.right() - config.getPageNumberOffsetRight();
        break;
      }
      default: {
        x1 = document.left() + config.getPageNumberOffsetLeft();
        x2 = document.left() + config.getPageNumberOffsetLeft()
            + config.getPageNumberReservedOffset();
      }
    }
    if (StringConstants.YES.equalsIgnoreCase(config.getPageNumberInBottom())) {
      y = document.bottom() - config.getPageNumberOffsetBottom();
    } else {
      y = document.top() + config.getPageNumberOffsetTop();
    }
    ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
        new Phrase(currentPageText, config.getFontDetail()),
        x1, y, NumericConstants.ZERO);
    cb.addTemplate(pageNumberTemplate, x2, y);
  }

  /**
   * 写入水印
   *
   * @param writer writer
   */
  private void addWatermark(PdfWriter writer) {
    if (Objects.nonNull(config.getWatermark())) {
      try {
        Image image = Image.getInstance(FileUtils.getBytes(config.getWatermark()));
        PdfContentByte content = writer.getDirectContentUnder();
        content.beginText();
        image.setAbsolutePosition(config.getWatermarkX(), config.getWatermarkY());
        content.addImage(image);
        content.endText();
      } catch (DocumentException | IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  /**
   * 如果居中对齐则需要动态计算横坐标位置
   *
   * @param document document
   * @param length   length
   * @return float
   */
  private float calculateXIfAlignCenter(Document document, float length) {
    float tmp = document.rightMargin() + document.right() + document.leftMargin() - document.left();
    if (length > 0) {
      tmp = tmp - length;
    }
    return tmp / 2.0F + 20F;
  }

}
