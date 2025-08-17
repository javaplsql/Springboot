package com.santander.digitalcore.accounts.overdraftrs.service;

import com.santander.darwin.core.exceptions.BadRequestDarwinException;
import com.santander.darwin.core.exceptions.NotFoundDarwinException;
import com.santander.digitalcore.accounts.overdraftrs.dto.OperatorReasonOverdraftRequest;
import com.santander.digitalcore.accounts.overdraftrs.dto.OperatorReasonOverdraftResponse;
import com.santander.digitalcore.accounts.overdraftrs.mapper.OperatorReasonOverdraftMapper;
import com.santander.digitalcore.accounts.util.lib.db.model.entity.OperatorReasonOverdraftEntity;
import com.santander.digitalcore.accounts.util.lib.db.model.repository.overdraft.OperatorReasonOverdraftRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OperatorReasonOverdraftServiceTest {

  @Mock
  private OperatorReasonOverdraftRepository repository;

  @Mock
  private OperatorReasonOverdraftMapper mapper;

  @InjectMocks
  private OperatorReasonOverdraftService service;


  @Test
  void testGetOperatorReasonOverdraftListReturnsMappedList() {
    var entity = new OperatorReasonOverdraftEntity();
    entity.setOperatorCode("CODE1");
    entity.setOperatorDescription("Description1");
    when(repository.findAll()).thenReturn(List.of(entity));
    when(mapper.mapToOperatorReasonOverdraftResponse(entity)).thenReturn(buildCodeReasonOverdraftResponse());

    List<OperatorReasonOverdraftResponse> result = service.getOperatorReasonOverdraftList();

    assertEquals(1, result.size(), "Expected size of result list to be 1");
    assertEquals("CODE1", result.get(0).getOperatorCode(), "Expected operator code to be CODE1");
    assertEquals("Description1", result.get(0).getOperatorDescription(), "Expected operator description to be Description1");
  }

  @Test
  void testGetOperatorReasonOverdraftListReturnsEmptyList() {
    when(repository.findAll()).thenReturn(Collections.emptyList());

    List<OperatorReasonOverdraftResponse> result = service.getOperatorReasonOverdraftList();

    assertTrue(result.isEmpty(), "Expected result list to be empty");
  }

  @Test
  void testGetActiveOperatorReasonOverdraftListReturnsMappedList() {
    var entity = new OperatorReasonOverdraftEntity();
    entity.setOperatorCode("CODE1");
    entity.setOperatorDescription("Description1");
    when(repository.findActive()).thenReturn(List.of(entity));
    when(mapper.mapToOperatorReasonOverdraftResponse(entity)).thenReturn(buildCodeReasonOverdraftResponse());

    List<OperatorReasonOverdraftResponse> result = service.getActiveOperatorReasonOverdraftList();

    assertEquals(1, result.size(), "Expected size of result list to be 1");
    assertEquals("CODE1", result.get(0).getOperatorCode(), "Expected operator code to be CODE1");
    assertEquals("Description1", result.get(0).getOperatorDescription(), "Expected operator description to be Description1");
  }

  @Test
  void testGetActiveOperatorReasonOverdraftListReturnsEmptyList() {
    when(repository.findActive()).thenReturn(Collections.emptyList());

    List<OperatorReasonOverdraftResponse> result = service.getActiveOperatorReasonOverdraftList();

    assertTrue(result.isEmpty(), "Expected result list to be empty");
  }

  @Test
  void testCreateOperatorReasonOverdraftSucceedsWhenNotExists() {
    OperatorReasonOverdraftRequest operatorReasonOverdraftRequest = new OperatorReasonOverdraftRequest("CODE2", "Description2");
    when(repository.findByPK("CODE2")).thenReturn(null);

    service.createOperatorReasonOverdraft(operatorReasonOverdraftRequest);

    verify(repository).insert(eq("CODE2"), eq("Description2"), anyString(), any(LocalDateTime.class));
  }

  @Test
  void testCreateOperatorReasonOverdraftThrowsExceptionWhenAlreadyExists() {
    OperatorReasonOverdraftRequest operatorReasonOverdraftRequest = new OperatorReasonOverdraftRequest("CODE3", "Description3");
    when(repository.findByPK("CODE3")).thenReturn(new OperatorReasonOverdraftEntity());

    assertThrows(BadRequestDarwinException.class, () -> service.createOperatorReasonOverdraft(operatorReasonOverdraftRequest),
        "Expected BadRequestDarwinException to be thrown");
  }

  @Test
  void testUpdateOperatorReasonOverdraftSucceedsWhenExists() {
    when(repository.findByPK("CODE4")).thenReturn(new OperatorReasonOverdraftEntity());

    service.updateOperatorReasonOverdraft("CODE4", "NewDesc");

    verify(repository).update(eq("CODE4"), eq("NewDesc"), anyString(), any(LocalDateTime.class));
  }

  @Test
  void testUpdateOperatorReasonOverdraftThrowsExceptionWhenNotExists() {
    when(repository.findByPK("CODE5")).thenReturn(null);

    assertThrows(NotFoundDarwinException.class, () -> service.updateOperatorReasonOverdraft("CODE5", "Desc"),
        "Expected NotFoundDarwinException to be thrown");
  }

  @Test
  void testDeleteOperatorReasonOverdraftSucceedsWhenExists() {
    when(repository.findByPK("CODE6")).thenReturn(new OperatorReasonOverdraftEntity());

    service.deleteOperatorReasonOverdraft("CODE6");

    verify(repository).delete(eq("CODE6"), anyString());
  }

  @Test
  void testDeleteOperatorReasonOverdraftThrowsExceptionWhenNotExists() {
    when(repository.findByPK("CODE7")).thenReturn(null);

    assertThrows(NotFoundDarwinException.class, () -> service.deleteOperatorReasonOverdraft("CODE7"),
        "Expected NotFoundDarwinException to be thrown");
  }

  private OperatorReasonOverdraftResponse buildCodeReasonOverdraftResponse() {
    return new OperatorReasonOverdraftResponse("CODE1", "Description1", "user",
        LocalDateTime.now(), "user", LocalDateTime.now(), "user", LocalDateTime.now());
  }
}
