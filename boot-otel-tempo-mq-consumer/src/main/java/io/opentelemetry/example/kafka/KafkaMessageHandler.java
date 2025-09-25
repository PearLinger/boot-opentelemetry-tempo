package io.opentelemetry.example.kafka;

import com.elegoo.framework.mq.kafka.annotation.KafKaAck;
import io.opentelemetry.example.feign.ProviderApi;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

/**
 * 处理设备和应用上下线状态的Kafka消息处理器
 *
 * @author yangyi
 * @version v 1.0
 * @date 2025/7/11 12:24
 */
@Slf4j
@Component
public class KafkaMessageHandler {
  @Autowired
  private ProviderApi providerApi;

  /**
   * 订阅设备和应用的上下线消息
   */
  @KafkaListener(topics = {"topentelemetry"}, groupId = "opentelemetry")
  @KafKaAck
  public void handle(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) {
    {
      try {
        Thread.sleep(3000);
        providerApi.flights();
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
      log.info("kafka-Received message: {}", record.value());
    }

  }
}
