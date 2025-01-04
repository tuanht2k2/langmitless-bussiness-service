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
//    @Query(value = "SELECT m FROM Messenger m " +
//            "JOIN accounts_messengers am ON am.messenger.id = m.id " +
//            "WHERE am.account.id IN :memberIds " +
//            "GROUP BY m.id " +
//            "HAVING COUNT(DISTINCT am.account.id) = :size", nativeQuery = true)
//    List<Messenger> findMessengerByMembers (@Param("accountId") String accountId);
}
