package com.kma.engfinity.repository;

import com.kma.engfinity.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, String> {
    @Query(value = "SELECT * " +
            "FROM tags " +
            "WHERE language = :language " +
            "AND name LIKE CONCAT('%', :name, '%') " +
            "ORDER BY name ASC",
            nativeQuery = true)
    List<Tag> findByLanguageAndName(
            @Param("language") String language,
            @Param("name") String name
    );

    boolean existsByNameAndLanguage(String name, String language);
}
