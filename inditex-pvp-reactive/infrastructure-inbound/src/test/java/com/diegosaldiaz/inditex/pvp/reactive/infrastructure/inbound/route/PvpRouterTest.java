package com.diegosaldiaz.inditex.pvp.reactive.infrastructure.inbound.route;

import com.diegosaldiaz.inditex.pvp.infrastructure.inbound.dto.GetPvp200ResponseDto;
import com.diegosaldiaz.inditex.pvp.reactive.application.port.inbound.GetPvpUseCasePort;
import com.diegosaldiaz.inditex.pvp.reactive.infrastructure.inbound.exception.ValidationException;
import com.diegosaldiaz.inditex.pvp.reactive.infrastructure.inbound.handler.PvpHandler;
import com.diegosaldiaz.inditex.pvp.reactive.infrastructure.inbound.mapper.PriceDomainModelToDtoMapper;
import com.diegosaldiaz.inditex.pvp.reactive.infrastructure.inbound.validation.DateValidation;
import com.diegosaldiaz.inditex.pvp.reactive.infrastructure.inbound.validation.NoNegativeIntegerValidation;
import io.micrometer.observation.ObservationRegistry;
import java.time.Instant;
import reactor.core.publisher.Mono;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {PvpRouter.class, PvpHandler.class})
@WebFluxTest
class PvpRouterTest {

  private static final Instant DATE = Instant.parse("2020-01-02T03:04:05Z");
  private static final int BRAND_ID = 1;
  private static final long PRODUCT_ID = 2L;

  @MockBean(name="observationRegistry")
  private ObservationRegistry registry;

  @Autowired
  private ApplicationContext context;

  @MockBean(name="getPvpUseCasePort")
  private GetPvpUseCasePort getPvpPort;

  @MockBean(name="piceDomainModelToDtoMapper")
  private PriceDomainModelToDtoMapper mapper;

  @MockBean(name="noNegativeIntegerValidation")
  private NoNegativeIntegerValidation noNegativeIntegerValidation;

  @MockBean(name="dateValidation")
  private DateValidation dateValidation;

  private WebTestClient testClient;

  @BeforeEach
  public void setUp() {
    testClient = WebTestClient.bindToApplicationContext(context).build();
  }

  @Test
  void testRouter() {
    Mockito.when(getPvpPort.query(BRAND_ID, PRODUCT_ID, DATE)).thenReturn(Mono.empty());
    Mockito.when(registry.observationConfig()).thenReturn(new ObservationRegistry.ObservationConfig());
    testClient
        .get().uri("/pvp-api/v1/brands/1/products/2/prices/pvp?date=2020-01-02T03:04:05Z")
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody(GetPvp200ResponseDto.class);

    Mockito.verify(getPvpPort, Mockito.times(1)).query(BRAND_ID, PRODUCT_ID, DATE);
  }

  @Test
  void testRouterFilterFail() {
    Mockito.doThrow(ValidationException.class).when(noNegativeIntegerValidation).accept(anyString(), anyString());
    testClient
        .get().uri("/pvp-api/v1/brands/-1/products/2/prices/pvp?date=2020-01-02T03:04:05Z")
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .exchange();
    Mockito.verify(getPvpPort, Mockito.never()).query(anyInt(), anyLong(), any(Instant.class));
  }

}
