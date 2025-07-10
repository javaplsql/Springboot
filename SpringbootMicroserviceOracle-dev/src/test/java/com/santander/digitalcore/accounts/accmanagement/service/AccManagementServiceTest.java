package com.java.developing.accounts.accountmanaging.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.util.Objects;

import javax.sql.DataSource;

import com.developing.app.accounts.accountmanaging.service.UuidService;
import com.developing.app.accounts.accountmanaging.service.accountmanagingService;
import com.java.developing.accounts.accnumeratorlib.service.AccNumeratorService;
import com.java.developing.accounts.accnumeratorlib.utils.Numeration;
import com.java.developing.accounts.util.lib.db.model.entity.ContractCounterjavaEntity;
import com.java.developing.accounts.util.lib.db.model.repository.ContractCounterjavaRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;

import com.java.developing.accounts.accountmanaging.model.Center;
import com.java.developing.accounts.accountmanaging.model.Product;
import com.java.developing.accounts.accountmanaging.model.genaccidentifiers.request.GenerateAccountIdentifiersPostRequest;
import com.java.developing.accounts.accountmanaging.model.genaccidentifiers.response.GenerateAccountIdentifiersPostResponse;
import com.java.developing.accounts.accountmanaging.repository.ContractIdentifiersRepositoryExt;

import com.java.developing.accounts.util.lib.db.model.entity.ContractIdentifiersEntity;

class accountmanagingServiceTest {

  @Mock
  private JdbcTemplate jdbcTemplate;

  @Mock
  private ContractIdentifiersRepositoryExt contractIdentifiersRepository;

  @Mock
  private ContractCounterjavaRepository contractCounterjavaRepository;

  @Mock
  private GenerateAccountIdentifiersPostRequest request;

  @Mock
  private Numeration numeration;

  @Mock
  private UuidService uuidService;

  @Mock
  private AccNumeratorService accNumeratorService;

