package com.diegosaldiaz.inditex.pvp.infrastructure.inbound.rest;

import com.diegosaldiaz.inditex.pvp.infrastructure.inbound.dto.ErrorDto;
import com.diegosaldiaz.inditex.pvp.infrastructure.inbound.dto.GetPvp200ResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import static org.assertj.core.api.Assertions.assertThat;

class GetPvpEndpointIT extends BaseRestIT {

  @Test
  void testRequestedScenario1() {
    makeGetPvpEndpointCall(BRAND_ID, PRODUCT_ID, offsetDateTime(DATE_2020_06_14, TIME_10_00))
        .expectStatus().isOk()
        .expectBody(GetPvp200ResponseDto.class)
        .value(dto -> {
          var pvpDto = dto.getData();
          assertThat(pvpDto.getPvp()).isEqualTo(35.50);
          // TODO complete validations
        });
  }

  @Test
  void testRequestedScenario2() {
    makeGetPvpEndpointCall(BRAND_ID, PRODUCT_ID, offsetDateTime(DATE_2020_06_14, TIME_16_00))
        .expectStatus().isOk()
        .expectBody(GetPvp200ResponseDto.class)
        .value(dto -> {
          var pvpDto = dto.getData();
          assertThat(pvpDto.getPvp()).isEqualTo(25.45);
          // TODO complete validations
        });
  }

  @Test
  void testRequestedScenario3() {
    makeGetPvpEndpointCall(BRAND_ID, PRODUCT_ID, offsetDateTime(DATE_2020_06_14, TIME_21_00))
        .expectStatus().isOk()
        .expectBody(GetPvp200ResponseDto.class)
        .value(dto -> {
          var pvpDto = dto.getData();
          assertThat(pvpDto.getPvp()).isEqualTo(35.50);
          // TODO complete validations
        });
  }

  @Test
  void testRequestedScenario4() {
    makeGetPvpEndpointCall(BRAND_ID, PRODUCT_ID, offsetDateTime(DATE_2020_06_15, TIME_10_00))
        .expectStatus().isOk()
        .expectBody(GetPvp200ResponseDto.class)
        .value(dto -> {
          var pvpDto = dto.getData();
          assertThat(pvpDto.getPvp()).isEqualTo(30.50);
          // TODO complete validations
        });
  }

  @Test
  void testRequestedScenario5() {
    makeGetPvpEndpointCall(BRAND_ID, PRODUCT_ID, offsetDateTime(DATE_2020_06_16, TIME_21_00))
        .expectStatus().isOk()
        .expectBody(GetPvp200ResponseDto.class)
        .value(dto -> {
          var pvpDto = dto.getData();
          assertThat(pvpDto.getPvp()).isEqualTo(38.95);
          // TODO complete validations
        });
  }

  @Test
  void testPriceNotFound() {
    makeGetPvpEndpointCall(BRAND_ID, PRODUCT_ID, offsetDateTime(DATE_2020_06_16.minusYears(1), TIME_21_00))
        .expectStatus().isNotFound()
        .expectBody(ErrorDto.class)
        .value(dto -> {
          assertThat(dto.getCode()).isNull();
          assertThat(dto.getMessage()).isNull();
          assertThat(dto.getRetryable()).isNull();
        });
  }

  @Test
  void testWrongEndpoint() {
    webTestClient
        .method(HttpMethod.GET)
        .uri("/unknown-endpoint")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isNotFound();
  }
}
