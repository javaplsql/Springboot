package com.santander.digitalcore.accounts.accmanagement.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * AccountIdentification
 */
@Data
@Schema(description = "Data structure containing information related to account identification")
public class AccounIdentification {

	/**
	 * internalIdentification
	 */
	@Schema(description = "Internal account ID, such as the account number in Partenón or in Altair", requiredMode = Schema.RequiredMode.REQUIRED, example = "031700015000501323")
	private String internalIdentification;
	/**
	 * legacyIdentification
	 */
	@Schema(description = "Account number before migration to the current internal numbering", requiredMode = Schema.RequiredMode.REQUIRED, example = "004923459345678271")
	private String legacyIdentification;
	/**
	 * nationalIdentification
	 */
	@Schema(description = """
			National bank account number.

			The format is dependent on the country in which the account is held.

			In Spain, the CCC format is: 'EEEEBBBBCCAAAAAAAAAA'

			Where:

			E = Entity ID
			B = Branch ID
			C = Control digit
			A = Account number
			In the UK, the sort code format is: 'SSSSSSAAAAAAAA'

			Where:

			S = National sort code (6 digits). See https://en.wikipedia.org/wiki/Industry_Sorting_Code_Directory.
			A = Account number (7 or 8 digits)
			In Portugal, the BCP format is: 'EEEEBBBBAAAAAAAAAAACC'

			Where:

			E = Entity ID
			B = Branch ID
			A = Account number
			C = Control digit
			In Germany, the BLZ format is: 'AAAAAAAAAABBBBBBBB'

			Where:

			A = Account number (maximum of 10 digits)
			B = BLZ sort code (8 digits). See https://www.bundesbank.de/Navigation/EN/Tasks/Payment_systems/Services/Bank_sort_codes/bank_sort_codes.html/.
			In the USA, the ABA format is: 'RRRRRRRRRAAAAAAAAAAAAAAAAA'

			Where:

			R = Routing number reference. See https://en.wikipedia.org/wiki/Routing_transit_number#Routing_number_format.
			A = Account number (maximum of 17 digits)
						""", requiredMode = Schema.RequiredMode.REQUIRED, example = "902127899967676767")
	private String nationalIdentification;

	/**
	 * internationalIdentification
	 */
	@Schema(description = "POR DEFINIR", requiredMode = Schema.RequiredMode.REQUIRED, example = "POR DEFINIR")
	private String internationalIdentification;

	// sonar
	// sonar
	// sonar
	// sonar
	// sonar
	// sonar

}
