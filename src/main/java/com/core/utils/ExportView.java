package com.core.utils;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ExportView {

    private List<?> dataList;           // 数据集合
    private String title;               // 标题
    private String sheetName;           // sheet名
    private Class<?> dataClass;         // 数据class

}
