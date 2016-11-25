package io.sequence.elastic;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IAtomicLong;

public class ElasticJob implements Job{
	
	private final static String COUNTER = "hazelcastCounter";

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		HazelcastInstance client = Hazelcast.newHazelcastInstance();
		IAtomicLong counter = client.getAtomicLong(COUNTER);
		counter.set(1);
		
		client.shutdown();
	}

}
