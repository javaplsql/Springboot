package com.developing.app.accounts.accountmanaging.service;

import com.java.developing.accounts.accountmanaging.repository.ContractIdentifiersRepositoryExt;
import com.java.developing.accounts.util.lib.db.utils.UtilLeanCoreRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
public class UuidService {

  private final ContractIdentifiersRepositoryExt contractIdentifiersRepository;
  private final JdbcTemplate jdbcTemplate;

  public UuidService(ContractIdentifiersRepositoryExt contractIdentifiersRepository, JdbcTemplate jdbcTemplate) {
    this.contractIdentifiersRepository = contractIdentifiersRepository;
    this.jdbcTemplate = jdbcTemplate;
  }

  public String generateUniqueContractId() throws SQLException {
    Connection conn = null;
    try {
      conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
      conn.setAutoCommit(false);

      String uuid;
      do {
        uuid = UUID.randomUUID().toString();
      } while (existsInDatabase(uuid, conn));
      return uuid;
    } catch (Exception e) {
      log.error("generateUniqueContractId ERROR {}", e.getMessage());
      UtilLeanCoreRepository.rollBack(conn);
      throw e;
    } finally {
      UtilLeanCoreRepository.closeConnection(conn);
    }
  }

  /**
   * Checks if the given UUID already exists in the database.
   *
   * @param uuid the UUID to check
   * @param conn the database connection to use for the check
   * @return true if the UUID exists, false otherwise
   */
  private boolean existsInDatabase(String uuid, Connection conn) {
    return contractIdentifiersRepository.existsByContractId(uuid, conn);
  }
}

