package com.diegosaldiaz.inditex.pvp;

import com.diegosaldiaz.inditex.pvp.infrastructure.inbound.dto.ErrorDto;
import com.diegosaldiaz.inditex.pvp.infrastructure.inbound.dto.GetPvp200ResponseDto;
import java.net.URI;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.util.UriBuilder;
import static org.assertj.core.api.Assertions.assertThat;

// TODO Refactor clase base
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase
@SpringBootTest(classes = InditexPvpServletMicroservice.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class GetPvpEndpointIT {

  private static final String PATH = "/brands/1/products/35455/prices/pvp";
  @Autowired
  protected WebTestClient webTestClient;

  @Test
  void testRequestedScenario1() {
    webTestClient
        .method(HttpMethod.GET)
        .uri(uriBuilder -> buildUri(uriBuilder, PATH, "2020-06-14T10:00:00Z"))
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
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
    webTestClient
        .method(HttpMethod.GET)
        .uri(uriBuilder -> buildUri(uriBuilder, PATH, "2020-06-14T16:00:00Z"))
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
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
    webTestClient
        .method(HttpMethod.GET)
        .uri(uriBuilder -> buildUri(uriBuilder, PATH, "2020-06-14T21:00:00Z"))
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
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
    webTestClient
        .method(HttpMethod.GET)
        .uri(uriBuilder -> buildUri(uriBuilder, PATH, "2020-06-15T10:00:00Z"))
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
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
    webTestClient
        .method(HttpMethod.GET)
        .uri(uriBuilder -> buildUri(uriBuilder, PATH, "2020-06-16T21:00:00Z"))
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
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
    webTestClient
        .method(HttpMethod.GET)
        .uri(uriBuilder -> buildUri(uriBuilder, PATH, "2023-09-11T00:00:00Z"))
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
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

  private URI buildUri(UriBuilder builder, String path, String date) {
    return builder.path(path)
        .queryParam("date",date)
        .build();
  }
}
