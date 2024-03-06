package org.samydevup.blogrestapi.config;

import org.samydevup.blogrestapi.security.JwtAuthenticationEntryPoint;
import org.samydevup.blogrestapi.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * * * CETTE CLASSE SERT A CONFIGURER L'AUTHENTIFICATION EN MEMOIRE OU EN BASE DE DONNEES DE SPRING SECURITY
 *
 * @Configuration : Annotation au niveau de la classe indiquant
 * qu'il s'agit d'une classe de définitions de bean(à partir de méthode annotée @Bean).
 * @EnableMethodSecurity : Annotation au niveau de la classe indiquant l'activation des annotation SpringSecurity :
 * @PreAuthorize , @PostAuthorize , @Prefilter , @PostFilter
 */
@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    private UserDetailsService userDetailsService;
    private JwtAuthenticationEntryPoint authenticationEntryPoint;
    private JwtAuthenticationFilter authenticationFilter;

    public SecurityConfig(UserDetailsService userDetailsService,
                          JwtAuthenticationEntryPoint authenticationEntryPoint,
                          JwtAuthenticationFilter authenticationFilter) {
        this.userDetailsService = userDetailsService;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.authenticationFilter = authenticationFilter;

    }

    @Bean
    public static PasswordEncoder passwordEncoder() { //methode permettant l'encodage des password de user
        return new BCryptPasswordEncoder();
    }

    /**
     * Méthode securityFilterChain permettant de mettre a disposition
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

         http.csrf((csrf) -> csrf.disable())//securise contre les attaques de type csrf
         .authorizeHttpRequests((authorize) ->
         //authorize.anyRequest().authenticated()) //-->commenté car demande une authentification user sur ts les endpoints
         authorize.requestMatchers(HttpMethod.GET, "/api/**").permitAll()//permet un accès total sur toutes les méthodes de type GET émises sur l'url commençant par /api/
         .requestMatchers("/api/auth/**").permitAll()
         .anyRequest().authenticated()//demande une authentification sur toutes les autres url des endpoints
         ).httpBasic(Customizer.withDefaults());
         */
        http.csrf((csrf) -> csrf.disable())//securise contre les attaques de type csrf
                .authorizeHttpRequests((authorize) ->
                        //authorize.anyRequest().authenticated()) //-->commenté car demande une authentification user sur ts les endpoints
                        authorize.requestMatchers(HttpMethod.GET, "/api/**").permitAll()//permet un accès total sur toutes les méthodes de type GET émises sur l'url commençant par /api/
                                .requestMatchers("/api/auth/**").permitAll()
                                .anyRequest().authenticated()//demande une authentification sur toutes les autres url des endpoints
                ).exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authenticationEntryPoint)
                ).sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        //rajouter le filtre d'authentification de jwt avant le filtre de springSecurity UsernamePasswordAuthenticationFilter
        http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    /***
     * Méthode permettant de génerer dans le context spring (pour être injectable dans toutes les autres classes du projet)
     * un objet de type AuthenticationManager : qui servira a authentifier via spring security les utilisateurs de nos endpoints
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
