package io.opentelemetry.example.job;

/**
 * @author yangyi
 * @version v 1.0
 * @date 2025/9/25 19:45
 */
import com.xxl.job.core.handler.annotation.XxlJob;
import io.opentelemetry.example.feign.ProviderApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DemoJobHandler {


  @Autowired
  private ProviderApi providerApi;

  /**
   * 带参数的任务示例
   */
  @XxlJob("fixRepeatRewardsData")
  public void fixRepeatRewardsData() throws Exception {
    log.info("xxl-job fixRepeatRewardsData");
    providerApi.flights();
  }
}