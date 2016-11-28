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
		
		
		private RuntimeSchema<Seckill> schema = RuntimeSchema.createFrom(Seckill.class);//序列化
		
		public Seckill getSeckill(long seckillId){
			
			//Redis逻辑
			try {
				Jedis jedis=jedisPool.getResource();
				try {
					String key="seckill:"+seckillId;
					//没有实现序列化 
					//get -> byte[] -> 反序列化 -> Object(Seckill)
					//采用自定义序列化
					//protostuff:pojo
					byte[] bytes= jedis.get(key.getBytes());//从缓存中获取对象
					if(bytes != null){
						Seckill seckill=schema.newMessage();//获取一个序列化的空对象
						ProtobufIOUtil.mergeFrom(bytes, seckill, schema);
						//seckill被反序列化
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
			//set: Object(Seckill) -> 序列化 -> byte[](字节数组) -> redis
			
			try {
				Jedis jedis=jedisPool.getResource();
				try {
					String key="seckill:"+seckill.getSeckillId();
					byte[] bytes=ProtobufIOUtil.toByteArray(seckill, schema, LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
					//超时缓存
					int timeout=60*60;//一小时
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
