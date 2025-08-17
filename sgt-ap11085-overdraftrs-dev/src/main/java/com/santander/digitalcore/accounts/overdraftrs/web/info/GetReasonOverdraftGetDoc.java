package com.santander.digitalcore.accounts.overdraftrs.web.info;


import com.santander.digitalcore.accounts.overdraftrs.dto.ReasonOverdraftDTO;
import com.santander.digitalcore.accounts.util.lib.core.error.custom.CustomGluonErrorModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * GetReasonOverdraftGetDoc
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Operation(
		description = "Retrieve the detail  of an Overdraft Reason",
		summary = "Get Reason Overdraft detail")
@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "OK",
				content = @Content(schema = @Schema(type = "array", implementation = ReasonOverdraftDTO.class), examples = {
						@ExampleObject(
								value = "[{\"entity\":\"0000000049\",\"brand\":\"SANTANDER\"," +
										"\"reasonCode\":\"01\",\"startDateTime\":\"2025-08-08 00:45:51.031\"," +
										"\"endDateTime\":\"2025-08-12 00:55:51.031\"," +
										"\"descData\":\"NOMINA DOMICILIADA\"," +
										"\"settlementIndicator\":\"N\",\"mandatory\":\"Y\"," +
										"\"defaultDate\":\"2025-09-20\",\"months\":9.3," +
										"\"counter\":3,\"condition\":\"<=\",\"creationUser\":\"ab000186\"," +
										"\"creationDateTime\":\"2025-08-08 18:53:39.229\"," +
										"\"lastUpdaterUser\":\"ab000186\"," +
										"\"lastUpdaterDateTime\":\"2025-08-08 18:53:39.229\"}]")
				})),
		@ApiResponse(responseCode = "204", description = "No Content",
				content = @Content(schema = @Schema())),
		@ApiResponse(responseCode = "401", description = "Unauthorized",
				content = @Content(schema = @Schema(implementation = CustomGluonErrorModel.class))),
		@ApiResponse(responseCode = "403", description = "Forbidden",
				content = @Content(schema = @Schema(implementation = CustomGluonErrorModel.class))),
		@ApiResponse(responseCode = "500", description = "Internal Server Error",
				content = @Content(schema = @Schema(implementation = CustomGluonErrorModel.class))),
		@ApiResponse(responseCode = "503", description = "Service Unavailable",
				content = @Content(schema = @Schema(implementation = CustomGluonErrorModel.class))),
		@ApiResponse(responseCode = "504", description = "Gateway Timeout",
				content = @Content(schema = @Schema(implementation = CustomGluonErrorModel.class)))
})
public @interface GetReasonOverdraftGetDoc {
	
}
