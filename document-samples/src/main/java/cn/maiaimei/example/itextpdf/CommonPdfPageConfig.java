package cn.maiaimei.example.itextpdf;

import cn.maiaimei.commons.lang.constants.NumericConstants;
import cn.maiaimei.commons.lang.constants.StringConstants;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import java.io.File;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommonPdfPageConfig {

  private String charsetName;
  private Rectangle pageSize;
  private String fontName;
  private String fontEncoding;

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
  private boolean hasHeaderFooter = false;
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
  private boolean hasPageNumber = true;
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
