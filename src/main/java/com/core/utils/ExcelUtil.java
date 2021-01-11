package com.core.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;

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
}
