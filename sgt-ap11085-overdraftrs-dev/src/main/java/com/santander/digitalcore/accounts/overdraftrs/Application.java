package com.santander.digitalcore.accounts.overdraftrs;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * The main class of the Spring applications.
 *
 * @author Santander Technology
 */
@SpringBootApplication
public class Application {

	/**
	 * Main method of the application
	 * where the application entry-point is.
	 * Depending on the kind of application,
	 * the context of Spring can be configured
	 * to support not web, reactive web, and
	 * servlet web applications.
	 *
	 * This application is defined as Servlet
	 * web application (WebApplicationType.SERVLET)
	 *
	 * @param args input arguments
	 */
	public static void main(String[] args) {


		new SpringApplicationBuilder(Application.class)
			.web(WebApplicationType.SERVLET)
			.run(args);
	}

}
