package com.developing.app.accounts.accountmanaging.config;

import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

/**
 * Creates the necessary objects to configure Springdoc OpenAPI. OpenAPI
 * automates the generation of
 * API documentation using spring boot projects This class only will be loaded
 * with Non production
 * profile.
 *
 * @author java Technology
 */
/*
 * This class may need manual changes.
 * Only if you have multiple Docket beans replace them with GroupedOpenApi
 * beans.
 * review: https://springdoc.org/#migrating-from-springfox
 */
@Configuration(proxyBeanMethods = false)
@Order(105)
public class SwaggerConfig {

  /**
   * Creates Springdoc object where the API Documentation is grouped by package
   * and path pattern
   *
   * @return GroupedOpenApi
   */
  @Bean
  public GroupedOpenApi api() {
    return GroupedOpenApi.builder().group("api-web")
        .packagesToScan("com.java.developing.accounts.accountmanaging.web")
        .addOpenApiCustomizer(this.customerGlobalHeaderOpenApiCustomiser()).build();
  }

  /**
   * Creates Springdoc object where it is defined or described the API definition.
   *
   * @param appVersion version of the application
   * @return OpenAPI
   */
  @Bean
  public OpenAPI apiInfo(@Value("${plsql.logging.paas-app-version}") String appVersion) {
    return new OpenAPI().info(new Info().title("accountmanaging")
            .description("""
                This API manages a physical or business customer's accounts.\
                The API can be used for:\
                Accounts:\
                Generate account identifiers.\s
                <a href="https://atom.gs.corp/atom/api-catalog/92/detail/12135"\
                >Documentation of acc-management microservice</a>\
                """)
            .version(appVersion))
        .components(new Components().addSecuritySchemes("BearerAuth",
            new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer")
                .bearerFormat("JWT")))
        .addSecurityItem(new SecurityRequirement().addList("BearerAuth"));
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
