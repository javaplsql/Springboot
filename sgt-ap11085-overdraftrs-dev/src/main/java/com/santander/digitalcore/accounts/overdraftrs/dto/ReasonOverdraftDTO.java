package com.santander.digitalcore.accounts.overdraftrs.dto;

import com.santander.digitalcore.accounts.overdraftrs.util.OperationType;
import com.santander.digitalcore.accounts.overdraftrs.util.Validations;
import com.santander.digitalcore.accounts.util.lib.core.validation.AbstractLeanCoreRequestValidator;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/** ReasonOverdraftDTO
 * This class represents a data structure containing information about an overdraft reason.
 * It includes fields such as entity, brand, reason code, start and end date/time, description,
 * settlement indicator, mandatory status, default date, months, counter, condition, creation user,
 * creation date/time, last updater user, and last update date/time.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Data structure containing information about an overdraft reason.")
public class ReasonOverdraftDTO extends AbstractLeanCoreRequestValidator {

  @Schema(description = "Entity associated with the overdraft reason.")
  private String entity;

  @Schema(description = "Brand associated with the overdraft reason.")
  private String brand;

  @Schema(description = "Reason code for the overdraft.")
  private String reasonCode;

  @Schema(description = "Start date and time of the overdraft reason.")
  private String startDateTime;

  @Schema(description = "End date and time of the overdraft reason.")
  private String endDateTime;

  @Schema(description = "Description of the overdraft reason.")
  private String descData;

  @Schema(description = "Settlement indicator for the overdraft reason.")
  private String settlementIndicator;

  @Schema(description = "Indicates if the overdraft reason is mandatory.")
  private String mandatory;

  @Schema(description = "Default date for the overdraft reason.")
  private String defaultDate;

  @Schema(description = "Number of months associated with the overdraft reason.")
  private BigDecimal months;

  @Schema(description = "Counter for the overdraft reason.")
  private BigDecimal counter;

  @Schema(description = "Condition associated with the overdraft reason.")
  private String condition;

  @Schema(description = "User who created the overdraft reason.")
  private String creationUser;

  @Schema(description = "Creation date and time of the overdraft reason.")
  private String creationDateTime;

  @Schema(description = "User who last updated the overdraft reason.")
  private String lastUpdaterUser;

  @Schema(description = "Last update date and time of the overdraft reason.")
  private String lastUpdaterDateTime;

  /**
   * Validates the ReasonOverdraftDTO object.
   * This method uses the default operation type and validates the entity, brand, and reason code.
   */
  @Override
  public void validate() {
    validate(OperationType.DEFAULT, entity, brand, reasonCode);
  }

  /**
   * Validates the ReasonOverdraftDTO object based on the provided operation type, entity, brand, and reason code.
   *
   * @param operation  The operation type to validate against.
   * @param entity     The entity associated with the overdraft reason.
   * @param brand      The brand associated with the overdraft reason.
   * @param reasonCode The reason code for the overdraft.
   */
  public void validate(OperationType operation, String entity, String brand, String reasonCode) {
    Validations.doValidations(this, operation, entity, brand, reasonCode);

  }
}
