package cn.maiaimei.example.itextpdf;

import cn.maiaimei.example.constants.NumericConstants;
import cn.maiaimei.example.constants.StringConstants;
import cn.maiaimei.example.utils.FileUtils;
import com.itextpdf.text.BaseColor;
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
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

public class CommonPdfPageEvent extends PdfPageEventHelper {

  private Config config;
  private PdfTemplate pageNumberTemplate;

  private CommonPdfPageEvent() {
  }

  public static CommonPdfPageEvent getInstance(Config config) {
    final CommonPdfPageEvent event = new CommonPdfPageEvent();
    event.config = config;
    return event;
  }

  /**
   * 文档打开时，创建模板
   */
  @Override
  public void onOpenDocument(PdfWriter writer, Document document) {
    if (StringConstants.YES.equalsIgnoreCase(config.hasHeaderFooter)) {
      if (config.baseFont == null) {
        try {
          config.baseFont = BaseFont.createFont();
        } catch (DocumentException | IOException e) {
          throw new RuntimeException(e);
        }
      }
      if (config.fontDetail == null) {
        config.fontDetail = new Font(config.baseFont, config.fontSize, config.fontStyle);
        config.fontDetail.setColor(config.fontColor);
      }
      if (StringConstants.YES.equalsIgnoreCase(config.hasPageNumber)) {
        pageNumberTemplate = writer.getDirectContent()
            .createTemplate(config.totalPageBoxWidth, config.totalPageBoxHeight);
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
    if (StringConstants.YES.equalsIgnoreCase(config.hasPageNumber)) {
      // 将模板替换成实际的 Y 值，兼容各种文档size
      pageNumberTemplate.beginText();
      // 设置模版的字体、颜色
      pageNumberTemplate.setFontAndSize(config.baseFont, config.fontSize);
      pageNumberTemplate.setColorFill(config.fontColor);
      // 渲染模版
      pageNumberTemplate.showText(String.format(config.totalPageFormat, writer.getPageNumber()));
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
    if (StringConstants.YES.equalsIgnoreCase(config.hasHeaderFooter)) {
      // 写入页眉
      if (StringUtils.hasText(config.headerText)) {
        addHeader(writer, document);
      }
      // 写入页脚
      if (StringUtils.hasText(config.footerText)) {
        addFooter(writer, document);
      }
      if (StringConstants.YES.equalsIgnoreCase(config.hasPageNumber)) {
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
    switch (config.headerAlignment) {
      case Element.ALIGN_CENTER: {
        x = calculateXIfAlignCenter(document,
            config.baseFont.getWidthPoint(config.headerText, config.fontSize));
        break;
      }
      case Element.ALIGN_RIGHT: {
        x = document.right() - config.headerOffsetRight;
        break;
      }
      default: {
        x = document.left() + config.headerOffsetLeft;
        break;
      }
    }
    y = document.top() + config.headerOffsetTop;
    ColumnText.showTextAligned(writer.getDirectContent(), config.headerAlignment,
        new Phrase(config.headerText, config.fontDetail), x, y, NumericConstants.ZERO);
  }

  /**
   * 写入页脚
   *
   * @param writer   writer
   * @param document document
   */
  private void addFooter(PdfWriter writer, Document document) {
    float x, y;
    switch (config.footerAlignment) {
      case Element.ALIGN_CENTER: {
        x = calculateXIfAlignCenter(document,
            config.baseFont.getWidthPoint(config.footerText, config.fontSize));
        break;
      }
      case Element.ALIGN_RIGHT: {
        x = document.right() - config.footerOffsetRight;
        break;
      }
      default: {
        x = document.left() + config.footerOffsetLeft;
        break;
      }
    }
    y = document.bottom() - config.footerOffsetBottom;
    ColumnText.showTextAligned(writer.getDirectContent(), config.footerAlignment,
        new Phrase(config.footerText, config.fontDetail), x, y, NumericConstants.ZERO);
  }

  /**
   * 写入页码
   *
   * @param writer   writer
   * @param document document
   */
  private void addPageNumber(PdfWriter writer, Document document) {
    String currentPageText = String.format(config.currentPageFormat, writer.getPageNumber());
    final float len = config.baseFont.getWidthPoint(currentPageText, config.fontSize);
    PdfContentByte cb = writer.getDirectContent();
    float x1, x2, y;
    switch (config.pageNumberAlignment) {
      case Element.ALIGN_CENTER: {
        x1 = calculateXIfAlignCenter(document, len);
        x2 = calculateXIfAlignCenter(document, 0);
        break;
      }
      case Element.ALIGN_RIGHT: {
        x1 = document.right() - config.pageNumberOffsetRight - config.pageNumberReservedOffset;
        x2 = document.right() - config.pageNumberOffsetRight;
        break;
      }
      default: {
        x1 = document.left() + config.pageNumberOffsetLeft;
        x2 = document.left() + config.pageNumberOffsetLeft + config.pageNumberReservedOffset;
      }
    }
    if (StringConstants.YES.equalsIgnoreCase(config.pageNumberInBottom)) {
      y = document.bottom() - config.pageNumberOffsetBottom;
    } else {
      y = document.top() + config.pageNumberOffsetTop;
    }
    ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
        new Phrase(currentPageText, config.fontDetail),
        x1, y, NumericConstants.ZERO);
    cb.addTemplate(pageNumberTemplate, x2, y);
  }

  /**
   * 写入水印
   *
   * @param writer writer
   */
  private void addWatermark(PdfWriter writer) {
    if (Objects.nonNull(config.watermark)) {
      try {
        Image image = Image.getInstance(FileUtils.getBytes(config.watermark));
        PdfContentByte content = writer.getDirectContentUnder();
        content.beginText();
        image.setAbsolutePosition(config.watermarkX, config.watermarkY);
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

  @Data
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class Config {

    /**
     * 基础字体
     */
    private BaseFont baseFont;
    /**
     * 利用基础字体生成的字体对象，一般用于生成中文文字
     */
    private Font fontDetail;
    /**
     * 字体大小
     */
    @Builder.Default
    private int fontSize = NumericConstants.TEN;
    /**
     * 字体颜色
     */
    @Builder.Default
    private BaseColor fontColor = BaseColor.GRAY;
    /**
     * 字体样式
     */
    @Builder.Default
    private int fontStyle = Font.NORMAL;
    /**
     * 是否设置页眉页脚
     */
    @Builder.Default
    private String hasHeaderFooter = StringConstants.NO;
    /**
     * 页眉文本
     */
    private String headerText;
    /**
     * 页眉对齐方式
     */
    @Builder.Default
    private int headerAlignment = Element.ALIGN_LEFT;
    /**
     * 页眉相对文档顶部的距离
     */
    private float headerOffsetTop;
    /**
     * 页眉相对文档左边的距离
     */
    private float headerOffsetLeft;
    /**
     * 页眉相对文档右边的距离
     */
    private float headerOffsetRight;
    /**
     * 页脚文本
     */
    private String footerText;
    /**
     * 页脚对齐方式
     */
    @Builder.Default
    private int footerAlignment = Element.ALIGN_LEFT;
    /**
     * 页脚相对文档底部的距离
     */
    private float footerOffsetBottom;
    /**
     * 页脚相对文档左边的距离
     */
    private float footerOffsetLeft;
    /**
     * 页脚相对文档右边的距离
     */
    private float footerOffsetRight;
    /**
     * 是否设置页码
     */
    @Builder.Default
    private String hasPageNumber = StringConstants.NO;
    /**
     * 页码是否在底部
     */
    @Builder.Default
    private String pageNumberInBottom = StringConstants.YES;
    /**
     * 页码对齐方式
     */
    @Builder.Default
    private int pageNumberAlignment = Element.ALIGN_LEFT;
    /**
     * 页码相对文档顶部的距离
     */
    private float pageNumberOffsetTop;
    /**
     * 页码相对文档底部的距离
     */
    private float pageNumberOffsetBottom;
    /**
     * 页码相对文档左边的距离
     */
    private float pageNumberOffsetLeft;
    /**
     * 页码相对文档右边的距离
     */
    private float pageNumberOffsetRight;
    /**
     * 页码保留偏移
     */
    private float pageNumberReservedOffset;
    /**
     * 当前页码格式
     */
    @Builder.Default
    private String currentPageFormat = "第 %s 页 / 共 ";
    /**
     * 总页码格式
     */
    @Builder.Default
    private String totalPageFormat = "%s 页";
    /**
     * 总页码方框宽度
     */
    @Builder.Default
    private float totalPageBoxWidth = 50F;
    /**
     * 总页码方框高度
     */
    @Builder.Default
    private float totalPageBoxHeight = 50F;
    /**
     * 水印文件
     */
    private File watermark;
    /**
     * 水印横坐标位置
     */
    private float watermarkX;
    /**
     * 水印纵坐标位置
     */
    private float watermarkY;
  }

}
