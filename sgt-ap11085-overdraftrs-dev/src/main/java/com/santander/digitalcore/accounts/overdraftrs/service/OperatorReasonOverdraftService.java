package com.santander.digitalcore.accounts.overdraftrs.service;

import com.santander.darwin.core.exceptions.BadRequestDarwinException;
import com.santander.darwin.core.exceptions.NotFoundDarwinException;
import com.santander.digitalcore.accounts.overdraftrs.dto.OperatorReasonOverdraftRequest;
import com.santander.digitalcore.accounts.overdraftrs.dto.OperatorReasonOverdraftResponse;
import com.santander.digitalcore.accounts.overdraftrs.mapper.OperatorReasonOverdraftMapper;
import com.santander.digitalcore.accounts.overdraftrs.util.Constants;
import com.santander.digitalcore.accounts.util.lib.core.utils.UtilLeanCoreData;
import com.santander.digitalcore.accounts.util.lib.db.model.repository.overdraft.OperatorReasonOverdraftRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service to manage the OperatorReasonOverdraft entities.
 * This service is responsible for handling business logic related to operator reasons for overdrafts.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class OperatorReasonOverdraftService {

  private final OperatorReasonOverdraftRepository operatorReasonOverdraftRepository;

  private final OperatorReasonOverdraftMapper operatorReasonOverdraftMapper;

  /**
   * Retrieves a list of all OperatorReasonOverdraft objects.
   *
   * @return a list of OperatorReasonOverdraft objects
   */
  public List<OperatorReasonOverdraftResponse> getOperatorReasonOverdraftList() {
    return operatorReasonOverdraftRepository.findAll()
        .stream()
        .map(operatorReasonOverdraftMapper::mapToOperatorReasonOverdraftResponse)
        .toList();
  }

  /**
   * Retrieves a list of all active OperatorReasonOverdraft objects.
   *
   * @return a list of active OperatorReasonOverdraft objects
   */
  public List<OperatorReasonOverdraftResponse> getActiveOperatorReasonOverdraftList() {
    return operatorReasonOverdraftRepository.findActive()
        .stream()
        .map(operatorReasonOverdraftMapper::mapToOperatorReasonOverdraftResponse)
        .toList();
  }

  /**
   * Creates a new OperatorReasonOverdraft.
   *
   * @param operatorReasonOverdraftRequest the OperatorReasonOverdraft object to be created
   */
  public void createOperatorReasonOverdraft(OperatorReasonOverdraftRequest operatorReasonOverdraftRequest) {
    if (operatorReasonOverdraftRepository.findByPK(operatorReasonOverdraftRequest.getOperatorCode()) != null) {
      log.error("OperatorReasonOverdraft with code {} already exists", operatorReasonOverdraftRequest.getOperatorCode());
      throw new BadRequestDarwinException("OVERDRAFTS-REASON-OPERATOR-REASON-0001",
          "OperatorReasonOverdraft already exists");
    }

    operatorReasonOverdraftRepository.insert(operatorReasonOverdraftRequest.getOperatorCode(),
        operatorReasonOverdraftRequest.getOperatorDescription(),
        UtilLeanCoreData.getUserId(Constants.MS_NAME),
        UtilLeanCoreData.getUtcTimestamp());
  }

  /**
   * Updates if exists an existing OperatorReasonOverdraft.
   *
   * @param code        the code of the OperatorReasonOverdraft to be updated
   * @param description the new description for the OperatorReasonOverdraft
   */
  public void updateOperatorReasonOverdraft(String code, String description) {
    if (operatorReasonOverdraftRepository.findByPK(code) == null) {
      log.error("OperatorReasonOverdraft with code {} doesn't exists", code);
      throw new NotFoundDarwinException("OVERDRAFTS-REASON-OPERATOR-REASON-0002",
          "OperatorReasonOverdraft doesn't exists");
    }

    operatorReasonOverdraftRepository.update(code, description,
        UtilLeanCoreData.getUserId(Constants.MS_NAME), UtilLeanCoreData.getUtcTimestamp());
  }

  /**
   * Deletes if exists an existing OperatorReasonOverdraft.
   *
   * @param code the code of the OperatorReasonOverdraft to be deleted
   */
  public void deleteOperatorReasonOverdraft(String code) {
    if (operatorReasonOverdraftRepository.findByPK(code) == null) {
      log.error("OperatorReasonOverdraft with code {} doesn't exists", code);
      throw new NotFoundDarwinException("OVERDRAFTS-REASON-OPERATOR-REASON-0002", "OperatorReasonOverdraft doesn't exists");
    }

    operatorReasonOverdraftRepository.delete(code, UtilLeanCoreData.getUserId(Constants.MS_NAME));
  }
}
