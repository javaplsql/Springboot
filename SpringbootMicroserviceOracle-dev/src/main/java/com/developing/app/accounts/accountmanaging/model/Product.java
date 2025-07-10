package com.developing.app.accounts.accountmanaging.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Product
 */
@Data
@Schema(description = "Data structure containing product information. Products can be services or goods.")
public class Product {

  /**
   * productCode
   */
	@Schema(description = "Product code", requiredMode = Schema.RequiredMode.REQUIRED, example = "0049300130")
  private String productCode;

  //sonar
  //sonar
  //sonar
  //sonar
  //sonar
  //sonar

}
