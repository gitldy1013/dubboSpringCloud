package org.cmcc.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.*;

@Slf4j
public class ResourcesFileUtils {

    public static File getResourcesFile(InputStream inputStream, File file) {
        OutputStream os = null;
        try {
            os = new FileOutputStream(file);
            int len = 0;
            byte[] buffer = new byte[8192];
            while ((len = inputStream.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }
            return file;
        } catch (IOException e) {
            log.error("异常:" + e);
        } finally {
            try {
                if (os != null) {
                    os.flush();
                    os.close();
                }
            } catch (IOException ex) {
                log.error(ex.getMessage());
            }
        }
        return null;
    }
}
