package org.samydevup.blogrestapi.config;

import org.samydevup.blogrestapi.security.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * * CETTE CLASSE SERT A CONFIGURER L'AUTHENTIFICATION EN MEMOIRE DE SPRING SECURITY
 *
 * @Configuration : annotation au niveau de la classe indiquant
 * qu'il s'agit d'une classe de définitions de bean(à partir de méthode annotée @Bean).
 *
 * @EnableMethodSecurity : annotation au niveau de la classe indiquant l'activation des annotation springSecurity :
 * @PreAuthorize , @PostAuthorize , @Prefilter , @PostFilter
 *
 */
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userDetailsService){
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public static PasswordEncoder passwordEncoder() { //methode permettant l'encodage des password de user
        return new BCryptPasswordEncoder();
    }

    /**
     * Méthode securityFilterChain permet de mettre a disposition
     * un filtre en tant que bean dans le security context servant a activer une authentification
     * obligatoire de tous les users de nos endpoints .
     *
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        /**
         * Activation de la basic authentification
         * sur les endPoints de nos controllers
         */
        http.csrf((csrf) -> csrf.disable())
                .authorizeHttpRequests((authorize) ->
                        //authorize.anyRequest().authenticated()) //-->commenté car demande un authentification user sur ts les endpoints
                        authorize.requestMatchers(HttpMethod.GET, "/api/**").permitAll()//permet un accès total sur toutes les méthodes de type GET émises sur l'url commençant par /api/
                                .anyRequest().authenticated()//demande une authentification sur toutes les autres url des endpoints
                ).httpBasic(Customizer.withDefaults());
        return http.build();
    }


    /***
     * Méthode permettant de generer dans le context spring
     * un AUTHENTICATION MANAGER : qui servira a authentifier via spring security les utilisateurs
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    /***
     * Méthode permettant de stocker dans le context de l'application
     * les credentials des users ayant la permission d'utiliser nos endpoints,
     * en lieu et place de les stocker dans le fichier application.properties
     *
     * NB : Creer dans la méthode autant d'objet de type UserDetails et les stocker dans un Objet de type InMemoryUserDetailsManager
     *      qui est retourné en fin de méthode .
     *
     *
     * @return InMemoryUserDetailsManager
     */
  /*  @Bean
    public UserDetailsService userDetailsService() {
        UserDetails samuel = User.builder()
                .username("samuel")
                .password(passwordEncoder().encode("samuel"))
                .roles("USER")
                .build();

        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder().encode("admin"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(samuel, admin);
    }*/

}
