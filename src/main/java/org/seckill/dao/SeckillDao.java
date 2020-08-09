package org.seckill.dao;

import org.apache.ibatis.annotations.Param;
import org.seckill.entity.SecKill;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface SeckillDao {
    int reduceNumber(@Param("seckillId") long seckillId, @Param("killTime") Date killTime);
    //因为是单个参数，所以即便名字对应不上也没关系！
    SecKill queryById(long seckillId);
    List<SecKill> queryAll(@Param("offset") int offset, @Param("limit") int limit);

    /**
     * 使用存储过程执行秒杀
     * @param paramMap
     */
    void killByProcedure(Map<String,Object> paramMap);
}
