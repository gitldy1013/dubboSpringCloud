package org.cmcc.service.common.uitl;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @ClassName CommonUtil
 * @Description 通用工具类
 * @Author cmcc
 * @Date 2019/1/8
 * Version  1.0
 */
public class CommonUtil {

    /**
     * 获取具体的异常信息
     *
     * @param ex
     * @return
     */
    public static String getExceptionDetail(Exception ex) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        try {
            ex.printStackTrace(pw);
            return sw.toString();
        } finally {
            pw.close();
        }
    }

}
