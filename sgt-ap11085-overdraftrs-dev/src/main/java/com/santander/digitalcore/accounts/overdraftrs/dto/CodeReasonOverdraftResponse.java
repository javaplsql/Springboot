package com.santander.digitalcore.accounts.overdraftrs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Represents a code and description and audit fields for an overdraft reason.
 */
@Data
@AllArgsConstructor
public class CodeReasonOverdraftResponse {

  private String reasonCode;

  private String reasonCodeDescription;

  private String creationUser;

  private LocalDateTime creationDateTime;

  private String lastUpdateUser;

  private LocalDateTime lastUpdateDateTime;

  private String cancellationUser;

  private LocalDateTime cancellationDateTime;
}
