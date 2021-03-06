package org.seckill.service;

import java.util.List;

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;

/**
 * 站在"使用者"角度设计接口
 * 三个方面：1.方法定义粒度2.参数 3.返回类型
 * @author admin
 *
 */
public interface SeckillService {
	
	/**
	 * 查询所有秒杀记录
	 * @return
	 */
	public List<Seckill> getSeckillList();
	
	/**
	 * 查询单个秒杀记录
	 * @return
	 */
	public Seckill getSeckillById(long seckillId);
	
	
	/**
	 * 秒杀开启时输出秒杀接口地址，否则输出系统时间和秒杀时间
	 */
	public Exposer exportSeckillUrl(long seckillId);
		
	/**
	 * 执行秒杀操作
	 * @param seckillId
	 * @param userPhone
	 * @param md5
	 */
	public SeckillExecution  executeSeckill(long seckillId,long userPhone,String md5) throws SeckillException,
	RepeatKillException,SeckillCloseException;	
	
	/**
	 * 执行秒杀操作(通过存储过程执行)
	 * @param seckillId
	 * @param userPhone
	 * @param md5
	 * @return
	 * @throws SeckillException
	 * @throws RepeatKillException
	 * @throws SeckillCloseException
	 */
	public SeckillExecution  executeSeckillProcedure(long seckillId,long userPhone,String md5);	
	
	
		
}
