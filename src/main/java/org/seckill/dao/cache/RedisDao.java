package org.seckill.dao.cache;

import org.seckill.entity.Seckill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * redis�����ݷ��ʶ���
 * @author SJW
 *
 */

public class RedisDao {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private final JedisPool jedisPool;
	
	public RedisDao(String ip,int port){
		jedisPool = new JedisPool(ip,port);
	}
	
	
	private RuntimeSchema<Seckill> schema = RuntimeSchema.createFrom(Seckill.class);
	
	//��redis���ó�seckill����
	public Seckill getSeckill(long seckillId){
		//redis�߼�����
		//�Ȼ�ȡ��һ��jedis����
		try {
			Jedis jedis = jedisPool.getResource();
			try {
				//���key������ͨ��������redis����ȡ��������Ҫ�Ļ������
				String key = "seckill:" + seckillId;
				//redis�ڲ���û�а�����ʵ�����л�����,��������ȡ���Ķ���ʵ������һ���������ļ�,����������Ҫͨ�������л���������ȡ����
				//�����ƶ���ת����������Ҫ��seckill����,��������ʹ��protostuff��ʵ�ַ����л��Ĳ���,����java�Դ���serilizeble
				//�ӿ�,��Ϊprotostuff���ٶȸ���
				byte[] bytes = jedis.get(key.getBytes());
				//�ж��Ƿ��õ���seckill�Ķ����ƶ���
				if(bytes != null){
					Seckill seckill = schema.newMessage();
					//���ù�����ʵ�ַ����л��õ����ǵ�seckill JAVA����
					ProtostuffIOUtil.mergeFrom(bytes, seckill, schema);
					return seckill;
				}
			} finally{
				jedis.close();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		
		return null;
	}
	
	
	//��redis��putһ��seckill����
	public String putSeckill(Seckill seckill){
		//��������ǰ�seckill�������л���һ������������,Ȼ��浽redis����
		try {
				Jedis jedis = jedisPool.getResource();
			try {
				String key = "seckill:" + seckill.getSeckillId();
				byte[] bytes = ProtostuffIOUtil.toByteArray(seckill, schema, 
						LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
				int timeout = 60 * 60; //����ʱ��һСʱ
				String result = jedis.setex(key.getBytes(), timeout, bytes);
				return result;
			} finally{
				jedis.close();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	
}
