package com.spring.mydiv.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.mydiv.Entity.User;

import antlr.collections.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author 12nov
 */
public interface UserRepository extends JpaRepository<User, Long>{
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String Email);
    Optional<User> findById(Long no);
    void deleteById(Long id);
}
