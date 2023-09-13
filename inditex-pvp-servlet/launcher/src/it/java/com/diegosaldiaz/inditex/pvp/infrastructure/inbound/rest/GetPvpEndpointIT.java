package com.diegosaldiaz.inditex.pvp.infrastructure.inbound.rest;

import com.diegosaldiaz.inditex.pvp.infrastructure.inbound.dto.ErrorDto;
import com.diegosaldiaz.inditex.pvp.infrastructure.inbound.dto.GetPvp200ResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import static org.assertj.core.api.Assertions.assertThat;

class GetPvpEndpointIT extends BaseRestIT {

  @Test
  void testRequestedScenario1() {
    makeGetPvpEndpointCall(BRAND_ID, PRODUCT_ID, offsetDateTime(DATE_2020_06_14, TIME_10_00))
        .expectStatus().isOk()
        .expectBody(GetPvp200ResponseDto.class)
        .value(dto -> assertThat(dto.getData().getPvp()).isEqualTo(35.50));
  }

  @Test
  void testRequestedScenario2() {
    makeGetPvpEndpointCall(BRAND_ID, PRODUCT_ID, offsetDateTime(DATE_2020_06_14, TIME_16_00))
        .expectStatus().isOk()
        .expectBody(GetPvp200ResponseDto.class)
        .value(dto -> assertThat(dto.getData().getPvp()).isEqualTo(25.45));
  }

  @Test
  void testRequestedScenario3() {
    makeGetPvpEndpointCall(BRAND_ID, PRODUCT_ID, offsetDateTime(DATE_2020_06_14, TIME_21_00))
        .expectStatus().isOk()
        .expectBody(GetPvp200ResponseDto.class)
        .value(dto -> assertThat(dto.getData().getPvp()).isEqualTo(35.50));
  }

  @Test
  void testRequestedScenario4() {
    makeGetPvpEndpointCall(BRAND_ID, PRODUCT_ID, offsetDateTime(DATE_2020_06_15, TIME_10_00))
        .expectStatus().isOk()
        .expectBody(GetPvp200ResponseDto.class)
        .value(dto -> assertThat(dto.getData().getPvp()).isEqualTo(30.50));
  }

  @Test
  void testRequestedScenario5() {
    makeGetPvpEndpointCall(BRAND_ID, PRODUCT_ID, offsetDateTime(DATE_2020_06_16, TIME_21_00))
        .expectStatus().isOk()
        .expectBody(GetPvp200ResponseDto.class)
        .value(dto -> assertThat(dto.getData().getPvp()).isEqualTo(38.95));
  }

  @Test
  void testPriceNotFound() {
    makeGetPvpEndpointCall(BRAND_ID, PRODUCT_ID, offsetDateTime(DATE_2020_06_16.minusYears(1), TIME_21_00))
        .expectStatus().isNotFound()
        .expectBody(ErrorDto.class)
        .value(dto -> {
          assertThat(dto.getCode()).isEqualTo("ITX-001");
          assertThat(dto.getMessage()).isEqualTo("Price not found error for Brand [1] Product [35455] date[2019-06-16T21:00]");
          assertThat(dto.getRetryable()).isTrue();
        });
  }

  @Test
  void testPriorityCollision() {
    makeGetPvpEndpointCall(BRAND_ID, PRODUCT_ID, offsetDateTime(DATE_2021_06_15, TIME_21_00))
        .expectStatus().isEqualTo(HttpStatus.CONFLICT)
        .expectBody(ErrorDto.class)
        .value(dto -> {
          assertThat(dto.getCode()).isEqualTo("ITX-002");
          assertThat(dto.getMessage()).isEqualTo("[2] prices share the max Priority [1] for Brand [1] Product [35455] date[2021-06-15T21:00]. Unable to disambiguate");
          assertThat(dto.getRetryable()).isTrue();
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
