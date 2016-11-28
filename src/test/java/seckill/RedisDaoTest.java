package seckill;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dao.SeckillDao;
import org.seckill.dto.cache.RedisDao;
import org.seckill.entity.Seckill;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
//∏ÊÀﬂjunit Spring≈‰÷√Œƒº˛
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class RedisDaoTest {
	private long seckillId=2;
	
	@Resource
	private RedisDao redisDao;
	
	@Resource
	private SeckillDao seckillDao;
	
	@Test
	public void testSeckill() throws Exception{
		Seckill seckill = redisDao.getSeckill(seckillId);
		if(seckill == null){
			seckill=seckillDao.queryById(seckillId);
			if(seckill != null){
				String result=redisDao.putSeckill(seckill);
				System.out.println(result);
				seckill=redisDao.getSeckill(seckillId);
				System.out.println(seckill);
				
			}
		}
	}
	
}
