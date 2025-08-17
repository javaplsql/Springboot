package com.santander.digitalcore.accounts.overdraftrs.web.info;

import com.santander.digitalcore.accounts.overdraftrs.dto.ReasonOverdraftDTO;
import com.santander.digitalcore.accounts.util.lib.core.error.custom.CustomGluonErrorModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * CreateReasonOverdraftPostDoc
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Operation(
		description = "Creates an Overdraft reason with the information provided in the request body.",
		summary = "Create Reason Overdraft")
@RequestBody(
		required = true,
		content = @Content(schema = @Schema(implementation = ReasonOverdraftDTO.class),
				examples = @ExampleObject(value = "{\"entity\":\"0000000049\"," +
						"\"brand\":\"SANTANDER\",\"reasonCode\":\"15\"," +
						"\"startDateTime\":\"2025-08-08 00:44:51.031\",\"descData\":\"Descripcion descubierto\"," +
						"\"settlementIndicator\":\"N\",\"mandatory\":\"Y\"," +
						"\"defaultDate\":\"2025-09-10\",\"months\":9.25," +
						"\"counter\":3,\"condition\":\"<=\",\"endDateTime\":\"2025-08-08 00:45:51.031\"}")))
@ApiResponses(value = {
		@ApiResponse(responseCode = "204", description = "No Content",
				content = @Content(schema = @Schema(implementation = Void.class))),
		@ApiResponse(responseCode = "400", description = "Bad request",
				content = @Content(schema = @Schema(implementation = CustomGluonErrorModel.class))),
		@ApiResponse(responseCode = "401", description = "Unauthorized",
				content = @Content(schema = @Schema(implementation = CustomGluonErrorModel.class))),
		@ApiResponse(responseCode = "403", description = "Forbidden",
				content = @Content(schema = @Schema(implementation = CustomGluonErrorModel.class))),
		@ApiResponse(responseCode = "404", description = "Not Found",
				content = @Content(schema = @Schema(implementation = CustomGluonErrorModel.class))),
		@ApiResponse(responseCode = "415", description = "Unsupported Media Type",
				content = @Content(schema = @Schema(implementation = CustomGluonErrorModel.class))),
		@ApiResponse(responseCode = "500", description = "Internal Server Error",
				content = @Content(schema = @Schema(implementation = CustomGluonErrorModel.class))),
		@ApiResponse(responseCode = "503", description = "Service Unavailable",
				content = @Content(schema = @Schema(implementation = CustomGluonErrorModel.class))),
		@ApiResponse(responseCode = "504", description = "Gateway Timeout",
				content = @Content(schema = @Schema(implementation = CustomGluonErrorModel.class)))

})
public @interface CreateReasonOverdraftPostDoc {
	
}
