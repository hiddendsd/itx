package com.diegosaldiaz.inditex.pvp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Inditex PVP Servlet Microservice.
 */
@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class InditexPvpServletMicroservice {
  public static void main(String[] args) {
    SpringApplication.run(InditexPvpServletMicroservice.class, args);
  }
}
