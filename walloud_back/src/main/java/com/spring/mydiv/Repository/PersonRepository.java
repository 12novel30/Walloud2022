package com.spring.mydiv.Repository;

import java.util.List;
import java.util.Optional;

import com.spring.mydiv.Entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


public interface PersonRepository extends JpaRepository<Person, Long> {
	List<Person> findByUser_Id(@Param(value = "user_id") Long id);

	Optional<Person> findByTravel_IdAndRole(Long id, Boolean role);

	Optional<Person> findByTravel_IdAndIsSuper(Long id, Boolean isSuper);

	List<Person> findByUser_IdAndIsSuper(Long id, Boolean isSuper);

	Optional<Person> findByUser_IdAndTravel_Id(Long id, Long id1);

	boolean existsByUser_IdAndTravel_Id(Long id, Long id1);

	List<Person> findByTravel_Id(Long id);

	@Override
	Optional<Person> findById(Long aLong);

	void deleteById(Long id);

	void delete(Person person);
	int countDistinctByTravel_Id(Long id);

}