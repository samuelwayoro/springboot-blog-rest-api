package org.samydevup.blogrestapi.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Schema( name = "PostDto", description = "payload utilisé pour échanger un objet de type Post entre le front et le back ")
@Data
public class PostDto {
    private Long id;
    //le titre devra obligatoirement être non null et avoir au moins 2 caractères
    @NotEmpty
    @Size(min = 2,message = "le titre du poste doit avoir au moins 2 caractères")
    @Schema(description = "titre du poste")
    private String title;
    //la description devra obligatoirement être non null et avoir au moins 10 caractères
    @NotNull
    @Size(min = 10,message = "la description doit avoir au moins 10 caractères")
    @Schema(description = "description du post")
    private String description;
    @Schema(description = "contenu du poste")
    //le titre doit être obligatoirement rempli
    @NotEmpty(message = "le contenu du post ne doit pas être vide")
    private String content;
    private Set<CommentDto> comments;

    private Long categoryId;
}
