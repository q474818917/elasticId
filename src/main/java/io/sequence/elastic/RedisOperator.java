package io.sequence.elastic;

import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisOperator {

	private String host = "127.0.0.1";

	private int port = 6379;

	// 0 - never expire
	private int expire = 0;
	
	// timeout for jedis try to connect to redis server, not expire time! In
	// milliseconds
	private int timeout = 0;

	private String password = "";

	private static JedisPool jedisPool = null;

	public RedisOperator() {

	}

	/**
	 * 初始化方法
	 */
	public void init() {
		if (jedisPool == null) {
			if (password != null && !"".equals(password)) {
				jedisPool = new JedisPool(new JedisPoolConfig(), host, port, timeout, password);
			} else if (timeout != 0) {
				jedisPool = new JedisPool(new JedisPoolConfig(), host, port, timeout);
			} else {
				jedisPool = new JedisPool(new JedisPoolConfig(), host, port);
			}

		}
	}

	/**
	 * get value from redis
	 * 
	 * @param key
	 * @return
	 */
	public byte[] get(byte[] key) {
		byte[] value = null;
		Jedis jedis = jedisPool.getResource();
		try {
			value = jedis.get(key);
		} finally {
			jedisPool.returnResource(jedis);
		}
		return value;
	}

	/**
	 * set
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public byte[] set(byte[] key, byte[] value) {
		Jedis jedis = jedisPool.getResource();
		try {
			jedis.set(key, value);
			if (this.expire != 0) {
				jedis.expire(key, this.expire);
			}
		} finally {
			jedisPool.returnResource(jedis);
		}
		return value;
	}

	/**
	 * set
	 * 
	 * @param key
	 * @param value
	 * @param expire
	 * @return
	 */
	public byte[] set(byte[] key, byte[] value, int expire) {
		Jedis jedis = jedisPool.getResource();
		try {
			jedis.set(key, value);
			if (expire != 0) {
				jedis.expire(key, expire);
			}
		} finally {
			jedisPool.returnResource(jedis);
		}
		return value;
	}

	/**
	 * <p>
	 * 通过key获取储存在redis中的value
	 * </p>
	 * <p>
	 * 并释放连接
	 * </p>
	 * 
	 * @param key
	 * @return 成功返回value 失败返回null
	 */
	public String get(String key) {
		Jedis jedis = jedisPool.getResource();
		String value = null;
		try {
			value = jedis.get(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jedisPool.returnResource(jedis);
		}
		return value;
	}

	/**
	 * <p>
	 * 向redis存入key和value,并释放连接资源
	 * </p>
	 * <p>
	 * 如果key已经存在 则覆盖
	 * </p>
	 * 
	 * @param key
	 * @param value
	 * @return 成功 返回OK 失败返回 0
	 */
	public String set(String key, String value) {
		Jedis jedis = jedisPool.getResource();
		try {
			jedis.set(key, value);
			if (expire != 0) {
				jedis.expire(key, expire);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "0";
		} finally {
			jedisPool.returnResource(jedis);
		}
		return value;
	}

	public List<String> mget(String... keys) {
		Jedis jedis = jedisPool.getResource();
		List<String> values = null;
		try {
			values = jedis.mget(keys);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jedisPool.returnResource(jedis);
		}
		return values;
	}

	/**
	 * <p>
	 * 批量的设置key:value,可以一个
	 * </p>
	 * <p>
	 * example:
	 * </p>
	 * <p>
	 * obj.mset(new String[]{"key2","value1","key2","value2"})
	 * </p>
	 * 
	 * @param keysvalues
	 * @return 成功返回OK 失败 异常 返回 null
	 * 
	 */
	public String mset(String... keysvalues) {
		Jedis jedis = jedisPool.getResource();
		String res = null;
		try {
			res = jedis.mset(keysvalues);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jedisPool.returnResource(jedis);
		}
		return res;
	}

	/**
	 * <p>
	 * 通过key同时设置 hash的多个field
	 * </p>
	 * 
	 * @param key
	 * @param hash
	 * @return 返回OK 异常返回null
	 */
	public String hmset(String key, Map<String, String> hash) {
		Jedis jedis = jedisPool.getResource();
		String res = null;
		try {
			res = jedis.hmset(key, hash);
			if (expire != 0) {
				jedis.expire(key, expire);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jedisPool.returnResource(jedis);
		}
		return res;
	}

	/**
	 * <p>
	 * 通过key 和 field 获取指定的 value
	 * </p>
	 * 
	 * @param key
	 * @param field
	 * @return 没有返回null
	 */
	public String hget(String key, String field) {
		Jedis jedis = jedisPool.getResource();
		String res = null;
		try {
			res = jedis.hget(key, field);
			if (expire != 0) {
				jedis.expire(key, expire);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jedisPool.returnResource(jedis);
		}
		return res;
	}
	
	public List<String> hvals(String key){
		Jedis jedis = jedisPool.getResource();
		List<String> res = null;
		try {
			res = jedis.hvals(key);
			if (expire != 0) {
				jedis.expire(key, expire);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jedisPool.returnResource(jedis);
		}
		return res;
	}
	
	public Set<String> hkeys(String key){
		Jedis jedis = jedisPool.getResource();
		Set<String> res = null;
		try {
			res = jedis.hkeys(key);
			if (expire != 0) {
				jedis.expire(key, expire);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jedisPool.returnResource(jedis);
		}
		return res;
	}
	
	/**
	 * <p>
	 * 通过key获取所有的field和value
	 * </p>
	 * 
	 * @param key
	 * @return
	 */
	public Map<String, String> hgetall(String key) {
		Jedis jedis = jedisPool.getResource();
		Map<String, String> res = null;
		try {
			res = jedis.hgetAll(key);
			if (expire != 0) {
				jedis.expire(key, expire);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jedisPool.returnResource(jedis);
		}
		return res;
	}
	
	/**
	 * del
	 * 
	 * @param key
	 */
	public void del(byte[] key) {
		Jedis jedis = jedisPool.getResource();
		try {
			jedis.del(key);
		} finally {
			jedisPool.returnResource(jedis);
		}
	}

	/**
	 * flush
	 */
	public void flushDB() {
		Jedis jedis = jedisPool.getResource();
		try {
			jedis.flushDB();
		} finally {
			jedisPool.returnResource(jedis);
		}
	}

	/**
	 * size
	 */
	public Long dbSize() {
		Long dbSize = 0L;
		Jedis jedis = jedisPool.getResource();
		try {
			dbSize = jedis.dbSize();
		} finally {
			jedisPool.returnResource(jedis);
		}
		return dbSize;
	}

	/**
	 * keys
	 * 
	 * @param regex
	 * @return
	 */
	public Set<byte[]> keys(String pattern) {
		Set<byte[]> keys = null;
		Jedis jedis = jedisPool.getResource();
		try {
			keys = jedis.keys(pattern.getBytes());
		} finally {
			jedisPool.returnResource(jedis);
		}
		return keys;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getExpire() {
		return expire;
	}

	public void setExpire(int expire) {
		this.expire = expire;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
