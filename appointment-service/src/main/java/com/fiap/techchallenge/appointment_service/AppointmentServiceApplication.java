package com.fiap.techchallenge.appointment_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class })
@EnableFeignClients
public class AppointmentServiceApplication {

	public static void main(String[] args) {
		// The application uses Spring MVC (servlet) stack. Don't force reactive type
		// which
		// causes Spring to look for a ReactiveWebServerFactory and fail to start.
		System.setProperty("spring.main.web-application-type", "servlet");
		SpringApplication.run(AppointmentServiceApplication.class, args);
	}

}
