package cn.maiaimei.example.itextpdf;

import cn.maiaimei.example.constants.NumericConstants;
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
import lombok.Builder;
import org.springframework.util.StringUtils;

public class CommonPdfPageEvent extends PdfPageEventHelper {

  private Property property;
  private PdfTemplate pageNumberTemplate;

  private CommonPdfPageEvent() {
  }

  public static CommonPdfPageEvent getInstance(Property property) {
    final CommonPdfPageEvent event = new CommonPdfPageEvent();
    event.property = property;
    return event;
  }

  /**
   * 文档打开时，创建模板
   */
  @Override
  public void onOpenDocument(PdfWriter writer, Document document) {
    if (property.hasHeaderFooter) {
      if (property.baseFont == null) {
        try {
          property.baseFont = BaseFont.createFont();
        } catch (DocumentException | IOException e) {
          throw new RuntimeException(e);
        }
      }
      if (property.fontDetail == null) {
        property.fontDetail = new Font(property.baseFont, property.fontSize, property.fontStyle);
        property.fontDetail.setColor(property.fontColor);
      }
      if (property.hasPageNumber) {
        pageNumberTemplate = writer.getDirectContent()
            .createTemplate(property.totalPageWidth, property.totalPageHeight);
      }
    }
  }

  /**
   * 关闭每页时，添加页眉页脚和水印
   */
  @Override
  public void onEndPage(PdfWriter writer, Document document) {
    // 添加页眉页脚
    this.addHeaderFooter(writer, document);
    // 添加水印
    this.addWatermark(writer);
  }

