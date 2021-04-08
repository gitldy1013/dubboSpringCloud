package org.cmcc.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 导出excel工具类
 */
@Slf4j
public class ExportExcelUtil {
    private List<String> headerList = new ArrayList<>();
    private List<String> keyList = new ArrayList<>();
    private SXSSFWorkbook wb;
    private SXSSFSheet sheet;
    private Map<String, CellStyle> styles;// 单元格样式

    /**
     * 创建表格样式
     *
     * @param wb 工作薄对象
     * @return 样式列表
     */
    private static Map<String, CellStyle> createStyles(Workbook wb) {
        Map<String, CellStyle> styles = new HashMap<String, CellStyle>();

        CellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        Font titleFont = wb.createFont();
        titleFont.setFontName("Arial");
        titleFont.setFontHeightInPoints((short) 16);
        titleFont.setBold(true);
        style.setFont(titleFont);
        styles.put("title", style);

        style = wb.createCellStyle();
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        Font dataFont = wb.createFont();
        dataFont.setFontName("Arial");
        dataFont.setFontHeightInPoints((short) 10);
        style.setFont(dataFont);
        styles.put("data", style);

        style = wb.createCellStyle();
        style.cloneStyleFrom(styles.get("data"));
        style.setAlignment(HorizontalAlignment.LEFT);
        styles.put("data1", style);

        style = wb.createCellStyle();
        style.cloneStyleFrom(styles.get("data"));
        style.setAlignment(HorizontalAlignment.CENTER);
        styles.put("data2", style);

        style = wb.createCellStyle();
        style.cloneStyleFrom(styles.get("data"));
        style.setAlignment(HorizontalAlignment.RIGHT);
        styles.put("data3", style);

        style = wb.createCellStyle();
        style.cloneStyleFrom(styles.get("data"));
        // style.setWrapText(true);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Font headerFont = wb.createFont();
        headerFont.setFontName("Arial");
        headerFont.setFontHeightInPoints((short) 10);
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(headerFont);
        styles.put("header", style);

        return styles;
    }

    /**
     * @param title     导出的excel标题
     * @param sheetName sheet页名字，不传默认sheet1
     * @param headerMap 列映射关系 key对应data中需要生成的字段代码，value 列显示的列名
     * @param data      需要生成excel的数据
     */
    public void exportData(String title, String sheetName, LinkedHashMap<String, String> headerMap, List<Map<String, Object>> data,
                           OutputStream out) {
        long start = System.currentTimeMillis();
        getHeader(headerMap);
        int rownum = initWorkbook(title, sheetName);
        setDataList(rownum, headerMap, data);
        try {
            wb.write(out);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            try {
                wb.close();
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
        log.info("导出花费时间：" + (System.currentTimeMillis() - start) + "ms");
    }

    /**
     * @param title     导出的excel标题 必传
     * @param sheetName sheet页名字，不传默认sheet1
     * @param headerMap 列映射关系 key对应data中需要生成的字段代码，value 列显示的列名
     * @param data      需要生成excel的数据
     */
    public void exportData(String title, String sheetName, LinkedHashMap<String, String> headerMap, List<Map<String, Object>> data,
                           String filePath) {
        File file = new File(filePath);
        file.getParentFile().mkdirs();
        OutputStream out = null;
        try {
            out = new FileOutputStream(file);
            this.exportData(title, sheetName, headerMap, data, out);
        } catch (FileNotFoundException e) {
            log.error("io异常", e);
        } finally {
            try {
                out.flush();
                out.close();
            } catch (IOException e) {
                log.error(e.getMessage());
            }

        }
    }

    /**
     * 向工作簿插入数据
     */
    private void setDataList(int rownum, LinkedHashMap<String, String> headerMap, List<Map<String, Object>> dataList) {
        StringBuilder sb = new StringBuilder();
        String key;
        Object val;
        Row row;
        for (Map<String, Object> data : dataList) {
            row = this.sheet.createRow(rownum++);
            int colunm = 0;
            for (int i = 0; i < keyList.size(); i++) {
                val = data.get(keyList.get(i));
                Cell cell = row.createCell(colunm);
                CellStyle style = styles.get("data");
                try {
                    if (val == null) {
                        cell.setCellValue("");
                    } else if (val instanceof String) {
                        cell.setCellValue((String) val);
                    } else if (val instanceof Integer) {
                        cell.setCellValue((Integer) val);
                    } else if (val instanceof Long) {
                        cell.setCellValue((Long) val);
                    } else if (val instanceof Double) {
                        cell.setCellValue((Double) val);
                    } else if (val instanceof Float) {
                        cell.setCellValue((Float) val);
                    } else if (val instanceof Date) {
                        DataFormat format = wb.createDataFormat();
                        style.setDataFormat(format.getFormat("yyyy-MM-dd"));
                        cell.setCellValue((Date) val);
                    } else {
                        cell.setCellValue(String.valueOf(val));
                    }
                } catch (Exception ex) {
                    log.debug("设值 [" + row.getRowNum() + "," + colunm + "] error: " + ex.toString());
                    assert val != null;
                    cell.setCellValue(val.toString());
                }
                cell.setCellStyle(style);
                sb.append(val).append(", ");
                colunm++;
            }
            sb.append("\n ");
        }
        log.debug("写单元格成功: \n " + sb.toString());
    }

    /**
     * 初始化工作簿 返回占用行数
     */
    private int initWorkbook(String title, String sheetName) {
        // 缓存500条
        this.wb = new SXSSFWorkbook(500);
        sheetName = StringUtils.isBlank(sheetName) ? "sheet1" : sheetName;
        this.sheet = wb.getSheet(sheetName);
        if (sheet == null) {
            sheet = wb.createSheet(sheetName);
        }
        styles = createStyles(wb);
        int rownum = 0;
        // 生成标题
        if (StringUtils.isNotBlank(title)) {
            Row titleRow = sheet.createRow(rownum++);
            titleRow.setHeightInPoints(30);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellStyle(styles.get("title"));
            titleCell.setCellValue(title);
            sheet.addMergedRegion(new CellRangeAddress(titleRow.getRowNum(), titleRow.getRowNum(), titleRow.getRowNum(),
                    headerList.size() - 1));
        }
        // 生成列名
        Row headerRow = sheet.createRow(rownum++);
        // 列高
        headerRow.setHeightInPoints(16);
        for (int i = 0; i < headerList.size(); i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellStyle(styles.get("header"));
            String[] ss = StringUtils.split(headerList.get(i), "**", 2);
            if (ss.length == 2) {
                cell.setCellValue(ss[0]);
                Comment comment = sheet.createDrawingPatriarch()
                        .createCellComment(new XSSFClientAnchor(0, 0, 0, 0, (short) 3, 3, (short) 5, 6));
                comment.setString(new XSSFRichTextString(ss[1]));
                cell.setCellComment(comment);
            } else {
                cell.setCellValue(headerList.get(i));
            }
            sheet.untrackColumnForAutoSizing(i);
        }
        for (int i = 0; i < headerList.size(); i++) {
            int colWidth = sheet.getColumnWidth(i) * 2;
            sheet.setColumnWidth(i, Math.max(colWidth, 3000));
        }
        log.debug("--------------表格标题列名初始化ok-------------------");
        return rownum;
    }

    /**
     * 获取列名
     */
    private void getHeader(LinkedHashMap<String, String> headerMap) {
        for (Entry<String, String> entry : headerMap.entrySet()) {
            headerList.add(entry.getValue());
            keyList.add(entry.getKey());
        }
    }
}
