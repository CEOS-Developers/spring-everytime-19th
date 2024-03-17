package com.ceos19.springeverytime.repository;

import com.ceos19.springeverytime.domain.Image;
import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ImageRepository {
    private final EntityManager em;

    public void save(Image image){
        em.persist(image);
    }

    public Image findOne(Long id){
        return em.find(Image.class,id);
    }

    public List<Image> findAll(){
        return em.createQuery("select i "
                + "from Image i ",Image.class)
                .getResultList();
    }

    public List<Image> findByPost(Long id){
        return em.createQuery("select i "
                + "from Image i "
                + "join fetch Post p "
                + "where p.id =: postId",Image.class)
                .setParameter("postId",id)
                .getResultList();
    }
}
