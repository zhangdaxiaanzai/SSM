package org.seckill.service.implservice;

import java.util.Date;
import java.util.List;

import org.seckill.dao.SeckillDao;
import org.seckill.dao.SuccessKilledDao;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.dto.cache.RedisDao;
import org.seckill.entity.Seckill;
import org.seckill.entity.SuccessKilled;
import org.seckill.enums.SeckillStatEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;


@Service
public class SeckillServiceImpl implements SeckillService {

	private Logger logger=LoggerFactory.getLogger(this.getClass());
	
	//ע��service����
	@Autowired
	private SeckillDao seckillDao;
	@Autowired
	private SuccessKilledDao successKilledDao;
	
	@Autowired
	private RedisDao redisDao;
	
	private final String  salt="sadGUT&^*&*^*efqweu234898**F&8f9oprkoo%^%^$^&";
	
	public List<Seckill> getSeckillList() {
		// TODO Auto-generated method stub
		return seckillDao.queryAll(0, 4);
	}

	public Seckill getSeckillById(long seckillId) {
		// TODO Auto-generated method stub
		return seckillDao.queryById(seckillId);
	}

	public Exposer exportSeckillUrl(long seckillId) {
		
		//�Ż��㣺�����Ż�:��ʱ�Ļ�����ά��һ����
		//1.����redis
		Seckill seckill=redisDao.getSeckill(seckillId);
		if(seckill == null){
			//2.�������ݿ�
			seckill=seckillDao.queryById(seckillId);
			if(seckill == null){
				return new Exposer(false, seckillId);
			}else{
				//3.����redis
				redisDao.putSeckill(seckill);
			}
		}
			Date startDate=seckill.getStartTime();
			Date endDate=seckill.getEndTime();
			Date dateNow=new Date();

			if(startDate.getTime() > dateNow.getTime() || endDate.getTime() < dateNow.getTime()){
				return new Exposer(false, seckillId, dateNow.getTime(), startDate.getTime(), endDate.getTime());
			}
			String md5=getMD5(seckillId);
			return new Exposer(true, md5, seckillId);
		
		 
		
	}
	
	private String getMD5(long seckillId){
		String base=seckillId+"/"+salt;
		String md5=DigestUtils.md5DigestAsHex(base.getBytes());
		return md5;
	}
	
	@Transactional
	public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
			throws SeckillException, RepeatKillException, SeckillCloseException {
		//ִ����ɱ�߼���1����棬2.���������¼
		try {
			
			if(md5 == null || !md5.equals(getMD5(seckillId))){
				throw new SeckillException("seckill data rewirte");//�����쳣
			}
			Date dateNow=new Date();
			
			//��¼�����¼
			int insertCount=successKilledDao.insertSuccessKilled(seckillId, userPhone);
			//Ψһ�Լ�⣨����Ƿ��ظ���ɱ��
			if(insertCount <= 0){
				throw new RepeatKillException("seckilled repeat");
			}else {
				//����棬�ȵ���Ʒ����
				int intUpdateCount=seckillDao.reduceNumber(seckillId, dateNow);
				
				if(intUpdateCount <= 0){
					//û�и��µ���棨��ɱ�Ѿ�������û�п���ˣ���rollback
					throw new SeckillCloseException("seckill id closed");
				}else {
					//��ɱ�ɹ�,commit
					SuccessKilled successKilled=successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
					return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS, successKilled);
				}
			}
			
		}catch (SeckillCloseException e1) {
			throw e1;
		} catch (RepeatKillException e2) {
			throw e2;
		}catch (Exception e) {
			//�����б������쳣ת��Ϊ�������쳣
			logger.error(e.getMessage(),e);
			throw new SeckillException(e.getMessage(), e);
			
		}
		
		
		
		
	}

}
