package com.kma.engfinity.repository;

import com.kma.engfinity.entity.AccountMessenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountMessengerRepository extends JpaRepository<AccountMessenger, String> {
    @Modifying
    @Query(value = "INSERT INTO accounts_messengers (account_id, messenger_id) VALUES (:values)", nativeQuery = true)
    void bulkInsert(@Param("values") String values);
}
