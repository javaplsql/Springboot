package com.santander.digitalcore.accounts.overdraftrs.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents a code and description for an overdraft operator.
 */
@Data
@AllArgsConstructor
public class OperatorReasonOverdraftRequest {

  @Size(min = 1, max = 4)
  private String operatorCode;

  @Size(min = 1, max = 120)
  private String operatorDescription;
}
