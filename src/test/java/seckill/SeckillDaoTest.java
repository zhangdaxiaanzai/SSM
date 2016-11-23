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
 * ����spring��junit���ϣ�junit����ʱ����springIOC����
 * spring-test,junit
 * @author admin
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
//����junit Spring�����ļ�
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SeckillDaoTest {
	
	//ע��Daoʵ��������
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
