package io.opentelemetry.example.flight.messaging;

import com.elegoo.framework.mq.kafka.producer.KafkaManager;
import java.util.concurrent.ExecutorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.opentelemetry.example.flight.model.Flight;
import io.opentelemetry.example.flight.service.FlightService;

@Component
public class FlightMQConsumer {

	private static final Logger LOGGER = LoggerFactory.getLogger(FlightMQConsumer.class);
	@Autowired
	@Qualifier("io-executor")
	private ExecutorService executorService;
	@Autowired
	private FlightService flightService;

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private KafkaManager kafkaManager;

	@RabbitListener(queues = "#{'${rabbitmq.flight.received.queue}'}")
	public void consumeMessage(String flightMessage) {
		try {
			LOGGER.trace("Message received: {} ", flightMessage);
			Flight flight = create(flightMessage);
			flightService.process(flight);
			LOGGER.debug("Message processed successfully");
			executorService.execute(()->{
				LOGGER.debug("Message Thread LOG");
			});
			kafkaManager.sendAsync("topentelemetry", flightMessage);
			Thread.sleep(3000);
		} catch (Exception e) {
			LOGGER.error("Unnable to process the Message", e);
		}
	}

	private Flight create(String flightMessage) throws JsonProcessingException {
		return mapper.readValue(flightMessage, Flight.class);
	}
}
