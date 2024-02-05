package org.samydevup.blogrestapi;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringbootBlogRestApiApplication {

	@Bean
	public ModelMapper modelMapper(){
		/***
		 * Ne jamais oublier d'enlever la methode toString() dans les entités
		 * pour ne pas avoir de dépendance cyclique (oneToMany ou ManyToMany).
		 * Quand l'on utilise lombock : enlever l'annotation @Data
		 */
		return new ModelMapper();
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringbootBlogRestApiApplication.class, args);
	}

}
