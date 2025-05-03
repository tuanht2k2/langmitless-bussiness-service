package com.kma.engfinity.repository;

import com.kma.engfinity.entity.AccountMessenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountMessengerRepository extends JpaRepository<AccountMessenger, String> {
    @Modifying
    @Query(value = "INSERT INTO accounts_messengers (account_id, messenger_id) VALUES (:values)", nativeQuery = true)
    void bulkInsert(@Param("values") String values);

    @Query("""
        SELECT m.id
        FROM AccountMessenger am1
        JOIN AccountMessenger am2 ON am1.messengerId = am2.messengerId
        JOIN Messenger m ON m.type = 'PERSONAL'
        WHERE am1.accountId = :account1
        AND am2.accountId = :account2
    """)
    List<String> findPersonalMessenger(
            @Param("account1") String account1,
            @Param("account2") String account2
    );

    List<AccountMessenger> findAllByMessengerId(String id);
}
