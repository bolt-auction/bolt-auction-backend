/*
package com.neoga.gateway.config;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.*;
import org.springframework.context.annotation.Bean;

public class RibbonConfig {

  @Bean
  public IPing ribbonPing(final IClientConfig config) {
    return new PingUrl(false, "/health");
  }

  @Bean
  public IRule ribbonRule(final IClientConfig config) {
    return new AvailabilityFilteringRule();
  }

}
*/
