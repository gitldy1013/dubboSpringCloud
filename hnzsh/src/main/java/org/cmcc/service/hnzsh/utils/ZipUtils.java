package org.cmcc.service.hnzsh.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtils {

    /**
     * 压缩指定的文件
     *
     * @param file    目标文件
     * @param zipFile 生成的压缩文件
     * @throws IOException
     */
    public static void packet(Path file, Path zipFile) throws IOException {
        OutputStream outputStream = Files.newOutputStream(zipFile, StandardOpenOption.CREATE_NEW);
        ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
        try {
            /*for (Path file : files) {
                if (Files.isDirectory(file)) {
                    continue;
                }*/
            try (InputStream inputStream = Files.newInputStream(file)) {
                // 创建一个压缩项，指定名称
                ZipEntry zipEntry = new ZipEntry(file.getFileName().toString());
                // 添加到压缩流
                zipOutputStream.putNextEntry(zipEntry);
                // 写入数据
                int len = 0;
                byte[] buffer = new byte[1024 * 10];
                while ((len = inputStream.read(buffer)) > 0) {
                    zipOutputStream.write(buffer, 0, len);
                }
                zipOutputStream.flush();
            }
            //   }
            // 完成所有压缩项的添加
            zipOutputStream.closeEntry();
        } finally {
            zipOutputStream.close();
            outputStream.close();
        }
    }

}
