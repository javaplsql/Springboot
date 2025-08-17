package com.santander.digitalcore.accounts.overdraftrs.web;

import com.santander.digitalcore.accounts.overdraftrs.dto.CodeReasonOverdraftRequest;
import com.santander.digitalcore.accounts.overdraftrs.dto.CodeReasonOverdraftResponse;
import com.santander.digitalcore.accounts.overdraftrs.service.CodeReasonOverdraftService;
import com.santander.digitalcore.accounts.overdraftrs.web.info.CreateCodeReasonOverdraftPostDoc;
import com.santander.digitalcore.accounts.overdraftrs.web.info.DeleteCodeReasonOverdraftDoc;
import com.santander.digitalcore.accounts.overdraftrs.web.info.GetCodeReasonOverdraftDoc;
import com.santander.digitalcore.accounts.overdraftrs.web.info.UpdateCodeReasonOverdraftPatchDoc;
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
 * Controller to manage the CodeReasonOverdraft entities.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/code_reason")
public class CodeReasonOverdraftController {

  private final CodeReasonOverdraftService codeReasonOverdraftService;

  /**
   * Endpoint to retrieve the list of CodeReasonOverdraft.
   *
   * @param active optional parameter to retrieve only the active records.
   * @return <ul>
   * <li>HTTP 200 OK with a list of CodeReasonOverdraft objects if available.</li>
   * <li>HTTP 204 No Content if there are no CodeReasonOverdrafts available.</li>
   * <li>HTTP 500 Internal Server Error if an unexpected error occurs.</li>
   * </ul>
   */
  @GetCodeReasonOverdraftDoc
  @GetMapping
  public ResponseEntity<List<CodeReasonOverdraftResponse>> getCodeReasonOverdraftList(
      @Parameter(required = false, description = "Active", example = "true")
      @RequestParam(required = false) Boolean active) {
    List<CodeReasonOverdraftResponse> codeReasonOverdraftRequests = active != null && active
        ? codeReasonOverdraftService.getActiveCodeReasonOverdraftList()
        : codeReasonOverdraftService.getCodeReasonOverdraftList();

    if (codeReasonOverdraftRequests.isEmpty()) {
      return ResponseEntity.noContent().build();
    }

    return ResponseEntity.ok(codeReasonOverdraftRequests);
  }

  /**
   * Endpoint to create a new CodeReasonOverdraft.
   *
   * @param request CodeReasonOverdraft object containing the details of the new code reason overdraft.
   * @return <ul>
   * <li>HTTP 201 Created if the code reason overdraft is created successfully.</li>
   * <li>HTTP 400 Bad Request if the request body is invalid or missing required fields
   * or if the CodeReasonOverdraft object already exists.</li>
   * <li>HTTP 500 Internal Server Error if an unexpected error occurs.</li>
   * </ul>
   */
  @CreateCodeReasonOverdraftPostDoc
  @PostMapping
  public ResponseEntity<Void> createCodeReasonOverdraft(@RequestBody @Valid CodeReasonOverdraftRequest request) {
    codeReasonOverdraftService.createCodeReasonOverdraft(request);
    return ResponseEntity.created(URI.create(request.getReasonCode())).build();
  }

  /**
   * Endpoint to update an existing CodeReasonOverdraft.
   *
   * @param reasonCode        the code of the reason to update.
   * @param reasonDescription the new description for the reason.
   * @return <ul>
   * <li>HTTP 204 No Content if the update is successful.</li>
   * <li>HTTP 400 Bad Request if the request parameter is invalid or missing.</li>
   * <li>HTTP 404 Not Found if the specified reason code does not exist.</li>
   * <li>HTTP 500 Internal Server Error if an unexpected error occurs.</li>
   * </ul>
   */
  @UpdateCodeReasonOverdraftPatchDoc
  @PatchMapping("/{reasonCode}")
  public ResponseEntity<Void> updateCodeReasonOverdraft(
      @Parameter(required = true, description = "Reason code", example = "01")
      @PathVariable String reasonCode,
      @Parameter(required = true, description = "Reason description", example = "NOMINA DOMICILIADA",
          schema = @Schema(type = "string", minLength = 1, maxLength = 120))
      @RequestParam @Size(min = 1, max = 120) String reasonDescription) {
    codeReasonOverdraftService.updateCodeReasonOverdraft(reasonCode, reasonDescription);
    return ResponseEntity.noContent().build();
  }

  /**
   * Endpoint to delete a CodeReasonOverdraft.
   *
   * @param reasonCode the code of the reason to delete.
   * @return <ul>
   * <li>HTTP 204 No Content if the deletion is successful.</li>
   * <li>HTTP 404 Not Found if the specified reason code does not exist.</li>
   * <li>HTTP 500 Internal Server Error if an unexpected error occurs.</li>
   * </ul>
   */
  @DeleteCodeReasonOverdraftDoc
  @DeleteMapping("/{reasonCode}")
  public ResponseEntity<Void> deleteCodeReasonOverdraft(
      @Parameter(required = true, description = "Reason code", example = "01")
      @PathVariable String reasonCode) {
    codeReasonOverdraftService.deleteCodeReasonOverdraft(reasonCode);
    return ResponseEntity.noContent().build();
  }

}