  /**
   * 关闭文档时，完成页眉页脚的渲染
   */
  @Override
  public void onCloseDocument(PdfWriter writer, Document document) {
    if (property.hasPageNumber) {
      // 将模板替换成实际的 Y 值，兼容各种文档size
      pageNumberTemplate.beginText();
      // 设置模版的字体、颜色
      pageNumberTemplate.setFontAndSize(property.baseFont, property.fontSize);
      pageNumberTemplate.setColorFill(property.fontColor);
      // 渲染模版
      pageNumberTemplate.showText(String.format(property.totalPageFormat, writer.getPageNumber()));
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
    if (property.hasHeaderFooter) {
      // 写入页眉
      if (StringUtils.hasText(property.headerText)) {
        addHeader(writer, document);
      }
      // 写入页脚
      if (StringUtils.hasText(property.footerText)) {
        addFooter(writer, document);
      }
      if (property.hasPageNumber) {
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
    float x = 0F, y = 0F;
    if (Element.ALIGN_CENTER == property.headerAlignment) {
      x = calculateXIfAlignCenter(document,
          property.baseFont.getWidthPoint(property.headerText, property.fontSize));
    } else if (property.headerX == 0) {
      x = document.left() + property.headerOffsetLeft;
    }
    if (property.headerY == 0) {
      y = document.top() + property.headerOffsetTop;
    }
    ColumnText.showTextAligned(writer.getDirectContent(), property.headerAlignment,
        new Phrase(property.headerText, property.fontDetail), x, y, NumericConstants.ZERO);
  }

  /**
   * 写入页脚
   *
   * @param writer   writer
   * @param document document
   */
  private void addFooter(PdfWriter writer, Document document) {
    float x = 0F, y = 0F;
    if (Element.ALIGN_CENTER == property.footerAlignment) {
      x = calculateXIfAlignCenter(document,
          property.baseFont.getWidthPoint(property.footerText, property.fontSize));
    } else if (property.footerX == 0) {
      x = document.left() + property.footerOffsetLeft;
    }
    if (property.footerY == 0) {
      y = document.bottom() - property.footerOffsetBottom;
    }
    ColumnText.showTextAligned(writer.getDirectContent(), property.footerAlignment,
        new Phrase(property.footerText, property.fontDetail), x, y, NumericConstants.ZERO);
  }

  /**
   * 写入页码
   *
   * @param writer   writer
   * @param document document
   */
  private void addPageNumber(PdfWriter writer, Document document) {
    String currentPageText = String.format(property.currentPageFormat, writer.getPageNumber());
    PdfContentByte cb = writer.getDirectContent();
    float x1 = 0;
    float x2 = 0;
    float y = document.bottom() - property.footerOffsetBottom;
    if (Element.ALIGN_CENTER == property.pageNumberAlignment) {
      x1 = calculateXIfAlignCenter(document,
          property.baseFont.getWidthPoint(currentPageText, property.fontSize));
      x2 = calculateXIfAlignCenter(document, 0);
    } else if (Element.ALIGN_LEFT == property.pageNumberAlignment) {
      x1 = document.left() + property.footerOffsetLeft + property.baseFont.getWidthPoint(
          currentPageText, property.fontSize);
      x2 = x1;
    } else {
      // TBD
    }
    ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
        new Phrase(currentPageText, property.fontDetail),
        x1, y, NumericConstants.ZERO);
    cb.addTemplate(pageNumberTemplate, x2, y);
  }

  /**
   * 写入水印
   *
   * @param writer writer
   */
  private void addWatermark(PdfWriter writer) {
    if (Objects.nonNull(property.watermark)) {
      try {
        Image image = Image.getInstance(FileUtils.getBytes(property.watermark));
        PdfContentByte content = writer.getDirectContentUnder();
        content.beginText();
        image.setAbsolutePosition(property.watermarkX, property.watermarkY);
        content.addImage(image);
        content.endText();
      } catch (DocumentException | IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  /**
   * 如果居中对齐动态计算X坐标
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

  @Builder
  public static class Property {

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
    private int fontSize = NumericConstants.TEN;
    /**
     * 字体颜色
     */
    private BaseColor fontColor = BaseColor.GRAY;
    /**
     * 字体样式
     */
    private int fontStyle = Font.NORMAL;
    /**
     * 是否设置页眉页脚
     */
    private boolean hasHeaderFooter = false;
    /**
     * 页眉文本
     */
    private String headerText;
    /**
     * 页眉对齐方式
     */
    private int headerAlignment = Element.ALIGN_LEFT;
    /**
     * 页眉横坐标位置
     */
    private float headerX = 0F;
    /**
     * 页眉纵坐标位置
     */
    private float headerY = 0F;
    /**
     * 页眉相对文档顶部的距离
     */
    private float headerOffsetTop = 0F;
    /**
     * 页眉相对文档左边的距离
     */
    private float headerOffsetLeft = 0F;
    /**
     * 页脚文本
     */
    private String footerText;
    /**
     * 页脚对齐方式
     */
    private int footerAlignment = Element.ALIGN_LEFT;
    /**
     * 页脚横坐标位置
     */
    private float footerX = 0F;
    /**
     * 页脚纵坐标位置
     */
    private float footerY = 0F;
    /**
     * 页脚相对文档底部的距离
     */
    private float footerOffsetBottom = 25F;
    /**
     * 页脚相对文档左边的距离
     */
    private float footerOffsetLeft = 0F;
    /**
     * 是否设置页码
     */
    private boolean hasPageNumber = false;
    /**
     * 页码对齐方式
     */
    private int pageNumberAlignment = Element.ALIGN_LEFT;
    /**
     * 当前页码格式
     */
    private String currentPageFormat = "第 %s 页 / 共 ";
    /**
     * 总页码格式
     */
    private String totalPageFormat = "%s 页";
    /**
     * 总页码方框宽度
     */
    private float totalPageWidth = 50F;
    /**
     * 总页码方框高度
     */
    private float totalPageHeight = 50F;
    /**
     * 水印文件
     */
    private File watermark;
    /**
     * 水印横坐标位置
     */
    private Float watermarkX;
    /**
     * 水印纵坐标位置
     */
    private Float watermarkY;
  }

}
