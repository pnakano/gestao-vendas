package com.gvendas.gestaovendas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

//Informa onde estão as entidades (se estiver no package principal se acha sozinho)
@EntityScan(basePackages = {"com.gvendas.gestaovendas.entity"})
//Informa onde estão os repository
@EnableJpaRepositories(basePackages = {"com.gvendas.gestaovendas.repository"})
//Informa onde estão os services e controllers e etc
@ComponentScan(basePackages = {"com.gvendas.gestaovendas.service", "com.gvendas.gestaovendas.controller", "com.gvendas.gestaovendas.exception"})
@SpringBootApplication
public class GestaoVendasApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestaoVendasApplication.class, args);
	}

}
