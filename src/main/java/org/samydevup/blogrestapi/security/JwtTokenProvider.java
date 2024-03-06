package org.samydevup.blogrestapi.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.samydevup.blogrestapi.exception.BlogAPIException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

/***
 * classe utilitaire mettant a disposition les methodes utilitaires suivantes :
 *  1- generateToken() ( à partir des app.jwt credentials dans le fichier application.properties) ->GENERER LE TOKEN POUR LES USER
 *  2- getUsername() : OBTENIR LE USERNAME DANS LE TOKEN DES REQUETTES CLIENTES
 *  3- validateToken() : VALIDER LE TOKEN JWT ENVOYE DANS LES REQUETTES CLIENTE
 *
 *  NB : CETTE CLASSE EST UTILISEE PAR LE FILTRE JwtAuthenticationFilter pour le filtre de toutes les requêtes entrantes
 */
@Component
public class JwtTokenProvider {
    @Value("${app.jwt-secret}")
    private String jwtSecret;
    @Value("${app.jwt-expiration-milliseconds}")
    private Long jwtExpirationDate;

    /**
     * Generation du token JWT à partir d'un objet authentication envoyée
     * par une requette cliente
     **/
    public String generateToken(Authentication authentication) {
        /**
         * 1-recup du username dans l'objet authentication
         * 2-création de la durée du token jwt
         * 3-génération du token en question à partir de la classe Jwts de io.jsonwebToken
         */
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);
        String token = Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(expireDate)
                .signWith(key())//attention prend un objet de type Key de java.security
                .compact();
        return token;
    }

    /** méthode utilitaire permettant de décoder
     * la clé sécrète utilisée dans le jwt
     * cette méthode est utilisée dans la méthode generateToken()
     */
    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    /***
     * méthode utilitaire permettant de recup
     * le username depuis un token envoyé
     * dans le header d'une requête
     */
    public String getUsername(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) key())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    /***
     * méthode utilitaire
     * permettant de valider un token
     * envoyé dans le header d'une requête
     * entrante : en comparant sa clé avec la clé du server
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser().
                    verifyWith((SecretKey) key())
                    .build()
                    .parse(token);
            return true;
        } catch (MalformedJwtException malformedJwtException) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Invalide JWT Token");
        } catch (ExpiredJwtException expiredJwtException) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Expired JWT Token");
        } catch (UnsupportedJwtException unsupportedJwtException) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Unsupported JWT token");
        } catch (IllegalArgumentException illegalArgumentException) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Jwt claims string is null or empty");
        }
    }


}
