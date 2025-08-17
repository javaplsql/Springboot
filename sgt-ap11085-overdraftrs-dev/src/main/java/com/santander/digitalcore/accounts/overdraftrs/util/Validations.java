package com.santander.digitalcore.accounts.overdraftrs.util;

import com.santander.digitalcore.accounts.overdraftrs.mapper.ReasonOverdraftMapper;
import com.santander.digitalcore.accounts.util.lib.core.exceptions.BadRequestDarwinExceptionLeancore;
import com.santander.digitalcore.accounts.util.lib.core.exceptions.ConflictDarwinExceptionLeancore;
import com.santander.digitalcore.accounts.util.lib.core.validation.ValidatorLeancore;
import com.santander.digitalcore.accounts.overdraftrs.dto.ReasonOverdraftDTO;
import com.santander.digitalcore.accounts.util.lib.db.model.entity.ReasonOverdraftEntity;

import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Validaciones para motivos de descubierto.
 * Esta clase contiene métodos estáticos para realizar validaciones
 * relacionadas con la creación, actualización, obtención, listado o eliminación
 * de motivos de descubierto.
 */
public class Validations extends ValidatorLeancore {

  private static final ReasonOverdraftMapper reasonOverdraftMapper = Mappers.getMapper(ReasonOverdraftMapper.class);

  /**
   * Realiza las validaciones necesarias para la creación, actualización,
   * obtención, listado o eliminación de un motivo de descubierto.
   *
   * @param operation      El tipo de operación a realizar (CREATE, GET, UPDATE, DELETE, LIST).
   * @param pathEntity     El identificador de la entidad en el path.
   * @param pathBrand      El identificador de la marca en el path.
   * @param pathReasonCode El código del motivo de descubierto en el path.
   */
  public static void doValidationsWithoutRequestBody(OperationType operation,
                                                     String pathEntity, String pathBrand, String pathReasonCode) {
    switch (operation) {
      case LIST:
        // Validaciones para LIST si aplica
        break;
      case GET:
        // Validaciones para GET si aplica
        // Validación de parámetros
        getOperationpathParamsValidation(pathEntity, pathBrand, pathReasonCode);
        break;
      case DELETE:
        // Validaciones para DELETE si aplica
        // Validación de parámetros
        deleteOperationPathParamsValidation(pathEntity, pathBrand, pathReasonCode);
        break;
      default:
        throw new IllegalArgumentException("Operación no soportada");
    }
  }

  /**
   * Realiza las validaciones necesarias para la actualización de un motivo de descubierto.
   *
   * @param pathEntity     El identificador de la entidad en el path.
   * @param pathBrand      El identificador de la marca en el path.
   * @param pathReasonCode El código del motivo de descubierto en el path.
   */
  private static void updateOperationPathParamsValidation(String pathEntity, String pathBrand, String pathReasonCode) {
    if (pathReasonCode == null || pathReasonCode.isEmpty()) {
      throw new IllegalArgumentException("OVERDRAFT-REASON-0013");
    }
    if (pathEntity == null || pathEntity.isEmpty()) {
      throw new IllegalArgumentException("OVERDRAFT-REASON-0014");
    }
    if (pathBrand == null || pathBrand.isEmpty()) {
      throw new IllegalArgumentException("OVERDRAFT-REASON-0015");
    }
  }

  /**
   * Realiza las validaciones necesarias para la eliminación de un motivo de descubierto.
   *
   * @param pathEntity     El identificador de la entidad en el path.
   * @param pathBrand      El identificador de la marca en el path.
   * @param pathReasonCode El código del motivo de descubierto en el path.
   */
  private static void deleteOperationPathParamsValidation(String pathEntity, String pathBrand, String pathReasonCode) {
    if (pathReasonCode == null || pathReasonCode.isEmpty()) {
      throw new IllegalArgumentException("OVERDRAFT-REASON-0010");
    }
    if (pathEntity == null || pathEntity.isEmpty()) {
      throw new IllegalArgumentException("OVERDRAFT-REASON-0011");
    }
    if (pathBrand == null || pathBrand.isEmpty()) {
      throw new IllegalArgumentException("OVERDRAFT-REASON-0012");
    }
  }

