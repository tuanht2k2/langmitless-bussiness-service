package com.kma.engfinity.repository;

import com.kma.engfinity.entity.Messenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessengerRepository extends JpaRepository<Messenger, String> {
    @Query(value = """
    SELECT 
        m.id,
        m.name,
        m.type,
        m.updated_at,
        JSON_ARRAYAGG(
            JSON_OBJECT(
                'id', a.id, 
                'name', a.name,
                'profileImage', a.profile_image
            )
        ) AS members
    FROM 
        messengers m
    INNER JOIN accounts_messengers am1 ON m.id = am1.messenger_id
    INNER JOIN accounts_messengers am2 ON m.id = am2.messenger_id
    INNER JOIN accounts a ON am2.account_id = a.id
    WHERE 
        am1.account_id = :accountId
    GROUP BY 
        m.id, m.name, m.type
    ORDER BY m.updated_at DESC 
    """, nativeQuery = true)
    List<Object[]> searchMessengers(@Param("accountId") String accountId);

    @Query(value = """
        SELECT a.id as account_id, a.name, a.profile_image FROM messengers m
        JOIN accounts_messengers am
        ON am.messenger_id = m.id
        JOIN accounts a
        ON a.id = am.account_id
        WHERE m.id = :messengerId
    """, nativeQuery = true)
    List<Object[]> getMembers(@Param("messengerId") String messengerId);
}
