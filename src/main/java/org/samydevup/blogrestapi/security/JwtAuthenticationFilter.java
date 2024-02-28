package org.samydevup.blogrestapi.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/***
 * FILTRE PERMETTANT D'EXECUTER LA METHODE doFilterInternal()
 * à chaque requête cliente entrante
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private JwtTokenProvider jwtTokenProvider;
    private UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, UserDetailsService customUserDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = customUserDetailsService;
    }

    /***
     * METHODE PERMETTANT
     * LA VERIFICATION DU TOKEN DE LA REQUETTE ENTRANTE
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        //recup le token de la requêtte entrante après traitement avec la méthode getTokenFromRequest()
        String token = getTokenFromRequest(request);

        //validation du token : d'adbord verifie si il non null ou vide ensuite valide le token avec jwtTokenProvider.validate()
        if(StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)){

            //recup le username dans le token
            String username = jwtTokenProvider.getUsername(token);

            //charger le user associé au token
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            //créons un objet de type : UsernamePasswordAuthenticationToken depuis spring security authentication
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );

            //ajoutons l'objet request a l'objet authenticationToken
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            //stocker l'objet authenticationToken dans le context Spring
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        //passer au filtre les objets request et response
        filterChain.doFilter(request,response);

    }

    private String getTokenFromRequest(HttpServletRequest request){
        //recup de le contenu de l'autorisation dans le header de la requette
        String bearerToken = request.getHeader("Authorization");

        //verifie si ce contenu n'est pas vide (avec la classe utilitaire de SpringFramework StringUtils.hasText()
        //et verifie si il commence à partir du text Bearer
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")){

            //retourne le contenu hormis le text Bearer(grâce a la méthode substring())
            return bearerToken.substring(7,bearerToken.length());
        }
        return null;
    }
}
