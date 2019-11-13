package com.cross_ni.training.hibernateintroduction.common;

import com.cross_ni.training.hibernateintroduction.model.Publisher;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
public class JPAContexConfiguration {

	@Bean
	public DataSource dataSource() {
		final DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("org.hsqldb.jdbcDriver");
		dataSource.setUrl("jdbc:hsqldb:mem:" + RandomStringUtils.randomAlphabetic(5));
		dataSource.setUsername("SA");
		dataSource.setPassword("");
		return dataSource;
	}
	
	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(dataSource());
		em.setPackagesToScan(packagesToScan());
		em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		em.setJpaPropertyMap(hibernateProperties());
		return em;
	}

	public String[] packagesToScan() {
		return new String[] { Publisher.class.getPackage().getName() };
	}

	public Map<String, Object> hibernateProperties() {
		Map<String, Object> hibernateProperties = new HashMap<>();
		hibernateProperties.put("hibernate.hbm2ddl.auto", "update");
		hibernateProperties.put("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
		return hibernateProperties;
	}

	@Bean
	public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
		return new JpaTransactionManager(emf);
	}
	
	@Bean
	public DBService dbService() {
		return new DBService();
	}

}