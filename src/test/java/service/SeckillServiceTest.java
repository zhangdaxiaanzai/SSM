package service;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit Spring配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml","classpath:spring/spring-service.xml"})
public class SeckillServiceTest {
	
	private final Logger logger=LoggerFactory.getLogger(this.getClass());
	@Resource
	SeckillService seckillService;
	
	@Test
	public void getSeckillListTest() throws Exception{
		List<Seckill> list = seckillService.getSeckillList();
		logger.info("list={}",list);
	}
	@Test
	public void getSeckillByIdTest() throws Exception{
		long id=1;
		Seckill seckill=seckillService.getSeckillById(id);
		logger.info("seckill={}",seckill);
	}
	@Test
	public void seckillLogicTest() throws Exception{
			long id=1;
			Exposer exposer=seckillService.exportSeckillUrl(id);
			if(exposer.isExposed()){
				long phone=12326766782L;
				String md5=exposer.getMd5();
				try {
					SeckillExecution seckillExecution= seckillService.executeSeckill(id, phone, md5);
					logger.info("result={}:",seckillExecution);
					
				} catch (RepeatKillException e) {
					logger.info(e.getMessage());
				}catch (SeckillCloseException e) {
					logger.info(e.getMessage());
				}
			}else {
				//秒杀未开启或已结束
				logger.warn("exposer={}",exposer);
			}
//			logger.info("exposer={}",exposer);
			//exposed=true, md5=7667a839dbf26d0269e6c293fe2ecb70, seckillId=1, now=0, start=0, end=0
	}
	/*@Test
	public void executeSeckillTest() throws Exception{
		long id=1;
		long phone=12326766782L;
		String md5="7667a839dbf26d0269e6c293fe2ecb70";
		try {
			SeckillExecution seckillExecution= seckillService.executeSeckill(id, phone, md5);
			logger.info("result={}:",seckillExecution);
			
		} catch (RepeatKillException e) {
			logger.info(e.getMessage());
		}catch (SeckillCloseException e) {
			logger.info(e.getMessage());
		}
		
		
	}*/
	@Test
	public void executeSeckillByProcedure(){
		long seckillId=1;
		long phone = 19887977657L;
		Exposer exposer=seckillService.exportSeckillUrl(seckillId);
		if(exposer.isExposed()){
			String md5=exposer.getMd5();
		SeckillExecution execution  =	seckillService.executeSeckillProcedure(seckillId, phone, md5);
			System.out.println(execution.getStateInfo());
		}
	}
}
