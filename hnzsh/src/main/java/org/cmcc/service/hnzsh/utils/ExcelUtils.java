package org.cmcc.service.hnzsh.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.cmcc.service.hnzsh.annotation.ExcelExportField;
import lombok.Data;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @Description 数据导出excel工具
 */
@SuppressWarnings("all")
public class ExcelUtils {

    private static String toString(Object obj) {
        if (obj == null) {
            return "";
        } else if (obj instanceof Date) {
            return DateFormatUtils.format(((Date) obj).getTime(), "yyyy-MM-dd HH:mm:ss");
        } else {
            return obj.toString();
        }
    }

    public static Object fromJson(String json, Type type) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(json, type);
    }

    /**
     * list对象转excel的sheet 默认格式
     *
     * @param list 数据
     * @param c    list中实例的类型
     * @return
     * @throws Exception
     */
    public SXSSFWorkbook exportExcel(String excelTitle, List list, Class c) throws Exception {
        return exportExcel(null, excelTitle, true, false, list, c);
    }

    /**
     * list对象转excel的sheet 自定义格式
     *
     * @param wb                excel表格
     * @param excelTitle        sheet标签名称
     * @param freezeFirstRow    是否冻结首行
     * @param freezeFirstColumn 是否冻结首列
     * @param list              数据
     * @param c                 list中实例的类型
     * @return
     * @throws Exception
     */
    public SXSSFWorkbook exportExcel(
            SXSSFWorkbook wb,
            String excelTitle,
            Boolean freezeFirstRow,
            Boolean freezeFirstColumn,
            List list,
            Class c)
            throws Exception {
        wb = workbookSetting(wb);
        SXSSFSheet sheet = SheetSetting(wb, excelTitle, freezeFirstRow, freezeFirstColumn);
        Row row = null;
        Cell cell = null;

        // 行号，默认当前sheet种最后一行的行号
        int rowc = sheet.getLastRowNum();

        // 通过对象反射获取表头信息
        Field[] fields = c.getDeclaredFields();
        List<FieldInfo> fieldInfos = new ArrayList<>();
        ExcelExportField excelExportField = null;
        for (Field field : fields) {
            if (field.isAnnotationPresent(ExcelExportField.class)) {
                excelExportField = field.getAnnotation(ExcelExportField.class);
                fieldInfos.add(new FieldInfo(excelExportField, field.getName()));
            }
        }
        Collections.sort(fieldInfos, Comparator.comparing(FieldInfo::getIndex));

        // 设置表头数据和宽度
        row = sheet.createRow(rowc);
        for (int i = 0; i < fieldInfos.size(); i++) {
            cell = row.createCell(i);
            CellStyle cellStyle = wb.createCellStyle();
            if (i == (fieldInfos.size() - 1)) {
                cellStyle.setAlignment(HorizontalAlignment.CENTER);
            } else {
                cellStyle.setAlignment(HorizontalAlignment.LEFT);
            }
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            Font font = wb.createFont();
            font.setFontHeightInPoints((short) 12);
            font.setBold(true);
            cellStyle.setFont(font);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(fieldInfos.get(i).getName());
            sheet.trackAllColumnsForAutoSizing();
            CellRangeAddress region = new CellRangeAddress(0, 1, i, i);
            sheet.addMergedRegion(region);
        }
        rowc = sheet.getLastRowNum() + 1;
        // 写入数据
        if (list != null && list.size() > 0) {
            FieldInfo fieldInfo = null;
            for (int i = 0; i < list.size(); i++) {
                rowc++;
                // 初始化一行
                row = sheet.createRow(rowc);
                // 每一列设置对应的数据
                for (int j = 0; j < fieldInfos.size(); j++) {
                    Cell rowCell = row.createCell(j);
                    fieldInfo = fieldInfos.get(j);
                    Field field = c.getDeclaredField(fieldInfo.getFieldName());
                    // 开启私有字段的权限
                    field.setAccessible(true);
                    if (fieldInfo.enumClass != null
                            && fieldInfo.enumClass.isEnum()
                            && fieldInfo.enumField.length() > 0
                            && fieldInfo.enumShowField.length() > 0
                            && field.get(list.get(i)) != null) {
                        rowCell.setCellValue(toString(getEnumValue(list.get(i), fieldInfo, field)));
                    } else if (fieldInfo.valueMap.length() > 0 && field.get(list.get(i)) != null) {
                        Map map = (Map) fromJson(fieldInfo.valueMap, HashMap.class);
                        rowCell.setCellValue(toString(map.get(field.get(list.get(i)).toString())));
                    } else {
                        PropertyDescriptor pd = new PropertyDescriptor(fieldInfos.get(j).getFieldName(), list.get(i).getClass());
                        Method getMethod = pd.getReadMethod();
                        if (getMethod.invoke(list.get(i)) != null) {
                            if (j == 3) {
                                DataFormat format = wb.createDataFormat();
                                CellStyle style = wb.createCellStyle();
                                style.setDataFormat(format.getFormat("h:mm:ss"));
                                style.setAlignment(HorizontalAlignment.RIGHT);
                                rowCell.setCellStyle(style);
                                rowCell.setCellValue(TimeUtils.getTimeStr(toString(getMethod.invoke(list.get(i)))));
                            } else {
                                rowCell.setCellValue(toString(getMethod.invoke(list.get(i))));
                            }
                        } else {
                            if (j == 3) {
                                DataFormat format = wb.createDataFormat();
                                CellStyle style = wb.createCellStyle();
                                style.setDataFormat(format.getFormat("h:mm:ss"));
                                style.setAlignment(HorizontalAlignment.RIGHT);
                                rowCell.setCellStyle(style);
                                rowCell.setCellValue(TimeUtils.getTimeStr(toString(field.get(list.get(i)))));
                            } else {
                                rowCell.setCellValue(toString(field.get(list.get(i))));
                            }
                        }
                    }
                    if (j == (fieldInfos.size() - 2)) {
                        // 调整每一列宽度
                        sheet.autoSizeColumn((short) j);
                        // 解决自动设置列宽中文失效的问题
                        sheet.setColumnWidth(j, sheet.getColumnWidth(j) * 17 / 10);
                    } else {
                        sheet.setColumnWidth(j, fieldInfos.get(j).getWidth() * 256);
                    }
                }
            }
        }
        return wb;
    }

    /**
     * list对象转excel的sheet(指定) 自定义格式
     *
     * @param wb    excel表格
     * @param sheet sheet标签
     * @param list  数据
     * @param c     list中实例的类型
     * @return
     * @throws Exception
     */
    public SXSSFWorkbook exportExcelAppointSheet(SXSSFWorkbook wb, Sheet sheet, List list, Class c)
            throws Exception {
        wb = workbookSetting(wb);
        Row row = null;
        Cell cell = null;

        // 行号，默认当前sheet种最后一行的行号
        int rowc = sheet.getLastRowNum();

        // 通过对象反射获取表头信息
        Field[] fields = c.getDeclaredFields();
        List<FieldInfo> fieldInfos = new ArrayList<>();
        ExcelExportField excelExportField = null;
        for (Field field : fields) {
            if (field.isAnnotationPresent(ExcelExportField.class)) {
                excelExportField = field.getAnnotation(ExcelExportField.class);
                fieldInfos.add(new FieldInfo(excelExportField, field.getName()));
            }
        }
        Collections.sort(fieldInfos, Comparator.comparing(FieldInfo::getIndex));

        // 设置表头数据和宽度
        row = sheet.createRow(rowc);
        for (int i = 0; i < fieldInfos.size(); i++) {
            cell = row.createCell(i);
            cell.setCellValue(fieldInfos.get(i).getName());
            sheet.setColumnWidth(i, fieldInfos.get(i).getWidth() * 256);
        }

        // 写入数据
        if (list != null && list.size() > 0) {
            FieldInfo fieldInfo = null;
            for (int i = 0; i < list.size(); i++) {
                rowc++;
                // 初始化一行
                row = sheet.createRow(rowc);
                // 每一列设置对应的数据
                for (int j = 0; j < fieldInfos.size(); j++) {
                    fieldInfo = fieldInfos.get(j);
                    Field field = c.getDeclaredField(fieldInfo.getFieldName());
                    // 开启私有字段的权限
                    field.setAccessible(true);
                    if (fieldInfo.enumClass != null
                            && fieldInfo.enumClass.isEnum()
                            && fieldInfo.enumField.length() > 0
                            && fieldInfo.enumShowField.length() > 0
                            && field.get(list.get(i)) != null) {
                        row.createCell(j).setCellValue(toString(getEnumValue(list.get(i), fieldInfo, field)));
                    } else if (fieldInfo.valueMap.length() > 0 && field.get(list.get(i)) != null) {
                        Map map = (Map) fromJson(fieldInfo.valueMap, HashMap.class);
                        row.createCell(j).setCellValue(toString(map.get(field.get(list.get(i)).toString())));
                    } else {
                        row.createCell(j).setCellValue(toString(field.get(list.get(i))));
                    }
                }
                row = null;
                fieldInfo = null;
            }
        }
        return wb;
    }

    /**
     * 通过枚举获取显示的数据
     *
     * @param item      对象
     * @param fieldInfo 枚举信息
     * @param field     字段
     * @return
     * @throws Exception
     */
    private Object getEnumValue(Object item, FieldInfo fieldInfo, Field field) throws Exception {
        Class enumClass = fieldInfo.getEnumClass();
        String enumField = fieldInfo.getEnumField();
        String enumShowField = fieldInfo.getEnumShowField();
        Method method = enumClass.getDeclaredMethod("values");
        Enum[] inter = (Enum[]) method.invoke(null);
        for (Enum cl : inter) {
            Field f1 = cl.getClass().getDeclaredField(enumField);
            f1.setAccessible(true);
            Field f2 = cl.getClass().getDeclaredField(enumShowField);
            f2.setAccessible(true);
            // 如果field的值等于枚举中的值，返回需要显示的值
            if (f1.get(cl).equals(field.get(item))) {
                return f2.get(cl);
            }
        }
        return null;
    }

    /**
     * 设置SXSSFWorkbook，默认设置内存中缓冲行数为1000
     *
     * @param wb
     * @return
     */
    private SXSSFWorkbook workbookSetting(SXSSFWorkbook wb) {
        if (wb == null) {
            wb = new SXSSFWorkbook(1000);
        }
        // 生成的临时文件压缩
        wb.setCompressTempFiles(true);
        // Excel样式
        CellStyle cellStyleB = getCellStyle(wb);
        // 单元格格式
        DataFormat dataFormat = wb.createDataFormat();
        cellStyleB.setDataFormat(dataFormat.getFormat("@"));
        return wb;
    }

    /**
     * 设置单元格式
     *
     * @param wb                工作页
     * @param excelTitle        sheet标签名称
     * @param freezeFirstRow    是否冻结首行
     * @param freezeFirstColumn 是否冻结首列
     * @return
     */
    private SXSSFSheet SheetSetting(
            SXSSFWorkbook wb,
            String excelTitle,
            Boolean freezeFirstRow,
            Boolean freezeFirstColumn) {
        // 获取已有的sheet
        SXSSFSheet sheet = null;
        Iterator<Sheet> sheetIterator = wb.sheetIterator();
        if (sheetIterator.hasNext()) {
            sheet = (SXSSFSheet) sheetIterator.next();
        }
        // 获取之前存在的sheet.如果不存在则创建
        if (null != sheet) {
            sheet = wb.getSheetAt(0);
        } else {
            sheet = excelTitle == null ? wb.createSheet() : wb.createSheet(excelTitle);
        }
        // 冻结行
        if (freezeFirstRow) {
            sheet.createFreezePane(0, 2, 0, 2);
        }
        if (freezeFirstColumn) {
            sheet.createFreezePane(1, 0, 1, 0);
        }
        // 设置显示数字0
        sheet.setDisplayZeros(true);
        return sheet;
    }

    public void makeFile(String filePath) throws IOException {
        File file = new File(filePath);
        String fileParentPath = file.getParent();
        // 文件创建
        if (!file.exists()) {
            new File(fileParentPath).mkdirs();
            file.createNewFile();
        }
    }

    /**
     * 设置样式
     *
     * @param swb
     * @return
     */
    public CellStyle getCellStyle(SXSSFWorkbook swb) {
        CellStyle cellStyle = swb.createCellStyle();
        Font font = swb.createFont();
        font.setFontHeightInPoints((short) 11);
        cellStyle.setFont(font); // 选择需要用到的字体格式
        cellStyle.setWrapText(true); // 设置自动换行
        return cellStyle;
    }

    @Data
    class FieldInfo {

        private String fieldName;
        private String name;
        private Integer index;
        private Class enumClass;
        private String enumField;
        private String enumShowField;
        private String valueMap;
        private int width;

        public FieldInfo(ExcelExportField excelExportField, String fieldName) {
            this.name = excelExportField.name();
            this.index = excelExportField.index();
            this.enumClass = excelExportField.enumClass();
            this.enumField = excelExportField.enumField();
            this.enumShowField = excelExportField.enumShowField();
            this.valueMap = excelExportField.valueMap();
            this.fieldName = fieldName;
            this.width = excelExportField.width();
        }
    }
}
