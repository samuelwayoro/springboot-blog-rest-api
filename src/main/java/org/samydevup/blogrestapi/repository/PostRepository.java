package org.samydevup.blogrestapi.repository;

import org.samydevup.blogrestapi.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {

    //retourner la liste de postes d'une catégorie donnée
    List<Post> findByCategoryId(Long categoryId);
}
