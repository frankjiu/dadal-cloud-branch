package com.core.config.generator;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2021-07-15
 */
public class Generator {

    /**
     * 注意: 设置是否覆盖旧文件, true:是; false:否
     */
    private static boolean OVER_WRITE = true;

    public static void main(String[] args) throws Exception {
        String property = System.getProperty("user.dir");
        System.err.println(">>>>>>>>>>> " + property + " >>>>>>>>>>>");
        List<String> warnings = new ArrayList<>();
        // File configFile = new File("D:/IDEA/workspace_idea/hyzc-business/src/main/resources/mybatis-generator.xml");
        String file = Generator.class.getResource("/mybatis-generator.xml").getFile();
        File configFile = new File(file);
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(configFile);
        DefaultShellCallback callback = new DefaultShellCallback(OVER_WRITE);
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
        myBatisGenerator.generate(null);
        System.err.println(">>>>>>>>>>> 代码生产完成! >>>>>>>>>>>");
    }

}