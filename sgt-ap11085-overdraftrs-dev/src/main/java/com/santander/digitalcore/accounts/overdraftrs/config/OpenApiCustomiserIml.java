package com.santander.digitalcore.accounts.overdraftrs.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.HeaderParameter;
import org.springdoc.core.customizers.OpenApiCustomizer;

/**
 * OpenApiCustomiserIml
 * 
 * @author SDS
 *
 */
public class OpenApiCustomiserIml implements OpenApiCustomizer {

	public void customise(OpenAPI openApi) {
		openApi.components(
				openApi
						.getComponents()
						
						.addParameters(
								"x-santander-client-id",
								new HeaderParameter()
										.required(Boolean.TRUE)
										.name("x-santander-client-id")
										.description("Client ID header")
										.schema(new StringSchema())
										.example("a1b30a84-7bf3-442e-84a0-e935d8163b5a"))
						
						.addParameters(
								"accept-language",
								new HeaderParameter()
										.required(Boolean.FALSE)
										.name("accept-language")
										.description("Language and country header.")
										.schema(new StringSchema()).example("es-ES"))
						
						);
		if (openApi.getPaths() != null) {
			openApi.getPaths().values().stream()
					.flatMap(pathItem -> pathItem.readOperations().stream())
					.forEach(
							operation -> {
								operation.addParametersItem(
										new HeaderParameter().$ref("#/components/parameters/x-santander-client-id"));
								operation.addParametersItem(
										new HeaderParameter().$ref("#/components/parameters/accept-language"));
							
							});
		}
	}

}
