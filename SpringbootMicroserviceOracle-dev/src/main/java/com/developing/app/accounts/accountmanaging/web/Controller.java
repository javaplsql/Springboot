package com.developing.app.accounts.accountmanaging.web;

import com.java.developing.accounts.util.lib.core.exceptions.BadRequestplsqlExceptionproject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.java.plsql.core.exceptions.HttpBaseplsqlException;
import com.java.developing.accounts.accountmanaging.model.genaccidentifiers.request.GenerateAccountIdentifiersPostRequest;
import com.java.developing.accounts.accountmanaging.model.genaccidentifiers.response.GenerateAccountIdentifiersPostResponse;
import com.java.developing.accounts.accountmanaging.service.accountmanagingService;
import com.java.developing.accounts.accountmanaging.web.info.GenerateAccountsIdentifiersPostDoc;
import com.java.developing.accounts.util.lib.core.exceptions.InternalServerErrorplsqlExceptionproject;
import com.java.developing.accounts.util.lib.core.funtionallog.FuntionalLogUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * Example controller for plsql web applications
 *
 * @author java Technology
 */
@RestController
@RequestMapping(path = "v5/accounts", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class Controller {

  private final FuntionalLogUtil funtionalLogUtil;
  private final accountmanagingService accountmanagingService;

  /**
   * Controller
   *
   * @param funtionalLogUtil     funtionalLogUtil
   * @param accountmanagingService generateAccountIdentifiersService
   */
  public Controller(FuntionalLogUtil funtionalLogUtil, accountmanagingService accountmanagingService) {
    this.funtionalLogUtil = funtionalLogUtil;
    this.accountmanagingService = accountmanagingService;
  }

  /**
   * for sonar vulnerability
   *
   * @param binder binder
   */
  @InitBinder
  public void initBinder(WebDataBinder binder) {

    binder.setDisallowedFields("");
  }

  /**
   * Method to control the "/generate_account_identifiers" endpoint that, when
   * receives a request, will return an Account Identifiers usin POST.
   * This method is used to generate account identifiers based on the
   * request provided.
   *
   * @param request the request containing the necessary data to generate account identifiers
   * @return response
   */

  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  @GenerateAccountsIdentifiersPostDoc
  @PostMapping(path = "generate_account_identifiers")
  public GenerateAccountIdentifiersPostResponse generateAccountsIdentifiersPost(
      @RequestBody GenerateAccountIdentifiersPostRequest request,
      @RequestHeader(value = "entityHeader") String entityHeader,
      @RequestHeader(value = "brandHeader") String brandHeader) {

    try {
      // Validar que los headers no sean nulos ni vacíos
      validateHeaders(entityHeader, brandHeader);

      // Validamos la entrada
      request.validate();

      // Inicializamos el log
      funtionalLogUtil.setRequest(request);

      var preAssigmentContractIndicator = "Y";
      // llamamos al servicio para recuperar el identificador interno
      GenerateAccountIdentifiersPostResponse response = accountmanagingService.generateAccountIdentifiers(request, entityHeader, brandHeader, preAssigmentContractIndicator);

      // dejamos traza de salida
      funtionalLogUtil.logTraceOK();

      return response;
    } catch (HttpBaseplsqlException e) {
      funtionalLogUtil.logTraceFuntionalError(e);
      throw e;
    } catch (Exception e) {
      funtionalLogUtil.logTraceInternalError(e);
      throw new InternalServerErrorplsqlExceptionproject("DC-COMMON-T-9999", "", e);
    }

  }

  private void validateHeaders(String entityHeader, String brandHeader) {
    if (entityHeader == null || entityHeader.isEmpty()) {
      throw new BadRequestplsqlExceptionproject("CONTRACT-MANAGEMENT-F-0009");
    }

    if (entityHeader.length() != 4) {
      throw new BadRequestplsqlExceptionproject("CONTRACT-MANAGEMENT-F-0012");
    }

    if (brandHeader == null || brandHeader.isEmpty()) {
      throw new BadRequestplsqlExceptionproject("CONTRACT-MANAGEMENT-F-0011");
    }
  }

}
