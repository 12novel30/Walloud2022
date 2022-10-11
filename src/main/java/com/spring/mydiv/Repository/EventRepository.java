package com.spring.mydiv.Repository;

import com.spring.mydiv.Entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
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

    @Transactional
    @Modifying
    @Query("update Event e set e.name = ?1, e.date = ?2, e.price = ?3, e.payerPersonid = ?4 where e.id = ?5")
    int updateNameAndDateAndPriceAndPayerPersonidById(String name, Date date, int price, Long payerPersonid, Long id);
}
