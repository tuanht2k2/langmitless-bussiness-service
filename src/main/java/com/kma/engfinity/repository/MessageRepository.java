package com.kma.engfinity.repository;

import com.kma.engfinity.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, String> {
    @Query(value = """
    SELECT m.id, m.created_by, m.created_at, m.messenger_id, m.file_url, m.content, a.id, a.name, a.profile_image FROM messages m
    JOIN accounts a
    ON a.id = m.created_by
    WHERE m.messenger_id = :messengerId
    ORDER BY m.created_at ASC 
    """, nativeQuery = true)
    List<Object[]> search (@Param("messengerId") String messengerId);

}
