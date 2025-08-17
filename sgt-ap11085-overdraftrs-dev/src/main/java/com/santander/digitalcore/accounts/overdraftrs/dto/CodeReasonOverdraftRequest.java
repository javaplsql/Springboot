package com.santander.digitalcore.accounts.overdraftrs.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents a code and description for an overdraft reason.
 */
@Data
@AllArgsConstructor
public class CodeReasonOverdraftRequest {

  @Size(min = 1, max = 4)
  private String reasonCode;

  @Size(min = 1, max = 120)
  private String reasonCodeDescription;
}
