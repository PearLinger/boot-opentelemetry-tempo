package io.opentelemetry.example.feign;

import io.opentelemetry.example.constants.ApiConstants;
import io.opentelemetry.example.entity.Flight;
import jakarta.annotation.security.PermitAll;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = ApiConstants.NAME)
public interface ProviderApi {

    /**
     * app事件数据发送
     * @return
     */
    @PostMapping("/flights")
    @PermitAll
    List<Flight>  flights();



}
