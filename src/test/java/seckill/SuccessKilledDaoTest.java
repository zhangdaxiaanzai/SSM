package seckill;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dao.SuccessKilledDao;
import org.seckill.entity.SuccessKilled;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
//∏ÊÀﬂjunit Spring≈‰÷√Œƒº˛
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SuccessKilledDaoTest {
	
	
	@Resource
	private SuccessKilledDao skDao;

	@Test
	public void insertSuccessKilledTest(){
		int count = skDao.insertSuccessKilled(1, 18079658769L);
		System.out.println(count);
		
	}
	
	
	@Test
	public void queryByIdWithSeckillTest(){
		SuccessKilled successKilled = skDao.queryByIdWithSeckill(1L,18079658768L);
		System.out.println(successKilled);
		System.out.println(successKilled.getSeckill());
	}
	
}
