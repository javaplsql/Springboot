package com.santander.digitalcore.accounts.overdraftrs.repository;

import com.santander.digitalcore.accounts.overdraftrs.util.Constants;
import com.santander.digitalcore.accounts.util.lib.db.model.entity.ReasonOverdraftEntity;
import com.santander.digitalcore.accounts.util.lib.db.model.entity.ReasonOverdraftEntityPK;
import com.santander.digitalcore.accounts.util.lib.db.model.repository.ReasonOverdraftRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * Repositorio para gestionar motivos de descubierto (overdraft) en la base de datos.
 * Extiende ReasonOverdraftRepository para proporcionar funcionalidades adicionales.
 */
@Slf4j
@Repository
public class ReasonOverdraftRepositoryExt extends ReasonOverdraftRepository {

  /**
   * Constructor que inicializa el repositorio con un JdbcTemplate.
   *
   * @param jdbcTemplate El JdbcTemplate utilizado para las operaciones de base de datos.
   */
  public ReasonOverdraftRepositoryExt(JdbcTemplate jdbcTemplate) {
    super(jdbcTemplate);
  }

  /**
   * Método para buscar un motivo de descubierto por su clave primaria.
   * Este método devuelve un Optional que contiene el motivo de descubierto si se encuentra,
   * o un Optional vacío si no se encuentra.
   *
   * @param entityPK la clave primaria del motivo de descubierto
   * @return un Optional que contiene el motivo de descubierto encontrado, o vacío si no se encuentra
   * @throws SQLException si ocurre un error al ejecutar la consulta SQL
   */
  public Optional<ReasonOverdraftEntity> findByPK(ReasonOverdraftEntityPK entityPK) throws SQLException {
    var sql = """
        SELECT
            ENTITY, BRAND, REASON_CODE, START_DATE_TIME, END_DATE_TIME,
            DESCDATA, SETTLEMENT_INDICATOR, MANDATORY, DEFAULTDATE,
            MONTHS, COUNTER, CONDITION, CREATION_USER, CREATION_DATE_TIME,
            LAST_UPDATER_USER, LAST_UPDATER_DATE_TIME
        FROM
            DC_REASON_OVERDRAFT
        WHERE
            ENTITY = ? AND BRAND = ? AND REASON_CODE = ?
        """;

    try (var conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setString(Constants.NUMBER_1, entityPK.getEntity());
      ps.setString(Constants.NUMBER_2, entityPK.getBrand());
      ps.setString(Constants.NUMBER_3, entityPK.getReasonCode());

      try (ResultSet rs = ps.executeQuery()) {
        return (rs.next()) ? Optional.ofNullable(mapper.mapRow(rs, 1)) : Optional.empty();
      }
    } catch (SQLException e) {
      log.error("Error findByPk");
      throw e;
    }
  }

