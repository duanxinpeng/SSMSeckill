package org.seckill.service;

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecutor;
import org.seckill.entity.SecKill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;

import java.util.List;

//完全站在使用者的角度设计接口
//方法定义粒度，参数，返回类型！
public interface SeckillService {
    //查询所有秒杀记录
    List<SecKill> getSeckillList();

    //根据id查询
    SecKill getById(long seckillId);

    //秒杀接口暴露
    //秒杀开启输出接口地址
    //否则输出系统时间和秒杀时间。
    Exposer exportSeckillUrl(long seckillId);

    /**
     * @param seckillId
     * @param userPhone
     * @param md5
     */
    SeckillExecutor executeSeckill(long seckillId, long userPhone, String md5) throws SeckillException, RepeatKillException, SeckillCloseException;

    /**
     * 异常的作用是告诉 Spring 的声明式事务去，现在不需要了
     * @param seckillId
     * @param userPhone
     * @param md5
     * @return
     */
    SeckillExecutor executeSeckillProcedure(long seckillId, long userPhone, String md5);
}
