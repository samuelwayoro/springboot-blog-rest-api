package org.samydevup.blogrestapi.controller;

import org.samydevup.blogrestapi.payload.LoginDto;
import org.samydevup.blogrestapi.payload.RegisterDto;
import org.samydevup.blogrestapi.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller , Endpoint exposant le service de connexion , deconnexion utilisateur a l'application
 */
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    private AuthService authService;

    public AuthenticationController(AuthService authService) {
        this.authService = authService;
    }

    /***
     * méthode de connexion utilisateur
     * @param loginDto
     * @return
     */
    @PostMapping(value = {"/login", "/signin"})//-->méthode accessible a la fois à : /api/auth/login et /api/auth/signin
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto) {
        String response = authService.Login(loginDto);

        //---> pareille que return new ResponseEntity<>(response, HttpStatus.OK);
        return ResponseEntity.ok(response);
    }

    /**
     * méthode permettant d'inscrire/ajouter un nouveau user
     *
     * @return
     */
    @PostMapping(value = {"/register", "/signup"})
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto) {
        String response = authService.register(registerDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
