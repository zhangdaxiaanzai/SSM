package org.seckill.dao;

import org.apache.ibatis.annotations.Param;
import org.seckill.entity.SuccessKilled;

public interface SuccessKilledDao {
	
	/**
	 * 插入秒杀成功记录
	 * @param seckillId
	 * @param userPhone
	 * @return 插入的行数
	 */
	public int insertSuccessKilled(@Param("seckillId") long seckillId,@Param("userPhone") long userPhone);
	
	/**
	 * 根据id查SuccessKilled并携带秒杀产品对象实体
	 * @param seckillId
	 * @return
	 */
	
	public SuccessKilled queryByIdWithSeckill(@Param("seckillId") long seckillId,@Param("userPhone") long userPhone);
}
