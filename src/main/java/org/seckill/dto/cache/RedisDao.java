package org.seckill.dto.cache;

import org.seckill.entity.Seckill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisDao {

	private final Logger logger=LoggerFactory.getLogger(this.getClass());
		private final JedisPool jedisPool;
		
		public RedisDao(String ip,int port){
			jedisPool=new JedisPool(ip,port);
		}
		
		
		private RuntimeSchema<Seckill> schema = RuntimeSchema.createFrom(Seckill.class);//���л�
		
		public Seckill getSeckill(long seckillId){
			
			//Redis�߼�
			try {
				Jedis jedis=jedisPool.getResource();
				try {
					String key="seckill:"+seckillId;
					//û��ʵ�����л� 
					//get -> byte[] -> �����л� -> Object(Seckill)
					//�����Զ������л�
					//protostuff:pojo
					byte[] bytes= jedis.get(key.getBytes());//�ӻ����л�ȡ����
					if(bytes != null){
						Seckill seckill=schema.newMessage();//��ȡһ�����л��Ŀն���
						ProtobufIOUtil.mergeFrom(bytes, seckill, schema);
						//seckill�������л�
						return seckill;
						
					}
					
					
				} finally {
					jedis.close();
				}
			} catch (Exception e) {
				logger.error(e.getMessage(),e);
			}
			return null;		
	    }
		public String putSeckill(Seckill seckill){
			//set: Object(Seckill) -> ���л� -> byte[](�ֽ�����) -> redis
			
			try {
				Jedis jedis=jedisPool.getResource();
				try {
					String key="seckill:"+seckill.getSeckillId();
					byte[] bytes=ProtobufIOUtil.toByteArray(seckill, schema, LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
					//��ʱ����
					int timeout=60*60;//һСʱ
					String result=jedis.setex(key.getBytes(), timeout, bytes);
					return result;
				} finally {
					jedis.close();
				}
			} catch (Exception e) {
				logger.error(e.getMessage(),e);
				
			}
			return null;
		}
		
}
