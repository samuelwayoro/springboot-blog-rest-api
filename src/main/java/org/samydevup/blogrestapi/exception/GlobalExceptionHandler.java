package org.samydevup.blogrestapi.exception;

import org.samydevup.blogrestapi.payload.ErrorDetails;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/***
 * Cette class est importante pour deux grandes raisons :
 *  1- son annotation @ControllerAdvice : qui lui permet d'être disponible dans le context de Spring en tant que Bean
 *  2- ses méthodes qui permettent de gerer (capturer et lancer vers le client) toutes les exceptions que l'on pourrais
 *     rencontrer dans l'utilisation des endpoints : les exceptions spécifiques et les exceptions globales
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * GESTION DES EXCEPTIONS SPECIFIQUES
     */

    //Gestion de ResourceNotFoundException : automatiquement lancé lorsqu'on obtient cette exeption dans le code métier
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
     * SECOND APPROCHE DE GESTION DES ERREURS LIEES AUX MAUVAIS PARAMETRES DTO
     *
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodeArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest webRequest) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField(); //recup du champ surlequel on a l'exception
            String message = error.getDefaultMessage(); //recup du message d'erreur
            errors.put(fieldName,message);
        });
        return  new ResponseEntity<>(errors,HttpStatus.BAD_REQUEST);
    }
    **/

    /**
     * GESTION DES EXCEPTIONS GLOBALES : tout autre type d'exception que l'on pourrais obtenir
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> globalException(Exception exception, WebRequest webRequest) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), exception.getMessage(), webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /***PREMIERE APPROCHE  :
     *
     * METHODE PERMETTANT DE GERER LES EXCEPTIONS LEVEES LORS DE LA RECEPTION DE PARAMETRES DTO NON VALIDES
     * PAR LE CLIENT
     * @param ex
     * @param headers
     * @param status
     * @param request
     * @return
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        /***
         * recuperation dans une map ,
         * des différentes exceptions et leur messages liées au non respect des contraintes des proprietes des classes DTO
         */
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField(); //recup du champ surlequel on a l'exception
            String message = error.getDefaultMessage(); //recup du message d'erreur
            errors.put(fieldName,message);
        });
        return  new ResponseEntity<>(errors,HttpStatus.BAD_REQUEST);
    }
}
