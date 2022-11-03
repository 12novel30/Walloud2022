package com.spring.mydiv.Repository;

import com.spring.mydiv.Dto.ParticipantDto;
import com.spring.mydiv.Entity.Event;
import com.spring.mydiv.Entity.Participant;
import com.spring.mydiv.Entity.Person;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author 12nov
 */
public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    List<Participant> findByPerson_Id(Long id);
    List<Participant> findByEvent_Id(Long id);
    void delete(Participant participant);
    @Transactional

    long deleteByPersonAndEvent(Person person, Event event);

    @Transactional
    @Modifying
    @Query("update Participant p set p.eventRole = ?1, p.chargedPrice = ?2 where p.person = ?3 and p.event = ?4")
    void updateEventRoleAndChargedPriceByPersonAndEvent(Boolean eventRole, Double chargedPrice, Person person, Event event);

    Optional<Participant> findByEvent_IdAndEventRole(Long id, Boolean eventRole);

}
