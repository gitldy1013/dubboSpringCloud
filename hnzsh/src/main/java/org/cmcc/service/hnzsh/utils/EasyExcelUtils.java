package org.cmcc.service.hnzsh.utils;

import com.alibaba.excel.EasyExcel;
import org.cmcc.service.hnzsh.pojo.DemoData;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EasyExcelUtils {

    public static void templateWrite() {
        String templateFileName = "D:/demo" + File.separator + "demo.xlsx";
        String fileName = "D:/demo" + File.separator + System.currentTimeMillis() + ".xlsx";
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        EasyExcel.write(fileName, DemoData.class).withTemplate(templateFileName).sheet().doWrite(data());
    }

    private static List<DemoData> data() {
        List<DemoData> list = new ArrayList<DemoData>();
        for (int i = 0; i < 10; i++) {
            DemoData data = new DemoData();
            data.setString("字符串" + i);
            data.setDate(new Date());
            data.setDoubleData(0.56);
            list.add(data);
        }
        return list;
    }

    public static void main(String[] args) {
        templateWrite();
    }
}
