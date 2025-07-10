package com.developing.app.accounts.accountmanaging.model.genaccidentifiers.request;

import com.java.developing.accounts.accountmanaging.model.Center;
import com.java.developing.accounts.accountmanaging.model.Product;
import com.java.developing.accounts.util.lib.core.exceptions.BadRequestplsqlExceptionproject;
import com.java.developing.accounts.util.lib.core.validation.AbstractprojectRequestValidator;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * GenerateAccountsIdentifiersRequest
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Data structure containing information about an account")
public class GenerateAccountIdentifiersPostRequest extends AbstractprojectRequestValidator {

	/**
	 * center
	 */
	@Schema(requiredMode = Schema.RequiredMode.REQUIRED)
	private Center center;

	/**
	 * product
	 */
	@Schema( requiredMode = Schema.RequiredMode.REQUIRED)
	private Product product;

	@Override
	public void validate() {
		validate(this);
	}

	public void validate(GenerateAccountIdentifiersPostRequest request) {

		if (request.getCenter() == null) {
			throw new BadRequestplsqlExceptionproject("CONTRACT-MANAGEMENT-F-0005");
		}
		validateCenter(request.getCenter().getCenterId());

		if (request.getProduct() == null) {
			throw new BadRequestplsqlExceptionproject("CONTRACT-MANAGEMENT-F-0004");
		}
		validateProduct(request.getProduct().getProductCode());

	}

	private void validateCenter(String centerId) {
		if (centerId == null || centerId.isEmpty()) {
			throw new BadRequestplsqlExceptionproject("CONTRACT-MANAGEMENT-F-0006");
		}

		if (centerId.length() > 4 || !centerId.matches("\\d+")) {
			throw new BadRequestplsqlExceptionproject("CONTRACT-MANAGEMENT-F-0007");
		}
	}

	private void validateProduct(String productCode) {
		if (productCode == null || productCode.isEmpty()) {
			throw new BadRequestplsqlExceptionproject("CONTRACT-MANAGEMENT-F-0001");
		}

		if (productCode.length() > 10 || !productCode.matches("\\d+")) {
			throw new BadRequestplsqlExceptionproject("CONTRACT-MANAGEMENT-F-0002");
		}
	}
//sonar
//sonar
//sonar
//sonar
}
