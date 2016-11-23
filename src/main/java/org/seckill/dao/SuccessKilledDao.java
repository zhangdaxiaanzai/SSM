package org.seckill.dao;

import org.apache.ibatis.annotations.Param;
import org.seckill.entity.SuccessKilled;

public interface SuccessKilledDao {
	
	/**
	 * ������ɱ�ɹ���¼
	 * @param seckillId
	 * @param userPhone
	 * @return ���������
	 */
	public int insertSuccessKilled(@Param("seckillId") long seckillId,@Param("userPhone") long userPhone);
	
	/**
	 * ����id��SuccessKilled��Я����ɱ��Ʒ����ʵ��
	 * @param seckillId
	 * @return
	 */
	
	public SuccessKilled queryByIdWithSeckill(@Param("seckillId") long seckillId,@Param("userPhone") long userPhone);
}
