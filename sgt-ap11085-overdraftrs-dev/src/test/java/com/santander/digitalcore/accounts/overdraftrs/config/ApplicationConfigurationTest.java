package com.santander.digitalcore.accounts.overdraftrs.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cache.CacheManager;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ApplicationConfiguration.class)
class ApplicationConfigurationTest {
	
	@InjectMocks
	private ApplicationConfiguration applicationConfiguration;

	@Mock
	private DataSource mockDataSource;

	@Mock
	private JdbcTemplate mockJdbcTemplate;
	
	@Mock
	private CacheManager mockCacheManager;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void jdbcTemplateBeanIsConfiguredWithProvidedDataSource() {
		DataSource dataSource = applicationConfiguration.dataSource();
	    Assertions.assertNotNull(dataSource);
	    JdbcTemplate jdbcTemplate = applicationConfiguration.jdbcTemplate(dataSource);
	    Assertions.assertNotNull(jdbcTemplate);
	    Assertions.assertEquals(dataSource, jdbcTemplate.getDataSource());
	}
}
