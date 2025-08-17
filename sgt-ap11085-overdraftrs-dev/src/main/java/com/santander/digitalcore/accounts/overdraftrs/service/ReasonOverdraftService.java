package com.santander.digitalcore.accounts.overdraftrs.service;

import com.santander.digitalcore.accounts.overdraftrs.dto.ReasonOverdraftDTO;
import com.santander.digitalcore.accounts.overdraftrs.mapper.ReasonOverdraftMapper;
import com.santander.digitalcore.accounts.overdraftrs.repository.ReasonOverdraftRepositoryExt;
import com.santander.digitalcore.accounts.util.lib.core.exceptions.BadRequestDarwinExceptionLeancore;
import com.santander.digitalcore.accounts.util.lib.core.exceptions.NotFoundDarwinExceptionLeancore;
import com.santander.digitalcore.accounts.util.lib.core.exceptions.InternalServerErrorDarwinExceptionLeancore;
import com.santander.digitalcore.accounts.util.lib.core.utils.UtilLeanCoreData;
import com.santander.digitalcore.accounts.overdraftrs.util.Constants;
import com.santander.digitalcore.accounts.util.lib.db.model.entity.CodeReasonOverdraftEntity;
import com.santander.digitalcore.accounts.util.lib.db.model.entity.CodeReasonOverdraftEntityPK;
import com.santander.digitalcore.accounts.util.lib.db.model.entity.OperatorReasonOverdraftEntityPK;
import com.santander.digitalcore.accounts.util.lib.db.model.entity.ReasonOverdraftEntity;
import com.santander.digitalcore.accounts.util.lib.db.model.entity.ReasonOverdraftEntityPK;
import com.santander.digitalcore.accounts.util.lib.db.model.repository.overdraft.CodeReasonOverdraftRepository;
import com.santander.digitalcore.accounts.util.lib.db.model.repository.overdraft.OperatorReasonOverdraftRepository;
import com.santander.digitalcore.accounts.overdraftrs.util.Validations;
import org.mapstruct.factory.Mappers;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

/**
 * Service class for managing overdraft reasons.
 * Provides methods to create, update, delete, and retrieve overdraft reasons.
 */
@Slf4j
@Service
@AllArgsConstructor
public class ReasonOverdraftService {

  private ReasonOverdraftRepositoryExt reasonOverdraftRepository;
  private final OperatorReasonOverdraftRepository operatorReasonOverdraftRepository;
  private final CodeReasonOverdraftRepository codeReasonOverdraftRepository;
  private static final ReasonOverdraftMapper reasonOverdraftMapper = Mappers.getMapper(ReasonOverdraftMapper.class);

  /**
   * Retrieves a reason overdraft by its reason code, entity, and brand.
   *
   * @param reasonCode the reason code of the overdraft
   * @param entity     the entity associated with the overdraft
   * @param brand      the brand associated with the overdraft
   * @return a ReasonOverdraftDTO object containing the overdraft details
   */
  public ReasonOverdraftDTO getReasonOverdraft(String reasonCode, String entity, String brand) {
    log.info("Fetching reason overdraft for reasonCode: {}, entity: {}, brand: {}", reasonCode, entity, brand);
    var reasonOverdraftEntityPK = reasonOverdraftMapper.mapToEntityPK(reasonCode, entity, brand);

    try {
      return reasonOverdraftRepository.findByPK(reasonOverdraftEntityPK)
          .map(reasonOverdraftMapper::mapToDto)
          .orElseThrow(() -> new NotFoundDarwinExceptionLeancore("OVERDRAFT-REASON-0001"));
    } catch (SQLException e) {
      log.error("DB Error - Error fetching reason overdraft: {}", e.getMessage());
      throw new InternalServerErrorDarwinExceptionLeancore("XX1111", e.getMessage(), e);
    }
  }

  /**
   * Retrieves a paginated list of reason overdrafts with optional filters.
   *
   * @param reasonCode the reason code to filter by (optional)
   * @param entity     the entity to filter by (optional)
   * @param brand      the brand to filter by (optional)
   * @param status     the status to filter by (optional)
   * @param offset     the offset for pagination
   * @param limit      the limit for pagination
   * @return a list of ReasonOverdraftDTO objects
   */
  public List<ReasonOverdraftDTO> getReasonOverdraftList(
      String reasonCode, String entity, String brand, String status, String offset, String limit
  ) {
    log.info("Fetching reason overdraft list with filters: " +
            "reasonCode={}, entity={}, brand={}, status={}, offset={}, limit={}",
        reasonCode, entity, brand, status, offset, limit);

    try {
      //Metodo findlist con filtros y paginacion
      List<ReasonOverdraftEntity> entities = reasonOverdraftRepository.findList(
          reasonCode, entity, brand, status, offset, limit
      );
      return entities.stream()
          .map(reasonOverdraftMapper::mapToDto)
          .toList();
    } catch (SQLException e) {
      log.error("DB Error - Error fetching reason overdraft list: {}", e.getMessage());
      throw new InternalServerErrorDarwinExceptionLeancore("XX1111", e.getMessage(), e);
    }
  }

