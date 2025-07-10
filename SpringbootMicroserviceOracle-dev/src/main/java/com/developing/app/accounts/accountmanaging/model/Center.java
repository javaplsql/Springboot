package com.developing.app.accounts.accountmanaging.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Center
 */
@Data
@Schema(description = "Data structure containing information about a bank centre")
public class Center {

    /**
     * centerId
     */
	
	@Schema(description = "Bank centre ID", requiredMode = Schema.RequiredMode.REQUIRED, minLength = 4, maxLength = 4, example = "1001")
    private String centerId;
    //sonar
    //sonar
    //sonar
    //sonar
    //sonar
    //sonar

}
