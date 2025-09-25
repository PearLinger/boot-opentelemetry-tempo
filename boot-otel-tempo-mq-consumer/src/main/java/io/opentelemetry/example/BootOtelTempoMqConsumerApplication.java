package io.opentelemetry.example;

import com.elegoo.framework.mq.redis.config.ElegooRedisMQConsumerAutoConfiguration;
import com.elegoo.framework.mq.redis.config.ElegooRedisMQProducerAutoConfiguration;
import com.elegoo.framework.redis.config.ElegooCacheAutoConfiguration;
import com.elegoo.framework.redis.config.ElegooRedisAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = { ElegooRedisMQProducerAutoConfiguration.class, ElegooRedisMQConsumerAutoConfiguration.class,
		ElegooRedisAutoConfiguration.class, ElegooCacheAutoConfiguration.class})
public class BootOtelTempoMqConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(BootOtelTempoMqConsumerApplication.class, args);
	}

}
