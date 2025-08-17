package com.santander.digitalcore.accounts.overdraftrs.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Creates the necessary objects to configure Springdoc OpenAPI. OpenAPI automates the
 * generation of API documentation using spring boot projects This class only will be
 * loaded with Non production profile.
 *
 * @author Santander Technology
 */
@Configuration(proxyBeanMethods = false)
class SwaggerConfig {

  /**
   * Creates Springdoc object where the API Documentation is grouped by package
   * and path pattern
   *
   * @return GroupedOpenApi
   */
  @Bean
  public GroupedOpenApi api() {
    return GroupedOpenApi.builder().group("api-web")
        .packagesToScan("com.santander.digitalcore.accounts.overdraftrs.web")
        .addOpenApiCustomizer(this.customerGlobalHeaderOpenApiCustomiser()).build();
  }

  /**
   * Creates Springdoc object where it is defined or described the API definition.
   *
   * @param appVersion version of the application
   * @return OpenAPI
   */
  @Bean
  public OpenAPI apiInfo(@Value("${darwin.logging.paas-app-version}") String appVersion) {
    return new OpenAPI().info(new Info().title("overdraftrs")
            .description("""
                        This API manages the DC_REASON_OVERDRAFT table.\
                        The API can be used for:\
                        Overdraft reasons:\
                        Create Overdraft reasons.\s
                        Update Overdraft reasons.\s
                        Get Overdraft reasons.\s
                        List Overdraft reasons.\s
                        Delete Overdraft reasons.\s
                        <a href="https://gluon.gs.corp/gluon/api-catalog/YY/detail/ZZZZZ"\
                        >Documentation of sgt-ap11085-overdraftrs microservice</a>\
                """)
            .version(appVersion))
        .components(new Components().addSecuritySchemes("BearerAuth",
            new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer")
                .bearerFormat("JWT")))
        .addSecurityItem(new SecurityRequirement().addList("BearerAuth"));
  }

  /**
   * Enable show common extensions in Swagger UI to show request parameters validations
   *
   * @param config the SwaggerUiConfigProperties object
   * @return the modified SwaggerUiConfigProperties object
   */
  @Bean
  @Primary
  public SwaggerUiConfigProperties swaggerUiConfig(SwaggerUiConfigProperties config) {
    config.setShowCommonExtensions(true);
    return config;
  }

  /**
   * Add all default architecture headers
   *
   * @return OpenApiCustomizer
   */
  public OpenApiCustomizer customerGlobalHeaderOpenApiCustomiser() {
    return openApi -> new OpenApiCustomiserIml().customise(openApi);
  }

}
