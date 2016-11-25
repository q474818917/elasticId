package io.sequence;

import io.sequence.elastic.ElasticSequence;
import io.sequence.elastic.RedisOperator;

public class AppTest {
    
	
	public void test(){
		/*String hazelcastCounter = "web_requests_counter"; // eg.: web_requests_counter
		HazelcastInstance client = Hazelcast.newHazelcastInstance();
		IAtomicLong counter = client.getAtomicLong(hazelcastCounter);
		
		ExecutorService executorService = Executors.newFixedThreadPool(99);
		final CountDownLatch latch = new CountDownLatch(99);
		for(int i = 0; i < 99; i++){
			executorService.execute(new Runnable(){
				@Override
				public void run() {
					Long incrementedValue = counter.incrementAndGet();
					System.out.println(System.currentTimeMillis() / 1000 + String.format("%02d", incrementedValue));
					latch.countDown();
				}
			});
		}
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("打印完成!!!");
		counter.set(2);
		System.out.println(counter.incrementAndGet());*/
		ElasticSequence sequence = new ElasticSequence();
		RedisOperator redisOperator = new RedisOperator();
		sequence.setThreadCount(5);
		sequence.setRedisOperator(redisOperator);
		
		sequence.init();
		while(true){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(sequence.nextId());
		}
	}
	
}
