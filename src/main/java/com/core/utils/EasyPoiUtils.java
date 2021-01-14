package com.core.utils;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.excel.export.styler.ExcelExportStylerBorderImpl;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class EasyPoiUtils {
    public EasyPoiUtils() {
    }

    public static <T> byte[] exportExcel(List<T> dataList, String title, String sheetName, Class<T> clz) {
        ExportParams exportParams = new ExportParams(title, sheetName, ExcelType.XSSF);
        exportParams.setStyle(ExcelExportStylerBorderImpl.class);
        exportParams.setHeight((short)-1);
        return exportExcel(dataList, clz, exportParams);
    }

    public static <T> byte[] exportExcel(List<T> dataList, Class<T> clz, ExportParams exportParams) {
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, clz, dataList);
        return workbook != null ? downLoadExcel(workbook) : new byte[0];
    }

    public static byte[] exportExcel(List<ExportView> exportViewList) {
        if (CollectionUtils.isNotEmpty(exportViewList)) {
            List<Map<String, Object>> sheetsList = Lists.newArrayListWithCapacity(CollectionUtils.size(exportViewList));
            Iterator var4 = exportViewList.iterator();

            while(var4.hasNext()) {
                ExportView exportView = (ExportView)var4.next();
                ExportParams exportParams = new ExportParams();
                exportParams.setType(ExcelType.XSSF);
                exportParams.setSheetName(exportView.getSheetName());
                exportParams.setTitle(exportView.getTitle());
                exportParams.setStyle(ExcelExportStylerBorderImpl.class);
                exportParams.setHeight((short)-1);
                Map<String, Object> map = new HashMap();
                map.put("title", exportParams);
                map.put("entity", exportView.getDataClass());
                map.put("data", exportView.getDataList());
                sheetsList.add(map);
            }

            Workbook workbook = ExcelExportUtil.exportExcel(sheetsList, ExcelType.XSSF);
            if (workbook != null) {
                return downLoadExcel(workbook);
            }
        }

        return new byte[0];
    }

    public static byte[] downLoadExcel(Workbook workbook) {
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();

            byte[] var2;
            try {
                workbook.write(os);
                var2 = os.toByteArray();
            } catch (Throwable var5) {
                try {
                    os.close();
                } catch (Throwable var4) {
                    var5.addSuppressed(var4);
                }

                throw var5;
            }

            os.close();
            return var2;
        } catch (IOException var6) {
            throw new RuntimeException(var6);
        }
    }

    public static <T> List<T> importExcel(MultipartFile file, Integer titleRows, Integer headerRows, Class<T> clz) {
        return importExcel(file, 0, titleRows, headerRows, clz);
    }

    public static <T> List<T> importExcel(MultipartFile file, Integer startSheetIndex, Integer titleRows, Integer headerRows, Class<T> clz) {
        if (ObjectUtils.isEmpty(file)) {
            return null;
        } else {
            ImportParams params = new ImportParams();
            params.setStartSheetIndex(startSheetIndex);
            params.setTitleRows(titleRows);
            params.setHeadRows(headerRows);

            try {
                return ExcelImportUtil.importExcel(file.getInputStream(), clz, params);
            } catch (Exception var7) {
                throw new RuntimeException(var7);
            }
        }
    }

    public static <T> List<T> importExcel(String filePath, Integer titleRows, Integer headerRows, Class<T> clz) {
        return StringUtils.isBlank(filePath) ? null : importExcel(new File(filePath), titleRows, headerRows, clz);
    }

    public static <T> List<T> importExcel(String filePath, Integer startSheetIndex, Integer titleRows, Integer headerRows, Class<T> clz) {
        return StringUtils.isBlank(filePath) ? null : importExcel(new File(filePath), startSheetIndex, titleRows, headerRows, clz);
    }

    public static <T> List<T> importExcel(File file, Integer titleRows, Integer headerRows, Class<T> clz) {
        return importExcel(file, 0, titleRows, headerRows, clz);
    }

    public static <T> List<T> importExcel(File file, Integer startSheetIndex, Integer titleRows, Integer headerRows, Class<T> clz) {
        if (!ObjectUtils.isEmpty(file) && file.exists()) {
            ImportParams params = new ImportParams();
            params.setStartSheetIndex(startSheetIndex);
            params.setTitleRows(titleRows);
            params.setHeadRows(headerRows);

            try {
                return ExcelImportUtil.importExcel(file, clz, params);
            } catch (Exception var7) {
                throw new RuntimeException(var7);
            }
        } else {
            return null;
        }
    }
}
