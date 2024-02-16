package org.samydevup.blogrestapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/***
 * L'annotation @Configuration
 * est une annotation au niveau de la classe indiquant
 * qu'il s'agit d'une classe de définitions de bean(à partir de méthode annotée @Bean).
 */
@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        /**
         * Activation de la basic authentification
         * sur les endPoints de nos controllers
         */
        http.csrf((csrf) -> csrf.disable())
                .authorizeHttpRequests((authorize) -> authorize.anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }

}
