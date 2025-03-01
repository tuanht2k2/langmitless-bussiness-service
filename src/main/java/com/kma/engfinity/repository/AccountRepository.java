package com.kma.engfinity.repository;

import com.kma.common.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, String> {
    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    Optional<Account> findByPhoneNumber(String phoneNumber);

    @Query(value = "SELECT * " +
            "FROM accounts " +
            "WHERE ((name LIKE CONCAT('%', :keyword, '%') " +
            "   OR full_name LIKE CONCAT('%', :keyword, '%') " +
            "   OR email LIKE CONCAT('%', :keyword, '%') " +
            "   OR address LIKE CONCAT('%', :keyword, '%') " +
            "   OR phone_number LIKE CONCAT('%', :keyword, '%')) " +
            "   AND (:role IS NULL OR role = :role)) " +
            "ORDER BY CASE WHEN :sortDir = 'ASC' THEN :sortBy END ASC, " +
            "         CASE WHEN :sortDir = 'DESC' THEN :sortBy END DESC " +
            "LIMIT :pageSize OFFSET :offset", nativeQuery = true)
    List<Account> search(
            @Param("offset") int offset,
            @Param("pageSize") int pageSize,
            @Param("sortBy") String sortBy,
            @Param("sortDir") String sortDir,
            @Param("keyword") String keyword,
            @Param("role") String role
    );


    @Query(value = "SELECT * FROM accounts WHERE phone_number IN :phoneNumbers", nativeQuery = true)
    List<Account> searchByPhoneNumbers(@Param("phoneNumbers") List<String> phoneNumbers);

    boolean existsByIdentification(String identification);
}
