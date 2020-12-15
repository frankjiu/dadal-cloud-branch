/**
 * All rights Reserved, Designed By www.xcompany.com
 *
 * @Package: com.controller
 * @author: Frankjiu
 * @date: 2020年6月1日
 * @version: V1.0
 */

package com.function.imports;

import com.modules.base.model.entity.Demo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 文件解析器
 * @author: Frankjiu
 * @date: 2020年6月1日
 */
@Component
@Slf4j
public class FileParser {

    public List<Demo> parse(String fileContent, String fileName) {
        List<Demo> list = new ArrayList<>();
        String REGEX = "^\\w+$";
        String prop = System.getProperty("file.separator");
        String path = System.getProperty("user.dir") + prop + fileContent + prop + fileName;
        BufferedReader bufReader = null;
        Demo demo = null;
        //解析
        try {
            File file = new File(path);
            InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "GBK");
            bufReader = new BufferedReader(isr);
            String line;
            // 逐行扫描
            while ((line = bufReader.readLine()) != null) {
                boolean matched = false;
                String record = null;
                if (line.trim().length() >= 1) {
                    record = line.trim();
                    matched = record.matches(REGEX);
                }
                // 如果数据行格式通过正则表达式则进行处理
                if (matched) {
                    demo = new Demo();
                    demo.setCardName(record);
                    list.add(demo);
                }
            }
        } catch (Exception e) {
            log.info("解析异常!" + e);
        } finally {
            try {
                bufReader.close();
            } catch (IOException e) {
                log.info(e.getMessage(), e);
            }
        }
        return list;
    }

}
