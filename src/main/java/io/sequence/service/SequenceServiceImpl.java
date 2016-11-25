package io.sequence.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.sequence.elastic.ElasticSequence;
import io.sequence.facade.SequenceService;

@Service("sequenceService")
public class SequenceServiceImpl implements SequenceService {
	
	private static Logger logger = LoggerFactory.getLogger(SequenceServiceImpl.class);
	
	@Autowired
	private ElasticSequence elasticSequence;
	
	@Override
	public String nextId() {
		String nextId = elasticSequence.nextId();
		logger.info("nextID is {}", nextId);
		return nextId;
	}
	
}