  /**
   * Método para buscar una lista de motivos de descubierto con paginación y offset.
   * Este método permite filtrar por código de razón, entidad, marca y estado.
   *
   * @param reasonCode the reason code
   * @param entity     the entity
   * @param brand      the brand
   * @param status     the status (ACT, CAN, PEN, or null for default active)
   * @param offset     the offset for pagination
   * @param limit      the limit for pagination
   * @return a list of ReasonOverdraftEntity objects matching the criteria
   * @throws SQLException if an SQL error occurs
   */
  public List<ReasonOverdraftEntity> findList(
      String reasonCode, String entity, String brand,
      String status, String offset, String limit) throws SQLException {

    List<ReasonOverdraftEntity> resultList = new ArrayList<>();
    List<Object> parameters = new ArrayList<>();

    var sql = buildQuery(status, reasonCode, entity, brand, parameters);

    if (offset != null && limit != null) {
      sql += " OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
      parameters.add(Integer.parseInt(offset));
      parameters.add(Integer.parseInt(limit));
    }

    try (var conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
      setPreparedStatementParameters(ps, parameters);

      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          resultList.add(mapper.mapRow(rs, rs.getRow()));
        }
      }
    } catch (SQLException e) {
      log.error("Error findList", e);
      throw e;
    }

    return resultList;
  }

  /**
   * Builds the SQL query for finding ReasonOverdraftEntity records based on the provided parameters.
   *
   * @param status     the status of the reason (ACT, CAN, PEN, or null for default active)
   * @param reasonCode the reason code to filter by
   * @param entity     the entity to filter by
   * @param brand      the brand to filter by
   * @param parameters the list to hold query parameters
   * @return the constructed SQL query string
   */
  private static String buildQuery(
      String status, String reasonCode, String entity, String brand, List<Object> parameters) {
    var sql = new StringBuilder("""
        SELECT
            *
        FROM
            DC_REASON_OVERDRAFT
        WHERE
            1=1
        """);

    var now = new Timestamp(System.currentTimeMillis());

    if (status == null || "ACT".equalsIgnoreCase(status)) {
      sql.append(" AND START_DATE_TIME <= ? AND END_DATE_TIME >= ?");
      parameters.add(now);
      parameters.add(now);
    } else if ("CAN".equalsIgnoreCase(status)) {
      sql.append(" AND (START_DATE_TIME > ? OR END_DATE_TIME < ?)");
      parameters.add(now);
      parameters.add(now);
    } else if ("PEN".equalsIgnoreCase(status)) {
      sql.append(" AND START_DATE_TIME > ?");
      parameters.add(now);
    } else {
      log.warn("Unhandled status: {}", status);
    }

    if (reasonCode != null) {
      sql.append(" AND REASON_CODE = ?");
      parameters.add(reasonCode);
    }
    if (entity != null) {
      sql.append(" AND ENTITY = ?");
      parameters.add(entity);
    }
    if (brand != null) {
      sql.append(" AND BRAND = ?");
      parameters.add(brand);
    }

    sql.append(" ORDER BY ENTITY, BRAND, REASON_CODE");
    return sql.toString();
  }

  /**
   * Sets the parameters for the PreparedStatement based on the provided list of parameters.
   *
   * @param ps         the PreparedStatement to set parameters on
   * @param parameters the list of parameters to set
   * @throws SQLException if an SQL error occurs while setting parameters
   */
  private static void setPreparedStatementParameters(
      PreparedStatement ps, List<Object> parameters) throws SQLException {
    for (var i = 0; i < parameters.size(); i++) {
      Object param = parameters.get(i);
      if (param instanceof String string) {
        ps.setString(i + 1, string);
      } else if (param instanceof Integer integer) {
        ps.setInt(i + 1, integer);
      } else if (param instanceof Timestamp timestamp) {
        ps.setTimestamp(i + 1, timestamp);
      } else {
        log.warn("Unhandled param type: {}", param.getClass().getSimpleName());
      }
    }
  }

  /**
   * Updates an existing ReasonOverdraftEntity in the database.
   *
   * @param reasonOverdraftEntity the entity to be updated
   * @throws SQLException if an SQL error occurs
   */
  public void update(ReasonOverdraftEntity reasonOverdraftEntity) throws SQLException {
    var sql = """
        UPDATE
            DC_REASON_OVERDRAFT
        SET
            START_DATE_TIME = ?, END_DATE_TIME = ?, DESCDATA = ?,
            SETTLEMENT_INDICATOR = ?, MANDATORY = ?, DEFAULTDATE = ?,
            MONTHS = ?, COUNTER = ?, CONDITION = ?, LAST_UPDATER_USER = ?,
            LAST_UPDATER_DATE_TIME = ?
        WHERE
            ENTITY = ? AND BRAND = ? AND REASON_CODE = ?
        """;

    try (var conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setObject(Constants.NUMBER_1, reasonOverdraftEntity.getStartDateTime());
      ps.setObject(Constants.NUMBER_2, reasonOverdraftEntity.getEndDateTime());
      ps.setString(Constants.NUMBER_3, reasonOverdraftEntity.getDescData());
      ps.setString(Constants.NUMBER_4, reasonOverdraftEntity.getSettlementIndicator());
      ps.setString(Constants.NUMBER_5, reasonOverdraftEntity.getMandatory());
      ps.setObject(Constants.NUMBER_6, reasonOverdraftEntity.getDefaultDate());
      ps.setBigDecimal(Constants.NUMBER_7, reasonOverdraftEntity.getMonths());
      ps.setBigDecimal(Constants.NUMBER_8, reasonOverdraftEntity.getCounter());
      ps.setString(Constants.NUMBER_9, reasonOverdraftEntity.getCondition());
      ps.setString(Constants.NUMBER_10, reasonOverdraftEntity.getLastUpdaterUser());
      ps.setObject(Constants.NUMBER_11, LocalDateTime.now());
      ps.setString(Constants.NUMBER_12, reasonOverdraftEntity.getPk().getEntity());
      ps.setString(Constants.NUMBER_13, reasonOverdraftEntity.getPk().getBrand());
      ps.setString(Constants.NUMBER_14, reasonOverdraftEntity.getPk().getReasonCode());

      ps.executeUpdate();
    } catch (SQLException e) {
      log.error("Error updateReasonOverdraft", e);
      throw e;
    }
  }

  /**
   * Deletes a ReasonOverdraftEntity by its primary key.
   *
   * @param entityPK the primary key of the entity to be deleted
   * @param userId   the user ID of the last updater
   * @throws SQLException if an SQL error occurs
   */
  public void delete(ReasonOverdraftEntityPK entityPK, String userId) throws SQLException {
    var sql = """
        UPDATE
            DC_REASON_OVERDRAFT
        SET
            LAST_UPDATER_USER = ?, LAST_UPDATER_DATE_TIME = ?, END_DATE_TIME = ?
        WHERE
            ENTITY = ? AND BRAND = ? AND REASON_CODE = ?
        """;

    try (var conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setString(Constants.NUMBER_1, userId);
      ps.setObject(Constants.NUMBER_2, LocalDateTime.now());
      ps.setObject(Constants.NUMBER_3, LocalDateTime.now());
      ps.setString(Constants.NUMBER_4, entityPK.getEntity());
      ps.setString(Constants.NUMBER_5, entityPK.getBrand());
      ps.setString(Constants.NUMBER_6, entityPK.getReasonCode());

      ps.executeUpdate();
    } catch (SQLException e) {
      log.error("Error deleteReasonOverdraft", e);
      throw e;

    }
  }

}
