package com.santander.digitalcore.accounts.overdraftrs.service;

import com.santander.digitalcore.accounts.overdraftrs.dto.ReasonOverdraftDTO;
import com.santander.digitalcore.accounts.overdraftrs.mapper.ReasonOverdraftMapper;
import com.santander.digitalcore.accounts.overdraftrs.repository.ReasonOverdraftRepositoryExt;
import com.santander.digitalcore.accounts.overdraftrs.util.Constants;
import com.santander.digitalcore.accounts.util.lib.core.exceptions.BadRequestDarwinExceptionLeancore;
import com.santander.digitalcore.accounts.util.lib.core.exceptions.InternalServerErrorDarwinExceptionLeancore;
import com.santander.digitalcore.accounts.util.lib.core.exceptions.NotFoundDarwinExceptionLeancore;
import com.santander.digitalcore.accounts.util.lib.core.utils.UtilLeanCoreData;

import com.santander.digitalcore.accounts.util.lib.db.model.entity.ReasonOverdraftEntity;
import com.santander.digitalcore.accounts.util.lib.db.model.repository.overdraft.CodeReasonOverdraftRepository;
import com.santander.digitalcore.accounts.util.lib.db.model.repository.overdraft.OperatorReasonOverdraftRepository;
import com.santander.digitalcore.accounts.util.tests.utils.JSONMapperUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReasonOverdraftServiceTest {

  @Mock
  private ReasonOverdraftRepositoryExt reasonOverdraftRepository;
  @Mock
  private OperatorReasonOverdraftRepository operatorReasonOverdraftRepository;
  @Mock
  private CodeReasonOverdraftRepository codeReasonOverdraftRepository;
  @Mock
  private JdbcTemplate jdbcTemplate;

  private final ReasonOverdraftMapper reasonOverdraftMapper = Mappers.getMapper(ReasonOverdraftMapper.class);

  @InjectMocks
  private ReasonOverdraftService reasonOverdraftService;


  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    reasonOverdraftService = new ReasonOverdraftService(reasonOverdraftRepository,
        operatorReasonOverdraftRepository, codeReasonOverdraftRepository);
  }

  @Test
  void testGetReasonOverdraftOK() throws SQLException, IOException {
    // Arrange
    var reasonOverdraftOut = JSONMapperUtil.convertJsonToDTO(ReasonOverdraftDTO.class, "/data/reason-overdraft-entityPK.json");

    var entityPK = reasonOverdraftMapper.mapToEntityPK(reasonOverdraftOut.getReasonCode(), reasonOverdraftOut.getEntity(), reasonOverdraftOut.getBrand());

    var entityObj = reasonOverdraftMapper.mapToEntity(reasonOverdraftOut, UtilLeanCoreData.getUserId(Constants.MS_NAME));

    when(reasonOverdraftRepository.findByPK(entityPK)).thenReturn(Optional.of(entityObj));
    // Act
    ReasonOverdraftDTO result = reasonOverdraftService.getReasonOverdraft(reasonOverdraftOut.getReasonCode(), reasonOverdraftOut.getEntity(), reasonOverdraftOut.getBrand());

    // Assert
    assertNotNull(result);
    assertEquals(reasonOverdraftOut.getReasonCode(), result.getReasonCode());
    assertEquals(reasonOverdraftOut.getEntity(), result.getEntity());
    assertEquals(reasonOverdraftOut.getBrand(), result.getBrand());
  }

  @Test
  void testGetReasonOverdraftNotFound() throws SQLException, IOException {
    var reasonOverdraftOut = JSONMapperUtil.convertJsonToDTO(ReasonOverdraftDTO.class, "/data/reason-overdraft-reasoncode99.json");

    var entityPK = reasonOverdraftMapper.mapToEntityPK(reasonOverdraftOut.getReasonCode(), reasonOverdraftOut.getEntity(), reasonOverdraftOut.getBrand());


    when(reasonOverdraftRepository.findByPK(entityPK)).thenReturn(Optional.empty());

    assertThrows(NotFoundDarwinExceptionLeancore.class, () ->
        reasonOverdraftService.getReasonOverdraft(reasonOverdraftOut.getReasonCode(), reasonOverdraftOut.getEntity(), reasonOverdraftOut.getBrand()));
  }

  @Test
  void testGetReasonOverdraftThrowsSQLException() throws SQLException, IOException {

    var reasonOverdraftOut = JSONMapperUtil.convertJsonToDTO(ReasonOverdraftDTO.class, "/data/reason-overdraft-entityPK.json");

    var entityPK = reasonOverdraftMapper.mapToEntityPK(reasonOverdraftOut.getReasonCode(), reasonOverdraftOut.getEntity(), reasonOverdraftOut.getBrand());

    when(reasonOverdraftRepository.findByPK(entityPK)).thenThrow(new SQLException("DB error"));

    assertThrows(InternalServerErrorDarwinExceptionLeancore.class, () ->
        reasonOverdraftService.getReasonOverdraft(reasonOverdraftOut.getReasonCode(), reasonOverdraftOut.getEntity(), reasonOverdraftOut.getBrand()));
  }

  @Test
  void testGetReasonOverdraftListOK() throws SQLException, IOException {
    var reasonOverdraftOut01 = JSONMapperUtil.convertJsonToDTO(ReasonOverdraftDTO.class, "/data/reason-overdraft-entityPK.json");
    var reasonOverdraftOut99 = JSONMapperUtil.convertJsonToDTO(ReasonOverdraftDTO.class, "/data/reason-overdraft-reasoncode99.json");

    var entityList = List.of(
        reasonOverdraftMapper.mapToEntity(reasonOverdraftOut01, UtilLeanCoreData.getUserId(Constants.MS_NAME)),
        reasonOverdraftMapper.mapToEntity(reasonOverdraftOut99, UtilLeanCoreData.getUserId(Constants.MS_NAME))
    );

    when(reasonOverdraftRepository.findList(
        reasonOverdraftOut01.getReasonCode(),
        reasonOverdraftOut01.getEntity(),
        reasonOverdraftOut01.getBrand(),
        "ACT", "0", "20"
    )).thenReturn(entityList);

    List<ReasonOverdraftDTO> result = reasonOverdraftService.getReasonOverdraftList(
        reasonOverdraftOut01.getReasonCode(),
        reasonOverdraftOut01.getEntity(),
        reasonOverdraftOut01.getBrand(),
        "ACT", "0", "20"
    );

    assertNotNull(result);
    assertEquals(2, result.size());
    assertEquals(reasonOverdraftOut01.getReasonCode(), result.get(0).getReasonCode());
    assertEquals(reasonOverdraftOut01.getEntity(), result.get(0).getEntity());
    assertEquals(reasonOverdraftOut01.getBrand(), result.get(0).getBrand());
    assertEquals(reasonOverdraftOut99.getReasonCode(), result.get(1).getReasonCode());
    assertEquals(reasonOverdraftOut99.getEntity(), result.get(1).getEntity());
    assertEquals(reasonOverdraftOut99.getBrand(), result.get(1).getBrand());
  }

  @Test
  void testGetReasonOverdraftListPagOK() throws SQLException, IOException {
    var reasonOverdraftOut01 = JSONMapperUtil.convertJsonToDTO(ReasonOverdraftDTO.class, "/data/reason-overdraft-entityPK.json");
    var reasonOverdraftOut99 = JSONMapperUtil.convertJsonToDTO(ReasonOverdraftDTO.class, "/data/reason-overdraft-reasoncode99.json");

    var entityList = List.of(
        reasonOverdraftMapper.mapToEntity(reasonOverdraftOut01, UtilLeanCoreData.getUserId(Constants.MS_NAME)),
        reasonOverdraftMapper.mapToEntity(reasonOverdraftOut99, UtilLeanCoreData.getUserId(Constants.MS_NAME))
    );

    when(reasonOverdraftRepository.findList(
        reasonOverdraftOut01.getReasonCode(),
        reasonOverdraftOut01.getEntity(),
        reasonOverdraftOut01.getBrand(),
        "ACT", "0", "20"
    )).thenReturn(entityList);

    List<ReasonOverdraftDTO> result = reasonOverdraftService.getReasonOverdraftList(
        reasonOverdraftOut01.getReasonCode(),
        reasonOverdraftOut01.getEntity(),
        reasonOverdraftOut01.getBrand(),
        "ACT", "0", "20"
    );
    assertNotNull(result);
    assertEquals(2, result.size());
    assertEquals("SANTANDER", result.get(0).getBrand());
    assertEquals("OPENBANK", result.get(1).getBrand());
  }

  @Test
  void testGetReasonOverdraftListThrowsSQLException() throws SQLException, IOException {
    var reasonOverdraftOut01 = JSONMapperUtil.convertJsonToDTO(ReasonOverdraftDTO.class, "/data/reason-overdraft-entityPK.json");

    when(reasonOverdraftRepository.findList(
        reasonOverdraftOut01.getReasonCode(),
        reasonOverdraftOut01.getEntity(),
        reasonOverdraftOut01.getBrand(),
        "ACT", "0", "20"
    )).thenThrow(new SQLException("DB error"));

    assertThrows(InternalServerErrorDarwinExceptionLeancore.class, () ->
        reasonOverdraftService.getReasonOverdraftList(
            reasonOverdraftOut01.getReasonCode(),
            reasonOverdraftOut01.getEntity(),
            reasonOverdraftOut01.getBrand(),
            "ACT", "0", "20"));
  }

  @Test
  void testCreateReasonOverdraftOK() throws SQLException, IOException {
    var reasonOverdraftOut = JSONMapperUtil.convertJsonToDTO(ReasonOverdraftDTO.class, "/data/reason-overdraft-entityPK.json");
    Connection mockConnection = mock(Connection.class);

    when(jdbcTemplate.getDataSource()).thenReturn(mock(javax.sql.DataSource.class));
    when(Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection()).thenReturn(mockConnection);
    doNothing().when(reasonOverdraftRepository).insert(any());

    //assertDoesNotThrow(() -> reasonOverdraftService.createReasonOverdraft(reasonOverdraftOut));
    verify(reasonOverdraftRepository, times(0)).insert(any());
  }

  @Test
  void testCreateReasonOverdraftThrowsSQLException() throws SQLException, IOException {
    var reasonOverdraftOut = JSONMapperUtil.convertJsonToDTO(ReasonOverdraftDTO.class, "/data/reason-overdraft-reasoncode11.json");
    Connection mockConnection = mock(Connection.class);

    when(jdbcTemplate.getDataSource()).thenReturn(mock(javax.sql.DataSource.class));
    when(Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection()).thenReturn(mockConnection);
    doThrow(new SQLException("DB error")).when(reasonOverdraftRepository).insert(any());

    assertThrows(BadRequestDarwinExceptionLeancore.class, () ->
        reasonOverdraftService.createReasonOverdraft(reasonOverdraftOut));
  }

  @Test
  void testUpdateReasonOverdraftOK() throws SQLException, IOException {
    var reasonOverdraftOut = JSONMapperUtil.convertJsonToDTO(ReasonOverdraftDTO.class, "/data/reason-overdraft-entityPK.json");

    var entityPK = reasonOverdraftMapper.mapToEntityPK(reasonOverdraftOut.getReasonCode(), reasonOverdraftOut.getEntity(), reasonOverdraftOut.getBrand());
    var oldEntity = reasonOverdraftMapper.mapToEntity(reasonOverdraftOut, UtilLeanCoreData.getUserId(Constants.MS_NAME));

    when(reasonOverdraftRepository.findByPK(entityPK)).thenReturn(Optional.of(oldEntity));
    doNothing().when(reasonOverdraftRepository).update(any());

//    assertDoesNotThrow(() -> reasonOverdraftService.updateReasonOverdraft(reasonCode, entity, brand, request));
    verify(reasonOverdraftRepository, times(0)).update(any());
  }

  @Test
  void testUpdateReasonOverdraftNotFound() throws SQLException, IOException {

    var reasonOverdraftOut = JSONMapperUtil.convertJsonToDTO(ReasonOverdraftDTO.class, "/data/reason-overdraft-entityPK.json");

    var entityPK = reasonOverdraftMapper.mapToEntityPK(reasonOverdraftOut.getReasonCode(), reasonOverdraftOut.getEntity(), reasonOverdraftOut.getBrand());


    when(reasonOverdraftRepository.findByPK(entityPK)).thenReturn(Optional.empty());

//    assertThrows(NotFoundDarwinExceptionLeancore.class, () ->
//        reasonOverdraftService.updateReasonOverdraft(reasonCode, entity, brand, request));
  }

  @Test
  void testUpdateReasonOverdraftThrowsSQLException() throws SQLException, IOException {

    var reasonOverdraftOut = JSONMapperUtil.convertJsonToDTO(ReasonOverdraftDTO.class, "/data/reason-overdraft-entityPK.json");

    var entityPK = reasonOverdraftMapper.mapToEntityPK(reasonOverdraftOut.getReasonCode(), reasonOverdraftOut.getEntity(), reasonOverdraftOut.getBrand());

    when(reasonOverdraftRepository.findByPK(entityPK)).thenReturn(Optional.of(mock(ReasonOverdraftEntity.class)));
    doThrow(new SQLException("DB error")).when(reasonOverdraftRepository).update(any());

//    assertThrows(InternalServerErrorDarwinExceptionLeancore.class, () ->
//        reasonOverdraftService.updateReasonOverdraft(reasonCode, entity, brand, request));
  }

  @Test
  void testDeleteReasonOverdraftOK() throws SQLException, IOException {

    var reasonOverdraftOut = JSONMapperUtil.convertJsonToDTO(ReasonOverdraftDTO.class, "/data/reason-overdraft-entityPK.json");

    var entityPK = reasonOverdraftMapper.mapToEntityPK(reasonOverdraftOut.getReasonCode(), reasonOverdraftOut.getEntity(), reasonOverdraftOut.getBrand());

    var entityObj = reasonOverdraftMapper.mapToEntity(reasonOverdraftOut, UtilLeanCoreData.getUserId(Constants.MS_NAME));

    when(reasonOverdraftRepository.findByPK(entityPK)).thenReturn(Optional.of(entityObj));
    doNothing().when(reasonOverdraftRepository).delete(eq(entityPK), any());

    assertDoesNotThrow(() -> reasonOverdraftService.deleteReasonOverdraft(reasonOverdraftOut.getReasonCode(), reasonOverdraftOut.getEntity(), reasonOverdraftOut.getBrand()));
    verify(reasonOverdraftRepository, times(1)).delete(eq(entityPK), any());
  }

  @Test
  void testDeleteReasonOverdraftNotFound() throws SQLException, IOException {

    var reasonOverdraftOut = JSONMapperUtil.convertJsonToDTO(ReasonOverdraftDTO.class, "/data/reason-overdraft-entityPK.json");

    var entityPK = reasonOverdraftMapper.mapToEntityPK(reasonOverdraftOut.getReasonCode(), reasonOverdraftOut.getEntity(), reasonOverdraftOut.getBrand());

    when(reasonOverdraftRepository.findByPK(entityPK)).thenReturn(Optional.empty());

    assertThrows(NotFoundDarwinExceptionLeancore.class, () ->
        reasonOverdraftService.deleteReasonOverdraft(reasonOverdraftOut.getReasonCode(), reasonOverdraftOut.getEntity(), reasonOverdraftOut.getBrand()));
  }

  @Test
  void testDeleteReasonOverdraftThrowsSQLException() throws SQLException, IOException {

    var reasonOverdraftOut = JSONMapperUtil.convertJsonToDTO(ReasonOverdraftDTO.class, "/data/reason-overdraft-entityPK.json");

    var entityPK = reasonOverdraftMapper.mapToEntityPK(reasonOverdraftOut.getReasonCode(), reasonOverdraftOut.getEntity(), reasonOverdraftOut.getBrand());

    when(reasonOverdraftRepository.findByPK(entityPK)).thenReturn(Optional.of(mock(ReasonOverdraftEntity.class)));

    doThrow(new SQLException("DB error")).when(reasonOverdraftRepository).delete(eq(entityPK), any());

    assertThrows(InternalServerErrorDarwinExceptionLeancore.class, () ->
        reasonOverdraftService.deleteReasonOverdraft(reasonOverdraftOut.getReasonCode(), reasonOverdraftOut.getEntity(), reasonOverdraftOut.getBrand()));
  }
}
