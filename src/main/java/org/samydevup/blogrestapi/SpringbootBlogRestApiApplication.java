package org.samydevup.blogrestapi;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "Spring boot Blog App Rest APIs",
                description = "Spring boot Blog App APIs Documentation",
                version = "v1.0",
                contact = @Contact(
                        name = "Ramesh",
                        email = "javaguides.net@gmail.com",
                        url = "https://www.javaguides.net"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.javaguides.net/licences"
                )
        ),
        externalDocs = @ExternalDocumentation(
                description = "Spring Boot Blog App Documentation",
                url = "https://github.com/RameshMF/springboot-blog-rest-api"
        )
)
public class SpringbootBlogRestApiApplication {

    @Bean
    public ModelMapper modelMapper() {
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
