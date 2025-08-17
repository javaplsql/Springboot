package com.santander.digitalcore.accounts.overdraftrs.web;

import com.santander.darwin.core.exceptions.BadRequestDarwinException;
import com.santander.darwin.core.exceptions.NotFoundDarwinException;
import com.santander.digitalcore.accounts.overdraftrs.dto.OperatorReasonOverdraftRequest;
import com.santander.digitalcore.accounts.overdraftrs.dto.OperatorReasonOverdraftResponse;
import com.santander.digitalcore.accounts.overdraftrs.service.OperatorReasonOverdraftService;
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
class OperatorReasonOverdraftControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private OperatorReasonOverdraftService operatorReasonOverdraftService;

  @Test
  void testGetOperatorReasonOverdraftListOk() throws Exception {
    when(operatorReasonOverdraftService.getOperatorReasonOverdraftList())
        .thenReturn(List.of(new OperatorReasonOverdraftResponse("==", "IGUAL", "user", LocalDateTime.now(), "user", LocalDateTime.now(), "user", LocalDateTime.now()),
            new OperatorReasonOverdraftResponse("!=", "Distinto", "user", LocalDateTime.now(), "user", LocalDateTime.now(), null, null)));

    mockMvc.perform(get("/operator_reason"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].operatorCode").value("=="))
        .andExpect(jsonPath("$[0].operatorDescription").value("IGUAL"))
        .andExpect(jsonPath("$[1].operatorCode").value("!="))
        .andExpect(jsonPath("$[1].operatorDescription").value("Distinto"));
  }

  @Test
  void testGetOperatorReasonOverdraftListNoContent() throws Exception {
    when(operatorReasonOverdraftService.getOperatorReasonOverdraftList()).thenReturn(Collections.emptyList());

    mockMvc.perform(get("/operator_reason"))
        .andExpect(status().isNoContent())
        .andExpect(jsonPath("$").doesNotExist());
  }

  @Test
  void testGetOperatorReasonOverdraftListInternalServerError() throws Exception {
    when(operatorReasonOverdraftService.getOperatorReasonOverdraftList()).thenThrow(new RuntimeException("Unexpected error"));

    mockMvc.perform(get("/operator_reason"))
        .andExpect(status().isInternalServerError());
  }

  @Test
  void testGetActiveOperatorReasonOverdraftListOk() throws Exception {
    when(operatorReasonOverdraftService.getOperatorReasonOverdraftList())
        .thenReturn(List.of(new OperatorReasonOverdraftResponse("==", "IGUAL", "user", LocalDateTime.now(), "user", LocalDateTime.now(), null, null),
            new OperatorReasonOverdraftResponse("!=", "Distinto", "user", LocalDateTime.now(), "user", LocalDateTime.now(), null, null)));

    mockMvc.perform(get("/operator_reason"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].operatorCode").value("=="))
        .andExpect(jsonPath("$[0].operatorDescription").value("IGUAL"))
        .andExpect(jsonPath("$[1].operatorCode").value("!="))
        .andExpect(jsonPath("$[1].operatorDescription").value("Distinto"));
  }

  @Test
  void testGetActiveOperatorReasonOverdraftListNoContent() throws Exception {
    when(operatorReasonOverdraftService.getOperatorReasonOverdraftList()).thenReturn(Collections.emptyList());

    mockMvc.perform(get("/operator_reason"))
        .andExpect(status().isNoContent())
        .andExpect(jsonPath("$").doesNotExist());
  }

  @Test
  void testGetActiveOperatorReasonOverdraftListInternalServerError() throws Exception {
    when(operatorReasonOverdraftService.getOperatorReasonOverdraftList()).thenThrow(new RuntimeException("Unexpected error"));

    mockMvc.perform(get("/operator_reason"))
        .andExpect(status().isInternalServerError());
  }

  @Test
  void testCreateOperatorReasonOverdraftCreated() throws Exception {
    var body = """
        {
          "operatorCode": "==",
          "operatorDescription": "IGUAL"
        }
        """;

    doNothing().when(operatorReasonOverdraftService).createOperatorReasonOverdraft(new OperatorReasonOverdraftRequest("==", "IGUAL"));

    mockMvc.perform(post("/operator_reason")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body)).
        andExpect(status().isCreated());
  }

  @Test
  void testCreateOperatorReasonOverdraftBadRequest() throws Exception {
    var body = """
        {
          "operatorCode": "",
          "operatorDescription": "IGUAL"
        }
        """;

    doNothing().when(operatorReasonOverdraftService).createOperatorReasonOverdraft(new OperatorReasonOverdraftRequest("==", "IGUAL"));

    mockMvc.perform(post("/operator_reason")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isBadRequest());
  }

  @Test
  void testCreateOperatorReasonOverdraftAlreadyExists() throws Exception {
    var body = """
        {
          "operatorCode": "==",
          "operatorDescription": "IGUAL"
        }
        """;

    doThrow(new BadRequestDarwinException("Already exists")).when(operatorReasonOverdraftService).createOperatorReasonOverdraft(new OperatorReasonOverdraftRequest("==", "IGUAL"));

    mockMvc.perform(post("/operator_reason")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isBadRequest());
  }

  @Test
  void testCreateOperatorReasonOverdraftInternalServerError() throws Exception {
    var body = """
        {
          "operatorCode": "==",
          "operatorDescription": "IGUAL"
        }
        """;

    doThrow(new RuntimeException("Unexcepted error")).when(operatorReasonOverdraftService).createOperatorReasonOverdraft(new OperatorReasonOverdraftRequest("==", "IGUAL"));

    mockMvc.perform(post("/operator_reason")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isInternalServerError());
  }

  @Test
  void testUpdateOperatorReasonOverdraftOk() throws Exception {
    var operatorCode = "==";
    var operatorDescription = "Updated Description";

    doNothing().when(operatorReasonOverdraftService).updateOperatorReasonOverdraft(operatorCode, operatorDescription);

    mockMvc.perform(patch("/operator_reason/" + operatorCode)
            .param("operatorDescription", operatorDescription))
        .andExpect(status().isNoContent());
  }

  @Test
  void testUpdateOperatorReasonOverdraftNotFound() throws Exception {
    var operatorCode = "==";
    var operatorDescription = "Updated Description";

    doThrow(new NotFoundDarwinException("Not found")).when(operatorReasonOverdraftService).updateOperatorReasonOverdraft(operatorCode, operatorDescription);

    mockMvc.perform(patch("/operator_reason/" + operatorCode)
            .param("operatorDescription", operatorDescription))
        .andExpect(status().isNotFound());
  }

  @Test
  void testUpdateOperatorReasonOverdraftBadRequest() throws Exception {
    var operatorCode = "==";
    var operatorDescription = "";

    mockMvc.perform(patch("/operator_reason/" + operatorCode)
            .param("operatorDescription", operatorDescription))
        .andExpect(status().isBadRequest());
  }

  @Test
  void testUpdateOperatorReasonOverdraftInternalServerError() throws Exception {
    var operatorCode = "==";
    var operatorDescription = "Updated Description";

    doThrow(new RuntimeException("Unexpected error")).when(operatorReasonOverdraftService).updateOperatorReasonOverdraft(operatorCode, operatorDescription);

    mockMvc.perform(patch("/operator_reason/" + operatorCode)
            .param("operatorDescription", operatorDescription))
        .andExpect(status().isInternalServerError());
  }

  @Test
  void testDeleteOperatorReasonOverdraftOk() throws Exception {
    var operatorCode = "==";

    doNothing().when(operatorReasonOverdraftService).deleteOperatorReasonOverdraft(operatorCode);

    mockMvc.perform(delete("/operator_reason/" + operatorCode))
        .andExpect(status().isNoContent());
  }

  @Test
  void testDeleteOperatorReasonOverdraftNotFound() throws Exception {
    var operatorCode = "==";

    doThrow(new NotFoundDarwinException("Not found")).when(operatorReasonOverdraftService).deleteOperatorReasonOverdraft(operatorCode);

    mockMvc.perform(delete("/operator_reason/" + operatorCode))
        .andExpect(status().isNotFound());
  }

  @Test
  void testDeleteOperatorReasonOverdraftInternalServerError() throws Exception {
    var operatorCode = "==";

    doThrow(new RuntimeException("Unexpected error")).when(operatorReasonOverdraftService).deleteOperatorReasonOverdraft(operatorCode);

    mockMvc.perform(delete("/operator_reason/" + operatorCode))
        .andExpect(status().isInternalServerError());
  }

}
