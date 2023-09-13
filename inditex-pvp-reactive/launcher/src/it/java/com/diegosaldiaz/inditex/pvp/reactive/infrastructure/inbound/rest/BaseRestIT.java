package com.diegosaldiaz.inditex.pvp.reactive.infrastructure.inbound.rest;

import com.diegosaldiaz.inditex.pvp.reactive.BaseIT;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

public class BaseRestIT extends BaseIT {

  protected static final String PVP_PATH = "/brands/%s/products/%s/prices/pvp";

  protected static int BRAND_ID = 1;
  protected static long PRODUCT_ID = 35455;

  protected static LocalDate DATE_2020_06_14 = LocalDate.of(2020, 6, 14);
  protected static LocalDate DATE_2020_06_15 = LocalDate.of(2020, 6, 15);
  protected static LocalDate DATE_2020_06_16 = LocalDate.of(2020, 6, 16);
  protected static LocalDate DATE_2021_06_15 = LocalDate.of(2021, 6, 15);
  protected static LocalTime TIME_10_00 = LocalTime.of(10,0,0);
  protected static LocalTime TIME_16_00 = LocalTime.of(16,0,0);
  protected static LocalTime TIME_21_00 = LocalTime.of(21,0,0);

  @Autowired
  protected WebTestClient webTestClient;

  protected WebTestClient.ResponseSpec makeGetPvpEndpointCall(int brandId, long productId, OffsetDateTime date) {
    return webTestClient
        .method(HttpMethod.GET)
        .uri(uriBuilder ->
            uriBuilder.path(String.format(PVP_PATH, brandId, productId))
                .queryParam("date", date.toString())
                .build()
        )
        .accept(MediaType.APPLICATION_JSON)
        .exchange();
  }

  protected OffsetDateTime offsetDateTime(LocalDate date, LocalTime time) {
    return OffsetDateTime.of(date, time, ZoneOffset.UTC);
  }
}