  /**
   * Realiza las validaciones necesarias para la obtención de un motivo de descubierto.
   *
   * @param pathEntity     El identificador de la entidad en el path.
   * @param pathBrand      El identificador de la marca en el path.
   * @param pathReasonCode El código del motivo de descubierto en el path.
   */
  private static void getOperationpathParamsValidation(String pathEntity, String pathBrand, String pathReasonCode) {
    if (pathReasonCode == null || pathReasonCode.isEmpty()) {
      throw new IllegalArgumentException("OVERDRAFT-REASON-0007");
    }
    if (pathEntity == null || pathEntity.isEmpty()) {
      throw new IllegalArgumentException("OVERDRAFT-REASON-0008");
    }
    if (pathBrand == null || pathBrand.isEmpty()) {
      throw new IllegalArgumentException("OVERDRAFT-REASON-0009");
    }
  }

  /**
   * Realiza las validaciones necesarias para la
   * creación, actualización, obtenciónm listado o eliminación de un motivo de descubierto.
   *
   * @param request        El objeto DTO que contiene los datos del motivo de descubierto.
   * @param operation      El tipo de operación a realizar (CREATE, GET, UPDATE, DELETE, LIST).
   * @param pathEntity     El identificador de la entidad en el path.
   * @param pathBrand      El identificador de la marca en el path.
   * @param pathReasonCode El código del motivo de descubierto en el path.
   */
  public static void doValidations(ReasonOverdraftDTO request,
                                   OperationType operation,
                                   String pathEntity, String pathBrand, String pathReasonCode) {
    switch (operation) {
      case CREATE:
        createValidation(request);
        break;
      case UPDATE:
        // Validaciones para UPDATE si aplica
        // Validación de parámetros
        updateOperationPathParamsValidation(pathEntity, pathBrand, pathReasonCode);
        break;
      case LIST:
        // Validaciones para LIST si aplica
        break;
      default:
        throw new IllegalArgumentException("Operación no soportada");
    }
  }

  /**
   * Realiza las validaciones necesarias para la creación de un motivo de desscubierto.
   *
   * @param request El objeto DTO que contiene los datos del motivo de descubierto.
   */
  public static void createValidation(ReasonOverdraftDTO request) {

    // ENTITY &  BRAND
    // Verificar contenido con la tabla BBCC_ENTITY_BRAND. Obligatorio.
    if (request.getReasonCode() == null || request.getReasonCode().isEmpty()) {
      throw new BadRequestDarwinExceptionLeancore("OVERDRAFT-REASON-0016");
    }
    if (request.getEntity() == null || request.getEntity().isEmpty()) {
      throw new BadRequestDarwinExceptionLeancore("OVERDRAFT-REASON-0017");
    }
    if (request.getBrand() == null || request.getBrand().isEmpty()) {
      throw new BadRequestDarwinExceptionLeancore("OVERDRAFT-REASON-0018");
    }
    // REASON_CODE
    // Verificar longitud VARCHAR(4) Obligatorio.
    validationReasonCode(request.getReasonCode());

    // DESCDATA
    // Obligatorio. No pedir en el bodyRequest. Recuperar la descripcion de la siguiente tabla:
    // DC_CODE_REASON_HOLD. Cada REASON_CODE tiene una descripción asociada.


    validationDate(reasonOverdraftMapper.mapToDateTime(request.getStartDateTime()),
        reasonOverdraftMapper.mapToDateTime(request.getEndDateTime()));
    // START_DATE_TIME (Inicio vigencia)
    // Obligatorio.  Recuperar fecha actual con Java y verificar START_DATE_TIME >= FECHA_ACTUAL

    // END_DATE_TIME (Fin vigencia)
    // Obligatorio. Recuperar fecha actual con Java y verificar END_DATE_TIME >= FECHA_ACTUAL.
    // ---> 9999-12-31 23:59:59.999 fecha válida.


    // MANDATORY
    // Obligatorio. Validar que sea "Y" "y" "N" "n". Si es minuscula, convertir a mayúscula.
    validationMandatory(request.getMandatory());

    // DEFAULTDATE
    // Obligatorio solo si mandatory es "Y" o "y".
    // Recuperar fecha actual con Java y verificar DEFAULTDATE >= FECHA_ACTUAL
    validationDefaultDate(reasonOverdraftMapper.mapToDate(request.getDefaultDate()), request.getMandatory());

    // MONTHS (Llevaremos la cantidad de meses que tenemos que calcular para controlar la fecha de fin de vigencia)
    // Parte entera = MESES
    // Parte decimal = DÍAS
    // Opcional. Validar que la parte decimal sea máximo 30. NUMBER(4,2) Si se informa con 99,99 se trata como null.
    BigDecimal months = request.getMonths();
    if (months != null) {
      validateMonths(request.getMonths());
    } else {
      request.setMonths(new BigDecimal("99.99"));
    }

    // COUNTER
    // Opcional. Verificar que sea un numero positivo. [0,9999] NUMER(4,0)
    validateCounter(request.getCounter());

    // CONDICION
    // Opcional: Validar que existe en DC_OPERATOR_REASON_OVERDRAFT. Verificado en el servicio.

    // SETTLEMENT_INDICATOR
    // Obligatorio. Validar que sea "Y" "y" "N" "n". Si es minuscula, convertir a mayúscula.
    // Validación del settlement Indicator
    validationSettlementIndicator(request.getSettlementIndicator());

    // LAST_UPDATER_USER
    // No pedir en el body request. Recuperar el usuario del token internamente. Por defecto será el nombre del micro.

    // LAST_UPDATER_DATE_TIME
    // No pedir en el body request. Recuperar fecha actual con Java y asignar al campo.

    // CREATION_USER
    // No pedir en el body request. Recuperar el usuario del token internamente. Por defecto será el nombre del micro.

    // CREATION_DATE_TIME
    // No pedir en el body request. Recuperar fecha actual con Java y asignar al campo.
  }

