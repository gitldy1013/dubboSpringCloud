package org.cmcc.service.sftp.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lumma on 2020/9/14.
 */
@Slf4j
public class TxtFileUtil {

    /**
     * 将list按行写入到txt文件中
     *
     * @param strings
     * @param path
     * @param fileName
     * @throws Exception
     */
    public static void writeFileContext(List<String> strings, String path, String fileName) {
        BufferedWriter writer = null;
        FileOutputStream writerStream = null;
        try {
            File fileDir = new File(path);
            fileDir.mkdirs();
            // 如果没有文件就创建
            File file = new File(path + fileName);
            if (!file.isFile()) {
                file.createNewFile();
            }
            writerStream = new FileOutputStream(file, true);
            writer = new BufferedWriter(new OutputStreamWriter(writerStream, StandardCharsets.UTF_8));
            for (String l : strings) {
                writer.write(l + "\r\n");
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
                if (writerStream != null) {
                    writerStream.close();
                }
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
    }

    /**
     * 获取txt文件内容并按行放入list中
     */
    public static List<String> getFileContext(String path) {
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        List<String> list = new ArrayList<String>();
        try {
            fileReader = new FileReader(path);
            bufferedReader = new BufferedReader(fileReader);
            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                if (str.trim().length() > 2) {
                    list.add(str);
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        } finally {
            try {
                if (fileReader != null) {
                    fileReader.close();
                }
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}
