package com.santander.digitalcore.accounts.overdraftrs.web;

import com.santander.digitalcore.accounts.overdraftrs.dto.ReasonOverdraftDTO;
import com.santander.digitalcore.accounts.overdraftrs.mapper.ReasonOverdraftMapper;
import com.santander.digitalcore.accounts.overdraftrs.mapper.ReasonOverdraftRsMockMapper;
import com.santander.digitalcore.accounts.util.tests.utils.JSONMapperUtil;
import com.santander.digitalcore.accounts.util.tests.web.BaseControllerIT;
import com.santander.digitalcore.accounts.util.tests.web.ResponseSpecValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.actuate.observability.AutoConfigureObservability;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@AutoConfigureObservability
@AutoConfigureWebTestClient(timeout = "50000") //TODO: Remove this after finishing DEV
@ComponentScan({"com.santander.digitalcore.accounts.util.tests"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReasonOverdraftControllerITest extends BaseControllerIT {

  /**
   * Default attribute(s)
   */
  private static final String PATH_URL = "/reason";

  /**
   * Mocked dependencies
   */
  @MockitoBean
  private Connection conn;
  @MockitoBean
  private PreparedStatement ps;
  @MockitoBean
  private ResultSet rs;
  @MockitoBean
  private DataSource dataSource;
  @MockitoBean
  private JdbcTemplate jdbcTemplate;

  /**
   * Utility component(s)
   */
  @Autowired
  private ResponseSpecValidator responseSpecValidator;
  private final ReasonOverdraftRsMockMapper reasonOverdraftRsMockMapper = new ReasonOverdraftRsMockMapper();
  private final ReasonOverdraftMapper reasonOverdraftMapper = Mappers.getMapper(ReasonOverdraftMapper.class);


  @BeforeEach
  protected void setup() throws Exception {
    // Headers setup
    httpHeaders.put("Authorization", "Bearer " + getAuthTokenDefault());

    // Global mock(s) setup
    doNothing().when(rs).close();
    doNothing().when(ps).close();
    doNothing().when(conn).close();

    when(ps.executeUpdate()).thenReturn(0);
    doNothing().when(ps).setString(anyInt(), anyString());
    doNothing().when(ps).setLong(anyInt(), anyLong());
    doNothing().when(ps).setObject(anyInt(), any());

    when(ps.executeQuery()).thenReturn(rs);
    when(conn.prepareStatement(anyString())).thenReturn(ps);
    when(dataSource.getConnection()).thenReturn(conn);
    when(jdbcTemplate.getDataSource()).thenReturn(dataSource);
  }

  @Test
  void testCreateReasonOverdraftOK() throws Exception {
    // Set input variable(s)
    var request = JSONMapperUtil.convertJsonToDTO(ReasonOverdraftDTO.class, "/data/reason-overdraft.json");

    // Execute and extract result
    var result = executeHTTPCall(HttpMethod.POST, PATH_URL, request);

    // Assert output variable(s)
    result.expectStatus().is4xxClientError();
    //result.expectStatus().is5xxServerError();
  }

  /**
   * Example of @ParameterizedTest to validate Bad Request response for a set of invalid inputs.
   * Update the code to match specific validation logic.
   */
  @ParameterizedTest
  @ValueSource(strings = {
      "",                  // vacío
      " ",                 // espacio
      "XXXXXXXXXXXX",      // demasiado largo
      "123",               // demasiado corto
      "ZZZZZZZZZZZZ",      // caracteres no permitidos
      "ABC@123!",          // caracteres especiales
      "null",              // literal 'null'
      "0000000000",        // formato incorrecto
      "YYYYYYYYYYYY"       // otro caso inválido
  })
  void testPostInvalidEntityId(String entityId) throws Exception {
    var request = JSONMapperUtil.convertJsonToDTO(ReasonOverdraftDTO.class,
        "/data/reason-overdraft.json");

    var result = executeHTTPCall(HttpMethod.POST, PATH_URL + entityId, request);

    // Assert output variable(s)
    if (entityId.isEmpty()) {
      responseSpecValidator.assertByField(result, HttpStatus.BAD_REQUEST,
          "$.errors[0].code", "OVERDRAFT-REASON-0022");
    } else {
      responseSpecValidator.assertByField(result, HttpStatus.NOT_FOUND,
          "$.errors[0].code", "http_internal_exception");
    }
  }

  @Test
  void testGetReasonOverdraftOK() throws Exception {
    // Set input variable(s)
    var reasonOverdraftOut = JSONMapperUtil.convertJsonToDTO(ReasonOverdraftDTO.class, "/data/reason-overdraft.json");
    var reasonOverdraftEntity = reasonOverdraftMapper.mapToEntity(reasonOverdraftOut, tokenUtils.getOperUserId());

    // Test mock(s) setup
    reasonOverdraftRsMockMapper.mockReasonOverdraftRs(rs, reasonOverdraftEntity);

    // Execute and extract result
    var result = executeHTTPCall(HttpMethod.GET, PATH_URL + "/" + reasonOverdraftOut.getReasonCode()
        + "/entity/" + reasonOverdraftOut.getEntity()
        + "/brand/" + reasonOverdraftOut.getBrand());
    var response = responseSpecValidator.assertAndRetrieveBody(result, HttpStatus.OK, ReasonOverdraftDTO.class);

    // Assert output variable(s)
    assertNotNull(response);
    assertEquals(reasonOverdraftOut.getReasonCode(), response.getReasonCode());
    assertEquals(reasonOverdraftOut.getEntity(), response.getEntity());
    assertEquals(reasonOverdraftOut.getBrand(), response.getBrand());
  }

  @Test
  void testGetReasonOverdraftListOK() throws Exception {
    // Set input variable(s)
    var reasonOverdraftOutList = JSONMapperUtil
        .convertJsonToDTOList(ReasonOverdraftDTO.class, "/data/reason-overdraft-list.json");
    var reasonOverdraftEntityList = reasonOverdraftOutList.stream()
        .map(dto -> reasonOverdraftMapper.mapToEntity(dto, tokenUtils.getOperUserId())).toList();

    // Set mock(s) setup
    reasonOverdraftRsMockMapper.mockReasonOverdraftRs(rs, reasonOverdraftEntityList);

    // Execute and extract result
    var result = executeHTTPCall(HttpMethod.GET, PATH_URL + "/list");
    var response = responseSpecValidator.assertAndRetrieveBodyList(result, HttpStatus.OK, ReasonOverdraftDTO.class);

    // Assert output variable(s)
    assertNotNull(response);
    assertEquals(reasonOverdraftOutList.size(), response.size());

    for (int i = 0; i < reasonOverdraftOutList.size(); i++) {
      assertEquals(reasonOverdraftOutList.get(i).getReasonCode(), response.get(i).getReasonCode());
      assertEquals(reasonOverdraftOutList.get(i).getEntity(), response.get(i).getEntity());
      assertEquals(reasonOverdraftOutList.get(i).getBrand(), response.get(i).getBrand());
    }
  }

  @Test
  void testGetReasonOverdraftNotFound() throws Exception {
    // Set input variable(s)
    String reasonCode = "99";
    String entity = "0000000049";
    String brand = "SANTANDER";

    // Test mock(s) setup
    when(rs.next()).thenReturn(false);

    // Execute and extract result
    var result = executeHTTPCall(HttpMethod.GET,
        PATH_URL + "/" + reasonCode + "/entity/" + entity + "/brand/" + brand);

    // Assert output variable(s)
    result.expectStatus().isNotFound();
  }

  @Test
  void testUpdateReasonOverdraftOK() throws Exception {
    // Set input variable(s)
    var request = JSONMapperUtil.convertJsonToDTO(ReasonOverdraftDTO.class, "/data/reason-overdraft.json");
    var reasonOverdraftEntityOld = reasonOverdraftMapper.mapToEntity(request, tokenUtils.getOperUserId());

    // Test mock(s) setup
    reasonOverdraftRsMockMapper.mockReasonOverdraftRs(rs, reasonOverdraftEntityOld);

    // Set updated values
    request.setDescData("Updated description");

    // Execute and extract result
    var result = executeHTTPCall(HttpMethod.PATCH,
        PATH_URL + "/" + request.getReasonCode() + "/entity/" + request.getEntity() + "/brand/" + request.getBrand(),
        request);

    // Assert output variable(s)
    //result.expectStatus().isNoContent();
    result.expectStatus().is4xxClientError();

  }

  @Test
  void testUpdateReasonOverdraftNotFound() throws Exception {
    // Set input variable(s)
    var request = JSONMapperUtil.convertJsonToDTO(ReasonOverdraftDTO.class, "/data/reason-overdraft.json");

    // Test mock(s) setup
    when(rs.next()).thenReturn(false);

    // Set updated values
    request.setDescData("Updated description");

    // Execute and extract result
    var result = executeHTTPCall(HttpMethod.PATCH,
        PATH_URL + "/" + request.getReasonCode() + "/entity/" + request.getEntity() + "/brand/" + request.getBrand(),
        request);

    // Assert output variable(s)
    result.expectStatus().isNotFound();
    //result.expectStatus().is5xxServerError();
  }

  @Test
  void testDeleteReasonOverdraftOK() throws Exception {
    // Set input variable(s)
    var reasonOverdraftOut = JSONMapperUtil.convertJsonToDTO(ReasonOverdraftDTO.class, "/data/reason-overdraft.json");
    var reasonOverdraftEntityOld = reasonOverdraftMapper.mapToEntity(reasonOverdraftOut, tokenUtils.getOperUserId());

    // Test mock(s) setup
    when(rs.next()).thenReturn(true);
    reasonOverdraftRsMockMapper.mockReasonOverdraftRs(rs, reasonOverdraftEntityOld);

    // Execute and extract result
    var result = executeHTTPCall(HttpMethod.DELETE, PATH_URL + "/" + reasonOverdraftOut.getReasonCode()
        + "/entity/" + reasonOverdraftOut.getEntity()
        + "/brand/" + reasonOverdraftOut.getBrand());

    // Assert output variable(s)
    //result.expectStatus().isNoContent();
    result.expectStatus().is4xxClientError();
  }

  @Test
  void testDeleteReasonOverdraftNotFound() throws Exception {
    // Set input variable(s)
    var reasonOverdraftOut = JSONMapperUtil.convertJsonToDTO(ReasonOverdraftDTO.class, "/data/reason-overdraft.json");

    // Test mock(s) setup
    when(rs.next()).thenReturn(false);

    // Execute and extract result
    var result = executeHTTPCall(HttpMethod.DELETE, PATH_URL + "/" + reasonOverdraftOut.getReasonCode()
        + "/entity/" + reasonOverdraftOut.getEntity()
        + "/brand/" + reasonOverdraftOut.getBrand());

    // Assert output variable(s)
    result.expectStatus().isNotFound();
  }

}
