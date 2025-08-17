package com.santander.digitalcore.accounts.overdraftrs.web;

import com.santander.digitalcore.accounts.overdraftrs.dto.ReasonOverdraftDTO;
import com.santander.digitalcore.accounts.overdraftrs.service.ReasonOverdraftService;

import com.santander.digitalcore.accounts.overdraftrs.util.OperationType;
import com.santander.digitalcore.accounts.overdraftrs.util.Validations;
import com.santander.digitalcore.accounts.overdraftrs.web.info.CreateReasonOverdraftPostDoc;
import com.santander.digitalcore.accounts.overdraftrs.web.info.DeleteReasonOverdraftDoc;
import com.santander.digitalcore.accounts.overdraftrs.web.info.GetReasonOverdraftGetDoc;
import com.santander.digitalcore.accounts.overdraftrs.web.info.GetReasonOverdraftListDoc;
import com.santander.digitalcore.accounts.overdraftrs.web.info.UpdateReasonOverdraftPatchDoc;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import lombok.AllArgsConstructor;

import java.util.List;
/** * Controlador REST para gestionar motivos de descubierto.
 * Proporciona endpoints para crear, actualizar, eliminar y consultar motivos de descubierto.
 */
@RestController
@AllArgsConstructor
@RequestMapping(path = "/reason")
public class ReasonOverdraftController {

  private ReasonOverdraftService reasonOverdraftService;

  /**
   * Endpoint para obtener un motivo de descubierto específico.
   *
   * @param reasonCode Código del motivo de descubierto.
   * @param entity     Entidad asociada al motivo.
   * @param brand      Marca asociada al motivo.
   * @return ResponseEntity con el motivo de descubierto solicitado.
   * <ul>
   * <li>HTTP 200 OK with a ReasonOverdraft object if available.</li>
   * <li>HTTP 204 No Content if there are no ReasonOverdraft available.</li>
   * <li>HTTP 500 Internal Server Error if an unexpected error occurs.</li>
   * </ul>   *
   */
  @GetReasonOverdraftGetDoc
  @GetMapping("/{reason_code}/entity/{entity}/brand/{brand}")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<ReasonOverdraftDTO> getReasonOverdraft(
      @PathVariable(name = "reason_code") String reasonCode,
      @PathVariable(name = "entity") String entity,
      @PathVariable(name = "brand") String brand) {
    // Validación de parámetros
    Validations.doValidationsWithoutRequestBody(OperationType.GET, entity, brand, reasonCode);
    var response = reasonOverdraftService.getReasonOverdraft(reasonCode, entity, brand);
    return ResponseEntity.ok(response);
  }

  /**
   * Endpoint para obtener una lista de motivos de descubierto.
   *
   * @param reasonCode Código del motivo de descubierto (opcional).
   * @param entity     Entidad asociada al motivo (opcional).
   * @param brand      Marca asociada al motivo (opcional).
   * @param status     Estado del motivo (opcional).
   * @param offset     Número de página para la paginación (por defecto 0).
   * @param limit      Tamaño de la página para la paginación (por defecto 20).
   * @return ResponseEntity con una lista de motivos de descubierto.
   * <ul>
   * <li>HTTP 200 OK with a list of ReasonOverdraft objects if available.</li>
   * <li>HTTP 204 No Content if there are no ReasonOverdrafts available.</li>
   * <li>HTTP 500 Internal Server Error if an unexpected error occurs.</li>
   * </ul>   *
   */
  @GetReasonOverdraftListDoc
  @GetMapping(path = "/list")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<List<ReasonOverdraftDTO>> getReasonOverdraftList(
      @RequestParam(name = "reason", required = false) String reasonCode,
      @RequestParam(name = "entity", required = false) String entity,
      @RequestParam(name = "brand", required = false) String brand,
      @RequestParam(name = "status", required = false) String status,
      @RequestParam(name = "_offset", defaultValue = "0") String offset,
      @RequestParam(name = "_limit", defaultValue = "20") String limit
  ) {
    var response = reasonOverdraftService.getReasonOverdraftList(reasonCode, entity, brand, status, offset, limit);
    return ResponseEntity.ok(response);
  }

