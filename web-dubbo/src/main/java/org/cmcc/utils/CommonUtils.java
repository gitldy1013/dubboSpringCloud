package org.cmcc.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;

@Slf4j
public class CommonUtils {
    /**
     * 创建多层目录结构文件
     */
    public static File createFileWithMultilevelDirectory(String[] directories, String fileName, String rootName) {
        //调用上面的创建多级目录的方法
        File filePath = createMultilevelDirectory(directories, rootName);
        File file = new File(filePath, fileName);
        boolean b = false;
        try {
            b = file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            log.info("创建文件目录异常", e);
        }
        assert b;
        return file;
    }

    //创建多级目录
    public static File createMultilevelDirectory(String[] directories, String rootPath) {
        if (directories.length == 0) {
            return null;
        }
        File root = new File(rootPath);
        for (String s : directories) {
            File directory = new File(root, s);
            boolean mkdir = directory.mkdir();
            assert mkdir;
            root = directory;
        }
        return root;
    }

    /**
     * 获取某个目录下所有文件名
     *
     * @return
     */
    public static String[] getFilesName(String path) {
        File file = new File(path);
        File[] tempList = file.listFiles();
        assert tempList != null;
        String[] filesName = new String[tempList.length];
        for (int i = 0; i < tempList.length; i++) {
            if (tempList[i].isFile()) {
                filesName[i] = tempList[i].getAbsolutePath();
            }
            if (tempList[i].isDirectory()) {
                getFilesName(tempList[i].getPath());
            }
        }
        return filesName;
    }
}
