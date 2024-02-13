package org.samydevup.blogrestapi.exception;

import org.samydevup.blogrestapi.payload.ErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

/***
 * Cette class est importante grâce a deux aspects :
 *  1- son annotation @ControllerAdvice : qui lui permet d'être disponible dans le context de Spring en tant que Bean
 *  2- ses méthodes qui permettent de lancer gerer (capturer et lancer vers le UI) toutes les exceptions que l'on pourrais
 *     rencontrer dans l'utilisation des endpoints : les exceptions spécifiques et les exceptions globales
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * GESTION DES EXCEPTIONS SPECIFIQUES
     */

    //Gestion de ResourceNotFoundException : lancer lorsque un User demande une info n'existant pas en bd
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleResourceNotFoundException(ResourceNotFoundException exception, WebRequest webRequest) {
        //webRequest.getDescription à false afin de ne pas renvoyer au client tous les détails d'erreur dans l'exception
        ErrorDetails errorDetails = new ErrorDetails(new Date(), exception.getMessage(), webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    //Gestion de BlogAPIException
    @ExceptionHandler(BlogAPIException.class)
    public ResponseEntity<ErrorDetails> handleBlogAPIException(BlogAPIException blogAPIException, WebRequest webRequest) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), blogAPIException.getMessage(), webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    /**
     * GESTION DES EXCEPTIONS GLOBALES
     */

}
