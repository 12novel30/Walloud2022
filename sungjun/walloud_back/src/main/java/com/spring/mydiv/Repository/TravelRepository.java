package com.spring.mydiv.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.mydiv.Entity.Travel;

/**
 * @author 12nov
 */
public interface TravelRepository extends JpaRepository<Travel, Long>{
	Optional<Travel> findByName(String name);
	Optional<Travel> findById(Long no);
	void deleteById(Long travelId);
}
