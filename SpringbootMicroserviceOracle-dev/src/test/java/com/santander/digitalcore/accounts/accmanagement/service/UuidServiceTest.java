package com.santander.digitalcore.accounts.accmanagement.service;

import com.santander.digitalcore.accounts.accmanagement.repository.ContractIdentifiersRepositoryExt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UuidServiceTest {

  @Mock
  private ContractIdentifiersRepositoryExt contractIdentifiersRepository;

  @Mock
  private JdbcTemplate jdbcTemplate;

  @InjectMocks
  private UuidService uuidService;

  @Test
  void generateUniqueContractIdReturnsNewUuidWhenNotInDatabase() throws SQLException {
    Connection mockConnection = mock(Connection.class);
    when(jdbcTemplate.getDataSource()).thenReturn(mock(javax.sql.DataSource.class));
    when(Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection()).thenReturn(mockConnection);
    when(contractIdentifiersRepository.existsByContractId(anyString(), eq(mockConnection))).thenReturn(false);

    String uuid = uuidService.generateUniqueContractId();

    assertNotNull(uuid);
    verify(contractIdentifiersRepository, times(1)).existsByContractId(uuid, mockConnection);
  }

  @Test
  void generateUniqueContractIdRetriesWhenUuidExistsInDatabase() throws SQLException {
    Connection mockConnection = mock(Connection.class);
    when(jdbcTemplate.getDataSource()).thenReturn(mock(javax.sql.DataSource.class));
    when(Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection()).thenReturn(mockConnection);
    when(contractIdentifiersRepository.existsByContractId(anyString(), eq(mockConnection)))
        .thenReturn(true)
        .thenReturn(false);

    String uuid = uuidService.generateUniqueContractId();

    assertNotNull(uuid);
    verify(contractIdentifiersRepository, times(2)).existsByContractId(anyString(), eq(mockConnection));
  }

  @Test
  void generateUniqueContractIdThrowsExceptionWhenConnectionFails(){
    when(jdbcTemplate.getDataSource()).thenReturn(null);

    assertThrows(NullPointerException.class, () -> uuidService.generateUniqueContractId());
  }

  @Test
  void generateUniqueContractIdRollsBackOnException() throws SQLException {
    Connection mockConnection = mock(Connection.class);
    when(jdbcTemplate.getDataSource()).thenReturn(mock(javax.sql.DataSource.class));
    when(Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection()).thenReturn(mockConnection);
    doThrow(new RuntimeException("Database error")).when(contractIdentifiersRepository).existsByContractId(anyString(), eq(mockConnection));

    assertThrows(RuntimeException.class, () -> uuidService.generateUniqueContractId());
    verify(mockConnection, times(1)).rollback();
  }
}
