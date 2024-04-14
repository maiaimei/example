package cn.maiaimei.example.pdf;

import cn.maiaimei.example.FileUtils;
import cn.maiaimei.example.NumericConstants;
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
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommonPdfPageEvent extends PdfPageEventHelper {

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
  private Integer fontSize;

  /**
   * 字体颜色
   */
  private BaseColor fontColor;

  /**
   * 是否设置页眉页脚
   */
  private boolean hasHeaderFooter;

  /**
   * 是否设置页码
   */
  private boolean hasPageNumber;

  /**
   * 页眉
   */
  private String headerString;

  /**
   * 页眉对齐方式
   */
  private Integer headerAlignment;

  /**
   * 页眉 X坐标
   */
  private Float headerX;

  /**
   * 页眉 Y坐标
   */
  private Float headerY;

  /**
   * 页脚
   */
  private String footerString;

  /**
   * 页脚对齐方式
   */
  private Integer footerAlignment;

  /**
   * 页脚 X坐标
   */
  private Float footerX;

  /**
   * 页脚 Y坐标
   */
  private Float footerY;

  /**
   * 页脚模板
   */
  private PdfTemplate footerTemplate;

  /**
   * 水印文件
   */
  private File watermark;

  /**
   * 水印文件 X坐标
   */
  private Float watermarkX;

  /**
   * 水印文件 Y坐标
   */
  private Float watermarkY;

  private CommonPdfPageEvent() {
  }

  public static CommonPdfPageEvent getInstance(Property property) {
    final CommonPdfPageEvent event = new CommonPdfPageEvent();
    event.baseFont = property.baseFont;
    event.fontDetail = property.fontDetail;
    event.fontSize = property.fontSize;
    event.fontColor = property.fontColor;
    event.hasHeaderFooter = property.hasHeaderFooter;
    event.hasPageNumber = property.hasPageNumber;
    event.headerString = property.headerString;
    event.headerAlignment = property.headerAlignment;
    event.headerX = property.headerX;
    event.headerY = property.headerY;
    event.footerString = property.footerString;
    event.footerAlignment = property.footerAlignment;
    event.footerX = property.footerX;
    event.footerY = property.footerY;
    return event;
  }

  /**
   * 文档打开时，创建模板
   */
  @Override
  public void onOpenDocument(PdfWriter writer, Document document) {
    if (hasHeaderFooter || hasPageNumber) {
      if (baseFont == null) {
        try {
          baseFont = BaseFont.createFont();
        } catch (DocumentException | IOException e) {
          throw new RuntimeException(e);
        }
      }

      if (fontDetail == null) {
        fontDetail = new Font(baseFont, fontSize, Font.NORMAL);
        fontDetail.setColor(fontColor);
      }
    }
    if (hasPageNumber) {
      // 共 页 的矩形的长宽高
      footerTemplate = writer.getDirectContent().createTemplate(50, 50);
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
    if (hasPageNumber) {
      // 将模板替换成实际的 Y 值，兼容各种文档size
      footerTemplate.beginText();
      // 设置模版的字体、颜色
      footerTemplate.setFontAndSize(baseFont, fontSize);
      footerTemplate.setColorFill(fontColor);
      String foot2 = " " + (writer.getPageNumber()) + " 页";
      // 渲染模版
      footerTemplate.showText(foot2);
      footerTemplate.endText();
      footerTemplate.closePath();
    }
  }

  /**
   * 添加页眉页脚
   *
   * @param writer   writer
   * @param document document
   */
  private void addHeaderFooter(PdfWriter writer, Document document) {
    if (hasHeaderFooter) {
      // 写入页眉
      addHeader(writer, document);
      // 写入页脚
      addFooter(writer, document);
    }
    if (hasPageNumber) {
      // 添加页码
      addPageNumber(writer, document);
    }
  }

  /**
   * 写入页眉
   *
   * @param writer   writer
   * @param document document
   */
  private void addHeader(PdfWriter writer, Document document) {
    if (Objects.isNull(headerAlignment)) {
      headerAlignment = Element.ALIGN_LEFT;
    }
    if (Objects.isNull(headerX)) {
      headerX = document.left();
    }
    if (Objects.isNull(headerY)) {
      headerY = document.top() + NumericConstants.TWENTY;
    }
    ColumnText.showTextAligned(writer.getDirectContent(), headerAlignment,
        new Phrase(headerString, fontDetail), headerX, headerY, NumericConstants.ZERO);
  }

  /**
   * 写入页脚
   *
   * @param writer   writer
   * @param document document
   */
  private void addFooter(PdfWriter writer, Document document) {
    if (Objects.isNull(footerAlignment)) {
      footerAlignment = Element.ALIGN_LEFT;
    }
    if (Objects.isNull(footerX)) {
      footerX = document.left();
    }
    if (Objects.isNull(footerY)) {
      footerY = document.bottom() - NumericConstants.TWENTY;
    }
    ColumnText.showTextAligned(writer.getDirectContent(), footerAlignment,
        new Phrase(footerString, fontDetail), footerX, footerY, NumericConstants.ZERO);
  }

  /**
   * 写入页码
   *
   * @param writer   writer
   * @param document document
   */
  private void addPageNumber(PdfWriter writer, Document document) {
    String foot1 = "第 " + writer.getPageNumber() + " 页 / 共";
    Phrase footer = new Phrase(foot1, fontDetail);

    /**
     * 计算前半部分的foot1的长度，后面好定位最后一部分的'Y页'这俩字的x轴坐标，字体长度也要计算进去 = len
     */
    float len = baseFont.getWidthPoint(foot1, fontSize);

    /**
     * 拿到当前的PdfContentByte
     */
    PdfContentByte cb = writer.getDirectContent();

    /**
     * 写入页脚1，x轴就是(右margin+左margin + right() -left()- len)/2.0F 再给偏移20F适合人类视觉感受，否则肉眼看上去就太偏左了
     * y轴就是底边界-20,否则就贴边重叠到数据体里了就不是页脚了；注意Y轴是从下往上累加的，最上方的Top值是大于Bottom好几百开外的
     */
    ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, footer,
        (document.rightMargin() + document.right() + document.leftMargin() - document.left()
            - len)
            / 2.0F + 20F,
        document.bottom() - 25, 0);

    /**
     * 写入页脚2的模板（就是页脚的Y页这俩字）添加到文档中，计算模板的和Y轴,X=(右边界-左边界 - 前半部分的len值)/2.0F + len ， y 轴和之前的保持一致，底边界-20
     */
    cb.addTemplate(footerTemplate,
        (document.rightMargin() + document.right() + document.leftMargin() - document.left())
            / 2.0F
            + 20F,
        document.bottom() - 25);
  }

  /**
   * 写入水印
   *
   * @param writer writer
   */
  private void addWatermark(PdfWriter writer) {
    if (Objects.nonNull(watermark)) {
      try {
        Image image = Image.getInstance(FileUtils.getBytes(watermark));
        PdfContentByte content = writer.getDirectContentUnder();
        content.beginText();
        image.setAbsolutePosition(watermarkX, watermarkY);
        content.addImage(image);
        content.endText();
      } catch (DocumentException | IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  @Builder
  static class Property {

    private BaseFont baseFont;
    private Font fontDetail;
    private Integer fontSize;
    private BaseColor fontColor;
    private boolean hasHeaderFooter;
    private boolean hasPageNumber;
    private String headerString;
    private Integer headerAlignment;
    private Float headerX;
    private Float headerY;
    private String footerString;
    private Integer footerAlignment;
    private Float footerX;
    private Float footerY;
  }

}