  /**
   * Creates a new reason overdraft.
   *
   * @param request the ReasonOverdraftDTO containing the details of the overdraft to create
   */
  public void createReasonOverdraft(ReasonOverdraftDTO request) {
    try {


      log.info("Creating reason overdraft with request: {}", request);

      //verifica si ya existe el registro
      var reasonOverdraftEntityPK =
          new ReasonOverdraftEntityPK(request.getEntity(), request.getBrand(), request.getReasonCode());
      if (reasonOverdraftRepository.findByPK(reasonOverdraftEntityPK).isPresent()) {
        log.error("ReasonOverdraft already exists for entity: {}, " +
            "brand: {}, reasonCode: {}", request.getEntity(), request.getBrand(), request.getReasonCode());
        throw new BadRequestDarwinExceptionLeancore("OVERDRAFT-REASON-0002", "ReasonOverdraft already exists");
      }

      // verifica si existe la condicion
      if (request.getCondition() != null) {
        var operatorReasonOverdraftEntityPK =
            new OperatorReasonOverdraftEntityPK(request.getCondition());
        if (operatorReasonOverdraftRepository.findByPK(operatorReasonOverdraftEntityPK.getOperatorCode()) == null) {
          log.error("OperatorReasonOverdraft doesn't exist: {}", request.getCondition());
          throw new BadRequestDarwinExceptionLeancore("OVERDRAFT-REASON-0003",
              "OperatorReasonOverdraft does not exist");
        }
      }
      // Initialize entities
      CodeReasonOverdraftEntity codeReasonOverdraftEntity;
      if (request.getReasonCode() != null) {
        var codeReasonOverdraftEntityPK =
            new CodeReasonOverdraftEntityPK(request.getReasonCode());
        codeReasonOverdraftEntity = codeReasonOverdraftRepository.findByPK(codeReasonOverdraftEntityPK.getReasonCode());
        if (codeReasonOverdraftEntity == null) {
          log.error("CodeReasonOverdraft doesn't exist: {}", request.getReasonCode());
          throw new BadRequestDarwinExceptionLeancore("OVERDRAFT-REASON-0004", "CodeReasonOverdraft does not exist");
        }
      } else {
        log.error("ReasonCode is mandatory");
        throw new BadRequestDarwinExceptionLeancore("OVERDRAFT-REASON-0005", "ReasonCode is mandatory");
      }
      //verifica si ya existe el registro fin

      var reasonOverdraftEntity = reasonOverdraftMapper.
          mapToEntity(request, UtilLeanCoreData.getUserId(Constants.MS_NAME));
      //SET the rest of the entity fields
      reasonOverdraftEntity.setDescData(codeReasonOverdraftEntity.getReasonCodeDescription());
      reasonOverdraftEntity.setLastUpdaterUser(UtilLeanCoreData.getUserId(Constants.MS_NAME));
      reasonOverdraftEntity.setLastUpdaterDateTime(UtilLeanCoreData.getUtcTimestamp());
      reasonOverdraftEntity.setCreationUser(UtilLeanCoreData.getUserId(Constants.MS_NAME));
      reasonOverdraftEntity.setCreationDateTime(UtilLeanCoreData.getUtcTimestamp());

      reasonOverdraftRepository.insert(reasonOverdraftEntity);
    } catch (SQLException e) {
      log.error("DB Error - Error creating reason overdraft: {}", e.getMessage());
      throw new InternalServerErrorDarwinExceptionLeancore("XX1111", e.getMessage(), e);
    }

    log.info("Reason overdraft created successfully");
  }

