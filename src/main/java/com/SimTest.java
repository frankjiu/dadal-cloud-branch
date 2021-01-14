package com;

import org.apache.poi.ss.usermodel.CellType;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2021-01-03
 */
public class SimTest {

    @Test
    public void test1() throws IOException {
        CellType type = CellType.NUMERIC;

        boolean f = type.name() == (CellType.NUMERIC.name());
        System.out.println(f);

    }

}
