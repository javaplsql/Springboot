package com.santander.digitalcore.accounts.accmanagement.model.genaccidentifiers.response;

import com.santander.digitalcore.accounts.accmanagement.model.AccounIdentification;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * GenerateAccountIdentifiersResponse
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Schema(description = "Data structure containing information about an account")
public class GenerateAccountIdentifiersPostResponse {

	/**
	 * accountIdentification
	 */
	@Schema( requiredMode = Schema.RequiredMode.REQUIRED)
	private AccounIdentification accounIdentification;



}