  /**
   * Updates an existing reason overdraft.
   *
   * @param reasonCode the reason code of the overdraft to update
   * @param entity     the entity associated with the overdraft
   * @param brand      the brand associated with the overdraft
   * @param request    the ReasonOverdraftDTO containing the updated details
   */
  public void updateReasonOverdraft(String reasonCode, String entity, String brand, ReasonOverdraftDTO request) {
    CodeReasonOverdraftEntity codeReasonOverdraftEntity;
    ReasonOverdraftEntity updated;

    log.info("Updating reason overdraft with request: {}", request);
    var reasonOverdraftEntityPK = reasonOverdraftMapper.mapToEntityPK(reasonCode, entity, brand);
    var msName = "";
    var reasonOverdraftEntityNew =
        reasonOverdraftMapper.mapToEntity(request, UtilLeanCoreData.getUserId(msName));

    try {

      Optional<ReasonOverdraftEntity> reasonOverdraftEntityOld =
          reasonOverdraftRepository.findByPK(reasonOverdraftEntityPK);
      if (reasonOverdraftEntityOld.isEmpty()) {
        throw new NotFoundDarwinExceptionLeancore("OVERDRAFT-REASON-0001");
      }

      if (reasonOverdraftEntityOld.get().equals(reasonOverdraftEntityNew)) {
        throw new BadRequestDarwinExceptionLeancore("No fields to update. " +
            "The new data is identical to the existing data.");
      }

      var codeReasonOverdraftEntityPK =
          new CodeReasonOverdraftEntityPK(request.getReasonCode());
      codeReasonOverdraftEntity = codeReasonOverdraftRepository.findByPK(codeReasonOverdraftEntityPK.getReasonCode());

      if (codeReasonOverdraftEntity == null) {
        log.error("CodeReasonOverdraft doesn't exist: {}", request.getReasonCode());
        throw new BadRequestDarwinExceptionLeancore("OVERDRAFT-REASON-0004", "CodeReasonOverdraft does not exist");
      }
      // verifica si existe la condicion
      if (request.getCondition() != null) {
        var operatorReasonOverdraftEntityPK =
            new OperatorReasonOverdraftEntityPK(request.getCondition());
        if (operatorReasonOverdraftRepository.findByPK(operatorReasonOverdraftEntityPK.getOperatorCode()) == null) {
          log.error("OperatorReasonOverdraft doesn't exist: {}", request.getCondition());
          throw new BadRequestDarwinExceptionLeancore("OVERDRAFT-REASON-0003",
              "OperatorReasonOverdraft does not exist");
        }
      }
      // Validación de datos de entrada
      Validations.updateValidation(request, reasonOverdraftEntityOld.get());

      updated = new ReasonOverdraftEntity();
      updated.setPk(reasonOverdraftEntityPK);
      updated.setDescData(codeReasonOverdraftEntity.getReasonCodeDescription());
      updated.setMandatory(request.getMandatory() != null ?
          request.getMandatory() : reasonOverdraftEntityOld.get().getMandatory());
      updated.setSettlementIndicator(request.getSettlementIndicator() != null ?
          request.getSettlementIndicator() : reasonOverdraftEntityOld.get().getSettlementIndicator());
      updated.setDefaultDate(request.getDefaultDate() != null ?
          reasonOverdraftMapper.mapToDate(request.getDefaultDate()) : reasonOverdraftEntityOld.get().getDefaultDate());
      updated.setMonths(request.getMonths() != null ?
          request.getMonths() : reasonOverdraftEntityOld.get().getMonths());
      updated.setCounter(request.getCounter() != null ?
          request.getCounter() : reasonOverdraftEntityOld.get().getCounter());
      updated.setCondition(request.getCondition() != null ?
          request.getCondition() : reasonOverdraftEntityOld.get().getCondition());
      updated.setLastUpdaterUser(UtilLeanCoreData.getUserId(Constants.MS_NAME));
      updated.setLastUpdaterDateTime(UtilLeanCoreData.getUtcTimestamp());
      updated.setStartDateTime(request.getStartDateTime() != null ?
          reasonOverdraftMapper.mapToDateTime(request.getStartDateTime()) :
          reasonOverdraftEntityOld.get().getStartDateTime());
      updated.setEndDateTime(request.getEndDateTime() != null ?
          reasonOverdraftMapper.mapToDateTime(request.getEndDateTime()) :
          reasonOverdraftEntityOld.get().getEndDateTime());

      reasonOverdraftRepository.update(updated);
    } catch (SQLException e) {
      log.error("DB Error - Error updating reason overdraft: {}", e.getMessage());
      throw new InternalServerErrorDarwinExceptionLeancore("XX1111", e.getMessage(), e);
    }

    log.info("Reason overdraft updated successfully");
  }

  /**
   * Deletes a reason overdraft by its reason code, entity, and brand.
   *
   * @param reasonCode the reason code of the overdraft to delete
   * @param entity     the entity associated with the overdraft
   * @param brand      the brand associated with the overdraft
   */
  public void deleteReasonOverdraft(String reasonCode, String entity, String brand) {
    log.info("Deleting reason overdraft for reasonCode: {}, entity: {}, brand: {}", reasonCode, entity, brand);
    var reasonOverdraftEntityPK = reasonOverdraftMapper.mapToEntityPK(reasonCode, entity, brand);

    try {
      Optional<ReasonOverdraftEntity> reasonOverdraftEntity =
          reasonOverdraftRepository.findByPK(reasonOverdraftEntityPK);
      if (reasonOverdraftEntity.isEmpty()) {
        throw new NotFoundDarwinExceptionLeancore("OVERDRAFT-REASON-0001");
      }

      // verifica si ya esta inactivo
      if (reasonOverdraftEntity.get().getEndDateTime() != null) {
        log.error("ReasonOverdraft is already inactive for " +
            "entity: {}, brand: {}, reasonCode: {}", entity, brand, reasonCode);
        throw new BadRequestDarwinExceptionLeancore("OVERDRAFT-REASON-0006", "ReasonOverdraft is already inactive");
      }

      reasonOverdraftRepository.delete(reasonOverdraftEntityPK, UtilLeanCoreData.getUserId(Constants.MS_NAME));
    } catch (SQLException e) {
      log.error("DB Error - Error deleting reason overdraft: {}", e.getMessage());
      throw new InternalServerErrorDarwinExceptionLeancore("XX1111", e.getMessage(), e);
    }

    log.info("Reason overdraft deleted successfully");
  }

}