  /**
   * Valida las fechas de inicio y fin de vigencia del descubierto.
   *
   * @param startDateTime Fecha de inicio de vigencia del descubierto.
   * @param endDateTime   Fecha de fin de vigencia del descubierto.
   */

  private static void validationDate(LocalDateTime startDateTime, LocalDateTime endDateTime) {


    // comprueba si es nulo o vacio
    if (startDateTime == null || startDateTime.toString().isEmpty()) {
      throw new BadRequestDarwinExceptionLeancore("OVERDRAFT-REASON-0032");
    }

    // comprueba si es nulo o vacio
    if (endDateTime == null || endDateTime.toString().isEmpty()) {
      throw new BadRequestDarwinExceptionLeancore("OVERDRAFT-REASON-0033");
    }

    // comprueba si startDateTime es posterior a endDateTime
    if (startDateTime.isAfter(endDateTime)) {
      throw new BadRequestDarwinExceptionLeancore("OVERDRAFT-REASON-0023");
    }
    // Solo fecha (YYYY-MM-DD)
    var fechaActual = LocalDate.now();
    // Convertir a LocalDateTime (YYYY-MM-DD 00:00:00)
    LocalDateTime fechaActualDateTime = fechaActual.atStartOfDay();

    // comprueba si startDateTime y endDateTime son anteriores a la fecha actual
    if (startDateTime.isBefore(fechaActualDateTime)) {
      throw new BadRequestDarwinExceptionLeancore("OVERDRAFT-REASON-0022");
    }

    // comprueba si endDateTime es anterior a la fecha actual
    if (endDateTime.isBefore(fechaActualDateTime)) {
      throw new BadRequestDarwinExceptionLeancore("OVERDRAFT-REASON-0031");
    }
  }

  /**
   * Validar que el campo mandatory no sea nulo o vacío, y que sea "Y" o "N".
   *
   * @param mandatory El valor del campo mandatory.
   */
  private static void validationMandatory(String mandatory) {
    if (mandatory == null || mandatory.isEmpty()) {
      throw new BadRequestDarwinExceptionLeancore("OVERDRAFT-REASON-0029");
    }

    // Convertir a mayúscula
    mandatory = mandatory.toUpperCase();

    // Validar que sea "Y" o "N"
    if (!"Y".equals(mandatory) && !"N".equals(mandatory)) {
      throw new BadRequestDarwinExceptionLeancore("OVERDRAFT-REASON-0030");
    }
  }

