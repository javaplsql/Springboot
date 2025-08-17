package com.santander.digitalcore.accounts.overdraftrs.web;

import com.santander.digitalcore.accounts.overdraftrs.dto.OperatorReasonOverdraftRequest;
import com.santander.digitalcore.accounts.overdraftrs.dto.OperatorReasonOverdraftResponse;
import com.santander.digitalcore.accounts.overdraftrs.service.OperatorReasonOverdraftService;
import com.santander.digitalcore.accounts.overdraftrs.web.info.CreateOperatorReasonOverdraftPostDoc;
import com.santander.digitalcore.accounts.overdraftrs.web.info.DeleteOperatorReasonOverdraftDoc;
import com.santander.digitalcore.accounts.overdraftrs.web.info.GetOperatorReasonOverdraftDoc;
import com.santander.digitalcore.accounts.overdraftrs.web.info.UpdateOperatorReasonOverdraftPatchDoc;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

/**
 * Controller to manage the OperatorReasonOverdraft entities.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/operator_reason")
public class OperatorReasonOverdraftController {

  private final OperatorReasonOverdraftService operatorReasonOverdraftService;

  /**
   * Endpoint to retrieve the list of OperatorReasonOverdraft.
   *
   * @param active optional parameter to retrieve only the active records.
   * @return <ul>
   * <li>HTTP 200 OK with a list of OperatorReasonOverdraft objects if available.</li>
   * <li>HTTP 204 No Content if there are no OperatorReasonOverdrafts available.</li>
   * <li>HTTP 500 Internal Server Error if an unexpected error occurs.</li>
   * </ul>
   */
  @GetOperatorReasonOverdraftDoc
  @GetMapping
  public ResponseEntity<List<OperatorReasonOverdraftResponse>> getOperatorReasonOverdraftList(
      @Parameter(required = false, description = "Active", example = "true")
      @RequestParam(required = false) Boolean active) {
    List<OperatorReasonOverdraftResponse> operatorReasonOverdraftRequests = active != null && active
        ? operatorReasonOverdraftService.getActiveOperatorReasonOverdraftList()
        : operatorReasonOverdraftService.getOperatorReasonOverdraftList();

    if (operatorReasonOverdraftRequests.isEmpty()) {
      return ResponseEntity.noContent().build();
    }

    return ResponseEntity.ok(operatorReasonOverdraftRequests);
  }

  /**
   * Endpoint to create a new OperatorReasonOverdraft.
   *
   * @param request OperatorReasonOverdraft object containing the details of the new code reason overdraft.
   * @return <ul>
   * <li>HTTP 201 Created if the code reason overdraft is created successfully.</li>
   * <li>HTTP 400 Bad Request if the request body is invalid or missing required fields
   * or if the OperatorReasonOverdraft object already exists.</li>
   * <li>HTTP 500 Internal Server Error if an unexpected error occurs.</li>
   * </ul>
   */
  @CreateOperatorReasonOverdraftPostDoc
  @PostMapping
  public ResponseEntity<Void> createOperatorReasonOverdraft(@RequestBody @Valid OperatorReasonOverdraftRequest request) {
    operatorReasonOverdraftService.createOperatorReasonOverdraft(request);
    return ResponseEntity.created(URI.create(request.getOperatorCode())).build();
  }

  /**
   * Endpoint to update an existing OperatorReasonOverdraft.
   *
   * @param operatorCode        the code of the reason to update.
   * @param operatorDescription the new description for the reason.
   * @return <ul>
   * <li>HTTP 204 No Content if the update is successful.</li>
   * <li>HTTP 400 Bad Request if the request parameter is invalid or missing.</li>
   * <li>HTTP 404 Not Found if the specified reason code does not exist.</li>
   * <li>HTTP 500 Internal Server Error if an unexpected error occurs.</li>
   * </ul>
   */
  @UpdateOperatorReasonOverdraftPatchDoc
  @PatchMapping("/{operatorCode}")
  public ResponseEntity<Void> updateOperatorReasonOverdraft(
      @Parameter(required = true, description = "Operator code", example = "==")
      @PathVariable String operatorCode,
      @Parameter(required = true, description = "Operator description", example = "IGUAL",
          schema = @Schema(type = "string", minLength = 1, maxLength = 120))
      @RequestParam @Size(min = 1, max = 120) String operatorDescription) {
    operatorReasonOverdraftService.updateOperatorReasonOverdraft(operatorCode, operatorDescription);
    return ResponseEntity.noContent().build();
  }

  /**
   * Endpoint to delete a OperatorReasonOverdraft.
   *
   * @param operatorCode the code of the reason to delete.
   * @return <ul>
   * <li>HTTP 204 No Content if the deletion is successful.</li>
   * <li>HTTP 404 Not Found if the specified reason code does not exist.</li>
   * <li>HTTP 500 Internal Server Error if an unexpected error occurs.</li>
   * </ul>
   */
  @DeleteOperatorReasonOverdraftDoc
  @DeleteMapping("/{operatorCode}")
  public ResponseEntity<Void> deleteOperatorReasonOverdraft(
      @Parameter(required = true, description = "Operator code", example = "==")
      @PathVariable String operatorCode) {
    operatorReasonOverdraftService.deleteOperatorReasonOverdraft(operatorCode);
    return ResponseEntity.noContent().build();
  }
}
