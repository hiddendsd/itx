package com.diegosaldiaz.inditex.pvp.reactive.infrastructure.inbound.route;

import com.diegosaldiaz.inditex.pvp.reactive.infrastructure.inbound.handler.PvpHandler;
import com.diegosaldiaz.inditex.pvp.reactive.infrastructure.inbound.validation.DateValidation;
import com.diegosaldiaz.inditex.pvp.reactive.infrastructure.inbound.validation.NoNegativeIntegerValidation;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.server.ServerRequest;

@ExtendWith(MockitoExtension.class)
class PvpRouterTest {

  @Test
  void testRoute() {
    var mockHandler = mock(PvpHandler.class);
    var mockIntValidator = mock(NoNegativeIntegerValidation.class);
    var mockDateValidator = mock(DateValidation.class);
    var routerFunction = new PvpRouter(mockIntValidator, mockDateValidator).route(mockHandler);

    // Set up a mock request
    ServerRequest request = mock(ServerRequest.class);
    when(request.method()).thenReturn(HttpMethod.GET);
    when(request.path()).thenReturn("/brands/1/products/2/prices/pvp");

    // Call the router function
    routerFunction.route(request).subscribe();

    // Verify that the correct method on the mock handler was called
    verify(mockHandler).get(request);
  }

}