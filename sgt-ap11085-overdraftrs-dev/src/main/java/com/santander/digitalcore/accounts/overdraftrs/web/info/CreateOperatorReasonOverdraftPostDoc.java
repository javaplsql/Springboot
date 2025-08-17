package com.santander.digitalcore.accounts.overdraftrs.web.info;

import com.santander.digitalcore.accounts.overdraftrs.dto.OperatorReasonOverdraftRequest;
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
 * Annotation to document the code operator Overdraft create method
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Operation(
    description = "Creates an Overdraft code operator with the information provided in the request body.",
    summary = "Create Code Operator Overdraft")
@RequestBody(
    required = true,
    content = @Content(schema = @Schema(implementation = OperatorReasonOverdraftRequest.class),
        examples = @ExampleObject(value = "{\"operatorCode\":\"==\",\"operatorDescription\":\"IGUAL\"}")))
@ApiResponses(value = {
    @ApiResponse(responseCode = "201", description = "Created",
        content = @Content(schema = @Schema())),
    @ApiResponse(responseCode = "400", description = "Bad Request",
        content = @Content(schema = @Schema(implementation = CustomGluonErrorModel.class))),
    @ApiResponse(responseCode = "401", description = "Unauthorized",
        content = @Content(schema = @Schema(implementation = CustomGluonErrorModel.class))),
    @ApiResponse(responseCode = "403", description = "Forbidden",
        content = @Content(schema = @Schema(implementation = CustomGluonErrorModel.class))),
    @ApiResponse(responseCode = "404", description = "Bad Request",
        content = @Content(schema = @Schema(implementation = CustomGluonErrorModel.class))),
    @ApiResponse(responseCode = "500", description = "Internal Server Error",
        content = @Content(schema = @Schema(implementation = CustomGluonErrorModel.class))),
    @ApiResponse(responseCode = "503", description = "Service Unavailable",
        content = @Content(schema = @Schema(implementation = CustomGluonErrorModel.class))),
    @ApiResponse(responseCode = "504", description = "Gateway Timeout",
        content = @Content(schema = @Schema(implementation = CustomGluonErrorModel.class)))
})
public @interface CreateOperatorReasonOverdraftPostDoc {

}
