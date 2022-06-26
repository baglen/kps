package com.kps.backend;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class KpsBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(KpsBackendApplication.class, args);
	}

	@Bean
	ModelMapper getMapper(){
		return new ModelMapper();
	}
}
