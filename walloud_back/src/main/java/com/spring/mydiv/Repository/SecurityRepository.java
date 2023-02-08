package com.spring.mydiv.Repository;

import com.spring.mydiv.Entity.SecurityAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SecurityRepository extends JpaRepository<SecurityAccount, Long> {
    Optional<SecurityAccount> findByEmail(String email);
}
