package com.santander.digitalcore.accounts.overdraftrs.service;

import com.santander.darwin.core.exceptions.BadRequestDarwinException;
import com.santander.darwin.core.exceptions.NotFoundDarwinException;
import com.santander.digitalcore.accounts.overdraftrs.dto.CodeReasonOverdraftRequest;
import com.santander.digitalcore.accounts.overdraftrs.dto.CodeReasonOverdraftResponse;
import com.santander.digitalcore.accounts.overdraftrs.mapper.CodeReasonOverdraftMapper;
import com.santander.digitalcore.accounts.util.lib.db.model.entity.CodeReasonOverdraftEntity;
import com.santander.digitalcore.accounts.util.lib.db.model.repository.overdraft.CodeReasonOverdraftRepository;
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
class CodeReasonOverdraftServiceTest {

  @Mock
  private CodeReasonOverdraftRepository repository;

  @Mock
  private CodeReasonOverdraftMapper mapper;

  @InjectMocks
  private CodeReasonOverdraftService service;


  @Test
  void testGetCodeReasonOverdraftListReturnsMappedList() {
    var entity = new CodeReasonOverdraftEntity();
    entity.setReasonCode("CODE1");
    entity.setReasonCodeDescription("Description1");
    when(repository.findAll()).thenReturn(List.of(entity));
    when(mapper.mapToCodeReasonOverdraftResponse(entity)).thenReturn(buildCodeReasonOverdraftResponse());

    List<CodeReasonOverdraftResponse> result = service.getCodeReasonOverdraftList();

    assertEquals(1, result.size(), "Expected size of result list to be 1");
    assertEquals("CODE1", result.get(0).getReasonCode(), "Expected reason code to be CODE1");
    assertEquals("Description1", result.get(0).getReasonCodeDescription(), "Expected reason description to be Description1");
  }

  @Test
  void testGetCodeReasonOverdraftListReturnsEmptyList() {
    when(repository.findAll()).thenReturn(Collections.emptyList());

    List<CodeReasonOverdraftResponse> result = service.getCodeReasonOverdraftList();

    assertTrue(result.isEmpty(), "Expected result list to be empty");
  }

  @Test
  void testGetActiveCodeReasonOverdraftListReturnsMappedList() {
    var entity = new CodeReasonOverdraftEntity();
    entity.setReasonCode("CODE1");
    entity.setReasonCodeDescription("Description1");
    when(repository.findActive()).thenReturn(List.of(entity));
    when(mapper.mapToCodeReasonOverdraftResponse(entity)).thenReturn(buildCodeReasonOverdraftResponse());

    List<CodeReasonOverdraftResponse> result = service.getActiveCodeReasonOverdraftList();

    assertEquals(1, result.size(), "Expected size of result list to be 1");
    assertEquals("CODE1", result.get(0).getReasonCode(), "Expected reason code to be CODE1");
    assertEquals("Description1", result.get(0).getReasonCodeDescription(), "Expected reason description to be Description1");
  }

  @Test
  void testGetActiveCodeReasonOverdraftListReturnsEmptyList() {
    when(repository.findActive()).thenReturn(Collections.emptyList());

    List<CodeReasonOverdraftResponse> result = service.getActiveCodeReasonOverdraftList();

    assertTrue(result.isEmpty(), "Expected result list to be empty");
  }

  @Test
  void testCreateCodeReasonOverdraftSucceedsWhenNotExists() {
    CodeReasonOverdraftRequest codeReasonOverdraftRequest = new CodeReasonOverdraftRequest("CODE2", "Description2");
    when(repository.findByPK("CODE2")).thenReturn(null);

    service.createCodeReasonOverdraft(codeReasonOverdraftRequest);

    verify(repository).insert(eq("CODE2"), eq("Description2"), anyString(), any(LocalDateTime.class));
  }

  @Test
  void testCreateCodeReasonOverdraftThrowsExceptionWhenAlreadyExists() {
    CodeReasonOverdraftRequest codeReasonOverdraftRequest = new CodeReasonOverdraftRequest("CODE3", "Description3");
    when(repository.findByPK("CODE3")).thenReturn(new CodeReasonOverdraftEntity());

    assertThrows(BadRequestDarwinException.class, () -> service.createCodeReasonOverdraft(codeReasonOverdraftRequest),
        "Expected BadRequestDarwinException to be thrown");
  }

  @Test
  void testUpdateCodeReasonOverdraftSucceedsWhenExists() {
    when(repository.findByPK("CODE4")).thenReturn(new CodeReasonOverdraftEntity());

    service.updateCodeReasonOverdraft("CODE4", "NewDesc");

    verify(repository).update(eq("CODE4"), eq("NewDesc"), anyString(), any(LocalDateTime.class));
  }

  @Test
  void testUpdateCodeReasonOverdraftThrowsExceptionWhenNotExists() {
    when(repository.findByPK("CODE5")).thenReturn(null);

    assertThrows(NotFoundDarwinException.class, () -> service.updateCodeReasonOverdraft("CODE5", "Desc"),
        "Expected NotFoundDarwinException to be thrown");
  }

  @Test
  void testDeleteCodeReasonOverdraftSucceedsWhenExists() {
    when(repository.findByPK("CODE6")).thenReturn(new CodeReasonOverdraftEntity());

    service.deleteCodeReasonOverdraft("CODE6");

    verify(repository).delete(eq("CODE6"), anyString());
  }

  @Test
  void testDeleteCodeReasonOverdraftThrowsExceptionWhenNotExists() {
    when(repository.findByPK("CODE7")).thenReturn(null);

    assertThrows(NotFoundDarwinException.class, () -> service.deleteCodeReasonOverdraft("CODE7"),
        "Expected NotFoundDarwinException to be thrown");
  }

  private CodeReasonOverdraftResponse buildCodeReasonOverdraftResponse() {
    return new CodeReasonOverdraftResponse("CODE1", "Description1", "user",
        LocalDateTime.now(), "user", LocalDateTime.now(), "user", LocalDateTime.now());
  }
}
