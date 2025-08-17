package com.santander.digitalcore.accounts.overdraftrs.web;

import com.santander.darwin.core.exceptions.BadRequestDarwinException;
import com.santander.darwin.core.exceptions.NotFoundDarwinException;
import com.santander.digitalcore.accounts.overdraftrs.dto.CodeReasonOverdraftRequest;
import com.santander.digitalcore.accounts.overdraftrs.dto.CodeReasonOverdraftResponse;
import com.santander.digitalcore.accounts.overdraftrs.service.CodeReasonOverdraftService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest
class CodeReasonOverdraftControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private CodeReasonOverdraftService codeReasonOverdraftService;

  @Test
  void testGetCodeReasonOverdraftListOk() throws Exception {
    when(codeReasonOverdraftService.getCodeReasonOverdraftList())
        .thenReturn(List.of(new CodeReasonOverdraftResponse("01", "Description 1", "user", LocalDateTime.now(), "user", LocalDateTime.now(), "user", LocalDateTime.now()),
            new CodeReasonOverdraftResponse("02", "Description 2", "user", LocalDateTime.now(), "user", LocalDateTime.now(), "user", LocalDateTime.now())));

    mockMvc.perform(get("/code_reason"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].reasonCode").value("01"))
        .andExpect(jsonPath("$[0].reasonCodeDescription").value("Description 1"))
        .andExpect(jsonPath("$[1].reasonCode").value("02"))
        .andExpect(jsonPath("$[1].reasonCodeDescription").value("Description 2"));
  }

  @Test
  void testGetCodeReasonOverdraftListNoContent() throws Exception {
    when(codeReasonOverdraftService.getCodeReasonOverdraftList()).thenReturn(Collections.emptyList());

    mockMvc.perform(get("/code_reason"))
        .andExpect(status().isNoContent())
        .andExpect(jsonPath("$").doesNotExist());
  }

  @Test
  void testGetCodeReasonOverdraftListInternalServerError() throws Exception {
    when(codeReasonOverdraftService.getCodeReasonOverdraftList()).thenThrow(new RuntimeException("Unexpected error"));

    mockMvc.perform(get("/code_reason"))
        .andExpect(status().isInternalServerError());
  }

  @Test
  void testGetActiveCodeReasonOverdraftListOk() throws Exception {
    when(codeReasonOverdraftService.getActiveCodeReasonOverdraftList())
        .thenReturn(List.of(new CodeReasonOverdraftResponse("01", "Description 1", "user", LocalDateTime.now(), "user", LocalDateTime.now(), null, null),
            new CodeReasonOverdraftResponse("02", "Description 2", "user", LocalDateTime.now(), "user", LocalDateTime.now(), null, null)));

    mockMvc.perform(get("/code_reason").param("active", "true"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].reasonCode").value("01"))
        .andExpect(jsonPath("$[0].reasonCodeDescription").value("Description 1"))
        .andExpect(jsonPath("$[1].reasonCode").value("02"))
        .andExpect(jsonPath("$[1].reasonCodeDescription").value("Description 2"));
  }

  @Test
  void testGetActiveCodeReasonOverdraftListNoContent() throws Exception {
    when(codeReasonOverdraftService.getActiveCodeReasonOverdraftList()).thenReturn(Collections.emptyList());

    mockMvc.perform(get("/code_reason").param("active", "true"))
        .andExpect(status().isNoContent())
        .andExpect(jsonPath("$").doesNotExist());
  }

  @Test
  void testGetActiveCodeReasonOverdraftListInternalServerError() throws Exception {
    when(codeReasonOverdraftService.getActiveCodeReasonOverdraftList()).thenThrow(new RuntimeException("Unexpected error"));

    mockMvc.perform(get("/code_reason").param("active", "true"))
        .andExpect(status().isInternalServerError());
  }

  @Test
  void testCreateCodeReasonOverdraftCreated() throws Exception {
    var body = """
        {
          "reasonCode": "01",
          "reasonDescription": "Description 1"
        }
        """;

    doNothing().when(codeReasonOverdraftService).createCodeReasonOverdraft(new CodeReasonOverdraftRequest("01", "Description 1"));

    mockMvc.perform(post("/code_reason")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body)).
        andExpect(status().isCreated());
  }

  @Test
  void testCreateCodeReasonOverdraftBadRequest() throws Exception {
    var body = """
        {
          "reasonCode": "",
          "reasonCodeDescription": "Description 1"
        }
        """;

    doNothing().when(codeReasonOverdraftService).createCodeReasonOverdraft(new CodeReasonOverdraftRequest("01", "Description 1"));

    mockMvc.perform(post("/code_reason")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isBadRequest());
  }

  @Test
  void testCreateCodeReasonOverdraftAlreadyExists() throws Exception {
    var body = """
        {
          "reasonCode": "01",
          "reasonCodeDescription": "Description 1"
        }
        """;

    doThrow(new BadRequestDarwinException("Already exists")).when(codeReasonOverdraftService).createCodeReasonOverdraft(new CodeReasonOverdraftRequest("01", "Description 1"));

    mockMvc.perform(post("/code_reason")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isBadRequest());
  }

  @Test
  void testCreateCodeReasonOverdraftInternalServerError() throws Exception {
    var body = """
        {
          "reasonCode": "01",
          "reasonCodeDescription": "Description 1"
        }
        """;

    doThrow(new RuntimeException("Unexcepted error")).when(codeReasonOverdraftService).createCodeReasonOverdraft(new CodeReasonOverdraftRequest("01", "Description 1"));

    mockMvc.perform(post("/code_reason")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isInternalServerError());
  }

  @Test
  void testUpdateCodeReasonOverdraftOk() throws Exception {
    var reasonCode = "01";
    var reasonDescription = "Updated Description";

    doNothing().when(codeReasonOverdraftService).updateCodeReasonOverdraft(reasonCode, reasonDescription);

    mockMvc.perform(patch("/code_reason/" + reasonCode)
            .param("reasonDescription", reasonDescription))
        .andExpect(status().isNoContent());
  }

  @Test
  void testUpdateCodeReasonOverdraftNotFound() throws Exception {
    var reasonCode = "01";
    var reasonDescription = "Updated Description";

    doThrow(new NotFoundDarwinException("Not found")).when(codeReasonOverdraftService).updateCodeReasonOverdraft(reasonCode, reasonDescription);

    mockMvc.perform(patch("/code_reason/" + reasonCode)
            .param("reasonDescription", reasonDescription))
        .andExpect(status().isNotFound());
  }

  @Test
  void testUpdateCodeReasonOverdraftBadRequest() throws Exception {
    var reasonCode = "01";
    var reasonDescription = "";

    mockMvc.perform(patch("/code_reason/" + reasonCode)
            .param("reasonDescription", reasonDescription))
        .andExpect(status().isBadRequest());
  }

  @Test
  void testUpdateCodeReasonOverdraftInternalServerError() throws Exception {
    var reasonCode = "01";
    var reasonDescription = "Updated Description";

    doThrow(new RuntimeException("Unexpected error")).when(codeReasonOverdraftService).updateCodeReasonOverdraft(reasonCode, reasonDescription);

    mockMvc.perform(patch("/code_reason/" + reasonCode)
            .param("reasonDescription", reasonDescription))
        .andExpect(status().isInternalServerError());
  }

  @Test
  void testDeleteCodeReasonOverdraftOk() throws Exception {
    var reasonCode = "01";

    doNothing().when(codeReasonOverdraftService).deleteCodeReasonOverdraft(reasonCode);

    mockMvc.perform(delete("/code_reason/" + reasonCode))
        .andExpect(status().isNoContent());
  }

  @Test
  void testDeleteCodeReasonOverdraftNotFound() throws Exception {
    var reasonCode = "01";

    doThrow(new NotFoundDarwinException("Not found")).when(codeReasonOverdraftService).deleteCodeReasonOverdraft(reasonCode);

    mockMvc.perform(delete("/code_reason/" + reasonCode))
        .andExpect(status().isNotFound());
  }

  @Test
  void testDeleteCodeReasonOverdraftInternalServerError() throws Exception {
    var reasonCode = "01";

    doThrow(new RuntimeException("Unexpected error")).when(codeReasonOverdraftService).deleteCodeReasonOverdraft(reasonCode);

    mockMvc.perform(delete("/code_reason/" + reasonCode))
        .andExpect(status().isInternalServerError());
  }

}
