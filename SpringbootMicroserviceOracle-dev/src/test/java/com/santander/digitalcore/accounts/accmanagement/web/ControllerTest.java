package com.santander.digitalcore.accounts.accmanagement.web;

import com.santander.darwin.core.exceptions.GenericDarwinException;
import com.santander.digitalcore.accounts.accmanagement.model.Center;
import com.santander.digitalcore.accounts.accmanagement.model.Product;
import com.santander.digitalcore.accounts.accmanagement.model.genaccidentifiers.request.GenerateAccountIdentifiersPostRequest;
import com.santander.digitalcore.accounts.accmanagement.model.genaccidentifiers.response.GenerateAccountIdentifiersPostResponse;
import com.santander.digitalcore.accounts.accmanagement.service.AccManagementService;
import com.santander.digitalcore.accounts.util.lib.core.exceptions.BadRequestDarwinExceptionLeancore;
import com.santander.digitalcore.accounts.util.lib.core.exceptions.InternalServerErrorDarwinExceptionLeancore;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.santander.digitalcore.accounts.util.lib.core.funtionallog.FuntionalLogUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.bind.WebDataBinder;

class ControllerTest {

  @Mock
  private FuntionalLogUtil funtionalLogUtil;

  @Mock
  private AccManagementService accManagementService;

  @InjectMocks
  private Controller controller;
  // Mock the dependencies

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testInitBinderSetsDisallowedFields() {
    // Arrange
    WebDataBinder binder = Mockito.mock(WebDataBinder.class);

    // Act
    controller.initBinder(binder);

    // Assert
    verify(binder).setDisallowedFields(""); // Verifica que se llamó con el argumento esperado
  }

  //
  @Test
  void generateAccountIdentifiers_SuccessfulExecution_ReturnsResponse() throws Exception {

    Center center = new Center();
    center.setCenterId("1001");
    Product product = new Product();
    product.setProductCode("0049300130");

    // Arrange
    GenerateAccountIdentifiersPostRequest request = new GenerateAccountIdentifiersPostRequest();
    request.setCenter(center);
    request.setProduct(product);

    GenerateAccountIdentifiersPostResponse expectedResponse = new GenerateAccountIdentifiersPostResponse();

    String entityHeader = "0049";
    String brandHeader = "brand-header-value";
    String preAssigmentContractIndicator = "Y";
    when(accManagementService.generateAccountIdentifiers(request, entityHeader, brandHeader, preAssigmentContractIndicator)).thenReturn(expectedResponse);

    // Act
    GenerateAccountIdentifiersPostResponse actualResponse = controller.generateAccountsIdentifiersPost(request, entityHeader, brandHeader);

    // Assert
    Assertions.assertEquals(expectedResponse, actualResponse);
    verify(funtionalLogUtil).setRequest(request);
    verify(funtionalLogUtil).logTraceOK();
  }

  //
  @Test
  void generateAccountsIdentifiersPostReturnsResponseWhenValidRequest() throws Exception {
    GenerateAccountIdentifiersPostRequest request = new GenerateAccountIdentifiersPostRequest();
    request.setCenter(new Center());
    request.getCenter().setCenterId("1001");
    request.setProduct(new Product());
    request.getProduct().setProductCode("0049300130");
    String entityHeader = "0049";
    String brandHeader = "brand-header-value";
    String preAssigmentContractIndicator = "Y";
    when(accManagementService.generateAccountIdentifiers(request, entityHeader, brandHeader, preAssigmentContractIndicator)).thenReturn(new GenerateAccountIdentifiersPostResponse());

    Assertions.assertNotNull(request);

  }

  @Test
  void generateAccountsIdentifiersPostThrowsBadRequestWhenValidationFails() throws Exception {
    GenerateAccountIdentifiersPostRequest request = mock(GenerateAccountIdentifiersPostRequest.class);
    when(request.getProduct()).thenReturn(mock(Product.class));
    when(request.getCenter()).thenReturn(mock(Center.class));

    String entityHeader = "0049";
    String brandHeader = "brand-header-value";
    String preAssigmentContractIndicator = "Y";
    when(accManagementService.generateAccountIdentifiers(request, entityHeader, brandHeader, preAssigmentContractIndicator))
        .thenThrow(new BadRequestDarwinExceptionLeancore("ACCMANAGEMENT-0001"));

    assertThrows(BadRequestDarwinExceptionLeancore.class, () -> controller.generateAccountsIdentifiersPost(request, entityHeader, brandHeader));
  }

  @Test
  void generateAccountsIdentifiersPostThrowsInternalServerErrorOnUnexpectedException() throws Exception {
    GenerateAccountIdentifiersPostRequest request = new GenerateAccountIdentifiersPostRequest();
    request.setCenter(new Center());
    request.getCenter().setCenterId("1001");
    request.setProduct(new Product());
    request.getProduct().setProductCode("0049300130");

    String entityHeader = "0049";
    String brandHeader = "brand-header-value";
    String preAssigmentContractIndicator = "Y";
    when(accManagementService.generateAccountIdentifiers(request, entityHeader, brandHeader, preAssigmentContractIndicator)).thenThrow(new RuntimeException("Unexpected error"));

    assertThrows(InternalServerErrorDarwinExceptionLeancore.class, () -> controller.generateAccountsIdentifiersPost(request, entityHeader, brandHeader));
  }


