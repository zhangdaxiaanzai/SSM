package seckill;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dao.SeckillDao;
import org.seckill.entity.Seckill;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 配置spring和junit整合，junit启动时加载springIOC容器
 * spring-test,junit
 * @author admin
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit Spring配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SeckillDaoTest {
	
	//注入Dao实现类依赖
	@Resource
	private SeckillDao seckillDao;
	
	@Test
	public void testQueryById() throws Exception{
		long id = 1;
		Seckill seckill=seckillDao.queryById(id);
		System.out.println(seckill.getName());
		System.out.println(seckill);
		
	}
	
	@Test
	public void testReduceNumber()throws Exception{
		long seckillId=1;
		Date killTime=new Date();
		int count=seckillDao.reduceNumber(seckillId, killTime);
		System.out.println(count);
		
	}
	
	
	@Test
	public void testQueryAll() throws Exception{
		List<Seckill> listSeckill = seckillDao.queryAll(1, 3); 
		for (Seckill seckill : listSeckill) {
			System.out.println("listSeckill:="+seckill);
		}
		
		
	}
	
}
