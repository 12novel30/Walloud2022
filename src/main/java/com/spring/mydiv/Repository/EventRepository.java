package com.spring.mydiv.Repository;

import com.spring.mydiv.Entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author 12nov
 */

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByTravel_Id(Long id);

    int countByTravel_Id(Long id);

    Event findFirstByTravel_IdOrderByDateAsc(Long id);

    Event findFirstByTravel_IdOrderByDateDesc(Long id);

    void delete(Event event);

    Optional<Event> findById(Long aLong);

    void deleteById(Long id);

}
