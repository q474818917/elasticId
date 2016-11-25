package io.sequence.elastic;

import org.joda.time.DateTime;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IAtomicLong;

public class ElasticSequence {
	
	private final static String COUNTER = "hazelcastCounter";
	private final static String SEQUENCEKEY = "sequence_key";
	
	private int threadCount;
	
	private HazelcastInstance client;
	
	private RedisOperator redisOperator;
	
	private IAtomicLong counter;
	
	public void init(){
		client = Hazelcast.newHazelcastInstance();
		counter = client.getAtomicLong(COUNTER);
		
		try{
			redisOperator.init();
			if(redisOperator.get(SEQUENCEKEY) != null && !redisOperator.get(SEQUENCEKEY).equals("0")){
				counter.set(Long.valueOf(redisOperator.get(SEQUENCEKEY)));
			}
		}catch(Throwable e){
			throw new ElasticException("Redis连接异常导致存储问题", e);
		}
		
		new ElasticRunnable().run();
	}
	
	public String nextId(){
		String prefixId = new DateTime().toString("yyMMdd") + (10 + (int)(Math.random()*90));
		this.overLimit(counter);
		long nextId = counter.incrementAndGet();
		
		try{
			redisOperator.set(SEQUENCEKEY, String.valueOf(nextId));
		}catch(Throwable e){
			throw new ElasticException("Redis连接异常导致读取问题", e);
		}
		
		return prefixId + String.format("%0" + threadCount + "d", nextId);
	}

	public int getThreadCount() {
		return threadCount;
	}

	public void setThreadCount(int threadCount) {
		this.threadCount = threadCount;
	}

	public HazelcastInstance getClient() {
		return client;
	}

	public void setClient(HazelcastInstance client) {
		this.client = client;
	}

	public RedisOperator getRedisOperator() {
		return redisOperator;
	}

	public void setRedisOperator(RedisOperator redisOperator) {
		this.redisOperator = redisOperator;
	}

	private void overLimit(IAtomicLong counter){
		String result = "1";
		for(int i = 0; i < threadCount; i++){
			result = result + "0";
		}
		if(counter.get() >= Long.valueOf(result) - 1){
			counter.set(1);
		}
	}
	
}
