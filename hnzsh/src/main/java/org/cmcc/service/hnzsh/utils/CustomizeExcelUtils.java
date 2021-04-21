package org.cmcc.service.hnzsh.utils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.util.ReflectionUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @author zhaolei
 * @date 2021-01-26 14:11
 */
@Slf4j
public class CustomizeExcelUtils {
    /**
     * @param templateFilePath  excel模板路径
     * @param targetFilePath     excel目标文件路径
     * @param list        数据实体类集合
     * @param c      实体类类型
     * @param fieldArr    需要写入excel的字段名称
     *
    * */
    public static void exportExcel(String templateFilePath, String targetFilePath, List list, Class c, String[] fieldArr,String statDt) {
        FileOutputStream out = null;
        FileInputStream in = null;
        try {
            // 模板路径 如 D:\home\app\模板.xlsx
            File file = new File(templateFilePath);
            if (file.exists()) {

                in = new FileInputStream(file);
                //读取excel模板
                XSSFWorkbook wb = new XSSFWorkbook(in);

                // 创建单元格样式
                XSSFCellStyle style = wb.createCellStyle();
                //创建单元格字体
                XSSFFont font = wb.createFont();
                //style.setFillForegroundColor((short)4); //设置要添加表格背景颜色
                //style.setFillPattern(FillPatternType.SOLID_FOREGROUND); //solid 填充
                style.setAlignment(HorizontalAlignment.CENTER); //文字水平居中
                style.setVerticalAlignment(VerticalAlignment.CENTER);//文字垂直居中
                style.setBorderBottom(BorderStyle.MEDIUM); //底边框加黑
                style.setBorderLeft(BorderStyle.MEDIUM);  //左边框加黑
                style.setBorderRight(BorderStyle.MEDIUM); // 有边框加黑
                style.setBorderTop(BorderStyle.MEDIUM); //上边框加黑
                font.setBold(true);                     //设置字体加粗
                style.setFont(font);

                //读取了模板内所有sheet内容
                XSSFSheet sheet = wb.getSheetAt(0);
                //如果这行没有了，整个公式都不会有自动计算的效果的
                sheet.setForceFormulaRecalculation(true);
                //合并单元格
            /*sheet.addMergedRegion(new CellRangeAddress(21, 21, 1, 2));
            sheet.getRow(21).getCell(1).setCellValue("合计");*/

                //设置金额的单元格数据类型
                XSSFDataFormat dataFormat = wb.createDataFormat();
                XSSFCellStyle dataCellStyle = wb.createCellStyle();
                dataCellStyle.setDataFormat(dataFormat.getFormat("#,##0.00"));
                dataCellStyle.setAlignment(HorizontalAlignment.CENTER); //文字水平居中
                dataCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);//文字垂直居中
                dataCellStyle.setBorderBottom(BorderStyle.MEDIUM); //底边框加黑
                dataCellStyle.setBorderLeft(BorderStyle.MEDIUM);  //左边框加黑
                dataCellStyle.setBorderRight(BorderStyle.MEDIUM); // 有边框加黑
                dataCellStyle.setBorderTop(BorderStyle.MEDIUM); //上边框加黑
                font.setBold(true);                     //设置字体加粗
                dataCellStyle.setFont(font);

                //设置第四行六列的单元格为当前日期
                XSSFRow dateRow = sheet.getRow(3);
                XSSFCell dateCell = dateRow.getCell(6);
                String statDate = "统计日期：" + TimeUtils.getPatternDateStr(statDt,"yyyyMMdd","yyyy年MM月dd日");
                dateCell.setCellValue(statDate);

                if (list != null && list.size() > 0) {
                    BigDecimal paymentAmtSum = new BigDecimal("0.00");
                    BigDecimal adjustPaymentAmtSum = new BigDecimal("0.00");
                    BigDecimal realPaymentAmtSum = new BigDecimal("0.00");
                    for (int i = 0; i < list.size(); i++) {

                        XSSFRow row = sheet.createRow(6 + i);
                        for (int j = 0; j < fieldArr.length; j++) {
                            Field field = c.getDeclaredField(fieldArr[j]);
                            // 开启私有字段的权限
                            ReflectionUtils.makeAccessible(field);
                            XSSFCell cell = row.createCell(j);
                            String s = field.getType().getName();
                            if ("java.math.BigDecimal".equals(s)) {
                                cell.setCellStyle(dataCellStyle);
                                String amt = field.get(list.get(i)).toString();
                                cell.setCellValue(Double.parseDouble(amt));
                                if("paymentAmt".equals(fieldArr[j])){
                                    paymentAmtSum = new BigDecimal(amt).add(paymentAmtSum);
                                    continue;
                                }else if("adjustPaymentAmt".equals(fieldArr[j])){
                                    adjustPaymentAmtSum = new BigDecimal(amt).add(adjustPaymentAmtSum);
                                    continue;
                                }else if("realPaymentAmt".equals(fieldArr[j])){
                                    realPaymentAmtSum = new BigDecimal(amt).add(realPaymentAmtSum);
                                    continue;
                                }

                            }


                            cell.setCellValue(field.get(list.get(i)).toString());
                            cell.setCellStyle(style);

                        }
                        if (i ==list.size()-1){
                            XSSFRow lastRow = sheet.createRow(6 + list.size());
                            XSSFCell cell0 = lastRow.createCell(0);
                            cell0.setCellValue("");
                            cell0.setCellStyle(style);

                            XSSFCell cell1 = lastRow.createCell(1);
                            cell1.setCellValue("");
                            cell1.setCellStyle(style);

                            XSSFCell cell2 = lastRow.createCell(2);
                            cell2.setCellValue("合计");
                            cell2.setCellStyle(style);

                            XSSFCell cell3 = lastRow.createCell(3);
                            cell3.setCellValue("");
                            cell3.setCellStyle(style);

                            XSSFCell cell4 = lastRow.createCell(4);
                            cell4.setCellValue(Double.parseDouble(paymentAmtSum.toString()));
                            cell4.setCellStyle(dataCellStyle);

                            XSSFCell cell5 = lastRow.createCell(5);
                            cell5.setCellStyle(dataCellStyle);
                            cell5.setCellValue(Double.parseDouble(adjustPaymentAmtSum.toString()));

                            XSSFCell cell6 = lastRow.createCell(6);
                            cell6.setCellValue(Double.parseDouble(realPaymentAmtSum.toString()));
                            cell6.setCellStyle(dataCellStyle);

                            XSSFCell cell7 =lastRow.createCell(7);
                            cell7.setCellValue("");
                            cell7.setCellStyle(style);
                            sheet.addMergedRegion(new CellRangeAddress(6+list.size(), 6+list.size(), 2, 3));


                        }
                    }


                }else {
                    log.info(statDate+"======没有查询到数据，推送空文件！==========");
                    XSSFRow row = sheet.getRow(10);
                    XSSFCell cell = row.getCell(2);
                    cell.setCellValue("合计");
                    sheet.addMergedRegion(new CellRangeAddress(10, 10, 2, 3));

                }
                //修改模板内容导出新模板
                out = new FileOutputStream(targetFilePath);
                log.info("生成excel文件-------------");
                wb.write(out);


            } else {
                log.info("模板文件不存在！！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    log.error(e.toString());
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    log.error(e.toString());
                }
            }
        }


    }



}
