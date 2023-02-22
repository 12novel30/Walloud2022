package com.spring.mydiv.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.mydiv.Entity.User;

public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByEmail(String Email);
    Optional<User> findById(Long no);

    boolean existsByEmail(String email);

    void deleteById(Long id);

}
