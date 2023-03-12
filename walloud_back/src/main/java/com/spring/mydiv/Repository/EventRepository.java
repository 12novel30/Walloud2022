package com.spring.mydiv.Repository;

import com.spring.mydiv.Entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByTravel_Id(Long id);
    Event findFirstByTravel_IdOrderByDateAsc(Long id);
    Event findFirstByTravel_IdOrderByDateDesc(Long id);
    Optional<Event> findById(Long aLong);

    void delete(Event event);
    void deleteById(Long id);



    int countByTravel_Id(Long id);
}