  /**
   * Validar que el campo settlementIndicator no sea nulo o vacío, y que sea "Y" o "N".
   *
   * @param settlementIndicator El valor del campo mandatory.
   */
  private static void validationSettlementIndicator(String settlementIndicator) {
    if (settlementIndicator == null || settlementIndicator.isEmpty()) {
      throw new BadRequestDarwinExceptionLeancore("OVERDRAFT-REASON-0034");
    }

    // Convertir a mayúscula
    settlementIndicator = settlementIndicator.toUpperCase();

    // Validar que sea "Y" o "N"
    if (!"Y".equals(settlementIndicator) && !"N".equals(settlementIndicator)) {
      throw new BadRequestDarwinExceptionLeancore("OVERDRAFT-REASON-0035");
    }
  }

  /**
   * Si mandatory es igual a "Y", se verifica que defaultDate no sea nulo. Si es nulo, se lanza una excepción.
   * Posteriormente, se valida que defaultDate no sea anterior a la fecha actual.
   * Si esta condición no se cumple, se lanza otra excepción.
   * En caso de que mandatory no sea "Y", se permite que defaultDate sea nulo.
   * Sin embargo, si se proporciona un valor para defaultDate, también se valida que
   * no sea anterior a la fecha actual.
   *
   * @param defaultDate defaultDate
   * @param mandatory   mandatory
   */
  private static void validationDefaultDate(LocalDate defaultDate, String mandatory) {
    // Solo fecha (YYYY-MM-DD)

    mandatory = mandatory.toUpperCase();

    if ("Y".equals(mandatory) && defaultDate == null) {
      throw new BadRequestDarwinExceptionLeancore("OVERDRAFT-REASON-0027");
    }
    var fechaActual = LocalDate.now();
    if (defaultDate != null && defaultDate.isBefore(fechaActual)) {
      throw new BadRequestDarwinExceptionLeancore("OVERDRAFT-REASON-0028");
    }
  }

  /**
   * Opcional. Verificar que sea un numero positivo. [0,9999] NUMER(4,0)
   *
   * @param counter counter
   */
  private static void validateCounter(BigDecimal counter) {
    if (counter != null && (counter.compareTo(BigDecimal.ZERO) < 0 || counter.compareTo(new BigDecimal("9999")) > 0)) {
      throw new BadRequestDarwinExceptionLeancore("OVERDRAFT-REASON-0026");
    }
  }

  /**
   * Opcional. Validar que la parte decimal sea máximo 30. NUMBER(4,2) Si se informa con 99,99 se trata como null.
   *
   * @param months months
   */
  private static void validateMonths(BigDecimal months) {

    // Verificar que la parte decimal sea máximo 30
    BigDecimal decimalPart = months.remainder(BigDecimal.ONE);
    if (decimalPart.compareTo(new BigDecimal("0.30")) > 0) {
      throw new BadRequestDarwinExceptionLeancore("OVERDRAFT-REASON-0024");
    }
    // Verificar que sea un número positivo
    if (months.compareTo(BigDecimal.ZERO) < 0 || months.compareTo(new BigDecimal("99.30")) > 0) {
      throw new BadRequestDarwinExceptionLeancore("OVERDRAFT-REASON-0025");
    }
  }

  /**
   * Verifica que el campo reasonCode no sea nulo o vacío, que su longitud esté entre 1 y 4 caracteres,
   * y que sea numérico.
   *
   * @param reasonCode El valor del campo reasonCode.
   */

  private static void validationReasonCode(String reasonCode) {
    // Verificar que la longitud del campo esté entre 1 y 4 caracteres
    if (reasonCode.length() > Constants.NUMBER_4 || reasonCode.length() <= Constants.NUMBER_1) {
      throw new BadRequestDarwinExceptionLeancore("OVERDRAFT-REASON-0019");
    }

    // Verificar que sea numerico
    if (!reasonCode.matches("\\d+")) {
      throw new BadRequestDarwinExceptionLeancore("OVERDRAFT-REASON-0020");
    }
  }

