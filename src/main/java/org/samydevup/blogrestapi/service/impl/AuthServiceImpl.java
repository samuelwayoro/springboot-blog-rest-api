package org.samydevup.blogrestapi.service.impl;

import org.samydevup.blogrestapi.entity.Role;
import org.samydevup.blogrestapi.entity.User;
import org.samydevup.blogrestapi.exception.BlogAPIException;
import org.samydevup.blogrestapi.payload.LoginDto;
import org.samydevup.blogrestapi.payload.RegisterDto;
import org.samydevup.blogrestapi.repository.RoleRepository;
import org.samydevup.blogrestapi.repository.UserRepository;
import org.samydevup.blogrestapi.security.JwtTokenProvider;
import org.samydevup.blogrestapi.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {

    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private JwtTokenProvider jwtTokenProvider;

    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /***
     * méthode de login (connexion) utilisateurs
     * @param loginDto
     * @return
     */
    @Override
    public String Login(LoginDto loginDto) {
        /**
         * Authentification de l'utilisateur demandant a se
         * connecter a partir de la proprieté : AuthenticationManager
         * nb : si l'authentification est ok on save le user dans le spring context
         *      sinon une exception interne a Authentication est lancée
         */
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsernameOrEmail(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.generateToken(authentication);
        /***
         * on retourne pour finir
         * un String message de connexion ok
         *  return "User connecté!!";
         */

        //on retourne a présent le token utilisateur en lieu et place d'une chaine de caractere
        return token;
    }

    @Override
    public String register(RegisterDto registerDto) {
        //verifier si le user existe deja en base de données : soit par son username ou son email
        if (userRepository.existsByUsername(registerDto.getUsername())) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Username déjà utilisé");
        }
        if (userRepository.existsByEmail(registerDto.getEmail())) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Email déjà utilisé");
        }
        //sinon on l'ajoute (inscrit) en base de données
        User user = new User();
        user.setUsername(registerDto.getUsername());
        user.setName(registerDto.getName());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));//tjrs encoder le password d'un nouveau user

        Set<Role> roles = new HashSet<>();
        Role role = roleRepository.findByName("ROLE_USER").get();
        roles.add(role);
        user.setRoles(roles);
        userRepository.save(user);
        return "Utilisateur bien inscrit!!";
    }
}
