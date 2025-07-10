package com.developing.app.accounts.accountmanaging.service;

import java.sql.Connection;
import java.util.Objects;
import com.java.developing.accounts.util.lib.db.model.repository.ContractIdentifiersRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.java.developing.accounts.accountmanaging.model.AccounIdentification;
import com.java.developing.accounts.accountmanaging.model.genaccidentifiers.request.GenerateAccountIdentifiersPostRequest;
import com.java.developing.accounts.accountmanaging.model.genaccidentifiers.response.GenerateAccountIdentifiersPostResponse;
import com.java.developing.accounts.accnumeratorlib.utils.Numeration;
import com.java.developing.accounts.accnumeratorlib.service.AccNumeratorService;
import com.java.developing.accounts.util.lib.core.utils.UtilLeanCoreData;
import com.java.developing.accounts.util.lib.db.model.entity.ContractIdentifiersEntity;

import com.java.developing.accounts.util.lib.db.utils.UtilLeanCoreRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * Domain layer service class, in this case the application business logic is
 * simply printing Hello World!
 *
 * @author Created by Team DCOE Mx
 */
@Service
@Slf4j
public class accountmanagingService {

  private final ContractIdentifiersRepository contractIdentifiersRepository;

  /**
   * jdbcTemplate
   */
  private final JdbcTemplate jdbcTemplate;

  private final Numeration numeration;

  /**
   * uuidService
   */
  private final UuidService uuidService;

  /**
   * AccNumeratorService
   */
  private final AccNumeratorService accNumeratorService;

  /**
   * Constructor de la clase.
   *
   * @param contractIdentifiersRepository      repositorio de la tabla DC_CONTRACT_IDENTIFIERS
   * @param jdbcTemplate                       JdbcTemplate para realizar operaciones de base de datos.
   * @param numeration                         Numerator para generar identificadores de cuenta.
   * @param uuidService                        Servicio para generar UUIDs únicos.
   * @param accNumeratorService                Servicio para manejar la numeración de cuentas.
   *
   */
  public accountmanagingService(ContractIdentifiersRepository contractIdentifiersRepository,
                              JdbcTemplate jdbcTemplate,
                              Numeration numeration, UuidService uuidService,
                              AccNumeratorService accNumeratorService) {
    this.contractIdentifiersRepository = contractIdentifiersRepository;
    this.jdbcTemplate = jdbcTemplate;
    this.numeration = numeration;
    this.uuidService = uuidService;
    this.accNumeratorService = accNumeratorService;
  }

  /**
   * generateInternalIdentification
   *
   * @param request      the request
   * @param entityHeader entity that travels in the header of the request
   * @param brandHeader brand that travels in the header of the request
   * @return contractID value retrieved by access DC_CONTRACT_COUNTER TABLE
   * @throws Exception Exception
   */
  public GenerateAccountIdentifiersPostResponse generateAccountIdentifiers(
      GenerateAccountIdentifiersPostRequest request, String entityHeader, String brandHeader, String preAssigmentContractIndicator) throws Exception {

    Connection conn = null;
    try {
      conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
      conn.setAutoCommit(false);

      //Accedemos a la tabla DC_CONTRACT_COUNTER_java por entity y center
      // Obtenemos el último número de contract_id para la entidad y centro
      //llamando al servicio de la libreria accnumeratorlib

      var accountNumber =
          accNumeratorService.getAccountNumber(
              request.getCenter().getCenterId(),
              entityHeader,
              brandHeader,
              conn);

      // componemos la response
      GenerateAccountIdentifiersPostResponse response = new GenerateAccountIdentifiersPostResponse();

      // 1. Generar UUID único
      String newUUIDContractId = uuidService.generateUniqueContractId();

      AccounIdentification accounIdentification = new AccounIdentification();

      // 2. Setear los valores de la response AccounIdentification
      accounIdentification.setInternalIdentification(newUUIDContractId);

      accounIdentification.
          setNationalIdentification(numeration
              .generateBBAN(entityHeader,
                  request.getCenter().getCenterId(), accountNumber));
      accounIdentification.setLegacyIdentification(
          numeration.generateIBAN(accounIdentification.getNationalIdentification(), "ES"));

      accounIdentification.setInternationalIdentification(
          numeration.generateIBAN(accounIdentification.getNationalIdentification(), "ES"));

      response.setAccounIdentification(accounIdentification);

      // Crear ContractIdentifiersEntity
      ContractIdentifiersEntity contractIdentifiersEntity =
          createContractIdentifiersEntity(entityHeader, brandHeader, newUUIDContractId, accounIdentification, preAssigmentContractIndicator);

      // Insertar en la base de datos - tabla DC_CONTRACT_IDENTIFIERS
      contractIdentifiersRepository
          .insertContractIdentifiers(contractIdentifiersEntity, conn);

      // Commit transaction
      UtilLeanCoreRepository.commit(conn);

      return response;
    } catch (Exception e) {
      log.error("generateAccountIdentifiers ERROR {}", e.getMessage());
      UtilLeanCoreRepository.rollBack(conn);
      throw e;
    } finally {
      UtilLeanCoreRepository.closeConnection(conn);
    }
  }

  /**
   * Crea una entidad ContractIdentifiersEntity con los datos proporcionados.
   *
   * @param entityHeader      La entidad que viaja en el header de la petición.
   * @param brandHeader          La marca que viaja en el header de la petición.
   * @param newUUIDContractId    El nuevo UUID del contrato.
   * @param accounIdentification La identificación de la cuenta.
   * @return ContractIdentifiersEntity
   */
  private ContractIdentifiersEntity createContractIdentifiersEntity(String entityHeader, String brandHeader, String newUUIDContractId, AccounIdentification accounIdentification, String preAssigmentContractIndicator) {
    ContractIdentifiersEntity contractIdentifiersEntity = new ContractIdentifiersEntity();
    contractIdentifiersEntity.setEntity(entityHeader);
    contractIdentifiersEntity.setBrand(brandHeader);
    contractIdentifiersEntity.setContractId(newUUIDContractId);
    contractIdentifiersEntity.setStatus(preAssigmentContractIndicator.equals("Y") ? "PR" : "AS");
    contractIdentifiersEntity.setLegacyIdentification(null);
    contractIdentifiersEntity.setNationalIdentification(accounIdentification.getNationalIdentification());
    contractIdentifiersEntity.setInternationalIdentification(accounIdentification.getInternationalIdentification());
    contractIdentifiersEntity.setLastUpdaterUser("fcm-app-accmanag");
    contractIdentifiersEntity.setLastUpdaterDateTime(UtilLeanCoreData.getUtcTimestamp());
    return contractIdentifiersEntity;
  }
}
