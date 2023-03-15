package cn.maiaimei.example;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.io.RandomAccessBuffer;
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@Slf4j
public class PdfBoxTest {

    String dir = "E:\\code\\swift-mt-spring-boot-workspace\\docs\\";
    String tmp = dir.concat("tmp\\");
    String pdf1 = "SWIFT_MT9XX_Bank Statement.pdf";
    String pdf2 = "swift_mt798_mig_v5.2.4_202204_final_0.pdf";

    /**
     * 通过byte数组读取远程pdf文本
     */
    @Test
    void readRemotePdfTextByBytes() {
        // mock byte array
        final byte[] bytes = mockByteArray(dir.concat(pdf1));
        try (final ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes)) {
            final PDFParser parser = new PDFParser(new RandomAccessBuffer(inputStream));
            parser.parse();
            final PDDocument document = parser.getPDDocument();
            final PDFTextStripper stripper = new PDFTextStripper();
            final String text = stripper.getText(document);
            System.out.println("--------------------------------------------------");
            System.out.println(text);
            System.out.println("--------------------------------------------------");
        } catch (Exception ex) {
            log.error("通过byte数组读取远程pdf文本发生异常", ex);
        }
    }

    private byte[] mockByteArray(String pathname) {
        File file = new File(pathname);
        int length = (int) file.length();
        byte[] bytes = new byte[length];
        // try-with-resources
        try (FileInputStream inputStream = new FileInputStream(file)) {
            inputStream.read(bytes);
        } catch (Exception ex) {
            log.error("mockByteArray发生异常", ex);
        } finally {
            // no need to add code to close InputStream (不需要添加关闭InputStream的代码)
            // it's close method will be internally called (它的关闭方法将被内部调用)
        }
        return bytes;
    }

    /**
     * 读取本地pdf文本
     */
    @Test
    void readLocalPdfText() {
        final File file = new File(dir.concat(pdf1));
        // 加载现有的PDF文档
        try (PDDocument document = PDDocument.load(file)) {
            // 新建一个PDF文本剥离器
            final PDFTextStripper stripper = new PDFTextStripper();
            // 从PDF文档对象中剥离文本
            final String text = stripper.getText(document);
            System.out.println("--------------------------------------------------");
            System.out.println(text);
            System.out.println("--------------------------------------------------");
        } catch (Exception ex) {
            log.error("读取本地pdf文本发生异常", ex);
        }
    }

    /**
     * 通过指定页码读取pdf文本
     */
    @Test
    void readPdfTextBySpecifyPageNumber() {
        final File file = new File(dir.concat(pdf2));
        try (PDDocument document = PDDocument.load(file)) {
            final Splitter splitter = new Splitter();
            splitter.setStartPage(349);
            splitter.setEndPage(358);
            final List<PDDocument> documents = splitter.split(document);
            final Iterator<PDDocument> iterator = documents.iterator();
            final PDFTextStripper stripper = new PDFTextStripper();
            while (iterator.hasNext()) {
                final PDDocument doc = iterator.next();
                final String text = stripper.getText(doc);
                System.out.println("--------------------------------------------------");
                System.out.println(text);
                System.out.println("--------------------------------------------------");
                doc.close();
            }
        } catch (Exception ex) {
            log.error("通过指定页码读取pdf文本发生异常", ex);
        }
    }

    /**
     * 创建pdf
     */
    @Test
    void createPdf01() throws IOException {
        // Creating PDF document object 
        PDDocument document = new PDDocument();
        for (int i = 0; i < 10; i++) {
            // Creating a blank page 
            PDPage blankPage = new PDPage();
            // Adding the blank page to the document
            document.addPage(blankPage);
        }
        //Saving the document
        String pathname = generatePdfName();
        document.save(pathname);
        //Closing the document
        document.close();
    }

    /**
     * 创建pdf
     */
    @Test
    void createPdf02() {
        String pathname = generatePdfName();
        // 创建空文档
        try (final PDDocument document = new PDDocument()) {
            final PDType1Font font = PDType1Font.HELVETICA;

            // 创建空白页
            final PDPage page = new PDPage();
            // 将页面添加到文档
            document.addPage(page);

            // 创建内容流
            final PDPageContentStream contentStream = new PDPageContentStream(document, page);
            // 开始文本操作
            contentStream.beginText();
            // 设置文本的字体和大小
            contentStream.setFont(font, 16);
            // 在偏移量 (x,y) 处开始新行
            contentStream.newLineAtOffset(50, 700);
            // 在指定位置显示文本
            contentStream.showText("This is only for test!!!");
            // 停止文本操作
            contentStream.endText();
            // 关闭内容流
            contentStream.close();

            // 保存文档
            document.save(pathname);
        } catch (Exception ex) {
            log.error("创建pdf发生异常", ex);
        } finally {
            // no need to add code to close InputStream (不需要添加关闭InputStream的代码)
            // it's close method will be internally called (它的关闭方法将被内部调用)  
            // 关闭文档
            // document.close();
        }
    }

    /**
     * 修改现有的PDF文档
     */
    @Test
    void modifyExistingPdf() throws IOException {
        String pathname = tmp.concat("7f5e9d2368734bcf8b1a946291d34662.pdf");
        // Loading an existing document 
        File file = new File(pathname);
        PDDocument document = PDDocument.load(file);
        // Adding a blank page to the document 
        document.addPage(new PDPage());
        //Saving the document 
        document.save(pathname);
        //Closing the document  
        document.close();
    }

    /**
     * 从现有文档中删除页面
     */
    @Test
    void removingPagesFromExistingPdf() throws IOException {
        String pathname = tmp.concat("7f5e9d2368734bcf8b1a946291d34662.pdf");
        // Loading an existing document 
        File file = new File(pathname);
        PDDocument document = PDDocument.load(file);
        // Listing the number of existing pages
        int noOfPages = document.getNumberOfPages();
        System.out.print("the number of existing pages is " + noOfPages);
        // Removing the pages, start at index 0
        document.removePage(1);
        //Saving the document 
        document.save(pathname);
        //Closing the document  
        document.close();
    }

    /**
     * 合并pdf
     */
    @Test
    void mergePdf() {
        try (final PDDocument document = new PDDocument()) {

        } catch (Exception ex) {
            log.error("合并pdf发生异常", ex);
        }
    }

    /**
     * pdf 转 tiff
     */
    @Test
    void pdfToTiff() {
        try (final PDDocument document = new PDDocument()) {

        } catch (Exception ex) {
            log.error("pdf 转 tiff 发生异常", ex);
        }
    }

    String generatePdfName() {
        return tmp.concat(UUID.randomUUID().toString().replaceAll("-", "").concat(".pdf"));
    }

}
