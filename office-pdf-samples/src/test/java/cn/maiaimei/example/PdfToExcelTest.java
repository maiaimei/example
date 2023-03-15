package cn.maiaimei.example;

import com.spire.pdf.FileFormat;
import com.spire.pdf.PdfDocument;
import lombok.SneakyThrows;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class PdfToExcelTest {

    String dir = "E:\\code\\swift-mt-spring-boot-workspace\\docs\\";
    String tmp = dir.concat("tmp\\");
    String pdf = "swift_mt798_mig_v5.2.4_202204_final_0.pdf";

    @Test
    void testSplitPdf() {
        splitPdf(dir.concat(pdf), 348, 359, 12);
    }

    /**
     * 拆分pdf
     * https://zhuanlan.zhihu.com/p/528044379
     *
     * @param pathname             需要拆分pdf所在路径
     * @param startPage            起始页码
     * @param endPage              结束页码
     * @param pageCountPerDocument 拆分后每个pdf的页数
     */
    @SneakyThrows
    private void splitPdf(String pathname, int startPage, int endPage, int pageCountPerDocument) {
        File file = new File(pathname);
        PDDocument pdDocument = PDDocument.load(file);
        // 拆分后的文件集合保存目录
        String saveTo = pathname.substring(0, pathname.lastIndexOf("."));
        File save = new File(saveTo);
        if (!save.exists()) {
            save.mkdirs();
        }
        PDPageTree pages = pdDocument.getPages();
        System.out.println("总页数：" + pages.getCount());
        int splitCount = 1;
        List<PDPage> list = new ArrayList<>();
        for (int i = 0; i < pages.getCount(); i++) {
            if (i <= startPage) {
                continue;
            }
            if (i > endPage) {
                break;
            }
            final PDPage page = pages.get(i);
            list.add(page);
            if (list.size() == pageCountPerDocument) {
                PDDocument split = new PDDocument();
                split.addPage(list.remove(0));
                // 每pageCountPerDocument页作为新的pdf输出，以数字序号命名
                split.save(new File(saveTo + "\\" + (splitCount++) + ".pdf"));
            }
        }
        // 不满pageCountPerDocument页的尾数处理
        if (list.size() > 0) {
            PDDocument split = new PDDocument();
            for (PDPage pdPage : list) {
                split.addPage(pdPage);
            }
            split.save(new File(saveTo + "\\" + (splitCount) + ".pdf"));
        }
    }

    /**
     * 合并excel
     * https://zhuanlan.zhihu.com/p/528044379
     */
    @SneakyThrows
    private void mergeExcel(String pathname) {
        Workbook workbook = WorkbookFactory.create(true);
        Sheet sheet = workbook.createSheet("sheet1");
        // excel文件集合
        long count = Files.list(Paths.get(pathname)).filter(path -> path.toString().endsWith(".xlsx")).count();
        System.out.println("总数：" + count);

        int rowNumber = 0;
        // excel文件以数字序列命名的
        for (long l = 1; l <= count; l++) {
            String fileName = pathname + l + ".xlsx";
            System.out.println("处理：" + fileName);
            Workbook temp = WorkbookFactory.create(new File(fileName));
            Sheet tempSheet = temp.getSheetAt(0);
            for (Row cells : tempSheet) {
                Row newRow = sheet.createRow(rowNumber++);
                // 复制行格式
                newRow.setHeight(cells.getHeight());
                newRow.setHeightInPoints(cells.getHeightInPoints());
                newRow.setRowStyle(cells.getRowStyle());
                newRow.setZeroHeight(cells.getZeroHeight());
                int cellNumber = 0;
                for (Cell cell : cells) {
                    // 复制列格式
                    sheet.setColumnWidth(cellNumber, tempSheet.getColumnWidth(cellNumber));
                    // 复制数据
                    Cell newCell = newRow.createCell(cellNumber);
                    CellType cellType = CellType.valueOf(cell.getCellType().name());
                    switch (cellType) {
                        case STRING:
                            newCell.setCellValue(cell.getStringCellValue());
                            break;
                        case BLANK:
                            newCell.setBlank();
                            break;
                        case NUMERIC:
                            newCell.setCellValue(cell.getNumericCellValue());
                            break;
                        default:

                    }
                    // 复制单元格格式
                    newCell.setCellComment(cell.getCellComment());
                    CellStyle cellStyle = workbook.createCellStyle();
                    cellStyle.cloneStyleFrom(cell.getCellStyle());
                    newCell.setCellStyle(cellStyle);
                    newCell.setHyperlink(cell.getHyperlink());
                    cellNumber++;
                }
            }
            // 合并单元格设置，合并区域的坐标需要根据上个文件的行数调整
            int lastRowNum = sheet.getLastRowNum() - tempSheet.getLastRowNum();
            for (CellRangeAddress mergedRegion : tempSheet.getMergedRegions()) {
                int firstRow = mergedRegion.getFirstRow();
                int lastRow = mergedRegion.getLastRow();
                mergedRegion.setFirstRow(firstRow + lastRowNum);
                mergedRegion.setLastRow(lastRow + lastRowNum);
                sheet.addMergedRegion(mergedRegion);
            }
            temp.close();
        }
        workbook.write(new FileOutputStream(pathname + "合并.xlsx"));
        workbook.close();
    }

    /**
     * https://www.e-iceblue.cn/pdf_java_conversion/convert-pdf-to-excel-in-java.html
     */
    @Test
    void testPDFtoExcel() {
        String rootPathname = "E:\\code\\swift-mt-spring-boot-workspace\\docs\\swift_mt798_mig_v5.2.4_202204_final_0";
        final File rootFile = new File(rootPathname);
        final File[] files = rootFile.listFiles();
        for (File file : files) {
            // 创建PdfDocument实例
            PdfDocument pdf = new PdfDocument();
            // 加载PDF文档
            pdf.loadFromFile(file.getAbsolutePath());
            // 保存为Excel
            pdf.saveToFile(rootPathname.concat(file.getName().concat(".xlsx")), FileFormat.XLSX);
        }
    }

}
