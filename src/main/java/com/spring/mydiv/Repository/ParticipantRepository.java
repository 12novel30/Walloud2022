package com.spring.mydiv.Repository;

import com.spring.mydiv.Entity.Event;
import com.spring.mydiv.Entity.Participant;
import com.spring.mydiv.Entity.Person;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

/**
 * @author 12nov
 */
public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    List<Participant> findByPerson_Id(Long id);
    List<Participant> findByEvent_Id(Long id);
    void delete(Participant participant);

    Optional<Participant> findByEvent_IdAndEventRole(Long id, Boolean eventRole);
}
