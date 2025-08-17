package com.santander.digitalcore.accounts.overdraftrs.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OpenApiCustomiserImlTest {
  @InjectMocks
  private OpenApiCustomiserIml openApiCustomiser;
  @Mock
  private OpenAPI mockOpenAPI;

  @BeforeEach
  void setUp() {
    openApiCustomiser = new OpenApiCustomiserIml();
    mockOpenAPI = mock(OpenAPI.class);
    when(mockOpenAPI.getComponents()).thenReturn(new OpenAPI().getComponents());
    when(mockOpenAPI.getPaths()).thenReturn(new Paths());
  }

  @Test
  void addsRequiredHeadersToComponents() {
    when(mockOpenAPI.getComponents()).thenReturn(new io.swagger.v3.oas.models.Components());
    openApiCustomiser.customise(mockOpenAPI);

    Parameter clientIdHeader = mockOpenAPI.getComponents().getParameters().get("x-santander-client-id");
    Parameter acceptLanguageHeader = mockOpenAPI.getComponents().getParameters().get("accept-language");

    assertNotNull(clientIdHeader);
    assertTrue(clientIdHeader.getRequired());
    assertEquals("x-santander-client-id", clientIdHeader.getName());

    assertNotNull(acceptLanguageHeader);
    assertFalse(acceptLanguageHeader.getRequired());
    assertEquals("accept-language", acceptLanguageHeader.getName());
  }

  @Test
  void addsHeadersToAllOperationsInPaths() {
    Paths paths = new Paths();
    paths.addPathItem("/example", new io.swagger.v3.oas.models.PathItem());
    when(mockOpenAPI.getPaths()).thenReturn(paths);
    when(mockOpenAPI.getComponents()).thenReturn(new io.swagger.v3.oas.models.Components());
    openApiCustomiser.customise(mockOpenAPI);

    mockOpenAPI.getPaths().values().forEach(pathItem ->
        pathItem.readOperations().forEach(operation -> {
          assertTrue(operation.getParameters().stream()
              .anyMatch(param -> "#/components/parameters/x-santander-client-id".equals(param.get$ref())));
          assertTrue(operation.getParameters().stream()
              .anyMatch(param -> "#/components/parameters/accept-language".equals(param.get$ref())));
        })
    );
  }

  @Test
  void handlesNullPathsGracefully() {
    when(mockOpenAPI.getPaths()).thenReturn(null);
    when(mockOpenAPI.getComponents()).thenReturn(new io.swagger.v3.oas.models.Components());
    assertDoesNotThrow(() -> openApiCustomiser.customise(mockOpenAPI));
  }
}
