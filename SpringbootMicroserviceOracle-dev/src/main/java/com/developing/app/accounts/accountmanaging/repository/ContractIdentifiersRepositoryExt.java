package com.developing.app.accounts.accountmanaging.repository;

import com.java.developing.accounts.util.lib.core.exceptions.InternalServerErrorplsqlExceptionproject;
import com.java.developing.accounts.util.lib.db.model.repository.ContractIdentifiersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Service
@Slf4j
public class ContractIdentifiersRepositoryExt extends ContractIdentifiersRepository {

  /**
   * Constructor that initializes the repository with a JdbcTemplate.
   *
   * @param jdbcTemplate The JdbcTemplate used for database operations.
   */
  public ContractIdentifiersRepositoryExt(JdbcTemplate jdbcTemplate) {
    super(jdbcTemplate);
  }

  /**
   * Checks if a contract identifier exists in the database by its contract ID.
   *
   * @param contractId The contract ID to check for existence.
   * @param conn       The database connection to use for the query.
   * @return true if the contract ID exists, false otherwise.
   * @throws InternalServerErrorplsqlExceptionproject If an error occurs while accessing the database.
   */
  public boolean existsByContractId(String contractId, Connection conn) {
    String sql = """
        SELECT COUNT(1)
        FROM
        DC_CONTRACT_IDENTIFIERS
        WHERE
        CONTRACT_ID = ?
        """;

    if (conn == null) {
      throw new InternalServerErrorplsqlExceptionproject("DC-COMMON-T-0001", "DATABASE_ERROR");
    }
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setString(1, contractId);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return rs.getInt(1) > 0;
        }
      }
    } catch (SQLException e) {
      log.error("Error al verificar la existencia existsByContractId del CONTRACT_ID '{}': {}", contractId, e.getMessage());
      throw new InternalServerErrorplsqlExceptionproject("DC-COMMON-T-0001", "DATABASE_ERROR", e);
    }
    return false;
  }

}
