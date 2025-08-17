package com.santander.digitalcore.accounts.overdraftrs.web.info;

import com.santander.digitalcore.accounts.overdraftrs.dto.OperatorReasonOverdraftRequest;
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
 * Annotation to document the operator reason Overdraft get method
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Operation(
    description = "Retrieve the list of Overdraft Operator Reason",
    summary = "Get Operator Reason Overdraft list")
@ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "OK",
        content = @Content(schema = @Schema(type = "array", implementation = OperatorReasonOverdraftRequest.class),
            examples = {
                @ExampleObject(
                    value = "[{\"operatorCode\":\"!=\",\"operatorDescription\":\"DISTINTO\"},{\"operatorCode\":\"==\",\"operatorDescription\":\"IGUAL\"},{\"operatorCode\":\"<=\",\"operatorDescription\":\"MENOR O IGUAL\"},{\"operatorCode\":\"<\",\"operatorDescription\":\"MENOR\"}]")
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
public @interface GetOperatorReasonOverdraftDoc {

}
