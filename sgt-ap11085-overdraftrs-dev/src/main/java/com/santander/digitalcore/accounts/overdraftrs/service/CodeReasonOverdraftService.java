package com.santander.digitalcore.accounts.overdraftrs.service;

import com.santander.darwin.core.exceptions.BadRequestDarwinException;
import com.santander.darwin.core.exceptions.NotFoundDarwinException;
import com.santander.digitalcore.accounts.overdraftrs.dto.CodeReasonOverdraftRequest;
import com.santander.digitalcore.accounts.overdraftrs.dto.CodeReasonOverdraftResponse;
import com.santander.digitalcore.accounts.overdraftrs.mapper.CodeReasonOverdraftMapper;
import com.santander.digitalcore.accounts.overdraftrs.util.Constants;
import com.santander.digitalcore.accounts.util.lib.core.utils.UtilLeanCoreData;
import com.santander.digitalcore.accounts.util.lib.db.model.repository.overdraft.CodeReasonOverdraftRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service to manage the CodeReasonOverdraft entities.
 * This service is responsible for handling business logic related to code reasons for overdrafts.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CodeReasonOverdraftService {

  private final CodeReasonOverdraftRepository codeReasonOverdraftRepository;

  private final CodeReasonOverdraftMapper codeReasonOverdraftMapper;

  /**
   * Retrieves a list of all CodeReasonOverdraft objects.
   *
   * @return a list of CodeReasonOverdraft objects
   */
  public List<CodeReasonOverdraftResponse> getCodeReasonOverdraftList() {
    return codeReasonOverdraftRepository.findAll()
        .stream()
        .map(codeReasonOverdraftMapper::mapToCodeReasonOverdraftResponse)
        .toList();
  }

  /**
   * Retrieves a list of all active CodeReasonOverdraft objects.
   *
   * @return a list of active CodeReasonOverdraft objects
   */
  public List<CodeReasonOverdraftResponse> getActiveCodeReasonOverdraftList() {
    return codeReasonOverdraftRepository.findActive()
        .stream()
        .map(codeReasonOverdraftMapper::mapToCodeReasonOverdraftResponse)
        .toList();
  }

  /**
   * Creates a new CodeReasonOverdraft.
   *
   * @param codeReasonOverdraftRequest the CodeReasonOverdraft object to be created
   */
  public void createCodeReasonOverdraft(CodeReasonOverdraftRequest codeReasonOverdraftRequest) {
    if (codeReasonOverdraftRepository.findByPK(codeReasonOverdraftRequest.getReasonCode()) != null) {
      log.error("CodeReasonOverdraft with code {} already exists", codeReasonOverdraftRequest.getReasonCode());
      throw new BadRequestDarwinException("OVERDRAFTS-REASON-CODE-REASON-0001", "CodeReasonOverdraft already exists");
    }

    codeReasonOverdraftRepository.insert(codeReasonOverdraftRequest.getReasonCode(),
        codeReasonOverdraftRequest.getReasonCodeDescription(),
        UtilLeanCoreData.getUserId(Constants.MS_NAME),
        UtilLeanCoreData.getUtcTimestamp());
  }

  /**
   * Updates if exists an existing CodeReasonOverdraft.
   *
   * @param code        the code of the CodeReasonOverdraft to be updated
   * @param description the new description for the CodeReasonOverdraft
   */
  public void updateCodeReasonOverdraft(String code, String description) {
    if (codeReasonOverdraftRepository.findByPK(code) == null) {
      log.error("CodeReasonOverdraft with code {} doesn't exists", code);
      throw new NotFoundDarwinException("OVERDRAFTS-REASON-CODE-REASON-0002", "CodeReasonOverdraft doesn't exists");
    }

    codeReasonOverdraftRepository.update(code, description,
        UtilLeanCoreData.getUserId(Constants.MS_NAME), UtilLeanCoreData.getUtcTimestamp());
  }

  /**
   * Deletes if exists an existing CodeReasonOverdraft.
   *
   * @param code the code of the CodeReasonOverdraft to be deleted
   */
  public void deleteCodeReasonOverdraft(String code) {
    if (codeReasonOverdraftRepository.findByPK(code) == null) {
      log.error("CodeReasonOverdraft with code {} doesn't exists", code);
      throw new NotFoundDarwinException("OVERDRAFTS-REASON-CODE-REASON-0002", "CodeReasonOverdraft doesn't exists");
    }

    codeReasonOverdraftRepository.delete(code, UtilLeanCoreData.getUserId(Constants.MS_NAME));
  }
}
