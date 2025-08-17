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
    description = "Retrieve the list of Overdraft Reason",
    summary = "Get Reason Overdraft list")
@ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "OK",
        content = @Content(schema = @Schema(type = "array", implementation = ReasonOverdraftDTO.class), examples = {
            @ExampleObject(
                value = """
                    [
                      {
                        "entity": "0000000049",
                        "brand": "SANTANDER",
                        "reasonCode": "01",
                        "startDateTime": "2025-08-08 00:45:51.031",
                        "endDateTime": "2025-08-12 00:55:51.031",
                        "descData": "NOMINA DOMICILIADA",
                        "settlementIndicator": "N",
                        "mandatory": "Y",
                        "defaultDate": "2025-09-20",
                        "months": 9.3,
                        "counter": 3,
                        "condition": "<=",
                        "creationUser": "ab000186",
                        "creationDateTime": "2025-08-08 18:53:39.229",
                        "lastUpdaterUser": "ab000186",
                        "lastUpdaterDateTime": "2025-08-08 18:53:39.229"
                      },
                      {
                        "entity": "0000000049",
                        "brand": "SANTANDER",
                        "reasonCode": "11",
                        "startDateTime": "2025-08-08 00:45:51.031",
                        "endDateTime": "2025-08-08 00:55:51.031",
                        "descData": "RECIBE ABONO EN TRATAMIENTOS PROPIOS DE HOY",
                        "settlementIndicator": "N",
                        "mandatory": "Y",
                        "defaultDate": "2025-09-20",
                        "months": 9.3,
                        "counter": 3,
                        "condition": "<=",
                        "creationUser": "ab000186",
                        "creationDateTime": "2025-08-08 12:14:25.364",
                        "lastUpdaterUser": "ab000186",
                        "lastUpdaterDateTime": "2025-08-08 12:14:25.364"
                      },
                      {
                        "entity": "0000000049",
                        "brand": "SANTANDER",
                        "reasonCode": "15",
                        "startDateTime": "2025-08-08 00:45:51.031",
                        "endDateTime": "2025-08-08 00:45:51.031",
                        "descData": "DESC ESPECIAL CUENTAS COLECTORAS O.N.C.E/L.A.E ",
                        "settlementIndicator": null,
                        "mandatory": "Y",
                        "defaultDate": "2025-09-10",
                        "months": 9.3,
                        "counter": 3,
                        "condition": "<=",
                        "creationUser": "ap11085-overdraftrs",
                        "creationDateTime": "2025-08-08 08:26:57.760",
                        "lastUpdaterUser": "ab000186",
                        "lastUpdaterDateTime": "2025-08-11 18:20:32.765"
                      },
                      {
                        "entity": "0000000049",
                        "brand": "SANTANDER",
                        "reasonCode": "48",
                        "startDateTime": "2025-07-28 13:54:51.031",
                        "endDateTime": "2025-07-30 17:43:25.910",
                        "descData": "Descripcion descubierto 48",
                        "settlementIndicator": "N",
                        "mandatory": "Y",
                        "defaultDate": "2025-09-10",
                        "months": 9,
                        "counter": 3,
                        "condition": "<=",
                        "creationUser": "testMotivo01",
                        "creationDateTime": "2025-07-30 17:36:53.359",
                        "lastUpdaterUser": "n11111",
                        "lastUpdaterDateTime": "2025-07-30 17:43:25.910"
                      },
                      {
                        "entity": "0000000049",
                        "brand": "SANTANDER",
                        "reasonCode": "49",
                        "startDateTime": "2025-07-28 13:54:51.031",
                        "endDateTime": "2025-07-30 18:34:47.432",
                        "descData": "Descripcion descubierto 49 Test12345",
                        "settlementIndicator": "N",
                        "mandatory": "Y",
                        "defaultDate": "2025-09-10",
                        "months": 9,
                        "counter": 3,
                        "condition": "<=",
                        "creationUser": "testMotivo01",
                        "creationDateTime": "2025-07-30 17:48:30.580",
                        "lastUpdaterUser": "n11111",
                        "lastUpdaterDateTime": "2025-07-30 18:34:47.428"
                      },
                      {
                        "entity": "0000000049",
                        "brand": "SANTANDER",
                        "reasonCode": "50",
                        "startDateTime": "2025-07-28 13:54:51.031",
                        "endDateTime": "2025-07-31 10:11:10.881",
                        "descData": "Descripcion descubierto 50 Test12345 bbb",
                        "settlementIndicator": "N",
                        "mandatory": "Y",
                        "defaultDate": "2025-09-10",
                        "months": 9,
                        "counter": 3,
                        "condition": "<=",
                        "creationUser": "testMotivo01",
                        "creationDateTime": "2025-07-30 18:35:04.955",
                        "lastUpdaterUser": "testMotivo01",
                        "lastUpdaterDateTime": "2025-07-31 10:03:44.586"
                      },
                      {
                        "entity": "0000000049",
                        "brand": "SANTANDER",
                        "reasonCode": "51",
                        "startDateTime": "2025-07-28 13:54:51.031",
                        "endDateTime": "2025-08-01 12:06:27.644",
                        "descData": "Descripcion descubierto 51 prueba bbb",
                        "settlementIndicator": "N",
                        "mandatory": "Y",
                        "defaultDate": null,
                        "months": 9,
                        "counter": 3,
                        "condition": "<=",
                        "creationUser": "testMotivo01",
                        "creationDateTime": "2025-08-01 12:05:28.101",
                        "lastUpdaterUser": "testMotivo01",
                        "lastUpdaterDateTime": "2025-08-01 12:05:59.918"
                      },
                      {
                        "entity": "0000000049",
                        "brand": "SANTANDER",
                        "reasonCode": "52",
                        "startDateTime": "2025-07-28 13:54:51.031",
                        "endDateTime": "2025-08-01 13:55:00.545",
                        "descData": "Descripcion descubierto 52 prueba bbb",
                        "settlementIndicator": "N",
                        "mandatory": "Y",
                        "defaultDate": "2025-09-10",
                        "months": 9,
                        "counter": 3,
                        "condition": "<=",
                        "creationUser": "USER_NOT_FOUND",
                        "creationDateTime": "2025-08-01 13:54:15.367",
                        "lastUpdaterUser": "USER_NOT_FOUND",
                        "lastUpdaterDateTime": "2025-08-01 13:55:00.545"
                      }
                    ]
                    """)
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
public @interface GetReasonOverdraftListDoc {

}
