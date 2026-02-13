package com.educator.users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    Page<User> findAll(Pageable pageable);

    @Query("""
            select count(distinct u.id) from User u
            join u.roles r
            where r.name = :roleName
            """)
    long countByRoleName(@Param("roleName") String roleName);
}