  @InjectMocks
  private accountmanagingService accountmanagingService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }


  @Test
  void generateAccountIdentifiersReturnsResponseWhenValidRequest() throws Exception {
    GenerateAccountIdentifiersPostRequest validRequest = mock(GenerateAccountIdentifiersPostRequest.class);
    Product product = mock(Product.class);
    Center center = mock(Center.class);
    when(validRequest.getProduct()).thenReturn(product);
    when(validRequest.getCenter()).thenReturn(center);
    when(product.getProductCode()).thenReturn("1234567890");
    when(center.getCenterId()).thenReturn("1001");

    Connection mockConnection = mock(Connection.class);
    ContractCounterjavaEntity accountCounter = new ContractCounterjavaEntity();
    accountCounter.setContractId("00000000000000000001");

    when(jdbcTemplate.getDataSource()).thenReturn(mock(javax.sql.DataSource.class));
    when(Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection()).thenReturn(mockConnection);
    when(validRequest.getProduct().getProductCode()).thenReturn("1234567890");
    when(validRequest.getCenter().getCenterId()).thenReturn("1001");
    when(contractCounterjavaRepository.findByEntityAndCenter(anyString(), anyString(), eq(mockConnection)))
        .thenReturn(accountCounter);
    when(uuidService.generateUniqueContractId()).thenReturn("mockedUUID");
    when(numeration.generateBBAN(anyString(), anyString(), anyString())).thenReturn("mockedBBAN");
    when(numeration.generateIBAN(anyString(), eq("ES"))).thenReturn("mockedIBAN");
    String entityHeader = "0049";
    String brandHeader = "brand-header-value";
    String preAssigmentContractIndicator = "Y";
    GenerateAccountIdentifiersPostResponse response = accountmanagingService.generateAccountIdentifiers(validRequest, entityHeader, brandHeader, preAssigmentContractIndicator);

    assertNotNull(response);
    assertNotNull(response.getAccounIdentification());
    assertEquals("mockedUUID", response.getAccounIdentification().getInternalIdentification());
    verify(contractIdentifiersRepository, times(1)).insertContractIdentifiers(any(ContractIdentifiersEntity.class), eq(mockConnection));
    verify(mockConnection, times(1)).commit();
  }

  @Test
  void generateAccountIdentifiersRollsBackWhenInsertFails() throws Exception {
    GenerateAccountIdentifiersPostRequest requestInsertFails = mock(GenerateAccountIdentifiersPostRequest.class);
    Product product = mock(Product.class);
    Center center = mock(Center.class);
    when(requestInsertFails.getProduct()).thenReturn(product);
    when(requestInsertFails.getCenter()).thenReturn(center);
    when(product.getProductCode()).thenReturn("1234567890");
    when(center.getCenterId()).thenReturn("1001");

    String newUUID = "mockedUUID";
    when(uuidService.generateUniqueContractId()).thenReturn(newUUID);
    when(numeration.generateBBAN(anyString(), anyString(), anyString())).thenReturn("mockedBBAN");
    when(numeration.generateIBAN(anyString(), eq("ES"))).thenReturn("mockedIBAN");

    Connection mockConnection = mock(Connection.class);
    when(jdbcTemplate.getDataSource()).thenReturn(mock(DataSource.class));
    when(Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection()).thenReturn(mockConnection);
    doThrow(new RuntimeException("Insert failed")).when(contractIdentifiersRepository).insertContractIdentifiers(any(ContractIdentifiersEntity.class), eq(mockConnection));
    String entityHeader = "0049";
    String brandHeader = "brand-header-value";
    String preAssigmentContractIndicator = "Y";
    Assertions.assertThrows(RuntimeException.class, () -> accountmanagingService.generateAccountIdentifiers(requestInsertFails, entityHeader, brandHeader, preAssigmentContractIndicator));
    verify(mockConnection, times(1)).rollback();
  }

  @Test
  void generateAccountIdentifiersThrowsExceptionWhenConnectionIsNull() {
    GenerateAccountIdentifiersPostRequest requestNullConnection = mock(GenerateAccountIdentifiersPostRequest.class);
    Product product = mock(Product.class);
    when(requestNullConnection.getProduct()).thenReturn(product);
    when(product.getProductCode()).thenReturn("1234567890");

    when(jdbcTemplate.getDataSource()).thenReturn(null);
    String entityHeader = "0049";
    String brandHeader = "brand-header-value";
    String preAssigmentContractIndicator = "Y";
    Assertions.assertThrows(NullPointerException.class, () -> accountmanagingService.generateAccountIdentifiers(requestNullConnection, entityHeader, brandHeader, preAssigmentContractIndicator));
  }

  @Test
  void generateAccountIdentifiersThrowsExceptionWhenUuidServiceFails() throws Exception {
    GenerateAccountIdentifiersPostRequest requestUuidFails = mock(GenerateAccountIdentifiersPostRequest.class);
    Product product = mock(Product.class);
    Center center = mock(Center.class);
    when(requestUuidFails.getProduct()).thenReturn(product);
    when(requestUuidFails.getCenter()).thenReturn(center);
    when(product.getProductCode()).thenReturn("1234567890");
    when(center.getCenterId()).thenReturn("1001");

    when(uuidService.generateUniqueContractId()).thenThrow(new RuntimeException("UUID generation failed"));

    Connection mockConnection = mock(Connection.class);
    when(jdbcTemplate.getDataSource()).thenReturn(mock(DataSource.class));
    when(Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection()).thenReturn(mockConnection);
    String entityHeader = "0049";
    String brandHeader = "brand-header-value";
    String preAssigmentContractIndicator = "Y";
    Assertions.assertThrows(RuntimeException.class, () -> accountmanagingService.generateAccountIdentifiers(requestUuidFails, entityHeader, brandHeader, preAssigmentContractIndicator));
    verify(mockConnection, times(1)).rollback();
  }
}

