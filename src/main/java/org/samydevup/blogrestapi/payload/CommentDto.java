package org.samydevup.blogrestapi.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentDto {
    private long id;
    @NotEmpty(message = "nom du commentateur non renseigné")
    private String name;
    @NotEmpty
    @Email(message = "adresse email non conforme")
    private String email;
    @NotEmpty
    @Size(min = 10,message = "le commentaire doit contenir au moins 10 caractères ")
    private String body;
}
