package sy.util;

import java.util.List;

import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisUtils {
	private static Logger logger = Logger.getLogger(RedisUtils.class);
	//redis地址
	private static String host = PropertiesUtil.getProperty("redis.host");
	private static JedisPool pool;

	/**
	 * 将数据存到key对应的redis链表中
	 * @param key 键值
	 * @param attrData json型数据
	 */
	public static void addData(String key,String attrData){
		Jedis jedis = null;
		try{
			if(pool == null)
				pool = getPool();
			jedis = pool.getResource();			
			jedis.lpush(key, attrData);
		}catch(Exception e){
			logger.error("jedis error",e);
		}finally{
			returnResource(pool,jedis);
		}
	}
	/**
	 * 按范围获取redis中存储的list
	 * @param key 键值
	 * @param start 起始位置
	 * @param end 结束位置
	 * @return
	 */
	public static List<String> getData(String key,int start,int end) throws Exception{
		if(end > 0 && end < start){
			logger.info("end can not bigger than start");
			return null;
		}
		Jedis jedis = null;
		try{
			if(pool == null)
				pool = getPool();
			jedis = pool.getResource();	
			List<String> list = jedis.lrange(key, start, end);
			return list;
		}catch(Exception e){
			logger.error("jedis error",e);
			throw e;
		}finally{
			returnResource(pool,jedis);
		}
	}
	
	/**
	 * 按范围保留数据
	 * @param key 键值
	 * @param start 起始位置
	 * @param end 结束位置
	 */
	public static void ltrimData(String key ,int start, int end){
		if(end > 0 && end < start){
			logger.info("end can not bigger than start");
			return;
		}
		Jedis jedis = null;
		try{
			if(pool == null)
				pool = getPool();
			jedis = pool.getResource();	
			//双向链表左往右保留下标为第start到end的数据
			jedis.ltrim(key, start, end);
		}catch(Exception e){
			logger.error("jedis error",e);
		}finally{
			returnResource(pool,jedis);
		}
	}
	
	/**
	 * 获取redis连接池对象
	 * @return
	 */
	public static JedisPool getPool() {  
		if (pool == null) {  
			JedisPoolConfig config = new JedisPoolConfig();  
			//控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；  
			//如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。  
			config.setMaxActive(500);  
			//控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。  
			config.setMaxIdle(5);  
			//表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；  
			config.setMaxWait(1000L * 100);  
			//在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；  
			config.setTestOnBorrow(true);  
			pool = new JedisPool(config,host,6379);
		}  
		return pool;  
	}  

	/** 
	 * redis连接返还到连接池 
	 * @param pool  
	 * @param redis 
	 */  
	public static void returnResource(JedisPool pool, Jedis redis) {  
		if (redis != null) {  
			pool.returnResource(redis);  
		}  
	} 
}