  /**
   * Endpoint para crear un nuevo motivo de descubierto.
   * Crea un nuevo motivo de descubierto.
   *
   * @param request DTO que contiene los datos del motivo de descubierto a crear.
   * @return ResponseEntity con estado NO_CONTENT si la creación es exitosa.
   * <ul>
   * <li>HTTP 201 Created if the reason overdraft is created successfully.</li>
   * <li>HTTP 400 Bad Request if the request body is invalid or missing required fields
   * or if the ReasonOverdraft object already exists.</li>
   * <li>HTTP 500 Internal Server Error if an unexpected error occurs.</li>
   * </ul>
   *
   */
  @CreateReasonOverdraftPostDoc
  @PostMapping
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public ResponseEntity<Void> createReasonOverdraft(@RequestBody ReasonOverdraftDTO request) {
    request.validate(OperationType.CREATE, null, null, null);
    reasonOverdraftService.createReasonOverdraft(request);
    return ResponseEntity.noContent().build();
  }

  /**
   * Endpoint para actualizar un motivo de descubierto existente.
   *
   * @param reasonCode Código del motivo de descubierto a actualizar.
   * @param entity     Entidad asociada al motivo.
   * @param brand      Marca asociada al motivo.
   * @param request    DTO que contiene los nuevos datos del motivo de descubierto.
   * @return ResponseEntity con estado NO_CONTENT si la actualización es exitosa.
   * <ul>
   * <li>HTTP 204 No Content if the update is successful.</li>
   * <li>HTTP 400 Bad Request if the request parameter is invalid or missing.</li>
   * <li>HTTP 404 Not Found if the specified reason code does not exist.</li>
   * <li>HTTP 500 Internal Server Error if an unexpected error occurs.</li>
   * </ul>   *
   */
  @UpdateReasonOverdraftPatchDoc
  @PatchMapping("/{reason_code}/entity/{entity}/brand/{brand}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public ResponseEntity<Void> updateReasonOverdraft(
      @PathVariable(name = "reason_code") String reasonCode,
      @PathVariable(name = "entity") String entity,
      @PathVariable(name = "brand") String brand,
      @RequestBody ReasonOverdraftDTO request) {

    request.validate(OperationType.UPDATE, entity, brand, reasonCode);
    reasonOverdraftService.updateReasonOverdraft(reasonCode, entity, brand, request);
    return ResponseEntity.noContent().build();
  }

  /**
   * Endpoint para efectuar la baja lógica de un motivo de descubierto.
   *
   * @param reasonCode Código del motivo de descubierto a dar de baja.
   * @param entity     Entidad asociada al motivo.
   * @param brand      Marca asociada al motivo.
   * @return ResponseEntity con estado NO_CONTENT si la baja es exitosa.
   * <ul>
   * <li>HTTP 204 No Content if the deletion is successful.</li>
   * <li>HTTP 404 Not Found if the specified reason code does not exist.</li>
   * <li>HTTP 500 Internal Server Error if an unexpected error occurs.</li>
   * </ul>   *
   */
  @DeleteReasonOverdraftDoc
  @DeleteMapping("/{reason_code}/entity/{entity}/brand/{brand}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public ResponseEntity<Void> deleteReasonOverdraft(
      @PathVariable(name = "reason_code") String reasonCode,
      @PathVariable(name = "entity") String entity,
      @PathVariable(name = "brand") String brand) {
    // Validación de parámetros
    Validations.doValidationsWithoutRequestBody(OperationType.DELETE, entity, brand, reasonCode);
    reasonOverdraftService.deleteReasonOverdraft(reasonCode, entity, brand);
    return ResponseEntity.noContent().build();
  }

}
