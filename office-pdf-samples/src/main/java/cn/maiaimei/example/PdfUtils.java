package cn.maiaimei.example;

import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * TODO: refactor PdfUtils
 */
public class PdfUtils {
    private static final Logger log = LoggerFactory.getLogger(PdfUtils.class);

    public static File mergeFileByPaths(List<String> sourcePaths, String targetPath) {
        List<File> files = new ArrayList<>();
        for (String sourcePath : sourcePaths) {
            files.add(new File(sourcePath));
        }

        return mergeFile(files, targetPath);
    }

    public static File mergeFile(List<File> files, String targetPath) {
        PDFMergerUtility mergePdf = new PDFMergerUtility();

        long totalSize = 0;
        try {
            for (File f : files) {
                if (f.exists() && f.isFile()) {
                    totalSize += f.length();
                    // 循环添加要合并的pdf
                    mergePdf.addSource(f);
                }
            }
            // 设置合并生成pdf文件名称
            mergePdf.setDestinationFileName(targetPath);
            // 合并pdf
            mergePdf.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());
        } catch (Exception ex) {
            log.error("pdf merge error", ex);
        }

        File file = new File(targetPath);
        if (log.isDebugEnabled()) {
            log.debug("pdf count is: {}, total size is: {}, after merge total size is: {}", files.size(), totalSize, file.length());
        }
        return file;
    }

    public static void main(String[] args) {
        String root = "C:\\Users\\lenovo\\Desktop\\pdf\\";
        List<String> sourcePaths = Arrays.asList(
                root + "mybatis.pdf",
                root + "swagger.pdf"
        );
        String targetPath = root + System.currentTimeMillis() + ".pdf";
        mergeFileByPaths(sourcePaths, targetPath);
    }
}
