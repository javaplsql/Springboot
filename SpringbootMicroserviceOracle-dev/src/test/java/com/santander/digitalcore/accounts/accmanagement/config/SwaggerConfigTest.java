package com.santander.digitalcore.accounts.accmanagement.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class SwaggerConfigTest {

	@InjectMocks
	private SwaggerConfig swaggerConfig;

	@Test
	void apiBeanShouldReturnGroupedOpenApiWithCorrectConfiguration() {
		GroupedOpenApi groupedOpenApi = swaggerConfig.api();

		Assertions.assertNotNull(groupedOpenApi);
		Assertions.assertEquals("api-web", groupedOpenApi.getGroup());
		Assertions.assertTrue(groupedOpenApi.getPackagesToScan().contains("com.santander.digitalcore.accounts.accmanagement.web"));
	}

	@Test
	void apiInfoShouldReturnOpenAPIWithCorrectInfoAndComponents() {
		String appVersion = "1.0.0";

		OpenAPI openAPI = swaggerConfig.apiInfo(appVersion);

		Assertions.assertNotNull(openAPI);
		Assertions.assertEquals("accmanagement", openAPI.getInfo().getTitle());
		Assertions.assertEquals(appVersion, openAPI.getInfo().getVersion());
		Assertions.assertNotNull(openAPI.getComponents().getSecuritySchemes().get("BearerAuth"));
	}

	@Test
	void customerGlobalHeaderOpenApiCustomiserShouldReturnNonNullCustomizer() {
		OpenApiCustomizer customizer = swaggerConfig.customerGlobalHeaderOpenApiCustomiser();

		Assertions.assertNotNull(customizer);
	}
}
