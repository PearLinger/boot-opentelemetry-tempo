package io.opentelemetry.example.config;

import com.xxl.job.core.executor.XxlJobExecutor;
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import jakarta.annotation.Resource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
  *
  *@author yangyi
  *@version v 1.0
  *@date 2025/9/25 20:22
  */
@Configuration
public class XxlJobConfig {

  @Resource
  XxlJobProperties properties;
  @Bean
  public XxlJobExecutor xxlJobExecutor() {
    XxlJobProperties.AdminProperties admin = properties.getAdmin();
    XxlJobProperties.ExecutorProperties executor = properties.getExecutor();

    // 初始化执行器
    XxlJobExecutor xxlJobExecutor = new XxlJobSpringExecutor();
    xxlJobExecutor.setIp(executor.getIp());
    xxlJobExecutor.setPort(executor.getPort());
    xxlJobExecutor.setAppname(executor.getAppName());
    xxlJobExecutor.setLogPath(executor.getLogPath());
    xxlJobExecutor.setLogRetentionDays(executor.getLogRetentionDays());
    xxlJobExecutor.setAdminAddresses(admin.getAddresses());
    xxlJobExecutor.setAccessToken(properties.getAccessToken());
    return xxlJobExecutor;
  }
}