  /**
   * Valida los campos de la request para el update de un motivo de descubierto existente.
   *
   * @param request El objeto DTO que contiene los datos del motivo de descubierto.
   * @param actual  La entidad actual del motivo de descubierto.
   */
  public static void updateValidation(ReasonOverdraftDTO request, ReasonOverdraftEntity actual) {
    // ENTITY &  BRAND
    // Verificar contenido con la tabla BBCC_ENTITY_BRAND. Obligatorio.
    if (request.getReasonCode() == null || request.getReasonCode().isEmpty()) {
      throw new BadRequestDarwinExceptionLeancore("OVERDRAFT-REASON-0016");
    }
    if (request.getEntity() == null || request.getEntity().isEmpty()) {
      throw new BadRequestDarwinExceptionLeancore("OVERDRAFT-REASON-0017");
    }
    if (request.getBrand() == null || request.getBrand().isEmpty()) {
      throw new BadRequestDarwinExceptionLeancore("OVERDRAFT-REASON-0018");
    }

    if (request.getReasonCode() != null) {
      validationReasonCode(request.getReasonCode());
    }
    if (request.getMonths() != null) {
      validateMonths(request.getMonths());
    }
    if (request.getCounter() != null) {
      validateCounter(request.getCounter());
    }

    validateStartDate(request, actual);
    validateEndDate(request, actual);

    // Validaciones mandatory y defaultDate
    if (request.getMandatory() != null) {
      validationMandatory(request.getMandatory());
      validationDefaultDate(reasonOverdraftMapper.mapToDate(request.getDefaultDate()), request.getMandatory());
    } else if (request.getDefaultDate() != null) {
      validationDefaultDate(reasonOverdraftMapper.mapToDate(request.getDefaultDate()), actual.getMandatory());
    } else {
      request.setMandatory(actual.getMandatory());
      request.setDefaultDate(reasonOverdraftMapper.mapToDateStr(actual.getDefaultDate()));
    }
    // Validación del settlement Indicator
    validationSettlementIndicator(request.getSettlementIndicator());
  }

  /**
   * Valida la fecha de finalización de vigencia del motivo de descubierto.
   *
   * @param request El objeto DTO que contiene los datos del motivo de descubierto.
   * @param actual  La entidad actual del motivo de descubierto.
   */
  private static void validateEndDate(ReasonOverdraftDTO request, ReasonOverdraftEntity actual) {
    if (request.getEndDateTime() != null) {
      if (request.getStartDateTime() != null) {
        if (reasonOverdraftMapper.mapToDateTime(request.getEndDateTime())
            .isBefore(reasonOverdraftMapper.mapToDateTime(request.getStartDateTime()))) {
          throw new BadRequestDarwinExceptionLeancore("OVERDRAFT-REASON-0021");
        }
      } else {
        if (reasonOverdraftMapper.mapToDateTime(request.getEndDateTime())
            .isBefore(actual.getStartDateTime())) {
          throw new BadRequestDarwinExceptionLeancore("OVERDRAFT-REASON-0021");
        }
      }
    }
  }

  /**
   * Valida la fecha de inicio de vigencia del motivo de descubierto.
   *
   * @param request El objeto DTO que contiene los datos del motivo de descubierto.
   * @param actual  La entidad actual del motivo de descubierto.
   */
  private static void validateStartDate(ReasonOverdraftDTO request, ReasonOverdraftEntity actual) {
    // Si request tiene startDateTime o endDateTime, se valida que no sean nulos y que cumplan las condiciones
    if (request.getStartDateTime() != null) {
      // Comparar si es posterior a la fecha actual
      if (reasonOverdraftMapper.mapToDateTime(request.getStartDateTime()).isBefore(LocalDateTime.now())) {
        throw new BadRequestDarwinExceptionLeancore("OVERDRAFT-REASON-0022");
      }
      // Si atual tiene endDateTime, se valida que startDateTime sea anterior o igual a endDateTime
      if (actual.getEndDateTime() != null && reasonOverdraftMapper.mapToDateTime(request.getStartDateTime())
          .isAfter(actual.getEndDateTime())) {
        throw new BadRequestDarwinExceptionLeancore("OVERDRAFT-REASON-0023");
      }
    }
  }

}

