package io.opentelemetry.example.flight.service;

import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import io.opentelemetry.example.flight.model.Flight;

@Service
public class FlightService {

	private static final Logger LOGGER = LoggerFactory.getLogger(FlightService.class);

	private StringRedisTemplate stringRedisTemplate;

	public FlightService(StringRedisTemplate stringRedisTemplate) {
		this.stringRedisTemplate = stringRedisTemplate;
	}

	public void process(Flight flight) {
		LOGGER.info("Processing : {}", flight);
		valExpire("valExpireKey");		
	}

	private void valExpire(String key) {
		stringRedisTemplate.opsForValue().set(key, "SomeValue", Duration.ofSeconds(1));
	}
}
