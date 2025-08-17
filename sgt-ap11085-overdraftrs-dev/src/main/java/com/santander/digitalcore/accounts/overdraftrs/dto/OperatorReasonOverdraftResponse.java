package com.santander.digitalcore.accounts.overdraftrs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Represents a code, description and audit fields for an overdraft operator.
 */
@Data
@AllArgsConstructor
public class OperatorReasonOverdraftResponse {

  private String operatorCode;

  private String operatorDescription;

  private String creationUser;

  private LocalDateTime creationDateTime;

  private String lastUpdateUser;

  private LocalDateTime lastUpdateDateTime;

  private String cancellationUser;

  private LocalDateTime cancellationDateTime;
}
