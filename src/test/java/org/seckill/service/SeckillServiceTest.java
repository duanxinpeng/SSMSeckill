package org.seckill.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecutor;
import org.seckill.entity.SecKill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
        "classpath:spring/spring-dao.xml",
        "classpath:spring/spring-service.xml"
})
public class SeckillServiceTest {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SeckillService seckillService;

    @Test
    public void getSeckillList() {
        List<SecKill> list = seckillService.getSeckillList();
        logger.info("list={}", list);
        //Closing non transactional SqlSession
    }

    @Test
    public void getById() {
        long id = 1000;
        SecKill secKill = seckillService.getById(id);
        logger.info("seckill= {}", secKill);
    }

    @Test
    public void exportSeckillUrl() {
        long id = 1000;
        Exposer exposer = seckillService.exportSeckillUrl(id);
        logger.info("exposer={}", exposer);
        /*
        exposed=true, md5='b9ef433a6ae030114bcb3bceec5001bc', seckillId=1000, now=0, start=0, end=0
         */
    }

    @Test
    public void executeSeckill() {
        long id = 1000;
        long phone = 1238981974L;
        String md5 = "b9ef433a6ae030114bcb3bceec5001bc";
        try {
            SeckillExecutor seckillExecutor = seckillService.executeSeckill(id, phone, md5);
            logger.info("SeckillExecutor ={}", seckillExecutor);
        } catch (RepeatKillException e1) {
            logger.error(e1.getMessage());
        } catch (SeckillCloseException e2) {
            logger.error(e2.getMessage());
        }
    }

    @Test
    public void testSeckillLogic() throws Exception {
        long id = 1000;
        Exposer exposer = seckillService.exportSeckillUrl(id);
        if (exposer.isExposed()) {
            logger.info("exposer={}", exposer);
            long phone = 12376548710L;
            String md5 = exposer.getMd5();
            try {
                SeckillExecutor executor = seckillService.executeSeckill(id, phone, md5);
                logger.info("result={}", executor);
            } catch (RepeatKillException e) {
                logger.error(e.getMessage());
            } catch (SeckillCloseException e) {
                logger.error(e.getMessage());
            }
        } else {
            logger.warn("exposer={}", exposer);
        }
    }

    @Test
    public void testKillByProcedure() throws Exception {
        long seckillId = 1000;
        long phone = 12345678901L;
        Exposer exposer = seckillService.exportSeckillUrl(seckillId);
        if (exposer.isExposed()) {
            String md5 = exposer.getMd5();
            SeckillExecutor executor = seckillService.executeSeckillProcedure(seckillId,phone,md5);
            logger.info(executor.getStateInfo());
        }
    }
}