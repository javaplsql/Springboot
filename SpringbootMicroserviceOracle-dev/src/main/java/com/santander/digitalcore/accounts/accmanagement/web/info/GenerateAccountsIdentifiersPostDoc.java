package com.santander.digitalcore.accounts.accmanagement.web.info;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.santander.digitalcore.accounts.accmanagement.model.genaccidentifiers.response.GenerateAccountIdentifiersPostResponse;
import com.santander.digitalcore.accounts.util.lib.core.error.custom.CustomGluonErrorModel;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

/**
 * GenerateAccountsIdentifiersPostDoc
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Operation( 
		description = 
		"""
		Generates account identifiers that can be used in a request sent to the 'POST /' operation.

		The bank centre and product must be provided in the request body.

		The response includes the created account identifiers.
			
		""",
			
		summary = "Generates account identifiers", 
		
		tags = { "" })
@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "Created", content = @Content(schema = @Schema(implementation = GenerateAccountIdentifiersPostResponse.class))),
		@ApiResponse(responseCode = "204", description = "No Content", content = @Content(schema = @Schema(implementation = Void.class))),	
		@ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = CustomGluonErrorModel.class))),
		@ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = CustomGluonErrorModel.class))),
		@ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = CustomGluonErrorModel.class))),
		@ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = CustomGluonErrorModel.class))),
		@ApiResponse(responseCode = "415", description = "Unsupported Media Type",content = @Content(schema = @Schema(implementation = CustomGluonErrorModel.class))),
		@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = CustomGluonErrorModel.class))),
		@ApiResponse(responseCode = "503", description = "Service Unavailable", content = @Content(schema = @Schema(implementation = CustomGluonErrorModel.class))),
		@ApiResponse(responseCode = "504", description = "Gateway Timeout", content = @Content(schema = @Schema(implementation = CustomGluonErrorModel.class)))

})
public @interface GenerateAccountsIdentifiersPostDoc {
	
}
