package com;

import com.modules.base.demo.model.entity.Demo;
import com.modules.base.demo.service.DemoService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.Date;

/**
 * @Description:
 * @Author: QiuQiang
 * @Date: 2020-12-10
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {CloudBranchApplication.class})
@Slf4j
public class TransactionTest {

    @Autowired
    private DemoService demoService;

    @Autowired
    private DataSourceTransactionManager transactionManager;

    @Test
    public void demoTestTrans() {
        DefaultTransactionDefinition transDef = new DefaultTransactionDefinition();
        transDef.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus transaction = transactionManager.getTransaction(transDef);
        try {
            // Demo demo = new Demo("testData");
            Demo demo2 = new Demo("testData", new Date());
            int i = demoService.insert(demo2);
            System.out.println(demo2.getId());
            // 制造异常..
            System.out.println(3/0);
            transactionManager.commit(transaction);
        } catch (Exception e) {
            log.info(e.getMessage(), e);
            transactionManager.rollback(transaction);
        }

    }
}
