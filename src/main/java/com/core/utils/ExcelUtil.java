package com.core.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2021-01-11
 */
public class ExcelUtil {

    public static void setCell(Cell cell, Object obj) {
        if (obj == null) {
            cell.setCellValue("");
        } else if (obj instanceof Integer) {
            cell.setCellValue((int) obj);
        } else if (obj instanceof BigDecimal) {
            cell.setCellValue(((BigDecimal) obj).doubleValue());
        } else if (obj instanceof String) {
            cell.setCellValue((String) obj);
        } else if (obj instanceof Double) {
            cell.setCellValue((double) obj);
        } else if (obj instanceof Long) {
            cell.setCellValue((long) obj);
        }
    }

    public static void setCell(Cell cell, Object obj, CellStyle cellStyle) {
        cell.setCellStyle(cellStyle);
        if (obj instanceof Integer) {
            cell.setCellValue((int) obj);
        } else if (obj instanceof BigDecimal) {
            cell.setCellValue(((BigDecimal) obj).doubleValue());
        } else if (obj instanceof String) {
            cell.setCellValue((String) obj);
        } else if (obj instanceof Double) {
            cell.setCellValue((double) obj);
        } else if (obj instanceof Long) {
            cell.setCellValue((long) obj);
        } else {
            cell.setCellValue("");
        }
    }

    public static String getCellVal(Cell cell) {
        String cellValue;
        switch (cell.getCellType()) {
            case NUMERIC: //数字
                cellValue = StringUtils.isEmpty(cell.toString()) ? "" : Double.valueOf(cell.getNumericCellValue()).longValue() + "";
                break;
            case STRING: //字符串
                cellValue = String.valueOf(cell.getStringCellValue());
                break;
            case BOOLEAN: //Boolean
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case FORMULA: //公式
                cellValue = String.valueOf(cell.getCellFormula());
                break;
            case BLANK: //空值
                cellValue = "";
                break;
            case ERROR: //错误
                cellValue = "illegal type of value";
                break;
            default:
                cellValue = "unknown value of type";
                break;
        }
        return cellValue;
    }

}