  @Test
  void testGenAccIdentifiers_SuccessfulExecution_ReturnsResponse() throws Exception {

    Center center = new Center();
    center.setCenterId("1001");
    Product product = new Product();
    product.setProductCode("0049300130");
    // Arrange
    GenerateAccountIdentifiersPostRequest request = new GenerateAccountIdentifiersPostRequest();
    request.setCenter(center);
    request.setProduct(product);

    String entityHeader = "0049";
    String brandHeader = "brand-header-value";

    when(accManagementService.generateAccountIdentifiers(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
        .thenThrow(new GenericDarwinException("XX1111", 500, "Internal Server Error", "Something is horribly wrong."));

    assertThrows(GenericDarwinException.class, () -> controller.generateAccountsIdentifiersPost(request, entityHeader, brandHeader));
  }

  @Test
  void controllerConstructorInitializesDependenciesCorrectly() {
    FuntionalLogUtil mockFuntionalLogUtil = mock(FuntionalLogUtil.class);
    AccManagementService mockAccManagementService = mock(AccManagementService.class);

    Controller controllerG = new Controller(mockFuntionalLogUtil, mockAccManagementService);

    Assertions.assertNotNull(controllerG);
  }
//
@Test
void testGenerateAccountsIdentifiers_ProductCodeNull() throws Exception {

  Center center = new Center();
  center.setCenterId("1001");
  Product product = new Product();
  product.setProductCode(null);
  // Arrange
  GenerateAccountIdentifiersPostRequest request = new GenerateAccountIdentifiersPostRequest();
  request.setCenter(center);
  request.setProduct(product);

  GenerateAccountIdentifiersPostResponse expectedResponse = new GenerateAccountIdentifiersPostResponse();

  String entityHeader = "0049";
  String brandHeader = "brand-header-value";
  String preAssigmentContractIndicator = "Y";
  when(accManagementService.generateAccountIdentifiers(request, entityHeader, brandHeader, preAssigmentContractIndicator)).thenReturn(expectedResponse);

  BadRequestDarwinExceptionLeancore exception = assertThrows(BadRequestDarwinExceptionLeancore.class, () ->
      controller.generateAccountsIdentifiersPost(request, entityHeader, brandHeader));

  Assertions.assertEquals("CONTRACT-MANAGEMENT-F-0001", exception.getErrorName());

}

  @Test
  void testGenerateAccountsIdentifiers_ProductCodeIsEmpty() throws Exception {

    Center center = new Center();
    center.setCenterId("1001");
    Product product = new Product();
    product.setProductCode("");
    // Arrange
    GenerateAccountIdentifiersPostRequest request = new GenerateAccountIdentifiersPostRequest();
    request.setCenter(center);
    request.setProduct(product);

    GenerateAccountIdentifiersPostResponse expectedResponse = new GenerateAccountIdentifiersPostResponse();

    String entityHeader = "0049";
    String brandHeader = "brand-header-value";
    String preAssigmentContractIndicator = "Y";
    when(accManagementService.generateAccountIdentifiers(request, entityHeader, brandHeader, preAssigmentContractIndicator)).thenReturn(expectedResponse);

    BadRequestDarwinExceptionLeancore exception = assertThrows(BadRequestDarwinExceptionLeancore.class, () ->
        controller.generateAccountsIdentifiersPost(request, entityHeader, brandHeader));

    Assertions.assertEquals("CONTRACT-MANAGEMENT-F-0001", exception.getErrorName());

  }
  @Test
  void generateAccountIdentifiers_WithoutCenterIdSuccessfulExecution() throws Exception {
    Center center = new Center();
    center.setCenterId("1001");
    Product product = new Product();
    product.setProductCode("0049300130");

    // Arrange
    GenerateAccountIdentifiersPostRequest request = new GenerateAccountIdentifiersPostRequest();
    request.setProduct(product);
    request.setCenter(center);

    GenerateAccountIdentifiersPostResponse expectedResponse = new GenerateAccountIdentifiersPostResponse();

    String entityHeader = "0049";
    String brandHeader = "brand-header-value";
    String preAssigmentContractIndicator = "Y";
    when(accManagementService.generateAccountIdentifiers(request, entityHeader, brandHeader, preAssigmentContractIndicator)).thenReturn(expectedResponse);

    // Act
    GenerateAccountIdentifiersPostResponse actualResponse = controller.generateAccountsIdentifiersPost(request, entityHeader, brandHeader);

    // Assert
    Assertions.assertEquals(expectedResponse, actualResponse);
    verify(funtionalLogUtil).setRequest(request);
    verify(funtionalLogUtil).logTraceOK();
  }
  @Test
  void generateAccountIdentifiers_ProductCodeWrongFormat() throws Exception {
    Center center = new Center();
    center.setCenterId("1001");
    Product product = new Product();
    product.setProductCode("0049ABCDEF");
    // Arrange
    GenerateAccountIdentifiersPostRequest request = new GenerateAccountIdentifiersPostRequest();
    request.setCenter(center);
    request.setProduct(product);

    String entityHeader = "0049";
    String brandHeader = "brand-header-value";
    String preAssigmentContractIndicator = "Y";
    when(accManagementService.generateAccountIdentifiers(request, entityHeader, brandHeader, preAssigmentContractIndicator))
        .thenThrow(new BadRequestDarwinExceptionLeancore("ACCMANAGEMENT-0002"));

    BadRequestDarwinExceptionLeancore exception = assertThrows(BadRequestDarwinExceptionLeancore.class, () ->
        controller.generateAccountsIdentifiersPost(request, entityHeader, brandHeader));

    Assertions.assertEquals("CONTRACT-MANAGEMENT-F-0002", exception.getErrorName());

  }
}
