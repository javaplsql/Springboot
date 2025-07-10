package com.java.developing.accounts.accountmanaging.repository;

import com.developing.app.accounts.accountmanaging.repository.ContractIdentifiersRepositoryExt;
import com.java.developing.accounts.util.lib.core.exceptions.InternalServerErrorplsqlExceptionproject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContractIdentifiersRepositoryExtTest {

  @Mock
  private JdbcTemplate jdbcTemplate;

  @InjectMocks
  private ContractIdentifiersRepositoryExt repositoryExt;

  @Test
  void returnsTrueWhenContractIdExistsInDatabase() throws SQLException {
    Connection mockConnection = mock(Connection.class);
    PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
    ResultSet mockResultSet = mock(ResultSet.class);

    when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
    when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
    when(mockResultSet.next()).thenReturn(true);
    when(mockResultSet.getInt(1)).thenReturn(1);

    boolean result = repositoryExt.existsByContractId("existingContractId", mockConnection);

    assertTrue(result);
  }

  @Test
  void returnsFalseWhenContractIdDoesNotExistInDatabase() throws SQLException {
    Connection mockConnection = mock(Connection.class);
    PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
    ResultSet mockResultSet = mock(ResultSet.class);

    when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
    when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
    when(mockResultSet.next()).thenReturn(true);
    when(mockResultSet.getInt(1)).thenReturn(0);

    boolean result = repositoryExt.existsByContractId("nonExistingContractId", mockConnection);

    assertFalse(result);
  }

  @Test
  void throwsExceptionWhenSqlErrorOccurs() throws SQLException {
    Connection mockConnection = mock(Connection.class);
    PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);

    when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
    when(mockPreparedStatement.executeQuery()).thenThrow(new SQLException("Database error"));

    InternalServerErrorplsqlExceptionproject exception = assertThrows(
        InternalServerErrorplsqlExceptionproject.class,
        () -> repositoryExt.existsByContractId("contractId", mockConnection)
    );

    assertEquals("DC-COMMON-T-0001", exception.getErrorName());
    assertEquals("DATABASE_ERROR", exception.getMessage());
  }

@Test
  void dothrowsExceptionWhenConnectionIsNull() {
    Connection mockConnection = null;
    InternalServerErrorplsqlExceptionproject exception = assertThrows(
        InternalServerErrorplsqlExceptionproject.class,
        () -> repositoryExt.existsByContractId("contractId", mockConnection)
    );

    assertEquals("DC-COMMON-T-0001", exception.getErrorName());
    assertEquals("DATABASE_ERROR", exception.getMessage());
  }
}
