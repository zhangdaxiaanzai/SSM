package org.seckill.service;

import java.util.List;

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;

/**
 * վ��"ʹ����"�Ƕ���ƽӿ�
 * �������棺1.������������2.���� 3.��������
 * @author admin
 *
 */
public interface SeckillService {
	
	/**
	 * ��ѯ������ɱ��¼
	 * @return
	 */
	public List<Seckill> getSeckillList();
	
	/**
	 * ��ѯ������ɱ��¼
	 * @return
	 */
	public Seckill getSeckillById(long seckillId);
	
	
	/**
	 * ��ɱ����ʱ�����ɱ�ӿڵ�ַ���������ϵͳʱ�����ɱʱ��
	 */
	public Exposer exportSeckillUrl(long seckillId);
		
	/**
	 * ִ����ɱ����
	 * @param seckillId
	 * @param userPhone
	 * @param md5
	 */
	public SeckillExecution  executeSeckill(long seckillId,long userPhone,String md5) throws SeckillException,
	RepeatKillException,SeckillCloseException;	
	
	
		
}
